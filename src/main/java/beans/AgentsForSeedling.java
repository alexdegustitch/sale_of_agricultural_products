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
@Table(name = "agents_for_seedling")
public class AgentsForSeedling {

    @Id
    @Column(name = "id_agents_seedling")
    private int id_agents_seedling;

    @Column(name = "id_seedling_nursery")
    private int id_seedling_nursery;

    @Column(name = "id_item")
    private int id_item;

    public int getId_agents_seedling() {
        return id_agents_seedling;
    }

    public void setId_agents_seedling(int id_agents_seedling) {
        this.id_agents_seedling = id_agents_seedling;
    }

    public int getId_seedling_nursery() {
        return id_seedling_nursery;
    }

    public void setId_seedling_nursery(int id_seedling_nursery) {
        this.id_seedling_nursery = id_seedling_nursery;
    }

    public int getId_item() {
        return id_item;
    }

    public void setId_item(int id_item) {
        this.id_item = id_item;
    }

    
    
}
