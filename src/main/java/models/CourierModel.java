/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import beans.Courier;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import util.HibernateUtil;

/**
 *
 * @author Aleksandar
 */
public class CourierModel {

    public static List<Courier> get_free_couriers_for_company(String company_name) {
        SessionFactory factory = HibernateUtil.getSessionFactory();

        Session session = factory.openSession();

        session.beginTransaction();
        List<Courier> couriers;

        Criteria cr = session.createCriteria(Courier.class);

        couriers = cr.add(Restrictions.eq("company_name", company_name)).add(Restrictions.eq("status", 1)).list();

        session.getTransaction().commit();
        session.close();

        return couriers;
    }

  
    public static List<Courier> get_busy_couriers() {
        SessionFactory factory = HibernateUtil.getSessionFactory();

        Session session = factory.openSession();

        session.beginTransaction();
        List<Courier> couriers;

        Criteria cr = session.createCriteria(Courier.class);

        couriers = cr.add(Restrictions.eq("status", 0)).list();

        session.getTransaction().commit();
        session.close();

        return couriers;
    }

    public static Courier get_courier_with_least_gift_time_for_company(String company_name) {
        SessionFactory factory = HibernateUtil.getSessionFactory();

        Session session = factory.openSession();

        session.beginTransaction();
        List<Courier> couriers;

        Criteria cr = session.createCriteria(Courier.class);

        couriers = cr.add(Restrictions.eq("status", 0)).add(Restrictions.eq("company_name", company_name)).addOrder(Order.asc("free_after_gifts")).list();

        session.getTransaction().commit();
        session.close();

        return couriers.get(0);
    }

     public static void add_courier(Courier c) {
        SessionFactory factory = HibernateUtil.getSessionFactory();

        Session session = factory.openSession();

        session.beginTransaction();

        session.save(c);

        session.getTransaction().commit();
        session.close();
    }
     
    public static void update_courier(Courier c) {
        SessionFactory factory = HibernateUtil.getSessionFactory();

        Session session = factory.openSession();

        session.beginTransaction();

        session.update(c);

        session.getTransaction().commit();
        session.close();
    }
}
