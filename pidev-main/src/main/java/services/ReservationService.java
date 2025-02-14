package services;

import models.Reservation;
import repositories.ReservationRepository;

import java.util.List;

public class ReservationService implements IService<Reservation> {
    private final ReservationRepository reservationRepository = new ReservationRepository();

    @Override
    public void create(Reservation reservation) {
        reservationRepository.create(reservation);
    }

    @Override
    public List<Reservation> getAll() {
        return reservationRepository.getAll();
    }

    @Override
    public Reservation getById(Long id) {
        return reservationRepository.getById(id);
    }

    @Override
    public void update(Reservation reservation) {
        reservationRepository.update(reservation);
    }

    @Override
    public void delete(Long id) {
        reservationRepository.delete(id);
    }

}
