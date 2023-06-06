/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import util.LocalDateAttributeConverter;

/**
 *
 * @author Aleksandar
 */
@Entity
public class Comment {
    
    @Id
    @Column(name = "id_comment")
    private int id_comment;
    
    @Column(name = "id_item")
    private int id_item;
    
    @Column(name  = "farmer_username")
    private String farmer_username;
    
    @Column(name = "comment")
    private String comment;

    @Column(name = "date_of_comment")
    @Convert(converter = LocalDateAttributeConverter.class)
    private LocalDate date_of_comment;
    
    @Transient
    private int mark = 0;
    
    @Column(name = "deleted")
    private int deleted;
    
    public int getId_comment() {
        return id_comment;
    }

    public void setId_comment(int id_comment) {
        this.id_comment = id_comment;
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getMark() {
        return mark;
    }

    public void setMark(int mark) {
        this.mark = mark;
    }

    public LocalDate getDate_of_comment() {
        return date_of_comment;
    }

    public void setDate_of_comment(LocalDate date_of_comment) {
        this.date_of_comment = date_of_comment;
    }

    public int getDeleted() {
        return deleted;
    }

    public void setDeleted(int deleted) {
        this.deleted = deleted;
    }

   
    
    
  
}
