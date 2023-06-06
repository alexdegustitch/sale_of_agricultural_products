/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import beans.Company;
import beans.Courier;
import beans.CourierOrder;
import beans.Item;
import beans.ItemOrder;
import beans.NurseryGarden;
import beans.Order;
import com.google.maps.errors.ApiException;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import models.CompanyModel;
import models.CourierModel;
import models.CourierOrderModel;
import models.ItemModel;
import models.ItemOrderModel;
import models.NurseryGardenModel;
import models.OrderModel;
import org.json.simple.parser.ParseException;
import util.DistanceTime;

/**
 *
 * @author Aleksandar
 */
@ManagedBean
@SessionScoped
public class CompanyController {

    private List<Order> items;
    private DistanceTime distance;
    private List<ItemOrder> items_in_order;

    public void check_type() {
        HttpSession session = util.SessionUtils.getSession();
        String type = (String) session.getAttribute("type");

        FacesContext context = FacesContext.getCurrentInstance();
        if (type == null) {
            try {
                context.getExternalContext().redirect("index.xhtml");
            } catch (IOException ex) {
                Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (type.equals("a")) {

            try {
                context.getExternalContext().redirect("admin.xhtml");
            } catch (IOException ex) {
                Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (type.equals("f")) {
            try {
                context.getExternalContext().redirect("farmer.xhtml");
            } catch (IOException ex) {
                Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void get_all_orders_for_company(String company_name) {

        items = OrderModel.get_all_orders_for_company(company_name);
        //for (int i = 0; i < items.size(); i++) {
          //  items.get(i).setPrice(round(items.get(i).getPrice(), 2));
        //}

    }

    public void get_items_for_order(int id_order) {

        items_in_order = ItemOrderModel.get_not_canceled_items_for_order(id_order);
    }

    public void accept_order(int id_order) throws ApiException {
        HttpSession session = util.SessionUtils.getSession();
        Company company = (Company) session.getAttribute("company");

        company = CompanyModel.find_company_by_short_name(company.getShort_name());

        //ItemOrder item = ItemOrderModel.get_item_order_by_id(id_item_order);
        Order order = OrderModel.find_order_by_id(id_order);

        List<Courier> couriers = CourierModel.get_free_couriers_for_company(company.getShort_name());

        if (!couriers.isEmpty()) {

            Courier current = couriers.get((int) (Math.random() * couriers.size()));
            current.setStatus(0);

            NurseryGarden ng = NurseryGardenModel.find_nursery_garden(order.getId_nursery());

            long seconds = 0;
            try {
                seconds = DistanceTime.getDriveDist(company.getPlace_of_the_company(), ng.getLocation());
            } catch (InterruptedException | IOException | NoSuchAlgorithmException | KeyManagementException | ParseException ex) {
                Logger.getLogger(CompanyController.class.getName()).log(Level.SEVERE, null, ex);
            }

            LocalDateTime start_time = LocalDateTime.now();
            LocalDateTime delivery_time = start_time.plusSeconds(seconds);

            //mora da se vrati
            seconds = seconds * 2;

            LocalDateTime end_time = start_time.plusSeconds(seconds);

            current.setDelivery_time(delivery_time);
            current.setFree_at_time(end_time);
            current.setFree_after_gifts(end_time);

            CourierOrder cor_o = new CourierOrder();
            cor_o.setStart_time(start_time);
            cor_o.setEnd_time(end_time);
            cor_o.setId_courier(current.getId_courier());
            cor_o.setId_order(id_order);
            cor_o.setOrder_arrived(0);

            CourierOrderModel.add_order_for_courier(cor_o);

            CourierModel.update_courier(current);

            company.setFree_couriers(company.getFree_couriers() - 1);
            CompanyModel.update_company(company);

            order.setStatus('s');
            order.setPriority(0);

            OrderModel.update_order(order);

            List<ItemOrder> order_items = ItemOrderModel.get_not_canceled_items_for_order(id_order);

            for (int i = 0; i < order_items.size(); i++) {
                ItemOrder it_o = order_items.get(i);
                it_o.setStatus('s');
                ItemOrderModel.update_item_order(it_o);
            }

            return;

        }
        order.setPriority(2);
        order.setStatus('w');

        OrderModel.update_order(order);

        List<ItemOrder> order_items = ItemOrderModel.get_not_canceled_items_for_order(id_order);

        for (int i = 0; i < order_items.size(); i++) {
            ItemOrder it_o = order_items.get(i);
            it_o.setStatus('w');
            ItemOrderModel.update_item_order(it_o);
        }
    }

    public void decline_order(int id_order) {
        HttpSession session = util.SessionUtils.getSession();
        Company company = (Company) session.getAttribute("company");

        //ItemOrder item = ItemOrderModel.get_item_order_by_id(id_item_order);
        Order order = OrderModel.find_order_by_id(id_order);

        order.setPriority(0);
        order.setStatus('d');

        OrderModel.update_order(order);

        List<ItemOrder> order_items = ItemOrderModel.get_not_canceled_items_for_order(id_order);

        for (int i = 0; i < order_items.size(); i++) {
            ItemOrder it_o = order_items.get(i);

            it_o.setStatus('d');
            ItemOrderModel.update_item_order(it_o);

            Item it = ItemModel.get_item_by_id(it_o.getId_item());

            it.setAmount(it.getAmount() + it_o.getAmount());

            ItemModel.update_item(it);
        }

    }

    public List<Order> getItems() {
        return items;
    }

    private static double round(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public void setItems(List<Order> items) {
        this.items = items;
    }

    public List<ItemOrder> getItems_in_order() {
        return items_in_order;
    }

    public void setItems_in_order(List<ItemOrder> items_in_order) {
        this.items_in_order = items_in_order;
    }

}
