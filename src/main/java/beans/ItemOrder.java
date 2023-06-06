/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

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
@Table(name = "item_order")
public class ItemOrder {
    
    @Id
    @Column(name = "id_item_order")
    private int id_item_order;
    
    @Column(name = "id_order")
    private int id_order;
    
    @Column(name = "id_item")
    private int id_item;
    
    @Column(name = "amount")
    private int amount;

    @Column(name = "item_name")
    private String item_name;
    
    @Column(name = "price")
    private double price;
    
    @Column(name = "status")
    private char status;
    
    @Column(name = "farmer_username")
    private String farmer_username;
    
    @Column(name = "id_nursery")
    private int id_nursery;
    
    
    public int getId_item_order() {
        return id_item_order;
    }

    public void setId_item_order(int id_item_order) {
        this.id_item_order = id_item_order;
    }

    public int getId_order() {
        return id_order;
    }

    public void setId_order(int id_order) {
        this.id_order = id_order;
    }

    public int getId_item() {
        return id_item;
    }

    public void setId_item(int id_item) {
        this.id_item = id_item;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public char getStatus() {
        return status;
    }

    public void setStatus(char status) {
        this.status = status;
    }

    public String getFarmer_username() {
        return farmer_username;
    }

    public void setFarmer_username(String farmer_username) {
        this.farmer_username = farmer_username;
    }

    public int getId_nursery() {
        return id_nursery;
    }

    public void setId_nursery(int id_nursery) {
        this.id_nursery = id_nursery;
    }
    
    
}
