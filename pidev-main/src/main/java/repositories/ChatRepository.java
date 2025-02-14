package repositories;

import models.Chat;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.List;

public class ChatRepository implements IRepository<Chat> {
    private static final SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();

    @Override
    public void create(Chat chat) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.persist(chat);
        transaction.commit();
        session.close();
    }

    @Override
    public List<Chat> getAll() {
        Session session = sessionFactory.openSession();
        List<Chat> chats = session.createQuery("FROM Chat", Chat.class).list();
        session.close();
        return chats;
    }

    @Override
    public Chat getById(Long id) {
        Session session = sessionFactory.openSession();
        Chat chat = session.get(Chat.class, id);
        session.close();
        return chat;
    }

    @Override
    public void update(Chat chat) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.update(chat);
        transaction.commit();
        session.close();
    }

    @Override
    public void delete(Long id) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        Chat chat = session.get(Chat.class, id);
        if (chat != null) {
            session.delete(chat);
        }
        transaction.commit();
        session.close();
    }
}
