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
import util.LocalDateTimeAttributeConverter;

/**
 *
 * @author Aleksandar
 */
@Entity
@Table(name = "courier_order")
public class CourierOrder {

    @Id
    @Column(name = "id_courier_order")
    private int id_courier_order;

    @Column(name = "id_order")
    private int id_order;

    @Column(name = "id_courier")
    private int id_courier;

    @Column(name = "start_time")
    @Convert(converter = LocalDateTimeAttributeConverter.class)
    private LocalDateTime start_time;

    @Column(name = "end_time")
    @Convert(converter = LocalDateTimeAttributeConverter.class)
    private LocalDateTime end_time;

    @Column(name = "order_arrived")
    private int order_arrived;

    @Column(name = "is_gift")
    private int is_gift;
    
    public int getId_courier_order() {
        return id_courier_order;
    }

    public void setId_courier_order(int id_courier_order) {
        this.id_courier_order = id_courier_order;
    }

    public int getId_order() {
        return id_order;
    }

    public void setId_order(int id_order) {
        this.id_order = id_order;
    }

    public int getOrder_arrived() {
        return order_arrived;
    }

    public void setOrder_arrived(int order_arrived) {
        this.order_arrived = order_arrived;
    }

    public int getId_courier() {
        return id_courier;
    }

    public void setId_courier(int id_courier) {
        this.id_courier = id_courier;
    }

    public LocalDateTime getStart_time() {
        return start_time;
    }

    public void setStart_time(LocalDateTime start_time) {
        this.start_time = start_time;
    }

    public LocalDateTime getEnd_time() {
        return end_time;
    }

    public void setEnd_time(LocalDateTime end_time) {
        this.end_time = end_time;
    }

    public int getIs_gift() {
        return is_gift;
    }

    public void setIs_gift(int is_gift) {
        this.is_gift = is_gift;
    }

    
}
