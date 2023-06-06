/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import beans.Company;
import beans.Courier;
import beans.Farmer;
import java.time.LocalDate;
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
public class CompanyModel {

    public static void add_company(Company company) {
        SessionFactory factory = HibernateUtil.getSessionFactory();
        Session session = factory.openSession();

        session.beginTransaction();

        session.save(company);

        session.getTransaction().commit();
        session.close();

    }

    public static Company find_company_by_short_name(String short_name) {
        SessionFactory factory = HibernateUtil.getSessionFactory();
        Session session = factory.openSession();

        session.beginTransaction();

        Criteria cr = session.createCriteria(Company.class);

        cr.add(Restrictions.eq("short_name", short_name));

        Company company = (Company) cr.uniqueResult();

        session.getTransaction().commit();
        session.close();
        return company;
    }

    public static List<Company> get_uncomfirmed_companies() {
        SessionFactory factory = HibernateUtil.getSessionFactory();
        Session session = factory.openSession();

        session.beginTransaction();

        Criteria cr = session.createCriteria(Company.class);

        List<Company> companies;

        companies = cr.add(Restrictions.eq("status", 0)).list();

        session.getTransaction().commit();
        session.close();

        return companies;
    }

    public static List<Company> get_comfirmed_companies() {
        SessionFactory factory = HibernateUtil.getSessionFactory();
        Session session = factory.openSession();

        session.beginTransaction();

        Criteria cr = session.createCriteria(Company.class);

        List<Company> companies;

        companies = cr.add(Restrictions.eq("status", 1)).list();

        session.getTransaction().commit();
        session.close();

        return companies;
    }

    public static void update_company(Company c) {
        SessionFactory factory = HibernateUtil.getSessionFactory();

        Session session = factory.openSession();

        session.beginTransaction();

        session.update(c);

        session.getTransaction().commit();
        session.close();
    }

    public static void delete_company(Company c) {
        SessionFactory factory = HibernateUtil.getSessionFactory();

        Session session = factory.openSession();

        session.beginTransaction();

        session.delete(c);

        session.getTransaction().commit();
        session.close();
    }
}
