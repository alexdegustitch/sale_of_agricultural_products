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
import javax.persistence.Transient;

/**
 *
 * @author Aleksandar
 */
@Entity
@Table(name = "nursery_garden")
public class NurseryGarden {

    @Id
    @Column(name = "id_garden")
    private int id_garden;

    @Column(name = "name")
    private String name;

    @Column(name = "location")
    private String location;

    @Column(name = "length")
    private int length;

    @Column(name = "width")
    private int width;

    @Column(name = "planted_seedling_number")
    private int planted_seedling_numer;

    @Column(name = "capacity")
    private int capacity;

    @Column(name = "amount_of_water")
    private int amount_of_water;

    @Column(name = "temperature")
    private double temperature;

    @Column(name = "farmer_username")
    private String farmer_username;

    @Transient
    private int free_planting_holes;

    @Transient
    private int not_free_planting_holes;

    @Column(name = "farmer_informed_w")
    private int farmer_informed_w;

    @Column(name = "farmer_informed_t")
    private int farmer_informed_t;

    public int getId_garden() {
        return id_garden;
    }

    public void setId_garden(int id_garden) {
        this.id_garden = id_garden;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getPlanted_seedling_numer() {
        return planted_seedling_numer;
    }

    public void setPlanted_seedling_numer(int planted_seedling_numer) {
        this.planted_seedling_numer = planted_seedling_numer;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getAmount_of_water() {
        return amount_of_water;
    }

    public void setAmount_of_water(int amount_of_water) {
        this.amount_of_water = amount_of_water;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public String getFarmer_username() {
        return farmer_username;
    }

    public void setFarmer_username(String farmer_username) {
        this.farmer_username = farmer_username;
    }

    public int getFree_planting_holes() {
        return free_planting_holes;
    }

    public void setFree_planting_holes(int free_planting_holes) {
        this.free_planting_holes = free_planting_holes;
    }

    public int getNot_free_planting_holes() {
        return not_free_planting_holes;
    }

    public void setNot_free_planting_holes(int not_free_planting_holes) {
        this.not_free_planting_holes = not_free_planting_holes;
    }

    public int getFarmer_informed_w() {
        return farmer_informed_w;
    }

    public void setFarmer_informed_w(int farmer_informed_w) {
        this.farmer_informed_w = farmer_informed_w;
    }

    public int getFarmer_informed_t() {
        return farmer_informed_t;
    }

    public void setFarmer_informed_t(int farmer_informed_t) {
        this.farmer_informed_t = farmer_informed_t;
    }

    
}
