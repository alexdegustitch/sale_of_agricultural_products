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
public class Storage {
    
    @Id
    @Column(name = "id_storage")
    private int id_storage;
    
    @Column(name = "id_nursery")
    private int id_nursery;
    
    @Column(name = "id_item")
    private int id_item;
    
    @Column(name = "amount")
    private int amount;

  
    
    
    public int getId_storage() {
        return id_storage;
    }

    public void setId_storage(int id_storage) {
        this.id_storage = id_storage;
    }

    public int getId_nursery() {
        return id_nursery;
    }

    public void setId_nursery(int id_nursery) {
        this.id_nursery = id_nursery;
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
