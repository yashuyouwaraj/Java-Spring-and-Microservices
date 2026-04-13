package com.yashu;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;


public class Main {
    public static void main(String[] args) {
//        Alien a3 = new Alien();
//        a3.setAid(103);
//        a3.setAname("Deepika");
//        a3.setTech("cloud");

//        Alien a4 = new Alien();
//        a4.setAid(104);
//        a4.setAname("Ruchi");
//        a4.setTech("AWS");
        Alien a6 = new Alien();
        a6.setAid(104);
        a6.setAname("Ruchi");
        a6.setTech("AWS");
        SessionFactory factory = new Configuration().addAnnotatedClass(com.yashu.Alien.class).configure().buildSessionFactory();

        Session session =factory.openSession();

        Transaction transaction = session.beginTransaction();

//       Alien a1 = session.find(Alien.class,102);
//       Alien a2 = session.get(Alien.class,101);
//
//       Alien a5 = session.find(Alien.class,104);
//       session.remove(a5);

        session.persist(a6);

//       session.merge(a3);
//        session.merge(a4);

       transaction.commit();

        System.out.println(a6);
        System.out.println(a6);
        session.close();
        factory.close();
    }
}