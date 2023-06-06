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
import util.LocalDateAttributeConverter;
import util.LocalDateTimeAttributeConverter;

/**
 *
 * @author Aleksandar
 */
@Entity
public class Cart {
    
    @Id
    @Column(name = "id_cart")
    private int id_cart;
    
    @Column(name = "farmer_username")
    private String farmer_username;
    
    @Column(name = "cart_start_time")
    @Convert(converter = LocalDateTimeAttributeConverter.class)
    private LocalDateTime cart_start_time;

    public int getId_cart() {
        return id_cart;
    }

    public void setId_cart(int id_cart) {
        this.id_cart = id_cart;
    }

    public String getFarmer_username() {
        return farmer_username;
    }

    public void setFarmer_username(String farmer_username) {
        this.farmer_username = farmer_username;
    }

    public LocalDateTime getCart_start_time() {
        return cart_start_time;
    }

    public void setCart_start_time(LocalDateTime cart_start_time) {
        this.cart_start_time = cart_start_time;
    }
    
    
    
}
