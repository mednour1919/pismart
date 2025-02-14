package repositories;

import models.Voyage;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import java.util.List;

public class VoyageRepository implements IRepository<Voyage> {
    private static final SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();

    @Override
    public void create(Voyage voyage) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.persist(voyage);
        transaction.commit();
        session.close();
    }

    @Override
    public List<Voyage> getAll() {
        Session session = sessionFactory.openSession();
        List<Voyage> voyages = session.createQuery("FROM Voyage", Voyage.class).list();
        session.close();
        return voyages;
    }

    @Override
    public Voyage getById(Long id) {
        Session session = sessionFactory.openSession();
        Voyage voyage = session.get(Voyage.class, id);
        session.close();
        return voyage;
    }

    @Override
    public void update(Voyage voyage) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.update(voyage);
        transaction.commit();
        session.close();
    }

    @Override
    public void delete(Long id) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        Voyage voyage = session.get(Voyage.class, id);
        if (voyage != null) {
            session.delete(voyage);
        }
        transaction.commit();
        session.close();
    }
}
