/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import beans.FriendlySeedling;
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
public class FriendlySeedlingModel {

    public static void add_new_friendly_seedling(FriendlySeedling seed) {
        SessionFactory factory = HibernateUtil.getSessionFactory();

        Session session = factory.openSession();
        session.beginTransaction();

        session.save(seed);

        session.getTransaction().commit();
        session.close();
    }

    public static boolean are_seedlings_friendly(int id_seed1, int id_seed2) {
        SessionFactory factory = HibernateUtil.getSessionFactory();

        Session session = factory.openSession();
        session.beginTransaction();

        Criteria cr = session.createCriteria(FriendlySeedling.class);

        FriendlySeedling fs = (FriendlySeedling) cr.add(Restrictions.eq("id_seedling1", id_seed1)).add(Restrictions.eq("id_seedling2", id_seed2)).uniqueResult();

        session.getTransaction().commit();
        session.close();

        return fs != null;
    }

    public static List<FriendlySeedling> get_seedlings_id1(int id_seed) {
        SessionFactory factory = HibernateUtil.getSessionFactory();

        Session session = factory.openSession();
        session.beginTransaction();

        Criteria cr = session.createCriteria(FriendlySeedling.class);

        List<FriendlySeedling> fs = cr.add(Restrictions.eq("id_seedling1", id_seed)).list();

        session.getTransaction().commit();
        session.close();

        return fs;
    }

    public static List<FriendlySeedling> get_seedlings_id2(int id_seed) {
        SessionFactory factory = HibernateUtil.getSessionFactory();

        Session session = factory.openSession();
        session.beginTransaction();

        Criteria cr = session.createCriteria(FriendlySeedling.class);

        List<FriendlySeedling> fs = cr.add(Restrictions.eq("id_seedling2", id_seed)).list();

        session.getTransaction().commit();
        session.close();

        return fs;
    }

    public static void delete_friendly_seedlings(FriendlySeedling fs) {
        SessionFactory factory = HibernateUtil.getSessionFactory();

        Session session = factory.openSession();
        session.beginTransaction();

        session.delete(fs);

        session.getTransaction().commit();
        session.close();
    }
}
