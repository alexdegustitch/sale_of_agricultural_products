/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import beans.Admin;
import beans.AgentsForSeedling;
import beans.Cart;
import beans.Comment;
import beans.Company;
import beans.Courier;
import beans.Farmer;
import beans.Item;
import beans.ItemCart;
import beans.ItemOrder;
import beans.NurseryGarden;
import beans.Order;
import beans.SeedlingInNursery;
import beans.SeedlingInStorage;
import beans.Storage;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import models.AgentsForSeedlingModel;
import models.CartModel;
import models.CommentModel;
import models.CompanyModel;
import models.CourierModel;
import models.FarmerModel;
import models.ItemCartModel;
import models.ItemModel;
import models.ItemOrderModel;
import models.NurseryGardenModel;
import models.OrderModel;
import models.RequestModel;
import models.SeedlingInNurseryModel;
import models.SeedlingInStorageModel;
import models.StorageModel;

/**
 *
 * @author Aleksandar
 */
@ManagedBean
@SessionScoped
public class AdminController {
    
    private List<Farmer> farmers;
    private List<Company> companies;
    
    private List<Farmer> confirmed_farmers;
    private List<Company> confirmed_companies;
    
    private Company company_info;
    private Farmer farmer_info;
    
    private Company current_company;
    private Farmer current_farmer;
    
    private Company company_copy;
    private Farmer farmer_copy;
    
