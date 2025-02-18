package repositories;

import models.Feedback;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.List;

public class FeedbackRepository implements IRepository<Feedback> {
    private static final SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();

    @Override
    public void create(Feedback feedback) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.persist(feedback);
        transaction.commit();
        session.close();
    }

    @Override
    public List<Feedback> getAll() {
        Session session = sessionFactory.openSession();
        List<Feedback> feedbacks = session.createQuery("FROM Feedback", Feedback.class).list();
        session.close();
        return feedbacks;
    }

    @Override
    public Feedback getById(Long id) {
        Session session = sessionFactory.openSession();
        Feedback feedback = session.get(Feedback.class, id);
        session.close();
        return feedback;
    }

    @Override
    public void update(Feedback feedback) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.update(feedback);
        transaction.commit();
        session.close();
    }

    @Override
    public void delete(Long id) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        Feedback feedback = session.get(Feedback.class, id);
        if (feedback != null) {
            session.delete(feedback);
        }
        transaction.commit();
        session.close();
    }
}
