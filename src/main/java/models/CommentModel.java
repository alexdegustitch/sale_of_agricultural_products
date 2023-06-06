/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import beans.Comment;
import beans.Item;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import util.HibernateUtil;

/**
 *
 * @author Aleksandar
 */
public class CommentModel {

    public static void add_comment_item_user(Comment comm) {
        SessionFactory factory = HibernateUtil.getSessionFactory();

        Session session = factory.openSession();
        session.beginTransaction();

        session.save(comm);

        session.getTransaction().commit();
        session.close();

    }
    
    public static void update_comment(Comment comm) {
        SessionFactory factory = HibernateUtil.getSessionFactory();

        Session session = factory.openSession();
        session.beginTransaction();

        session.update(comm);

        session.getTransaction().commit();
        session.close();

    }

    public static List<Comment> get_all_comments_for_items(int id_item) {
        SessionFactory factory = HibernateUtil.getSessionFactory();

        Session session = factory.openSession();
        session.beginTransaction();

        Criteria cr = session.createCriteria(Comment.class);

        List<Comment> comments;

        comments = cr.add(Restrictions.eq("id_item", id_item)).addOrder(Order.desc("date_of_comment")).list();

        session.getTransaction().commit();
        session.close();

        return comments;
    }
    
    public static List<Comment> get_all_comments_from_user(String username) {
        SessionFactory factory = HibernateUtil.getSessionFactory();

        Session session = factory.openSession();
        session.beginTransaction();

        Criteria cr = session.createCriteria(Comment.class);

        List<Comment> comments;

        comments = cr.add(Restrictions.eq("farmer_username", username)).list();

        session.getTransaction().commit();
        session.close();

        return comments;
    }

    public static Comment get_comment_for_user_item(int id_item, String farmer_username) {
        SessionFactory factory = HibernateUtil.getSessionFactory();

        Session session = factory.openSession();
        session.beginTransaction();

        Criteria cr = session.createCriteria(Comment.class);

        Comment comment;

        comment = (Comment) cr.add(Restrictions.eq("id_item", id_item)).add(Restrictions.eq("farmer_username", farmer_username)).uniqueResult();

        session.getTransaction().commit();
        session.close();

        return comment;
    }

    public static List<Comment> get_all_comments_for_items_not_user(int id_item, String farmer_username) {
        SessionFactory factory = HibernateUtil.getSessionFactory();

        Session session = factory.openSession();
        session.beginTransaction();

        Criteria cr = session.createCriteria(Comment.class);

        List<Comment> comments;

        comments = cr.add(Restrictions.eq("id_item", id_item)).add(Restrictions.ne("farmer_username", farmer_username)).addOrder(Order.desc("date_of_comment")).list();

        session.getTransaction().commit();
        session.close();

        return comments;
    }
}
