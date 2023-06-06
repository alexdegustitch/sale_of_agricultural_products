/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import beans.SeedlingInNursery;
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
public class SeedlingInNurseryModel {

    public static void add_new_seedling_nursery(int id_nursery, int id_seedling, int capacity) {
        SessionFactory factory = HibernateUtil.getSessionFactory();

        for (int i = 0; i < capacity; i++) {
            Session session = factory.openSession();
            session.beginTransaction();

            SeedlingInNursery sin = new SeedlingInNursery();
            sin.setActive(0);
            sin.setHarvested_time(null);
            sin.setId_nursery(id_nursery);
            sin.setId_seedling(id_seedling);
            sin.setPlot_number(i + 1);
            sin.setPlanted_time(null);

            session.save(sin);
            session.getTransaction().commit();
            session.close();
        }

    }

    public static void delete_seedling_nursery(SeedlingInNursery sin) {
        SessionFactory factory = HibernateUtil.getSessionFactory();

        Session session = factory.openSession();
        session.beginTransaction();

        session.delete(sin);

        session.getTransaction().commit();
        session.close();

    }

    public static SeedlingInNursery find_plot_for_nursery(int id_nursery, int plot) {
        SessionFactory factory = HibernateUtil.getSessionFactory();

        Session session = factory.openSession();
        session.beginTransaction();

        Criteria cr = session.createCriteria(SeedlingInNursery.class);

        SeedlingInNursery sin = (SeedlingInNursery) cr.add(Restrictions.eq("id_nursery", id_nursery)).add(Restrictions.eq("plot_number", plot)).uniqueResult();

        session.getTransaction().commit();
        session.close();
        return sin;
    }

    public static List<SeedlingInNursery> get_all_seedlings_for_nursery(int id_nursery) {
        SessionFactory factory = HibernateUtil.getSessionFactory();

        Session session = factory.openSession();
        session.beginTransaction();

        Criteria cr = session.createCriteria(SeedlingInNursery.class);

        List<SeedlingInNursery> list = cr.add(Restrictions.eq("id_nursery", id_nursery)).list();

        session.getTransaction().commit();
        session.close();
        return list;
    }

    public static void add_seedling_for_plot(SeedlingInNursery seed) {
        SessionFactory factory = HibernateUtil.getSessionFactory();

        Session session = factory.openSession();
        session.beginTransaction();

        session.update(seed);

        session.getTransaction().commit();
        session.close();

    }

    public static void remove_seedling_for_plot(SeedlingInNursery seed) {
        SessionFactory factory = HibernateUtil.getSessionFactory();

        Session session = factory.openSession();
        session.beginTransaction();

        session.update(seed);

        session.getTransaction().commit();
        session.close();
    }

    public static int get_number_of_planted_seedlings(int id_nursery) {
        SessionFactory factory = HibernateUtil.getSessionFactory();

        Session session = factory.openSession();
        session.beginTransaction();

        Criteria cr = session.createCriteria(SeedlingInNursery.class);

        List<SeedlingInNursery> list = cr.add(Restrictions.eq("id_nursery", id_nursery)).add(Restrictions.eq("active", 2)).list();

        session.getTransaction().commit();
        session.close();

        return list.size();
    }

    public static int get_number_of_free_planting_holes(int id_nursery) {
        SessionFactory factory = HibernateUtil.getSessionFactory();

        Session session = factory.openSession();
        session.beginTransaction();

        Criteria cr = session.createCriteria(SeedlingInNursery.class);

        List<SeedlingInNursery> list = cr.add(Restrictions.eq("id_nursery", id_nursery)).add(Restrictions.eq("active", 0)).list();

        session.getTransaction().commit();
        session.close();

        return list.size();
    }

    public static void change_active_type(SeedlingInNursery seed) {
        SessionFactory factory = HibernateUtil.getSessionFactory();

        Session session = factory.openSession();

        session.beginTransaction();

        session.update(seed);

        session.getTransaction().commit();

        session.close();
    }
}
