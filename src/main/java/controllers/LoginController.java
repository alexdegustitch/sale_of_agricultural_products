/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import beans.Admin;
import beans.Cart;
import beans.Company;
import beans.Farmer;
import beans.Item;
import beans.ItemCart;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpSession;
import models.AdminModel;
import models.CartModel;
import models.CompanyModel;
import models.FarmerModel;
import models.ItemCartModel;
import models.ItemModel;

/**
 *
 * @author Aleksandar
 */
@ManagedBean
@ViewScoped
public class LoginController {

    private String username;
    private String password;

    private String old_password;
    private String new_passowrd;
    private String confirm_new_password;

    private Boolean check_password(String password) {
        String regex = "(?=.*[A-Z])(?=.*\\d)(?=.*[-+()[\\\\]~!@#$%^&*+}{\":;'/?.><,`\\\\\\\\])([A-Z]|[a-z])[A-Za-z\\d-+()[\\\\]~!@#$%^&*+}{\":;'/?.><,`\\\\\\\\]{6,50}";
        return password.matches(regex);
    }

    public void sendFromGMail() {
        String from = "paripovic.aleks@gmail.com";
        String pass = "akiaki84908590";
        String[] to = {"pa160417d@student.etf.bg.ac.rs"};
        String subject = "Proba";
        String body = "RADI!";
        
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
            for (int i = 0; i < to.length; i++) {
                toAddress[i] = new InternetAddress(to[i]);
            }

            for (int i = 0; i < toAddress.length; i++) {
                message.addRecipient(Message.RecipientType.TO, toAddress[i]);
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

    public void check() {
        try {

            String host = "pop.gmail.com";// change accordingly
            String mailStoreType = "pop3s";
            String user = "paripovic.aleks@gmail.com";// change accordingly
            String password = "akiaki84908590";// change accordingly
            //create properties field
            Properties properties = System.getProperties();

            properties.setProperty("mail.pop3s.host", host);
            properties.setProperty("mail.pop3s.port", "995");
            properties.setProperty("mail.pop3s.starttls.enable", "true");
            properties.setProperty("mail.pop3s.ssl.trust", host); // add this line
            properties.setProperty("mail.pop3s.ssl.trust", "*");
            Session emailSession = Session.getInstance(properties);
            emailSession.setDebug(true);

            //create the POP3 store object and connect with the pop server
            Store store = emailSession.getStore("pop3s");

            store.connect(host, user, password);

            //create the folder object and open it
            Folder emailFolder = store.getFolder("INBOX");
            emailFolder.open(Folder.READ_ONLY);

            // retrieve the messages from the folder in an array and print it
            Message[] messages = emailFolder.getMessages();
            System.out.println("messages.length---" + messages.length);

            for (int i = 0, n = 10; i < n; i++) {
                Message message = messages[i];
                System.out.println("---------------------------------");
                System.out.println("Email Number " + (i + 1));
                System.out.println("Subject: " + message.getSubject());
                System.out.println("From: " + message.getFrom()[0]);
                System.out.println("Text: " + message.getContent().toString());

            }

            //close the store and folder objects
            emailFolder.close(false);
            store.close();

        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String change_password(String type) {

        switch (type) {
            case "a":
                HttpSession session = util.SessionUtils.getSession();
                Admin admin = (Admin) session.getAttribute("admin");

                if (!admin.getAdmin_password().equals(old_password)) {
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Old password is not correct.", "Old password is not correct."));
                    return null;
                }
                
                if(old_password.equals(new_passowrd)){
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR, "New password must be different from the old one.", "New password must be different from the old one."));
                    return null;
                }
                

                /*if (!check_password(new_passowrd)) {
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_WARN, "Password must meet complexity requirements.", "Password must meet complexity requirements."));
                    return null;
                }*/
                admin.setAdmin_password(new_passowrd);
                AdminModel.update_admin(admin);

                session.removeAttribute("admin");

                return "index?faces-redirect=true";

            case "c":
                HttpSession session_c = util.SessionUtils.getSession();
                Company company = (Company) session_c.getAttribute("company");

                if (!company.getPassword().equals(old_password)) {
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Old password is not correct.", "Old password is not correct."));
                    return null;
                }

                if(old_password.equals(new_passowrd)){
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR, "New password must be different from the old one.", "New password must be different from the old one."));
                    return null;
                }
                
                
                if (!check_password(new_passowrd)) {
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR, "New password must meet complexity requirements.", "New password must meet complexity requirements."));
                    return null;
                }

