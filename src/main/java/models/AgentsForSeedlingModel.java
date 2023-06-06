/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import beans.AgentsForSeedling;
import beans.Item;
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
public class AgentsForSeedlingModel {

    public static void add_agent_seedling(int id_item, int id_seedling_nursery) {
        SessionFactory factory = HibernateUtil.getSessionFactory();

        Session session = factory.openSession();
        session.beginTransaction();

        AgentsForSeedling afs = new AgentsForSeedling();

        afs.setId_item(id_item);
        afs.setId_seedling_nursery(id_seedling_nursery);

        session.save(afs);

        session.getTransaction().commit();
        session.close();

    }

    public static List<AgentsForSeedling> get_agents_for_seedling(int id_seedling_nursery) {
        SessionFactory factory = HibernateUtil.getSessionFactory();

        Session session = factory.openSession();
        session.beginTransaction();

        List<AgentsForSeedling> agents;

        Criteria cr = session.createCriteria(AgentsForSeedling.class);

        agents = cr.add(Restrictions.eq("id_seedling_nursery", id_seedling_nursery)).list();

        session.getTransaction().commit();
        session.close();

        return agents;
    }

    public static void delete_agents_for_seedling(List<AgentsForSeedling> agents) {
        SessionFactory factory = HibernateUtil.getSessionFactory();

        Session session = factory.openSession();
        
        for (int i = 0; i < agents.size(); i++) {
            session.beginTransaction();
            session.delete(agents.get(i));
            session.getTransaction().commit();
        }

        session.close();
    }
}
