/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import beans.Order;
import beans.Storage;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import util.HibernateUtil;

/**
 *
 * @author Aleksandar
 */
public class OrderModel {

    public static List<Order> get_arrived_orders_for_farmer(String farmer_username) {

        SessionFactory factory = HibernateUtil.getSessionFactory();

        Session session = factory.openSession();

        session.beginTransaction();
        List<Order> orders;

        Criteria cr = session.createCriteria(Order.class);

        orders = cr.add(Restrictions.eq("farmer_username", farmer_username)).add(Restrictions.ne("status", 'A')).list();

        session.getTransaction().commit();
        session.close();

        return orders;
    }

    public static void add_new_order(Order o) {
        SessionFactory factory = HibernateUtil.getSessionFactory();

        Session session = factory.openSession();

        session.beginTransaction();

        session.save(o);

        session.getTransaction().commit();
        session.close();

    }

    public static List<Order> get_orders_for_date(LocalDate date) {

        SessionFactory factory = HibernateUtil.getSessionFactory();

        Session session = factory.openSession();

        session.beginTransaction();
        List<Order> orders;

        Criteria cr = session.createCriteria(Order.class);

        orders = cr.add(Restrictions.eq("date", date)).list();

        session.getTransaction().commit();
        session.close();

        return orders;
    }

    public static List<Order> get_accepted_orders_for_date_company(LocalDate date, String company_name) {

        SessionFactory factory = HibernateUtil.getSessionFactory();

        Session session = factory.openSession();

        session.beginTransaction();
        List<Order> orders;

        Criteria cr = session.createCriteria(Order.class);

        Object[] obj = {'a', 's'};

        orders = cr.add(Restrictions.eq("date", date)).add(Restrictions.in("status", obj)).add(Restrictions.gt("price", 0.0)).add(Restrictions.eq("company_name", company_name)).list();

        session.getTransaction().commit();
        session.close();

        return orders;
    }

    public static void update_order(Order o) {
        SessionFactory factory = HibernateUtil.getSessionFactory();

        Session session = factory.openSession();

        session.beginTransaction();

        session.update(o);

        session.getTransaction().commit();
        session.close();

    }

    public static Order find_order_by_id(int id_order) {
        SessionFactory factory = HibernateUtil.getSessionFactory();

        Session session = factory.openSession();

        session.beginTransaction();

        Criteria cr = session.createCriteria(Order.class);

        Order o = (Order) cr.add(Restrictions.eq("id_order", id_order)).uniqueResult();

        session.getTransaction().commit();
        session.close();

        return o;
    }

    public static int get_order_max_id() {
        SessionFactory factory = HibernateUtil.getSessionFactory();

        Session session = factory.openSession();
        session.beginTransaction();

        DetachedCriteria maxId = DetachedCriteria.forClass(Order.class)
                .setProjection(Projections.max("id_order"));
        Order o = (Order) session.createCriteria(Order.class)
                .add(Property.forName("id_order").eq(maxId))
                .list().get(0);

        int id = o.getId_order();
        session.getTransaction().commit();
        session.close();
        return id;
    }

    public static List<Order> get_all_orders_for_farmer(String farmer_username) {
        SessionFactory factory = HibernateUtil.getSessionFactory();

        Session session = factory.openSession();

        session.beginTransaction();
        List<Order> orders;

        Criteria cr = session.createCriteria(Order.class);

        orders = cr.add(Restrictions.eq("farmer_username", farmer_username)).list();

        session.getTransaction().commit();
        session.close();

        return orders;
    }

    public static List<Order> get_all_ordered_orders_for_farmer(String farmer_username) {
        SessionFactory factory = HibernateUtil.getSessionFactory();

        Session session = factory.openSession();

        session.beginTransaction();
        List<Order> orders;

        Criteria cr = session.createCriteria(Order.class);

        orders = cr.add(Restrictions.eq("farmer_username", farmer_username)).add(Restrictions.eq("status", 'o')).list();

        session.getTransaction().commit();
        session.close();

        return orders;
    }

    public static List<Order> get_all_waiting_orders_for_farmer(String farmer_username) {
        SessionFactory factory = HibernateUtil.getSessionFactory();

        Session session = factory.openSession();

        session.beginTransaction();
        List<Order> orders;

        Criteria cr = session.createCriteria(Order.class);

        orders = cr.add(Restrictions.eq("farmer_username", farmer_username)).add(Restrictions.eq("status", 'w')).list();

        session.getTransaction().commit();
        session.close();

        return orders;
    }

    public static List<Order> get_all_ordered_orders_for_company(String company_name) {
        SessionFactory factory = HibernateUtil.getSessionFactory();

        Session session = factory.openSession();

        session.beginTransaction();
        List<Order> orders;

        Criteria cr = session.createCriteria(Order.class);

        orders = cr.add(Restrictions.eq("company_name", company_name)).add(Restrictions.eq("status", 'o')).list();

        session.getTransaction().commit();
        session.close();

        return orders;
    }

    public static List<Order> get_all_waiting_orders_for_company(String company_name) {
        SessionFactory factory = HibernateUtil.getSessionFactory();

        Session session = factory.openSession();

        session.beginTransaction();
        List<Order> orders;

        Criteria cr = session.createCriteria(Order.class);

        orders = cr.add(Restrictions.eq("company_name", company_name)).add(Restrictions.eq("status", 'w')).list();

        session.getTransaction().commit();
        session.close();

        return orders;
    }

    public static List<Order> get_all_orders_for_company(String company_name) {
        SessionFactory factory = HibernateUtil.getSessionFactory();

        Session session = factory.openSession();

        session.beginTransaction();
        List<Order> orders;

        Criteria cr = session.createCriteria(Order.class);

        orders = cr.add(Restrictions.eq("company_name", company_name)).addOrder(org.hibernate.criterion.Order.desc("priority")).addOrder(org.hibernate.criterion.Order.desc("date_of_order")).list();

        session.getTransaction().commit();
        session.close();

        return orders;
    }
}
