/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import beans.Request;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import util.HibernateUtil;

/**
 *
 * @author Aleksandar
 */
public class RequestModel {
    
    
    public static void add_request(String username, int type){
        
        SessionFactory factory = HibernateUtil.getSessionFactory();
        
        Session session = factory.openSession();
        
        session.beginTransaction();
        
        Request request = new Request();
        
        request.setType(type);
        request.setUsername(username);
          
        session.save(request);
        
        session.getTransaction().commit();
        session.close();
    }
}
