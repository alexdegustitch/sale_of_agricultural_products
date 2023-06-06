/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import beans.Company;
import beans.Courier;
import beans.Farmer;
import java.io.IOException;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import models.CompanyModel;
import models.CourierModel;
import models.FarmerModel;
import recaptcha.VerifyRecaptcha;

/**
 *
 * @author Aleksandar
 */
@ManagedBean
@ViewScoped
public class AdminAddUserController {
    
    private String first_name;
    private String last_name;
    private String username;
    private String password;
    private String confirmPassword;
    private LocalDate date_of_birth;
    private String place_of_birth;
    private String message;
    private String email;
    private String contact_number;
    private boolean captcha;
    private boolean enabled;
    private String captcha_response;
    
    private String short_name;
    private String full_name;
    private LocalDate date_of_founding;
    private String place_of_the_company;
    
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
    
    public Boolean validate_email() {
        return email.equals("hej");
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
    
    private Boolean check_only_letters(String name) {
        String regex = "[A-Za-z]+";
        return name.matches(regex);
    }
    
    public String register_farmer() {
        try {
            
            boolean password_pass = check_password(password);
            
            boolean place = check_only_letters(place_of_birth);
            boolean firstName = check_only_letters(first_name);
            boolean lastName = check_only_letters(last_name);
            boolean email_check = check_email(this.email);
            boolean phone_num = check_phone_number(contact_number);

            //System.out.println("number: " + contact_number);
            Farmer f = FarmerModel.find_farmer_by_username(username);
            Company c = CompanyModel.find_company_by_short_name(username);
            
            boolean username_not_taken = f == null && c == null;
            
            if (password_pass && place && firstName && lastName && email_check && phone_num && username_not_taken) {
                Farmer farmer = new Farmer();
                
                farmer.setFirst_name(first_name);
                farmer.setLast_name(last_name);
                farmer.setPassword(password);
                farmer.setPlace_of_birth(place_of_birth);
                farmer.setEmail(email);
                farmer.setUsername(username);
                farmer.setContact_phone(contact_number);
                farmer.setDate_of_birth(date_of_birth);
                farmer.setStatus(1);
                
                FarmerModel.add_farmer(farmer);
                return "admin_all_users";
            } else {
                
                if (!username_not_taken) {
                    username = null;
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_WARN, "Username already taken.", "Username already taken."));
                }
                
                if (!firstName) {
                    first_name = null;
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR, "First name must contain only letters.", "First name must contain only letters."));
                }
                
                if (!lastName) {
                    last_name = null;
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Last name must contain only letters.", "Last name must contain only letters."));
                }
                
                if (!password_pass) {
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Password must meet complexity requirements.", "Password must meet complexity requirements."));
                }
                
                if (!place) {
                    place_of_birth = null;
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Place of birth must contain only letters.", "Place of birth must contain only letters."));
                }
                
                if (!phone_num) {
                    contact_number = null;
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR, "You must enter valid phone number.", "You must enter valid phone number"));
                }
                
                if (!email_check) {
                    email = null;
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR, "You must enter valid e-mail address.", "You must enter valid e-mail address."));
                }
                
                return null;
                
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        
    }
    
    public String register_company() {
        try {

            //System.out.println("ok");
            //System.out.println("recaptcha : " + gRecaptchaResponse);
            boolean password_pass = check_password(password);
            
            boolean place = check_only_letters(place_of_the_company);
            
            boolean email_check = check_email(email);

            //System.out.println("number: " + contact_number);
            Farmer f = FarmerModel.find_farmer_by_username(username);
            Company c = CompanyModel.find_company_by_short_name(username);
            
            boolean username_not_taken = f == null && c == null;
            
            if (password_pass && email_check && username_not_taken) {
                Company company = new Company();
                
                company.setFull_name(full_name);
                company.setShort_name(short_name);
                company.setPassword(password);
                company.setPlace_of_the_company(place_of_the_company);
                company.setDate_of_founding(date_of_founding);
                company.setFree_couriers(5);
                company.setEmail(email);
                company.setStatus(1);
                CompanyModel.add_company(company);
                
                for (int i = 0; i < 5; i++) {
                    Courier courier = new Courier();
                    courier.setCompany_name(company.getShort_name());
                    courier.setCourier_number(i + 1);
                    courier.setStatus(1);
                    CourierModel.add_courier(courier);
                }
                //RequestModel.add_request(short_name, 2);
                return "admin_all_users";
            } else {
                
                if (!username_not_taken) {
                    short_name = null;
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_WARN, "Short name already taken.", "Short name already taken."));
                }
                
                if (!password_pass) {
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Password must meet complexity requirements.", "Password must meet complexity requirements."));
                }
                
                /*if (!place) {
                    place_of_the_company = null;
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Place of the company must contain only letters.", "Place of the company must contain only letters."));
                }*/
                
                if (!email_check) {
                    email = null;
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR, "You must enter valid e-mail address.", "You must enter valid e-mail address."));
                }
                
                return null;
                
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        
    }
    
    public String getMessage() {
        return message;
    }
    
    public String proba() {
        System.out.println("sve ok");
        return null;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public String getConfirmPassword() {
        return confirmPassword;
    }
    
    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
    
    public String getFirst_name() {
        return first_name;
    }
    
    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }
    
    public String getLast_name() {
        return last_name;
    }
    
    public void setLast_name(String last_name) {
        this.last_name = last_name;
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
    
    public LocalDate getDate_of_birth() {
        return date_of_birth;
    }
    
    public void setDate_of_birth(LocalDate date_of_birth) {
        this.date_of_birth = date_of_birth;
    }
    
    public String getPlace_of_birth() {
        return place_of_birth;
    }
    
    public void setPlace_of_birth(String place_of_birth) {
        this.place_of_birth = place_of_birth;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getContact_number() {
        return contact_number;
    }
    
    public void setContact_number(String contact_number) {
        this.contact_number = contact_number;
    }
    
    public boolean isCaptcha() {
        return captcha;
    }
    
    public void setCaptcha(boolean captcha) {
        this.captcha = captcha;
    }
    
    public boolean isEnabled() {
        return enabled;
    }
    
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    
    public String getCaptcha_response() {
        return captcha_response;
    }
    
    public void setCaptcha_response(String captcha_response) {
        this.captcha_response = captcha_response;
    }
    
    public String getShort_name() {
        return short_name;
    }
    
    public void setShort_name(String short_name) {
        this.short_name = short_name;
    }
    
    public String getFull_name() {
        return full_name;
    }
    
    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }
    
    public LocalDate getDate_of_founding() {
        return date_of_founding;
    }
    
    public void setDate_of_founding(LocalDate date_of_founding) {
        this.date_of_founding = date_of_founding;
    }
    
    public String getPlace_of_the_company() {
        return place_of_the_company;
    }
    
    public void setPlace_of_the_company(String place_of_the_company) {
        this.place_of_the_company = place_of_the_company;
    }
    
}
