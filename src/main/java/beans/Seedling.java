/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 *
 * @author Aleksandar
 */
@Entity
public class Seedling {
    
    @Id
    @Column(name = "id_seedling")
    private int id_seedling;
    
    @Column(name = "name")
    private String name;
    
    @Column(name = "company_name")
    private String company_name;
    
    @Column(name = "harvest_days")
    private int harvest_days;

    public int getId_seedling() {
        return id_seedling;
    }

    public void setId_seedling(int id_seedling) {
        this.id_seedling = id_seedling;
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

    public int getHarvest_days() {
        return harvest_days;
    }

    public void setHarvest_days(int harvest_days) {
        this.harvest_days = harvest_days;
    }
    
    
}
