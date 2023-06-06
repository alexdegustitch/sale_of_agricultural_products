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
import util.LocalDateTimeAttributeConverter;

/**
 *
 * @author Aleksandar
 */
@Entity
public class Courier {

    @Id
    @Column(name = "id_courier")
    private int id_courier;

    @Column(name = "company_name")
    private String company_name;

    @Column(name = "courier_number")
    private int courier_number;

    @Column(name = "status")
    private int status;

    @Column(name = "free_at_time")
    @Convert(converter = LocalDateTimeAttributeConverter.class)
    private LocalDateTime free_at_time;

    @Column(name = "delivery_time")
    @Convert(converter = LocalDateTimeAttributeConverter.class)
    private LocalDateTime delivery_time;

    @Column(name = "free_after_gifts")
    @Convert(converter = LocalDateTimeAttributeConverter.class)
    private LocalDateTime free_after_gifts;
    
    public int getId_courier() {
        return id_courier;
    }

    public void setId_courier(int id_courier) {
        this.id_courier = id_courier;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public int getCourier_number() {
        return courier_number;
    }

    public void setCourier_number(int courier_number) {
        this.courier_number = courier_number;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public LocalDateTime getFree_at_time() {
        return free_at_time;
    }

    public void setFree_at_time(LocalDateTime free_at_time) {
        this.free_at_time = free_at_time;
    }

    public LocalDateTime getDelivery_time() {
        return delivery_time;
    }

    public void setDelivery_time(LocalDateTime delivery_time) {
        this.delivery_time = delivery_time;
    }

    public LocalDateTime getFree_after_gifts() {
        return free_after_gifts;
    }

    public void setFree_after_gifts(LocalDateTime free_after_gifts) {
        this.free_after_gifts = free_after_gifts;
    }

    
    
}
