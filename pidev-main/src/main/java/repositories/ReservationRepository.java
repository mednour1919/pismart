package repositories;

import models.Reservation;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.List;

public class ReservationRepository implements IRepository<Reservation> {
    private static final SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();

    @Override
    public void create(Reservation reservation) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.persist(reservation);
        transaction.commit();
        session.close();
    }

    @Override
    public List<Reservation> getAll() {
        Session session = sessionFactory.openSession();
        List<Reservation> reservations = session.createQuery("FROM Reservation", Reservation.class).list();
        session.close();
        return reservations;
    }

    @Override
    public Reservation getById(Long id) {
        Session session = sessionFactory.openSession();
        Reservation reservation = session.get(Reservation.class, id);
        session.close();
        return reservation;
    }

    @Override
    public void update(Reservation reservation) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.update(reservation);
        transaction.commit();
        session.close();
    }

    @Override
    public void delete(Long id) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        Reservation reservation = session.get(Reservation.class, id);
        if (reservation != null) {
            session.delete(reservation);
        }
        transaction.commit();
        session.close();
    }
}
