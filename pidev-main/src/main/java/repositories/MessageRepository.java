package repositories;

import models.Chat;
import models.Message;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.List;

public class MessageRepository implements IRepository<Message> {
    private static final SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();

    @Override
    public void create(Message message) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.persist(message);
        transaction.commit();
        session.close();
    }

    @Override
    public List<Message> getAll() {
        Session session = sessionFactory.openSession();
        List<Message> messages = session.createQuery("FROM Message", Message.class).list();
        session.close();
        return messages;
    }

    @Override
    public Message getById(Long id) {
        Session session = sessionFactory.openSession();
        Message message = session.get(Message.class, id);
        session.close();
        return message;
    }

    @Override
    public void update(Message message) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.update(message);
        transaction.commit();
        session.close();
    }

    @Override
    public void delete(Long id) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        Message message = session.get(Message.class, id);
        if (message != null) {
            session.delete(message);
        }
        transaction.commit();
        session.close();
    }

    public List<Message> getByChat(Chat chat) {
        Session session = sessionFactory.openSession();
        List<Message> messages = session.createQuery("FROM Message WHERE chat = :chat", Message.class).setParameter("chat", chat).list();
        session.close();
        return messages;
    }
}
