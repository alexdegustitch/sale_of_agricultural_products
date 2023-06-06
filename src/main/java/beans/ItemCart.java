/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author Aleksandar
 */
@Entity
@Table(name = "item_cart")
public class ItemCart {
    
    @Id
    @Column(name ="id_item_cart")
    private int id_item_cart;
    
    @Column(name = "id_cart")
    private int id_cart;
    
    @Column(name = "id_item")
    private int id_item;
    
    @Column(name = "amount")
    private int amount;

    public int getId_item_cart() {
        return id_item_cart;
    }

    public void setId_item_cart(int id_item_cart) {
        this.id_item_cart = id_item_cart;
    }

    public int getId_cart() {
        return id_cart;
    }

    public void setId_cart(int id_cart) {
        this.id_cart = id_cart;
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
    
    
    
}
