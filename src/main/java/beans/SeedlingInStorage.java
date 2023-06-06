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
@Table(name = "seedling_in_storage")
public class SeedlingInStorage {
    
    
    @Id
    @Column(name = "id_seedling_storage")
    private int id_seedling_storage;
    
    @Column(name = "company_name")
    private String company_name;
    
    @Column(name = "id_item")
    private int id_item;
    
    @Column(name = "id_storage")
    private int id_storage;

    public int getId_seedling_storage() {
        return id_seedling_storage;
    }

    public void setId_seedling_storage(int id_seedling_storage) {
        this.id_seedling_storage = id_seedling_storage;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public int getId_item() {
        return id_item;
    }

    public void setId_item(int id_item) {
        this.id_item = id_item;
    }

    public int getId_storage() {
        return id_storage;
    }

    public void setId_storage(int id_storage) {
        this.id_storage = id_storage;
    }
    
    
}
