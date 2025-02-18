package services;

import models.Event;
import repositories.EventRepository;

import java.util.List;

public class EventService implements IService<Event> {
    private final EventRepository eventRepository = new EventRepository();

    @Override
    public void create(Event event) {
        eventRepository.create(event);
    }

    @Override
    public List<Event> getAll() {
        return eventRepository.getAll();
    }

    @Override
    public Event getById(Long id) {
        return eventRepository.getById(id);
    }

    @Override
    public void update(Event event) {
        eventRepository.update(event);
    }

    @Override
    public void delete(Long id) {
        eventRepository.delete(id);
    }

}
