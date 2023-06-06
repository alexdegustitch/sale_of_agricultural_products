/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import beans.ItemOrder;
import beans.Order;
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
public class ItemOrderModel {

    public static List<ItemOrder> get_items_for_order(int id_order) {
        SessionFactory factory = HibernateUtil.getSessionFactory();

        Session session = factory.openSession();

        session.beginTransaction();
        List<ItemOrder> items;

        Criteria cr = session.createCriteria(ItemOrder.class);

        items = cr.add(Restrictions.eq("id_order", id_order)).list();

        session.getTransaction().commit();
        session.close();

        return items;
    }
    
    

    public static List<ItemOrder> get_not_canceled_items_for_order(int id_order) {
        SessionFactory factory = HibernateUtil.getSessionFactory();

        Session session = factory.openSession();

        session.beginTransaction();
        List<ItemOrder> items;

        Criteria cr = session.createCriteria(ItemOrder.class);

        items = cr.add(Restrictions.eq("id_order", id_order)).add(Restrictions.ne("status", 'c')).list();

        session.getTransaction().commit();
        session.close();

        return items;
    }

    public static void update_item_order(ItemOrder item) {
        SessionFactory factory = HibernateUtil.getSessionFactory();

        Session session = factory.openSession();

        session.beginTransaction();

        session.update(item);

        session.getTransaction().commit();
        session.close();
    }

    public static ItemOrder get_item_order_by_id(int id_item_order) {
        SessionFactory factory = HibernateUtil.getSessionFactory();

        Session session = factory.openSession();

        session.beginTransaction();
        ItemOrder item;

        Criteria cr = session.createCriteria(ItemOrder.class);

        item = (ItemOrder) cr.add(Restrictions.eq("id_item_order", id_item_order)).uniqueResult();

        session.getTransaction().commit();
        session.close();

        return item;
    }

    public static boolean already_ordered_item(int id_item, int id_order) {
        SessionFactory factory = HibernateUtil.getSessionFactory();

        Session session = factory.openSession();

        session.beginTransaction();
        ItemOrder item;

        Criteria cr = session.createCriteria(ItemOrder.class);

        item = (ItemOrder) cr.add(Restrictions.eq("id_order", id_order)).add(Restrictions.eq("id_item", id_item)).uniqueResult();

        session.getTransaction().commit();
        session.close();

        return (item != null);
    }

    public static List<ItemOrder> get_all_orders_for_company(String company_name) {
        SessionFactory factory = HibernateUtil.getSessionFactory();

        Session session = factory.openSession();

        session.beginTransaction();

        List<ItemOrder> items;

        Criteria cr = session.createCriteria(ItemOrder.class);

        items = cr.add(Restrictions.eq("company_name", company_name)).addOrder(org.hibernate.criterion.Order.desc("priority")).list();

        session.getTransaction().commit();
        session.close();

        return items;
    }

    public static List<ItemOrder> get_all_orders_for_nursery(int id_nursery) {
        SessionFactory factory = HibernateUtil.getSessionFactory();

        Session session = factory.openSession();

        session.beginTransaction();

        List<ItemOrder> items;

        Criteria cr = session.createCriteria(ItemOrder.class);

        Object[] array = {'w', 's', 'o', 'g'};

        items = cr.add(Restrictions.eq("id_nursery", id_nursery)).add(Restrictions.in("status", array)).list();

        session.getTransaction().commit();
        session.close();

        return items;
    }

    public static void add_item_order(ItemOrder it) {
        SessionFactory factory = HibernateUtil.getSessionFactory();

        Session session = factory.openSession();

        session.beginTransaction();

        session.save(it);

        session.getTransaction().commit();
        session.close();
    }
}
