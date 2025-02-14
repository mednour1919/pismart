package repositories;

import models.Reclamation;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.List;

public class ReclamationRepository implements IRepository<Reclamation>  {
    private static final SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();

    @Override
    public void create(Reclamation reclamation) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.persist(reclamation);
        transaction.commit();
        session.close();
    }

    @Override
    public List<Reclamation> getAll() {
        Session session = sessionFactory.openSession();
        List<Reclamation> reclamations = session.createQuery("FROM Reclamation", Reclamation.class).list();
        session.close();
        return reclamations;
    }

    @Override
    public Reclamation getById(Long id) {
        Session session = sessionFactory.openSession();
        Reclamation reclamation = session.get(Reclamation.class, id);
        session.close();
        return reclamation;
    }

    @Override
    public void update(Reclamation reclamation) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.update(reclamation);
        transaction.commit();
        session.close();
    }

    @Override
    public void delete(Long id) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        Reclamation reclamation = session.get(Reclamation.class, id);
        if (reclamation != null) {
            session.delete(reclamation);
        }
        transaction.commit();
        session.close();
    }
}
