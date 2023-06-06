/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import beans.ItemCart;
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
public class ItemCartModel {

    public static ItemCart get_item_in_cart_for_user(int id_cart, int id_item) {
        SessionFactory factory = HibernateUtil.getSessionFactory();

        Session session = factory.openSession();
        session.beginTransaction();

        Criteria cr = session.createCriteria(ItemCart.class);

        ItemCart cart = (ItemCart) cr.add(Restrictions.eq("id_cart", id_cart)).add(Restrictions.eq("id_item", id_item)).uniqueResult();

        session.getTransaction().commit();
        session.close();

        return cart;
    }

    public static void update_item_in_cart(ItemCart cart) {
        SessionFactory factory = HibernateUtil.getSessionFactory();

        Session session = factory.openSession();
        session.beginTransaction();

        session.update(cart);

        session.getTransaction().commit();
        session.close();
    }

    public static void add_item_to_cart(ItemCart cart) {
        SessionFactory factory = HibernateUtil.getSessionFactory();

        Session session = factory.openSession();
        session.beginTransaction();

        session.save(cart);

        session.getTransaction().commit();
        session.close();
    }

    public static List<ItemCart> get_items_in_cart_for_user(int id_cart) {
        SessionFactory factory = HibernateUtil.getSessionFactory();

        Session session = factory.openSession();
        session.beginTransaction();

        Criteria cr = session.createCriteria(ItemCart.class);

        List<ItemCart> items_in_cart = cr.add(Restrictions.eq("id_cart", id_cart)).list();

        session.getTransaction().commit();
        session.close();

        return items_in_cart;
    }

    public static void remove_item_from_cart(ItemCart item_cart) {
        SessionFactory factory = HibernateUtil.getSessionFactory();

        Session session = factory.openSession();
        session.beginTransaction();

        session.delete(item_cart);

        session.getTransaction().commit();
        session.close();
    }
}
