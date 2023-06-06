/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import beans.Item;
import beans.Seedling;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import util.HibernateUtil;

/**
 *
 * @author Aleksandar
 */
public class ItemModel {

    public static Item get_item_by_id(int id_item) {
        SessionFactory factory = HibernateUtil.getSessionFactory();

        Session session = factory.openSession();
        session.beginTransaction();

        Criteria cr = session.createCriteria(Item.class);

        Item item = (Item) cr.add(Restrictions.eq("id_item", id_item)).uniqueResult();

        session.getTransaction().commit();
        session.close();
        return item;
    }

    public static Item get_item_by_name_and_company(String name, String company_name) {
        SessionFactory factory = HibernateUtil.getSessionFactory();

        Session session = factory.openSession();
        session.beginTransaction();

        Criteria cr = session.createCriteria(Item.class);

        Item item = (Item) cr.add(Restrictions.eq("name", name)).add(Restrictions.eq("company_name", company_name)).uniqueResult();

        session.getTransaction().commit();
        session.close();
        return item;
    }

    public static List<Item> get_all_active_items() {
        SessionFactory factory = HibernateUtil.getSessionFactory();

        Session session = factory.openSession();
        session.beginTransaction();

        Criteria cr = session.createCriteria(Item.class);

        List<Item> items;

        items = cr.add(Restrictions.eq("active", 1)).list();

        session.getTransaction().commit();
        session.close();

        return items;
    }

    public static List<Item> get_all_agents_for_company_in_stock(String company_name) {
        SessionFactory factory = HibernateUtil.getSessionFactory();

        Session session = factory.openSession();
        session.beginTransaction();

        Criteria cr = session.createCriteria(Item.class);

        List<Item> items;

        items = cr.add(Restrictions.eq("active", 1)).add(Restrictions.eq("type", 'A')).add(Restrictions.ge("amount", 1)).add(Restrictions.eq("company_name", company_name)).list();

        session.getTransaction().commit();
        session.close();

        return items;
    }

    public static List<Item> get_all_active_seedlings() {
        SessionFactory factory = HibernateUtil.getSessionFactory();

        Session session = factory.openSession();
        session.beginTransaction();

        Criteria cr = session.createCriteria(Item.class);

        List<Item> items;

        items = cr.add(Restrictions.eq("active", 1)).add(Restrictions.eq("type", 'S')).list();

        session.getTransaction().commit();
        session.close();

        return items;
    }

    public static List<Item> get_all_active_agents() {
        SessionFactory factory = HibernateUtil.getSessionFactory();

        Session session = factory.openSession();
        session.beginTransaction();

        Criteria cr = session.createCriteria(Item.class);

        List<Item> items;

        items = cr.add(Restrictions.eq("active", 1)).add(Restrictions.eq("type", 'A')).list();

        session.getTransaction().commit();
        session.close();

        return items;

    }

    public static List<Item> get_all_active_items_for_company(String company_name) {
        SessionFactory factory = HibernateUtil.getSessionFactory();

        Session session = factory.openSession();
        session.beginTransaction();

        Criteria cr = session.createCriteria(Item.class);

        List<Item> items;

        items = cr.add(Restrictions.eq("company_name", company_name)).list();

        session.getTransaction().commit();
        session.close();

        return items;
    }

    public static List<Item> get_all_active_seedlings_for_company(String company_name) {
        SessionFactory factory = HibernateUtil.getSessionFactory();

        Session session = factory.openSession();
        session.beginTransaction();

        Criteria cr = session.createCriteria(Item.class);

        List<Item> items;

        items = cr.add(Restrictions.eq("type", 'S')).add(Restrictions.eq("company_name", company_name)).list();

        session.getTransaction().commit();
        session.close();

        return items;
    }

    public static List<Item> get_all_active_agents_for_company(String company_name) {
        SessionFactory factory = HibernateUtil.getSessionFactory();

        Session session = factory.openSession();
        session.beginTransaction();

        Criteria cr = session.createCriteria(Item.class);

        List<Item> items;

        items = cr.add(Restrictions.eq("type", 'A')).add(Restrictions.eq("company_name", company_name)).list();

        session.getTransaction().commit();
        session.close();

        return items;

    }

    public static void add_new_item(Item item) {
        SessionFactory factory = HibernateUtil.getSessionFactory();

        Session session = factory.openSession();
        session.beginTransaction();

        session.save(item);

        session.getTransaction().commit();
        session.close();

    }
    
    
    public static void delete_item(Item item) {
        SessionFactory factory = HibernateUtil.getSessionFactory();

        Session session = factory.openSession();
        session.beginTransaction();

        session.delete(item);

        session.getTransaction().commit();
        session.close();

    }

    public static int get_item_max_id() {
        SessionFactory factory = HibernateUtil.getSessionFactory();

        Session session = factory.openSession();
        session.beginTransaction();

        DetachedCriteria maxId = DetachedCriteria.forClass(Item.class)
                .setProjection(Projections.max("id_item"));
        Item item = (Item) session.createCriteria(Item.class)
                .add(Property.forName("id_item").eq(maxId))
                .list().get(0);

        int id = item.getId_item();
        session.getTransaction().commit();
        session.close();
        return id;
    }

    public static void update_item(Item item) {
        SessionFactory factory = HibernateUtil.getSessionFactory();

        Session session = factory.openSession();
        session.beginTransaction();

        session.update(item);

        session.getTransaction().commit();
        session.close();

    }

}
