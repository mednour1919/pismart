package repositories;

import models.Event;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.List;

public class EventRepository implements IRepository<Event> {
    private static final SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();

    @Override
    public void create(Event event) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.persist(event);
        transaction.commit();
        session.close();
    }

    @Override
    public List<Event> getAll() {
        Session session = sessionFactory.openSession();
        List<Event> events = session.createQuery("FROM Event", Event.class).list();
        session.close();
        return events;
    }

    @Override
    public Event getById(Long id) {
        Session session = sessionFactory.openSession();
        Event event = session.get(Event.class, id);
        session.close();
        return event;
    }

    @Override
    public void update(Event event) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.update(event);
        transaction.commit();
        session.close();
    }

    @Override
    public void delete(Long id) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        Event event = session.get(Event.class, id);
        if (event != null) {
            session.delete(event);
        }
        transaction.commit();
        session.close();
    }
}