    private String date_format;
    
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
        } else if (type.equals("f")) {
            try {
                context.getExternalContext().redirect("farmer.xhtml");
            } catch (IOException ex) {
                Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private Boolean check_password(String password) {
        String regex = "(?=.*[A-Z])(?=.*\\d)(?=.*[-+()[\\\\]~!@#$%^&*+}{\":;'/?.><,`\\\\\\\\])([A-Z]|[a-z])[A-Za-z\\d-+()[\\\\]~!@#$%^&*+}{\":;'/?.><,`\\\\\\\\]{6,50}";
        return password.matches(regex);
    }
    
    private Boolean check_email(String email) {
        String regex = "[^@\\s]+@[^@\\s\\.]+\\.[^@\\.\\s]+";
        return email.matches(regex);
    }
    
    private Boolean check_phone_number(String phone_number) {
        String regex = "\\d{3}/\\d{3}-\\d{3,4}";
        return phone_number.matches(regex);
    }
    
    private Boolean check_date_format(String date) {
        String regex = "\\d{2}/\\d{2}/\\d{4}";
        return date.matches(regex);
    }
    
    private Boolean check_full_date(int day, int month, int year) {
        boolean leap = false;
        
        if (year % 4 == 0) {
            if (year % 100 == 0) {
                // year is divisible by 400, hence the year is a leap year
                if (year % 400 == 0) {
                    leap = true;
                } else {
                    leap = false;
                }
            } else {
                leap = true;
            }
        } else {
            leap = false;
        }
        
        if (year < 1900 && year > 2020) {
            return false;
        }
        
        if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) {
            return day > 0 && day <= 31;
        }
        if (month == 4 || month == 6 || month == 9 || month == 11) {
            return day > 0 && day <= 30;
        }
        if (month == 2) {
            if (leap) {
                return day > 0 && day <= 29;
            } else {
                return day > 0 && day <= 28;
            }
        }
        
        return false;
    }
    
    private Boolean check_date(String date) {
        if (check_date_format(date)) {
            int day = Integer.parseInt(date.substring(0, 2));
            int month = Integer.parseInt(date.substring(3, 5));
            int year = Integer.parseInt(date.substring(6));
            
            if (check_full_date(day, month, year)) {
                LocalDate now = LocalDate.now();
                LocalDate date_ = LocalDate.of(year, month, day);
                
                return !now.isBefore(date_);
                
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
    
    private Boolean check_only_letters(String name) {
        String regex = "[A-Za-z]+";
        return name.matches(regex);
    }
    
    public void get_all_uncomfirmed_users() {
        
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
        } else if (type.equals("f")) {
            try {
                context.getExternalContext().redirect("farmer.xhtml");
            } catch (IOException ex) {
                Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        farmers = FarmerModel.get_uncomfirmed_farmers();
        companies = CompanyModel.get_uncomfirmed_companies();
    }
    
    public void get_all_confirmed_users() {
        check_type();
        confirmed_farmers = FarmerModel.get_comfirmed_farmers();
        confirmed_companies = CompanyModel.get_comfirmed_companies();
    }
    
    public String get_farmer(String username) {
        current_farmer = FarmerModel.find_farmer_by_username(username);
        farmer_copy = FarmerModel.find_farmer_by_username(username);
        date_format = current_farmer.getDate_of_birth().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        return "farmer_profile?faces-redirect=true";
    }
    
    public String get_company(String company_name) {
        current_company = CompanyModel.find_company_by_short_name(company_name);
        company_copy = CompanyModel.find_company_by_short_name(company_name);
        date_format = current_company.getDate_of_founding().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        return "company_profile?faces-redirect=true";
    }
    
    public void find_company(String company_name) {
        company_info = CompanyModel.find_company_by_short_name(company_name);
    }
    
    public void find_farmer(String username) {
        farmer_info = FarmerModel.find_farmer_by_username(username);
    }
    
    public void save_changes(String type) {
        
        if (type.equals("F")) {
            
            boolean firstName = check_only_letters(current_farmer.getFirst_name());
            boolean lastName = check_only_letters(current_farmer.getLast_name());
            boolean password_pass = check_password(current_farmer.getPassword());
            boolean place = check_only_letters(current_farmer.getPlace_of_birth());
            boolean email_check = check_email(current_farmer.getEmail());
            boolean phone_num = check_phone_number(current_farmer.getContact_phone());
            boolean date_check = check_date(date_format);
            
            if (firstName && lastName && password_pass && place && email_check && phone_num && date_check) {
                
                current_farmer.setDate_of_birth(LocalDate.parse(date_format, DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                //System.out.println(current_farmer.getDate_of_birth());
                FarmerModel.update_farmer(current_farmer);
                farmer_copy = FarmerModel.find_farmer_by_username(current_farmer.getUsername());
                return;
            }
            
            if (!firstName) {
                current_farmer.setFirst_name(farmer_copy.getFirst_name());
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "First name must contain only letters.", "First name must contain only letters."));
            }
            
            if (!lastName) {
                current_farmer.setLast_name(farmer_copy.getLast_name());
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Last name must contain only letters.", "Last name must contain only letters."));
            }
            
            if (!password_pass) {
                current_farmer.setPassword(farmer_copy.getPassword());
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Password must meet complexity requirements.", "Password must meet complexity requirements."));
            }
            
            if (!date_check) {
                date_format = current_farmer.getDate_of_birth().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Date of Birth must be in formatt 'dd/MM/yyyy' and must be before " + LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), "Date of Birth must be in formatt 'dd/MM/yyyy' and must be before " + LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
            }
            
            if (!place) {
                current_farmer.setPlace_of_birth(farmer_copy.getPlace_of_birth());
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Place of birth must contain only letters.", "Place of birth must contain only letters."));
            }
            
            if (!phone_num) {
                current_farmer.setContact_phone(farmer_copy.getContact_phone());
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "You must enter valid phone number.", "You must enter valid phone number"));
            }
            
            if (!email_check) {
                current_farmer.setEmail(farmer_copy.getEmail());
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "You must enter valid e-mail address.", "You must enter valid e-mail address."));
            }
            
        } else {
            
            boolean password_pass = check_password(current_company.getPassword());
            
            boolean place = !current_company.getPlace_of_the_company().isEmpty();
            
            boolean full_name = !current_company.getFull_name().isEmpty();
            
            boolean email_check = check_email(current_company.getEmail());
            
            boolean date_check = check_date(date_format);
            
            if (password_pass && place && email_check && full_name && date_check) {
                current_company.setDate_of_founding(LocalDate.parse(date_format, DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                
                CompanyModel.update_company(current_company);
                company_copy = CompanyModel.find_company_by_short_name(current_company.getShort_name());
                return;
                
            }
            
            if (!full_name) {
                current_company.setFull_name(company_copy.getFull_name());
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Full Name is required.", "Full Name is required."));
            }
            
            if (!password_pass) {
                current_company.setPassword(company_copy.getPassword());
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Password must meet complexity requirements.", "Password must meet complexity requirements."));
            }
            
            if (!date_check) {
                date_format = current_company.getDate_of_founding().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Date of Founding must be in formatt 'dd/MM/yyyy' and must be before " + LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), "Date of Founding must be in formatt 'dd/MM/yyyy' and must be before " + LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
            }
            
            if (!place) {
                current_company.setPlace_of_the_company(company_copy.getPlace_of_the_company());
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Place of the company is required.", "Place of the company is required."));
            }
            
            if (!email_check) {
                current_company.setEmail(company_copy.getEmail());
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "You must enter valid e-mail address.", "You must enter valid e-mail address."));
            }
            
        }
    }
    
    public String delete(String type) {
        if (type.equals("F")) {
            List<NurseryGarden> nurseries = NurseryGardenModel.get_all_nursery_gardens_for_user(current_farmer.getUsername());
            for (int i = 0; i < nurseries.size(); i++) {
                NurseryGarden ng = nurseries.get(i);
                
                List<SeedlingInNursery> sin = SeedlingInNurseryModel.get_all_seedlings_for_nursery(ng.getId_garden());
                for (int j = 0; j < sin.size(); j++) {
                    SeedlingInNursery s = sin.get(j);
                    
                    List<AgentsForSeedling> agents = AgentsForSeedlingModel.get_agents_for_seedling(s.getId_seedling_nursery());
                    
                    AgentsForSeedlingModel.delete_agents_for_seedling(agents);
                    
                    SeedlingInNurseryModel.delete_seedling_nursery(s);
                }
                
                List<Storage> storage_items = StorageModel.get_all_items_for_nursery(ng.getId_garden());
                for (int j = 0; j < storage_items.size(); j++) {
                    StorageModel.delete_storage_item(storage_items.get(j));
                }
                
                List<SeedlingInStorage> sin_storage = SeedlingInStorageModel.find_all_seedlings_in_storage(ng.getId_garden());
                for (int j = 0; j < sin_storage.size(); j++) {
                    SeedlingInStorageModel.delete_seedling_for_storage(sin_storage.get(j));
                }
                
                NurseryGardenModel.delete_nursery(ng);
            }
            Cart cart = CartModel.get_cart_for_user(current_farmer.getUsername());
            
            if (cart != null) {
                List<ItemCart> items = ItemCartModel.get_items_in_cart_for_user(cart.getId_cart());
                for (int i = 0; i < items.size(); i++) {
                    Item it = ItemModel.get_item_by_id(items.get(i).getId_item());
                    it.setAmount(it.getAmount() + items.get(i).getAmount());
                    ItemModel.update_item(it);
                    
                    ItemCartModel.remove_item_from_cart(items.get(i));
                }
                CartModel.delete_cart_for_user(cart);
            }
            
            List<Comment> comments = CommentModel.get_all_comments_from_user(current_farmer.getUsername());
            for (int j = 0; j < comments.size(); j++) {
                Comment comment = comments.get(j);
                comment.setDeleted(1);
                CommentModel.update_comment(comment);
            }
            
            List<Order> orders = OrderModel.get_all_ordered_orders_for_farmer(current_farmer.getUsername());
            
            for (int j = 0; j < orders.size(); j++) {
                Order o = orders.get(j);
                List<ItemOrder> items_in_order = ItemOrderModel.get_items_for_order(o.getId_order());
                for (int k = 0; k < items_in_order.size(); k++) {
                    ItemOrder item_in_order = items_in_order.get(k);
                    
                    item_in_order.setStatus('c');
                    
                    Item it = ItemModel.get_item_by_id(item_in_order.getId_item());
                    it.setAmount(it.getAmount() + item_in_order.getAmount());
                    
                    ItemModel.update_item(it);
                    ItemOrderModel.update_item_order(item_in_order);
                }
                o.setPriority(0);
                o.setStatus('c');
                OrderModel.update_order(o);
            }
            
            orders = OrderModel.get_all_waiting_orders_for_farmer(current_farmer.getUsername());
            
            for (int j = 0; j < orders.size(); j++) {
                Order o = orders.get(j);
                List<ItemOrder> items_in_order = ItemOrderModel.get_items_for_order(o.getId_order());
                for (int k = 0; k < items_in_order.size(); k++) {
                    ItemOrder item_in_order = items_in_order.get(k);
                    item_in_order.setStatus('c');
                    
                    Item it = ItemModel.get_item_by_id(item_in_order.getId_item());
                    it.setAmount(it.getAmount() + item_in_order.getAmount());
                    
                    ItemModel.update_item(it);
                    ItemOrderModel.update_item_order(item_in_order);
                }
                o.setStatus('c');
                o.setPriority(0);
                OrderModel.update_order(o);
            }
            
            FarmerModel.delete_farmer(current_farmer);
        } else {
            
            List<Item> items = ItemModel.get_all_active_items_for_company(current_company.getShort_name());
            for (int i = 0; i < items.size(); i++) {
                Item it = items.get(i);
                it.setActive(0);
                ItemModel.update_item(it);
            }
            
            List<Order> orders = OrderModel.get_all_ordered_orders_for_company(current_company.getShort_name());
            
            for (int j = 0; j < orders.size(); j++) {
                Order o = orders.get(j);
                List<ItemOrder> items_in_order = ItemOrderModel.get_items_for_order(o.getId_order());
                for (int k = 0; k < items_in_order.size(); k++) {
                    ItemOrder item_in_order = items_in_order.get(k);
                    item_in_order.setStatus('d');
                    
                    Item it = ItemModel.get_item_by_id(item_in_order.getId_item());
                    it.setAmount(it.getAmount() + item_in_order.getAmount());
                    
                    ItemModel.update_item(it);
                    ItemOrderModel.update_item_order(item_in_order);
                }
                o.setStatus('d');
                o.setPriority(0);
                OrderModel.update_order(o);
            }
            
            orders = OrderModel.get_all_waiting_orders_for_company(current_company.getShort_name());
            
            for (int j = 0; j < orders.size(); j++) {
                Order o = orders.get(j);
                List<ItemOrder> items_in_order = ItemOrderModel.get_items_for_order(o.getId_order());
                for (int k = 0; k < items_in_order.size(); k++) {
                    ItemOrder item_in_order = items_in_order.get(k);
                    item_in_order.setStatus('d');
                    
                    Item it = ItemModel.get_item_by_id(item_in_order.getId_item());
                    it.setAmount(it.getAmount() + item_in_order.getAmount());
                    
                    ItemModel.update_item(it);
                    ItemOrderModel.update_item_order(item_in_order);
                }
                o.setStatus('d');
                o.setPriority(0);
                OrderModel.update_order(o);
            }
            
            List<SeedlingInStorage> seedlings_storage = SeedlingInStorageModel.find_seedling_in_storage_for_company(current_company.getShort_name());
            for (int i = 0; i < seedlings_storage.size(); i++) {
                SeedlingInStorageModel.delete_seedling_for_storage(seedlings_storage.get(i));
            }
            
            CompanyModel.delete_company(current_company);
        }
        
        return "admin_all_users?faces-redirect=true";
    }
    
    public void accept(String type, String name) {
        if (type.equals("F")) {
            Farmer f = FarmerModel.find_farmer_by_username(name);
            f.setStatus(1);
            FarmerModel.update_farmer(f);
        } else {
            Company c = CompanyModel.find_company_by_short_name(name);
            c.setStatus(1);
            CompanyModel.update_company(c);
            
            for (int i = 0; i < 5; i++) {
                Courier courier = new Courier();
                courier.setStatus(1);
                courier.setCompany_name(c.getShort_name());
                courier.setCourier_number(i + 1);
                CourierModel.add_courier(courier);
            }
        }
    }
    
    public void decline(String type, String name) {
        if (type.equals("F")) {
            Farmer f = FarmerModel.find_farmer_by_username(name);
            FarmerModel.delete_farmer(f);
        } else {
            Company c = CompanyModel.find_company_by_short_name(name);
            CompanyModel.delete_company(c);
        }
    }
    
    public List<Farmer> getFarmers() {
        return farmers;
    }
    
    public void setFarmers(List<Farmer> farmers) {
        this.farmers = farmers;
    }
    
    public List<Company> getCompanies() {
        return companies;
    }
    
    public void setCompanies(List<Company> companies) {
        this.companies = companies;
    }
    
    public Company getCompany_info() {
        return company_info;
    }
    
    public void setCompany_info(Company company_info) {
        this.company_info = company_info;
    }
    
    public Farmer getFarmer_info() {
        return farmer_info;
    }
    
    public void setFarmer_info(Farmer farmer_info) {
        this.farmer_info = farmer_info;
    }
    
    public Company getCurrent_company() {
        return current_company;
    }
    
    public void setCurrent_company(Company current_company) {
        this.current_company = current_company;
    }
    
    public Farmer getCurrent_farmer() {
        return current_farmer;
    }
    
    public void setCurrent_farmer(Farmer current_farmer) {
        this.current_farmer = current_farmer;
    }
    
    public List<Farmer> getConfirmed_farmers() {
        return confirmed_farmers;
    }
    
    public void setConfirmed_farmers(List<Farmer> confirmed_farmers) {
        this.confirmed_farmers = confirmed_farmers;
    }
    
    public List<Company> getConfirmed_companies() {
        return confirmed_companies;
    }
    
    public void setConfirmed_companies(List<Company> confirmed_companies) {
        this.confirmed_companies = confirmed_companies;
    }
    
    public String getDate_format() {
        return date_format;
    }
    
    public void setDate_format(String date_format) {
        this.date_format = date_format;
    }
    
}
