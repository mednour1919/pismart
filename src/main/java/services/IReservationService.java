package services;

import models.Reservation;
import java.sql.Date;
import java.util.List;

public interface IReservationService {
    void addReservation(Reservation reservation);
    void updateReservation(Reservation reservation);
    void deleteReservation(int id);
    Reservation findById(int id);
    List<Reservation> findByDate(Date date);
    List<Reservation> findAll();
}