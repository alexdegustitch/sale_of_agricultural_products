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
public class Farmer {
    
    @Id
    @Column(name="username")
    private String username;
    
    @Column(name="first_name")
    private String first_name;
    
    @Column(name="last_name")
    private String last_name;
    
    @Column(name="password")
    private String password;
    
    @Column(name="date_of_birth")
    @Convert(converter = LocalDateAttributeConverter.class)
    private LocalDate date_of_birth;
    
    @Column(name="place_of_birth")
    private String place_of_birth;
    
    @Column(name="contact_phone")
    private String contact_phone;
    
    @Column(name="email")
    private String email;

    @Column(name="status")
    private int status;
    
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getContact_phone() {
        return contact_phone;
    }

    public void setContact_phone(String contact_phone) {
        this.contact_phone = contact_phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
    
    
    
    
}