                company.setPassword(new_passowrd);
                CompanyModel.update_company(company);

                session_c.removeAttribute("company");

                return "index?faces-redirect=true";

            default:
                HttpSession session_f = util.SessionUtils.getSession();
                Farmer farmer = (Farmer) session_f.getAttribute("user");

                System.out.println(farmer.getPassword());
                System.out.println(old_password);
                if (!farmer.getPassword().equals(old_password)) {
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Old password is not correct.", "Old password is not correct."));
                    return null;
                }

                if(old_password.equals(new_passowrd)){
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR, "New password must be different from the old one.", "New password must be different from the old one."));
                    return null;
                }
                
                if (!check_password(new_passowrd)) {
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR, "New password must meet complexity requirements.", "New password must meet complexity requirements."));
                    return null;
                }

                System.out.println("BRISANJEEE");
                farmer.setPassword(new_passowrd);
                FarmerModel.update_farmer(farmer);

                session_f.removeAttribute("user");
                session_f.removeAttribute("user_nursery");

                return "index?faces-redirect=true";
        }

    }

    public String logout(String type) {

        switch (type) {
            case "a":
                HttpSession session = util.SessionUtils.getSession();
                session.removeAttribute("type");
                session.removeAttribute("admin");

                return "index?faces-redirect=true";
            case "c":
                HttpSession session_c = util.SessionUtils.getSession();
                session_c.removeAttribute("type");
                session_c.removeAttribute("company");

                return "index?faces-redirect=true";
            default:
                HttpSession session_f = util.SessionUtils.getSession();

                Farmer f = (Farmer) session_f.getAttribute("user");

                Cart cart = CartModel.get_cart_for_user(f.getUsername());

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

                session_f.removeAttribute("user");
                session_f.removeAttribute("user_nursery");
                session_f.removeAttribute("type");

                return "index?faces-redirect=true";
        }
    }

    public String login() {

        Admin admin = AdminModel.find_admin_by_username(username);

        Farmer farmer = FarmerModel.find_farmer_by_username(username);
        Company company = CompanyModel.find_company_by_short_name(username);

        if (admin != null && admin.getAdmin_password().equals(password)) {
            HttpSession session = util.SessionUtils.getSession();
            session.setAttribute("admin", admin);
            session.setAttribute("type", "a");

            return "admin?faces-redirect=true";
        }

        if (farmer == null && company == null) {
            username = null;
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "No user found with given username/company name.", "No user found with given username/company name."));
            return null;
        }

        if (farmer != null) {
            if (farmer.getPassword().equals(password)) {

                if (farmer.getStatus() == 0) {
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Your registration request is still not accepted.", "Your registration request is still not accepted."));
                    return null;
                }

                HttpSession session = util.SessionUtils.getSession();
                session.setAttribute("user", farmer);
                session.setAttribute("type", "f");
                return "farmer?faces-redirect=true";
            } else {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Incorrect password.", "Incorrect password."));
                return null;
            }

        }
        if (company != null) {
            if (company.getPassword().equals(password)) {

                if (company.getStatus() == 0) {
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Your registration request is still not accepted.", "Your registration request is still not accepted."));
                    return null;
                }

                HttpSession session = util.SessionUtils.getSession();
                session.setAttribute("company", company);
                session.setAttribute("type", "c");
                return "company?faces-redirect=true";
            } else {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Incorrect password.", "Incorrect password."));
                return null;
            }

        }
        return null;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getOld_password() {
        return old_password;
    }

    public void setOld_password(String old_password) {
        this.old_password = old_password;
    }

    public String getNew_passowrd() {
        return new_passowrd;
    }

    public void setNew_passowrd(String new_passowrd) {
        this.new_passowrd = new_passowrd;
    }

    public String getConfirm_new_password() {
        return confirm_new_password;
    }

    public void setConfirm_new_password(String confirm_new_password) {
        this.confirm_new_password = confirm_new_password;
    }

}
