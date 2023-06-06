/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import beans.Admin;
import beans.Farmer;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import util.HibernateUtil;

/**
 *
 * @author Aleksandar
 */
public class AdminModel {

    public static Admin find_admin_by_username(String username) {
        SessionFactory factory = HibernateUtil.getSessionFactory();
        Session session = factory.openSession();

        session.beginTransaction();

        Criteria cr = session.createCriteria(Admin.class);

        cr.add(Restrictions.eq("admin_username", username));

        Admin a = (Admin) cr.uniqueResult();

        session.getTransaction().commit();
        session.close();
        return a;
    }

    public static void update_admin(Admin a) {
        SessionFactory factory = HibernateUtil.getSessionFactory();
        Session session = factory.openSession();

        session.beginTransaction();

        session.update(a);

        session.getTransaction().commit();
        session.close();

    }
}
