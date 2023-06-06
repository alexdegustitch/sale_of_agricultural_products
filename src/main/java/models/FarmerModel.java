/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import beans.Farmer;
import java.time.LocalDate;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import util.HibernateUtil;

/**
 *
 * @author Aleksandar
 */
public class FarmerModel {

    public static void add_farmer(Farmer farmer) {
        SessionFactory factory = HibernateUtil.getSessionFactory();
        Session session = factory.openSession();

        session.beginTransaction();

        
        session.save(farmer);

        session.getTransaction().commit();
        session.close();

    }

    public static List<Farmer> get_uncomfirmed_farmers() {
        SessionFactory factory = HibernateUtil.getSessionFactory();
        Session session = factory.openSession();

        session.beginTransaction();

        Criteria cr = session.createCriteria(Farmer.class);

        List<Farmer> farmers;

        farmers = cr.add(Restrictions.eq("status", 0)).list();

        session.getTransaction().commit();
        session.close();

        return farmers;
    }

    public static List<Farmer> get_comfirmed_farmers() {
        SessionFactory factory = HibernateUtil.getSessionFactory();
        Session session = factory.openSession();

        session.beginTransaction();

        Criteria cr = session.createCriteria(Farmer.class);

        List<Farmer> farmers;

        farmers = cr.add(Restrictions.eq("status", 1)).list();

        session.getTransaction().commit();
        session.close();

        return farmers;
    }
    
    public static Farmer find_farmer_by_username(String username) {
        SessionFactory factory = HibernateUtil.getSessionFactory();
        Session session = factory.openSession();

        session.beginTransaction();

        Criteria cr = session.createCriteria(Farmer.class);

        cr.add(Restrictions.eq("username", username));

        Farmer farmer = (Farmer) cr.uniqueResult();

        session.getTransaction().commit();
        session.close();
        return farmer;
    }

    public static void update_farmer(Farmer f) {
        SessionFactory factory = HibernateUtil.getSessionFactory();
        Session session = factory.openSession();

        session.beginTransaction();

        session.update(f);

        session.getTransaction().commit();
        session.close();

    }
    
    public static void delete_farmer(Farmer f) {
        SessionFactory factory = HibernateUtil.getSessionFactory();
        Session session = factory.openSession();

        session.beginTransaction();

        session.delete(f);

        session.getTransaction().commit();
        session.close();

    }
}
