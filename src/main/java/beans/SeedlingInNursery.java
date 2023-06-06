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
import util.LocalDateAttributeConverter;
import util.LocalDateTimeAttributeConverter;

/**
 *
 * @author Aleksandar
 */
@Entity
@Table(name = "seedling_in_nursery")
public class SeedlingInNursery {

    @Id
    @Column(name = "id_seedling_nursery")
    private int id_seedling_nursery;

    @Column(name = "id_nursery")
    private int id_nursery;

    @Column(name = "id_seedling")
    private int id_seedling;

    @Column(name = "plot_number")
    private int plot_number;

    @Column(name = "planted_time")
    @Convert(converter = LocalDateTimeAttributeConverter.class)
    private LocalDateTime planted_time;

    @Column(name = "harvested_time")
    @Convert(converter = LocalDateTimeAttributeConverter.class)
    private LocalDateTime harvested_time;

    @Column(name = "active")
    private int active;

    public int getId_seedling_nursery() {
        return id_seedling_nursery;
    }

    public void setId_seedling_nursery(int id_seedling_nursery) {
        this.id_seedling_nursery = id_seedling_nursery;
    }

    public int getId_nursery() {
        return id_nursery;
    }

    public void setId_nursery(int id_nursery) {
        this.id_nursery = id_nursery;
    }

    public int getId_seedling() {
        return id_seedling;
    }

    public void setId_seedling(int id_seedling) {
        this.id_seedling = id_seedling;
    }

    public int getPlot_number() {
        return plot_number;
    }

    public void setPlot_number(int plot_number) {
        this.plot_number = plot_number;
    }

    public LocalDateTime getPlanted_time() {
        return planted_time;
    }

    public void setPlanted_time(LocalDateTime planted_time) {
        this.planted_time = planted_time;
    }

    public LocalDateTime getHarvested_time() {
        return harvested_time;
    }

    public void setHarvested_time(LocalDateTime harvest_time) {
        this.harvested_time = harvest_time;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

}
