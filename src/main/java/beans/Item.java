/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

/**
 *
 * @author Aleksandar
 */
@Entity
public class Item {

    @Id
    @Column(name = "id_item")
    private int id_item;

    @Column(name = "name")
    private String name;

    @Column(name = "company_name")
    private String company_name;

    @Column(name = "days")
    private int days;

    @Column(name = "amount")
    private int amount;

    @Column(name = "price")
    private double price;

    @Column(name = "active")
    private int active;

    @Column(name = "type")
    private char type;

    @Column(name = "mark")
    private double mark;

    @Column(name = "description")
    private String description;

    @Transient
    private int amount_in_storage;

    @Transient
    private char status;

    @Transient
    private String company_full_name;

    
    @Transient
    private int item_order_id;
    
    public int getId_item() {
        return id_item;
    }

    public void setId_item(int id_item) {
        this.id_item = id_item;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public char getType() {
        return type;
    }

    public void setType(char type) {
        this.type = type;
    }

    public int getAmount_in_storage() {
        return amount_in_storage;
    }

    public void setAmount_in_storage(int amount_in_storage) {
        this.amount_in_storage = amount_in_storage;
    }

    public char getStatus() {
        return status;
    }

    public void setStatus(char status) {
        this.status = status;
    }

    public double getMark() {
        return mark;
    }

    public void setMark(double mark) {
        this.mark = mark;
    }

    public String getCompany_full_name() {
        return company_full_name;
    }

    public void setCompany_full_name(String company_full_name) {
        this.company_full_name = company_full_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getItem_order_id() {
        return item_order_id;
    }

    public void setItem_order_id(int item_order_id) {
        this.item_order_id = item_order_id;
    }

    
}
