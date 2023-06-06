/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import beans.Mark;
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
public class MarkModel {

    public static Mark get_mark_for_item_farmer(int id_item, String farmer_username) {
        SessionFactory factory = HibernateUtil.getSessionFactory();

        Session session = factory.openSession();
        session.beginTransaction();

        Mark m;
        Criteria cr = session.createCriteria(Mark.class);

        m = (Mark) cr.add(Restrictions.eq("id_item", id_item)).add(Restrictions.eq("farmer_username", farmer_username)).uniqueResult();

        session.getTransaction().commit();;
        session.close();

        return m;
    }

    public static void add_mark_for_item_farmer(Mark mark) {
        SessionFactory factory = HibernateUtil.getSessionFactory();

        Session session = factory.openSession();

        session.beginTransaction();

        session.save(mark);

        session.getTransaction().commit();
        session.close();

    }

    public static List<Mark> get_all_marks_for_item(int id_item) {
        SessionFactory factory = HibernateUtil.getSessionFactory();

        Session session = factory.openSession();
        session.beginTransaction();

        List<Mark> marks;
        Criteria cr = session.createCriteria(Mark.class);

        marks = cr.add(Restrictions.eq("id_item", id_item)).list();

        session.getTransaction().commit();;
        session.close();

        return marks;
    }
}
