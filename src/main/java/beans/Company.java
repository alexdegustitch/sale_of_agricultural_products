/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import util.LocalDateAttributeConverter;

/**
 *
 * @author Aleksandar
 */
@Entity
public class Company {

    @Id
    @Column(name = "short_name")
    private String short_name;

    @Column(name = "full_name")
    private String full_name;

    @Column(name = "password")
    private String password;

    @Column(name = "date_of_founding")
    @Convert(converter = LocalDateAttributeConverter.class)
    private LocalDate date_of_founding;

    @Column(name = "place_of_the_company")
    private String place_of_the_company;

    @Column(name = "email")
    private String email;

    @Column(name = "free_couriers")
    private int free_couriers;

    @Column(name = "status")
    private int status;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public void setPlace_of_the_company(String place_of_company) {
        this.place_of_the_company = place_of_company;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getFree_couriers() {
        return free_couriers;
    }

    public void setFree_couriers(int free_couriers) {
        this.free_couriers = free_couriers;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    
}
