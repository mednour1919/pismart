package repositories;

import models.Destination;
import models.Voyage;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.List;

public class DestinationRepository implements IRepository<Destination>  {
    private static final SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();

    @Override
    public void create(Destination destination) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.persist(destination);
        transaction.commit();
        session.close();
    }

    @Override
    public List<Destination> getAll() {
        Session session = sessionFactory.openSession();
        List<Destination> destinations = session.createQuery("FROM Destination", Destination.class).list();
        session.close();
        return destinations;
    }

    @Override
    public Destination getById(Long id) {
        Session session = sessionFactory.openSession();
        Destination destination = session.get(Destination.class, id);
        session.close();
        return destination;
    }

    @Override
    public void update(Destination destination) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.update(destination);
        transaction.commit();
        session.close();
    }

    @Override
    public void delete(Long id) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        Destination destination = session.get(Destination.class, id);
        if (destination != null) {
            session.delete(destination);
        }
        transaction.commit();
        session.close();
    }
}
