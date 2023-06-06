/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import beans.Admin;
import beans.SeedlingInStorage;
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
public class SeedlingInStorageModel {

    public static SeedlingInStorage find_seedling_in_storage(int id_item, int id_storage) {
        SessionFactory factory = HibernateUtil.getSessionFactory();
        Session session = factory.openSession();

        session.beginTransaction();

        Criteria cr = session.createCriteria(SeedlingInStorage.class);

        SeedlingInStorage s = (SeedlingInStorage) cr.add(Restrictions.eq("id_item", id_item)).add(Restrictions.eq("id_storage", id_storage)).uniqueResult();

        session.getTransaction().commit();
        session.close();

        return s;
    }

    public static List<SeedlingInStorage> find_seedling_in_storage_for_company(String company_name) {
        SessionFactory factory = HibernateUtil.getSessionFactory();
        Session session = factory.openSession();

        session.beginTransaction();

        Criteria cr = session.createCriteria(SeedlingInStorage.class);

        List<SeedlingInStorage> list = cr.add(Restrictions.eq("id_storage", company_name)).list();

        session.getTransaction().commit();
        session.close();

        return list;
    }

    public static void add_seedling_for_storage(SeedlingInStorage ss) {
        SessionFactory factory = HibernateUtil.getSessionFactory();
        Session session = factory.openSession();

        session.beginTransaction();

        session.save(ss);

        session.getTransaction().commit();
        session.close();

    }

    public static void delete_seedling_for_storage(SeedlingInStorage ss) {
        SessionFactory factory = HibernateUtil.getSessionFactory();
        Session session = factory.openSession();

        session.beginTransaction();

        session.delete(ss);

        session.getTransaction().commit();
        session.close();

    }

    public static List<SeedlingInStorage> find_all_seedlings_in_storage(int id_storage) {
        SessionFactory factory = HibernateUtil.getSessionFactory();
        Session session = factory.openSession();

        session.beginTransaction();

        Criteria cr = session.createCriteria(SeedlingInStorage.class);

        List<SeedlingInStorage> list = cr.add(Restrictions.eq("id_storage", id_storage)).list();

        session.getTransaction().commit();
        session.close();

        return list;
    }

    public static List<SeedlingInStorage> find_all_seedlings_in_storage_for_company(int id_storage, String company_name) {
        SessionFactory factory = HibernateUtil.getSessionFactory();
        Session session = factory.openSession();

        session.beginTransaction();

        Criteria cr = session.createCriteria(SeedlingInStorage.class);

        List<SeedlingInStorage> list = cr.add(Restrictions.eq("id_storage", id_storage)).add(Restrictions.eq("company_name", company_name)).list();

        session.getTransaction().commit();
        session.close();

        return list;
    }
}
