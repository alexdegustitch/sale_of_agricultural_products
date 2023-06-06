/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import util.LocalDateAttributeConverter;
import util.LocalDateTimeAttributeConverter;

/**
 *
 * @author Aleksandar
 */
@Entity
@Table(name = "\"Order\"")
public class Order {

    @Id
    @Column(name = "id_order")
    private int id_order;

    @Column(name = "farmer_username")
    private String farmer_username;

    @Column(name = "date_of_order")
    @Convert(converter = LocalDateTimeAttributeConverter.class)
    private LocalDateTime date_of_order;

    @Column(name = "id_nursery")
    private int id_nursery;

    @Column(name = "price")
    private double price;

    @Column(name = "company_name")
    private String company_name;

    @Column(name = "status")
    private char status;

    @Column(name = "priority")
    private int priority;

    @Column(name = "date")
    @Convert(converter = LocalDateAttributeConverter.class)
    private LocalDate date;
    
    public int getId_order() {
        return id_order;
    }

    public void setId_order(int id_order) {
        this.id_order = id_order;
    }

    public String getFarmer_username() {
        return farmer_username;
    }

    public void setFarmer_username(String farmer_username) {
        this.farmer_username = farmer_username;
    }

    public LocalDateTime getDate_of_order() {
        return date_of_order;
    }

    public void setDate_of_order(LocalDateTime date_of_order) {
        this.date_of_order = date_of_order;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getId_nursery() {
        return id_nursery;
    }

    public void setId_nursery(int id_nursery) {
        this.id_nursery = id_nursery;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public char getStatus() {
        return status;
    }

    public void setStatus(char status) {
        this.status = status;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    
}
