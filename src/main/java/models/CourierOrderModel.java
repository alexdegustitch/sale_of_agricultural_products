/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import beans.Company;
import beans.CourierOrder;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
public class CourierOrderModel {

    public static void add_order_for_courier(CourierOrder c) {
        SessionFactory factory = HibernateUtil.getSessionFactory();
        Session session = factory.openSession();

        session.beginTransaction();

        session.save(c);

        session.getTransaction().commit();
        session.close();

    }

    public static CourierOrder get_next_gift_order_for_courier(int id_courier){
        SessionFactory factory = HibernateUtil.getSessionFactory();
        Session session = factory.openSession();

        session.beginTransaction();

        Criteria cr = session.createCriteria(CourierOrder.class);
        
        List<CourierOrder> orders = cr.add(Restrictions.eq("id_courier", id_courier)).add(Restrictions.eq("order_arrived", 0)).add(Restrictions.eq("is_gift", 1)).addOrder(Order.asc("start_time")).list();

        session.getTransaction().commit();
        session.close();
        
        return orders.get(0);
    }
    
    public static void update_order_for_courier(CourierOrder c) {
        SessionFactory factory = HibernateUtil.getSessionFactory();
        Session session = factory.openSession();

        session.beginTransaction();

        session.update(c);

        session.getTransaction().commit();
        session.close();

    }
    
    public static CourierOrder get_order_for_courier(int id_courier, LocalDateTime free_date) {
        SessionFactory factory = HibernateUtil.getSessionFactory();
        Session session = factory.openSession();

        session.beginTransaction();

        Criteria cr = session.createCriteria(CourierOrder.class);
        
        CourierOrder co = (CourierOrder) cr.add(Restrictions.eq("id_courier", id_courier)).add(Restrictions.eq("end_time", free_date)).add(Restrictions.eq("order_arrived", 0)).uniqueResult();

        session.getTransaction().commit();
        session.close();

        return co;
    }
}
