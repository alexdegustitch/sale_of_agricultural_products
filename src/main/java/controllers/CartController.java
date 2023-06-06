/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import beans.Cart;
import beans.Company;
import beans.Farmer;
import beans.Item;
import beans.ItemCart;
import beans.ItemOrder;
import beans.NurseryGarden;
import beans.Order;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import models.CartModel;
import models.CompanyModel;
import models.ItemCartModel;
import models.ItemModel;
import models.ItemOrderModel;
import models.NurseryGardenModel;
import models.OrderModel;

/**
 *
 * @author Aleksandar
 */
@ManagedBean
@SessionScoped
public class CartController {

    private Cart current_cart;
    private List<Item> items_in_cart;
    private double sum;
    private List<NurseryGarden> nurseries;
    private Integer id_nursery;

    private boolean button;

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
        } else if (type.equals("c")) {

            try {
                context.getExternalContext().redirect("company.xhtml");
            } catch (IOException ex) {
                Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (type.equals("a")) {
            try {
                context.getExternalContext().redirect("admin.xhtml");
            } catch (IOException ex) {
                Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
     
    public void get_items_in_cart() {
        HttpSession session = util.SessionUtils.getSession();
        Farmer f = (Farmer) session.getAttribute("user");

        sum = 0;
        current_cart = CartModel.get_cart_for_user(f.getUsername());
        items_in_cart = new LinkedList<>();
        if (current_cart != null) {
            List<ItemCart> items;
            items = ItemCartModel.get_items_in_cart_for_user(current_cart.getId_cart());

            for (int i = 0; i < items.size(); i++) {
                Item it = ItemModel.get_item_by_id(items.get(i).getId_item());
                Company c = CompanyModel.find_company_by_short_name(it.getCompany_name());
                it.setCompany_full_name(c.getFull_name());
                it.setAmount_in_storage(items.get(i).getAmount());

                sum += it.getPrice() * it.getAmount_in_storage();

                items_in_cart.add(it);

            }
        }

        nurseries = NurseryGardenModel.get_all_nursery_gardens_for_user(f.getUsername());

    }

    public void remove_item_from_cart(int id_item) {
        ItemCart item_cart = ItemCartModel.get_item_in_cart_for_user(current_cart.getId_cart(), id_item);

        ItemCartModel.remove_item_from_cart(item_cart);

        Item it = ItemModel.get_item_by_id(id_item);
        it.setAmount(it.getAmount() + item_cart.getAmount());

        ItemModel.update_item(it);

        List<ItemCart> item_list = ItemCartModel.get_items_in_cart_for_user(current_cart.getId_cart());
        if (item_list == null || item_list.isEmpty()) {
            CartModel.delete_cart_for_user(current_cart);
        }
    }

    public void order_order() {

        if (id_nursery == null) {
            return;
        }

        HttpSession session = util.SessionUtils.getSession();
        Farmer f = (Farmer) session.getAttribute("user");

        sum = 0;
        current_cart = CartModel.get_cart_for_user(f.getUsername());
        items_in_cart = new LinkedList<>();

        HashMap<String, List<ItemCart>> map = new HashMap<>();

        if (current_cart != null) {
            List<ItemCart> items;
            items = ItemCartModel.get_items_in_cart_for_user(current_cart.getId_cart());

            for (int i = 0; i < items.size(); i++) {
                Item it = ItemModel.get_item_by_id(items.get(i).getId_item());
                Company c = CompanyModel.find_company_by_short_name(it.getCompany_name());

                List<ItemCart> list = map.get(c.getShort_name());
                if (list != null) {
                    list.add(items.get(i));
                    map.replace(c.getShort_name(), list);
                } else {
                    list = new LinkedList<>();
                    list.add(items.get(i));
                    map.put(c.getShort_name(), list);
                }
            }

            for (Map.Entry<String, List<ItemCart>> entry : map.entrySet()) {
                String company_name = entry.getKey();
                List<ItemCart> items_list = entry.getValue();

                double order_price = 0;
                Order o = new Order();
                o.setCompany_name(company_name);
                o.setDate_of_order(LocalDateTime.now());
                o.setDate(LocalDate.now());
                o.setFarmer_username(f.getUsername());
                o.setId_nursery(id_nursery);
                o.setPriority(1);
                o.setStatus('o');

                OrderModel.add_new_order(o);
                int max_id_order = OrderModel.get_order_max_id();

                for (int i = 0; i < items_list.size(); i++) {

                    ItemCart item_c = items_list.get(i);

                    Item it = ItemModel.get_item_by_id(item_c.getId_item());

                    ItemOrder it_o = new ItemOrder();
                    it_o.setAmount(item_c.getAmount());
                    it_o.setFarmer_username(f.getUsername());
                    it_o.setId_item(it.getId_item());
                    it_o.setId_order(max_id_order);
                    it_o.setItem_name(it.getName());
                    it_o.setStatus('o');
                    it_o.setPrice(item_c.getAmount() * it.getPrice());
                    it_o.setId_nursery(id_nursery);

                    ItemOrderModel.add_item_order(it_o);

                    ItemCartModel.remove_item_from_cart(item_c);

                    order_price += it_o.getPrice();
                }

                o = OrderModel.find_order_by_id(max_id_order);
                o.setPrice(order_price);
                OrderModel.update_order(o);
                //it.setAmount(it.getAmount() - items.get(i).getAmount()); jer smo ovo vec uradili, mozda treba amount in storage da se promeni?

                //sum += it.getPrice() * it.getAmount_in_storage();
            }
        }

        CartModel.delete_cart_for_user(current_cart);
        id_nursery = null;
    }

    private static double round(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public void order_button() {
        System.out.println("hej");
    }

    public Cart getCurrent_cart() {
        return current_cart;
    }

    public void setCurrent_cart(Cart current_cart) {
        this.current_cart = current_cart;
    }

    public List<Item> getItems_in_cart() {
        return items_in_cart;
    }

    public void setItems_in_cart(List<Item> items_in_cart) {
        this.items_in_cart = items_in_cart;
    }

    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }

    public List<NurseryGarden> getNurseries() {
        return nurseries;
    }

    public void setNurseries(List<NurseryGarden> nurseries) {
        this.nurseries = nurseries;
    }

    public Integer getId_nursery() {
        return id_nursery;
    }

    public void setId_nursery(Integer id_nursery) {
        this.id_nursery = id_nursery;
    }

    public boolean isButton() {
        return button;
    }

    public void setButton(boolean button) {
        this.button = button;
    }

}
