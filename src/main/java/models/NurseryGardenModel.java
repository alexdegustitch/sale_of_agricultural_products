/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import beans.NurseryGarden;
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
public class NurseryGardenModel {

    public static List<NurseryGarden> get_all_nursery_gardens_for_user(String username) {
        SessionFactory factory = HibernateUtil.getSessionFactory();

        Session session = factory.openSession();
        session.beginTransaction();

        List<NurseryGarden> gardens;

        Criteria c = session.createCriteria(NurseryGarden.class);
        gardens = c.add(Restrictions.eq("farmer_username", username)).list();

        session.getTransaction().commit();
        session.close();
        return gardens;
    }

    public static NurseryGarden find_nursery_garden(int id) {
        SessionFactory factory = HibernateUtil.getSessionFactory();

        Session session = factory.openSession();
        session.beginTransaction();

        NurseryGarden garden;

        Criteria c = session.createCriteria(NurseryGarden.class);

        c.add(Restrictions.eq("id_garden", id));
        garden = (NurseryGarden) c.uniqueResult();

        session.getTransaction().commit();
        session.close();
        return garden;
    }

    public static void add_nursery(String farmer_username, String nursery_name, String nursery_place, int length, int width) {
        SessionFactory factory = HibernateUtil.getSessionFactory();

        Session session = factory.openSession();
        session.beginTransaction();

        NurseryGarden ng = new NurseryGarden();

        ng.setAmount_of_water(200);
        ng.setTemperature(20.0);
        ng.setCapacity(width * length);
        ng.setFarmer_username(farmer_username);
        ng.setLength(length);
        ng.setWidth(width);
        ng.setLocation(nursery_place);
        ng.setName(nursery_name);
        ng.setPlanted_seedling_numer(0);

        session.save(ng);

        session.getTransaction().commit();
        session.close();
    }

    public static int get_max_id() {
        SessionFactory factory = HibernateUtil.getSessionFactory();

        Session session = factory.openSession();
        session.beginTransaction();

        DetachedCriteria maxId = DetachedCriteria.forClass(NurseryGarden.class)
                .setProjection(Projections.max("id_garden"));
        NurseryGarden ng = (NurseryGarden) session.createCriteria(NurseryGarden.class)
                .add(Property.forName("id_garden").eq(maxId))
                .list().get(0);

        int id = ng.getId_garden();
        session.getTransaction().commit();
        session.close();
        return id;
    }

    public static void update_garden(NurseryGarden ng) {
        SessionFactory factory = HibernateUtil.getSessionFactory();

        Session session = factory.openSession();
        session.beginTransaction();

        session.update(ng);

        session.getTransaction().commit();
        session.close();
    }
    
    public static void delete_nursery(NurseryGarden ng) {
        SessionFactory factory = HibernateUtil.getSessionFactory();

        Session session = factory.openSession();
        session.beginTransaction();

        session.delete(ng);

        session.getTransaction().commit();
        session.close();
    }

    public static List<NurseryGarden> get_all_nurseries() {
        SessionFactory factory = HibernateUtil.getSessionFactory();

        Session session = factory.openSession();
        session.beginTransaction();

        Criteria cr = session.createCriteria(NurseryGarden.class);
        List<NurseryGarden> nurseries = cr.list();

        session.getTransaction().commit();
        session.close();

        return nurseries;
    }
}
