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
@Table(name = "friendly_seedling")
public class FriendlySeedling {
    
    @Id
    @Column(name = "id_friendly_seedling")
    private int id_friendly_seedling;
    
    @Column(name = "id_seedling1")
    private int id_seedling1;
    
    @Column(name = "id_seedling2")
    private int id_seedling2;

    public int getId_friendly_seedling() {
        return id_friendly_seedling;
    }

    public void setId_friendly_seedling(int id_friendly_seedling) {
        this.id_friendly_seedling = id_friendly_seedling;
    }

    public int getId_seedling1() {
        return id_seedling1;
    }

    public void setId_seedling1(int id_seedling1) {
        this.id_seedling1 = id_seedling1;
    }

    public int getId_seedling2() {
        return id_seedling2;
    }

    public void setId_seedling2(int id_seedling2) {
        this.id_seedling2 = id_seedling2;
    }
    
    
}
