/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import beans.Cart;
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
public class CartModel {

    public static Cart get_cart_for_user(String farmer_username) {
        SessionFactory factory = HibernateUtil.getSessionFactory();

        Session session = factory.openSession();
        session.beginTransaction();

        Criteria cr = session.createCriteria(Cart.class);

        Cart cart = (Cart) cr.add(Restrictions.eq("farmer_username", farmer_username)).uniqueResult();

        session.getTransaction().commit();
        session.close();

        return cart;
    }

    public static void add_cart_for_user(Cart cart) {
        SessionFactory factory = HibernateUtil.getSessionFactory();

        Session session = factory.openSession();
        session.beginTransaction();

        session.save(cart);

        session.getTransaction().commit();
        session.close();
    }

    public static List<Cart> get_all_carts() {
        SessionFactory factory = HibernateUtil.getSessionFactory();

        Session session = factory.openSession();
        session.beginTransaction();

        Criteria cr = session.createCriteria(Cart.class);

        List<Cart> carts = cr.list();

        session.getTransaction().commit();
        session.close();

        return carts;
    }

    public static void delete_cart_for_user(Cart cart) {
        SessionFactory factory = HibernateUtil.getSessionFactory();

        Session session = factory.openSession();
        session.beginTransaction();

        session.delete(cart);

        session.getTransaction().commit();
        session.close();
    }
}
