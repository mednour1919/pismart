package services;

import models.Voyage;
import repositories.VoyageRepository;
import java.util.List;

public class VoyageService implements IService<Voyage> {
    private final VoyageRepository voyageRepository = new VoyageRepository();

    @Override
    public void create(Voyage voyage) {
        voyageRepository.create(voyage);
    }

    @Override
    public List<Voyage> getAll() {
        return voyageRepository.getAll();
    }

    @Override
    public Voyage getById(Long id) {
        return voyageRepository.getById(id);
    }

    @Override
    public void update(Voyage voyage) {
        voyageRepository.update(voyage);
    }

    @Override
    public void delete(Long id) {
        voyageRepository.delete(id);
    }

}
