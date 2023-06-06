/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import beans.Item;
import beans.Storage;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import util.HibernateUtil;

/**
 *
 * @author Aleksandar
 */
public class StorageModel {

    public static List<Storage> get_all_items_for_nursery(int id_nursery) {

        SessionFactory factory = HibernateUtil.getSessionFactory();

        Session session = factory.openSession();

        session.beginTransaction();
        List<Storage> agents;

        Criteria cr = session.createCriteria(Storage.class);

        agents = cr.add(Restrictions.eq("id_nursery", id_nursery)).list();

        session.getTransaction().commit();
        session.close();

        return agents;
    }

    public static Storage get_item_for_nursery(int id_nursery, int id_item) {
        SessionFactory factory = HibernateUtil.getSessionFactory();

        Session session = factory.openSession();

        session.beginTransaction();

        Criteria cr = session.createCriteria(Storage.class);

        Storage agent = (Storage) cr.add(Restrictions.eq("id_nursery", id_nursery)).add(Restrictions.eq("id_item", id_item)).uniqueResult();

        session.getTransaction().commit();
        session.close();

        return agent;
    }

    public static void update_storage(Storage s) {
        SessionFactory factory = HibernateUtil.getSessionFactory();

        Session session = factory.openSession();

        session.beginTransaction();

        session.update(s);

        session.getTransaction().commit();
        session.close();

    }
    
    public static void delete_storage_item(Storage s) {
        SessionFactory factory = HibernateUtil.getSessionFactory();

        Session session = factory.openSession();

        session.beginTransaction();

        session.delete(s);

        session.getTransaction().commit();
        session.close();

    }

    public static void add_item_in_storage(Storage s) {
        SessionFactory factory = HibernateUtil.getSessionFactory();

        Session session = factory.openSession();

        session.beginTransaction();

        session.save(s);

        session.getTransaction().commit();
        session.close();

    }

    public static void decrease_amount_for_item(int id_nursery, int id_item) {
        SessionFactory factory = HibernateUtil.getSessionFactory();

        Session session = factory.openSession();

        session.beginTransaction();
        Storage agent;

        Criteria cr = session.createCriteria(Storage.class);

        agent = (Storage) cr.add(Restrictions.eq("id_nursery", id_nursery)).add(Restrictions.eq("id_item", id_item)).uniqueResult();

        agent.setAmount(agent.getAmount() - 1);
        if (agent.getAmount() == 0) {
            session.delete(agent);
        } else {
            session.save(agent);
        }

        session.getTransaction().commit();
        session.close();
    }

}
