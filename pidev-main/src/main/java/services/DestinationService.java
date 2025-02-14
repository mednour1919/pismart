package services;

import models.Destination;
import repositories.DestinationRepository;

import java.util.List;

public class DestinationService implements IService<Destination> {
    private final DestinationRepository destinationRepository = new DestinationRepository();

    @Override
    public void create(Destination destination) {
        destinationRepository.create(destination);
    }

    @Override
    public List<Destination> getAll() {
        return destinationRepository.getAll();
    }

    @Override
    public Destination getById(Long id) {
        return destinationRepository.getById(id);
    }

    @Override
    public void update(Destination destination) {
        destinationRepository.update(destination);
    }

    @Override
    public void delete(Long id) {
        destinationRepository.delete(id);
    }

}
