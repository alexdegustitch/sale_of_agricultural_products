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
public class Mark {
    
    @Id
    @Column(name = "id_mark")
    private int id_mark;
    
    @Column(name = "id_item")
    private int id_item;
    
    @Column(name = "farmer_username")
    private String farmer_username;
    
    @Column(name = "mark")
    private int mark;

    public int getId_mark() {
        return id_mark;
    }

    public void setId_mark(int id_mark) {
        this.id_mark = id_mark;
    }

    public int getId_item() {
        return id_item;
    }

    public void setId_item(int id_item) {
        this.id_item = id_item;
    }

    public String getFarmer_username() {
        return farmer_username;
    }

    public void setFarmer_username(String farmer_username) {
        this.farmer_username = farmer_username;
    }

    public int getMark() {
        return mark;
    }

    public void setMark(int mark) {
        this.mark = mark;
    }
    
    
}
