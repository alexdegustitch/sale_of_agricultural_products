/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import beans.Cart;
import beans.Company;
import beans.Courier;
import beans.CourierOrder;
import beans.Farmer;
import beans.Item;
import beans.ItemCart;
import beans.ItemOrder;
import beans.NurseryGarden;
import beans.Order;
import beans.SeedlingInStorage;
import beans.Storage;
import com.google.maps.errors.ApiException;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import models.CartModel;
import models.CompanyModel;
import models.CourierModel;
import models.CourierOrderModel;
import models.FarmerModel;
import models.ItemCartModel;
import models.ItemModel;
import models.ItemOrderModel;
import models.NurseryGardenModel;
import models.OrderModel;
import models.SeedlingInStorageModel;
import models.StorageModel;
import org.json.simple.parser.ParseException;

/**
 *
 * @author Aleksandar
 */
@Singleton
public class BackgroundJobManager {

    private String message;

    //try
    @Schedule(hour = "*", minute = "*", second = "*/10", persistent = false)
    public void someDailyJob() {
        System.out.println("THIS WORKS!");
    }

    @Schedule(hour = "0", minute = "0", second = "0", dayOfWeek = "1", persistent = false)
    public void send_gifts() {
        List<NurseryGarden> nurseries = NurseryGardenModel.get_all_nurseries();
        HashMap<String, Integer> map;

        for (int i = 0; i < nurseries.size(); i++) {
            map = new HashMap<>();

            NurseryGarden ng = nurseries.get(i);
            List<SeedlingInStorage> items = SeedlingInStorageModel.find_all_seedlings_in_storage(ng.getId_garden());

            for (int j = 0; j < items.size(); j++) {
                SeedlingInStorage ss = items.get(j);
                if (map.containsKey(ss.getCompany_name())) {
                    Integer num = map.get(ss.getCompany_name());
                    num++;

                    map.put(ss.getCompany_name(), num);
                } else {
                    map.put(ss.getCompany_name(), 1);
                }
            }

            for (Map.Entry<String, Integer> entry : map.entrySet()) {
                String key = entry.getKey();
                Integer value = entry.getValue();

                if (value > 10) {
                    //napraviti_order

                    List<SeedlingInStorage> items_company = SeedlingInStorageModel.find_all_seedlings_in_storage_for_company(ng.getId_garden(), key);
                    List<Item> agents = ItemModel.get_all_agents_for_company_in_stock(key);

                    if (agents.isEmpty()) {
                        continue;
                    }

                    Company company = CompanyModel.find_company_by_short_name(key);

                    for (int k = 0; k < items_company.size(); k++) {
                        SeedlingInStorageModel.delete_seedling_for_storage(items_company.get(k));
                    }

                    Item gift_agent = agents.get((int) (Math.random() * agents.size()));

                    gift_agent.setAmount(gift_agent.getAmount() - 1);

                    ItemModel.update_item(gift_agent);

                    LocalDateTime now = LocalDateTime.now();

                    Order o = new Order();
                    o.setCompany_name(key);
                    o.setDate_of_order(now);
                    o.setFarmer_username(ng.getFarmer_username());
                    o.setId_nursery(ng.getId_garden());
                    o.setPriority(0);
                    o.setStatus('g');
                    o.setDate(LocalDate.now());

                    OrderModel.add_new_order(o);

                    int max_id_order = OrderModel.get_order_max_id();

                    ItemOrder it_order = new ItemOrder();
                    it_order.setAmount(1);
                    it_order.setFarmer_username(ng.getFarmer_username());
                    it_order.setId_item(gift_agent.getId_item());
                    it_order.setId_order(max_id_order);
                    it_order.setId_nursery(ng.getId_garden());
                    it_order.setItem_name(gift_agent.getName());
                    it_order.setStatus('g');

                    ItemOrderModel.add_item_order(it_order);

                    List<Courier> free_couriers = CourierModel.get_free_couriers_for_company(key);

                    if (free_couriers.size() > 0) {
                        Courier c = free_couriers.get((int) (Math.random() * free_couriers.size()));

                        try {
                            long seconds = DistanceTime.getDriveDist(company.getPlace_of_the_company(), ng.getLocation());
                            c.setDelivery_time(now.plusSeconds(seconds));
                            c.setFree_at_time(now.plusSeconds(seconds * 2));
                            c.setStatus(0);
                            c.setFree_after_gifts(now.plusSeconds(seconds * 2));

                            CourierModel.update_courier(c);

                            CourierOrder cu_order = new CourierOrder();
                            cu_order.setId_courier(c.getId_courier());
                            cu_order.setId_order(max_id_order);
                            cu_order.setIs_gift(1);
                            cu_order.setOrder_arrived(0);
                            cu_order.setStart_time(now);
                            cu_order.setEnd_time(now.plusSeconds(seconds * 2));

                            CourierOrderModel.add_order_for_courier(cu_order);

                        } catch (ApiException | InterruptedException | IOException | NoSuchAlgorithmException | KeyManagementException | ParseException ex) {
                            Logger.getLogger(BackgroundJobManager.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    } else {
                        Courier c = CourierModel.get_courier_with_least_gift_time_for_company(key);
                        try {
                            long seconds = DistanceTime.getDriveDist(company.getPlace_of_the_company(), ng.getLocation());

                            CourierOrder cu_order = new CourierOrder();
                            cu_order.setId_courier(c.getId_courier());
                            cu_order.setId_order(max_id_order);
                            cu_order.setIs_gift(1);
                            cu_order.setOrder_arrived(0);
                            cu_order.setStart_time(c.getFree_after_gifts());

                            c.setFree_after_gifts(c.getFree_after_gifts().plusSeconds(seconds * 2));

                            cu_order.setEnd_time(c.getFree_after_gifts());

                            CourierOrderModel.add_order_for_courier(cu_order);
                            CourierModel.update_courier(c);

                        } catch (ApiException | InterruptedException | IOException | NoSuchAlgorithmException | KeyManagementException | ParseException ex) {
                            Logger.getLogger(BackgroundJobManager.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }

                }
            }
        }
        System.out.println("MONDAY UPDATE!");
    }

    /* @Schedule(hour = "0", minute = "0", second = "0", dayOfWeek = "1", persistent = false)
    public void send_gifts() {

        HashMap<String, Integer> map;

        List<NurseryGarden> nurseries = NurseryGardenModel.get_all_nurseries();
        for (int i = 0; i < nurseries.size(); i++) {

            map = new HashMap<>();

            NurseryGarden ng = nurseries.get(i);
            List<Storage> items = StorageModel.get_all_items_for_nursery(ng.getId_garden());
            for (int j = 0; j < items.size(); j++) {
                Item it = ItemModel.get_item_by_id(items.get(i).getId_item());
                if (it.getType() == 'S') {
                    if (map.containsKey(it.getCompany_name())) {
                        Integer num = map.get(it.getCompany_name());
                        num++;

                        map.put(it.getCompany_name(), num);
                    } else {
                        map.put(it.getCompany_name(), 1);
                    }
                }
            }

            for (Map.Entry<String, Integer> entry : map.entrySet()) {
                String key = entry.getKey();
                Integer value = entry.getValue();

                if (value > 10) {

                }
            }
        }
        System.out.println("MONDAY UPDATE!");
    }*/
    //update User's cart
    @Schedule(hour = "*", minute = "*", persistent = false)
    public void update_cart_and_orders() {
        System.out.println("CART UPDATED");
        List<Cart> carts = CartModel.get_all_carts();
        for (int i = 0; i < carts.size(); i++) {
            Cart cart = carts.get(i);
            LocalDateTime now = LocalDateTime.now();
            now = now.minusMinutes(30);
            if (now.isAfter(cart.getCart_start_time())) {
                List<ItemCart> items = ItemCartModel.get_items_in_cart_for_user(cart.getId_cart());
                for (int j = 0; j < items.size(); j++) {
                    ItemCart item_cart = items.get(j);
                    Item item = ItemModel.get_item_by_id(item_cart.getId_item());
                    item.setAmount(item.getAmount() + item_cart.getAmount());
                    ItemModel.update_item(item);

                    ItemCartModel.remove_item_from_cart(item_cart);
                }

                CartModel.delete_cart_for_user(cart);

            }
        }

        System.out.println("COURIERS UPDATED");

        List<Courier> couriers = CourierModel.get_busy_couriers();

        LocalDateTime now = LocalDateTime.now();
        for (int i = 0; i < couriers.size(); i++) {
            Courier courier = couriers.get(i);
            if (now.isAfter(courier.getDelivery_time())) {
                CourierOrder co = CourierOrderModel.get_order_for_courier(courier.getId_courier(), courier.getFree_at_time());
                if (co != null) {
                    co.setOrder_arrived(1);
                    CourierOrderModel.update_order_for_courier(co);

                    Order order = OrderModel.find_order_by_id(co.getId_order());

                    order.setPriority(0);

                    order.setStatus('a');
                    OrderModel.update_order(order);

                    List<ItemOrder> items = ItemOrderModel.get_items_for_order(order.getId_order());
                    System.out.println(items);
                    for (int j = 0; j < items.size(); j++) {

                        ItemOrder it = items.get(j);
                        it.setStatus('a');
                        ItemOrderModel.update_item_order(it);
                        System.out.println("Name: " + it.getId_item() + " j: " + j);

                        Item item_ = ItemModel.get_item_by_id(it.getId_item());

                        if (item_.getType() == 'S') {
                            SeedlingInStorage ss = SeedlingInStorageModel.find_seedling_in_storage(it.getId_item(), it.getId_nursery());

                            if (ss == null) {
                                SeedlingInStorage seedling_in_storage = new SeedlingInStorage();

                                seedling_in_storage.setCompany_name(item_.getCompany_name());
                                seedling_in_storage.setId_item(it.getId_item());
                                seedling_in_storage.setId_storage(it.getId_nursery());

                                SeedlingInStorageModel.add_seedling_for_storage(seedling_in_storage);
                            }
                        }

                        Storage storage_item = StorageModel.get_item_for_nursery(it.getId_nursery(), it.getId_item());

                        if (storage_item == null) {
                            Storage s = new Storage();
                            s.setAmount(it.getAmount());
                            s.setId_item(it.getId_item());
                            s.setId_nursery(it.getId_nursery());

                            StorageModel.add_item_in_storage(s);
                        } else {
                            storage_item.setAmount(storage_item.getAmount() + it.getAmount());
                            StorageModel.update_storage(storage_item);
                        }
                    }

                }
            }

            if (now.isAfter(courier.getFree_at_time())) {
                if (courier.getFree_after_gifts().isAfter(courier.getFree_at_time())) {
                    CourierOrder co_order = CourierOrderModel.get_next_gift_order_for_courier(courier.getId_courier());

                    courier.setFree_at_time(co_order.getEnd_time());
                    long time_diff = co_order.getStart_time().until(co_order.getEnd_time(), ChronoUnit.SECONDS);
                    courier.setDelivery_time(co_order.getStart_time().plusSeconds(time_diff / 2));
                    CourierModel.update_courier(courier);
                } else {
                    courier.setStatus(1);
                    CourierModel.update_courier(courier);
                }
            }

        }
    }

    //every hour update water amount and temperature in the nurseries
    
    //@Schedule(hour = "13", minute = "56", second = "0", dayOfWeek = "5", persistent = false)
    @Schedule(hour = "*", persistent = false)
    public void update_nursery_parameters() {

        List<NurseryGarden> nurseries;
        StringBuilder sb, help;
        boolean inform = false;
        boolean send_email = false;

        List<Farmer> farmers = FarmerModel.get_comfirmed_farmers();
        for (int i = 0; i < farmers.size(); i++) {
            nurseries = NurseryGardenModel.get_all_nursery_gardens_for_user(farmers.get(i).getUsername());

            send_email = false;
            sb = new StringBuilder();

            sb.append("Dear " + farmers.get(i).getFirst_name() + ",\n\n");
            sb.append("We just want to inform You about some of your nurseries.\n\n");

            for (int j = 0; j < nurseries.size(); j++) {
                NurseryGarden nursery = nurseries.get(j);
                inform = false;
                help = new StringBuilder();

                if (nursery.getAmount_of_water() > 0) {
                    nursery.setAmount_of_water(nursery.getAmount_of_water() - 1);
                }
                nursery.setTemperature(nursery.getTemperature() - 0.5);
                NurseryGardenModel.update_garden(nursery);

                if (nursery.getAmount_of_water() < 75 && nursery.getFarmer_informed_w() == 0) {
                    help.append(" Amount of water is below 75l!");
                    nursery.setFarmer_informed_w(1);
                    inform = true;
                    send_email = true;
                }

                if (nursery.getTemperature() < 12 && nursery.getFarmer_informed_t() == 0) {
                    help.append(" Temperature is below 12ºC!");
                    nursery.setFarmer_informed_t(1);
                    inform = true;
                    send_email = true;
                }

                if (inform) {
                    sb.append("Nursery '" + nursery.getName() + "' requires maintenance!" + help.toString() + "\n");
                }

                NurseryGardenModel.update_garden(nursery);
            }

            if (send_email) {
                sb.append("\n");
                sb.append("Best Regards, \n");
                sb.append("Farm&More Team.");

                String from = "paripovic.aleks@gmail.com";
                String pass = "akiaki84908590";
                String[] to = {farmers.get(i).getEmail()};
                String subject = "Farm&More - Nursery Maintenance Note";
                String body = sb.toString();

                Properties props = System.getProperties();
                String host = "smtp.gmail.com";
                props.setProperty("mail.smtp.starttls.enable", "true");
                props.setProperty("mail.smtp.host", host);
                props.setProperty("mail.smtp.user", from);
                props.setProperty("mail.smtp.password", pass);
                props.setProperty("mail.smtp.port", "587");
                props.setProperty("mail.smtp.auth", "true");
                props.setProperty("mail.smtp.ssl.trust", "*");

                Session session = Session.getInstance(props);
                MimeMessage message = new MimeMessage(session);

                try {
                    message.setFrom(new InternetAddress(from));
                    InternetAddress[] toAddress = new InternetAddress[to.length];

                    // To get the array of addresses
                    for (int m = 0; m < to.length; m++) {
                        toAddress[m] = new InternetAddress(to[m]);
                    }

                    for (int m = 0; m < toAddress.length; m++) {
                        message.addRecipient(Message.RecipientType.TO, toAddress[m]);
                    }

                    message.setSubject(subject);
                    message.setText(body);
                    Transport transport = session.getTransport("smtp");
                    transport.connect(host, from, pass);
                    transport.sendMessage(message, message.getAllRecipients());
                    transport.close();
                } catch (AddressException ae) {
                    ae.printStackTrace();
                } catch (MessagingException me) {
                    me.printStackTrace();
                }
            }
        }

        System.out.println("NURSERIES UPDATED");
        /* HttpSession session = util.SessionUtils.getSession();
        Farmer farmer = (Farmer) session.getAttribute("user");

        int water;
        double temp;

        int num;
        StringBuilder sb;
        if (farmer != null) {
            nurseries = NurseryGardenModel.get_all_nursery_gardens_for_user(farmer.getUsername());
            sb = new StringBuilder();
            num = 0;
            for (int i = 0; i < nurseries.size(); i++) {
                NurseryGarden nursery = nurseries.get(i);
                water = nursery.getAmount_of_water();
                temp = nursery.getTemperature();

                if (water < 75 || temp < 12) {
                    num++;
                    if (num > 1) {
                        sb.append(", \"").append(nursery.getName()).append("\"");
                    } else {
                        sb.append("\"").append(nursery.getName()).append("\"");
                    }
                }
            }

            if (num == 0) {
                message = null;
            } else if (num == 1) {
                message = "Nursery " + sb.toString() + " requires maintenance";
            } else {
                message = "Nurseries " + sb.toString() + " require maintenance";
            }
        }*/
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
