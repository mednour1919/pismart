package services;

import models.Reclamation;
import repositories.ReclamationRepository;

import java.util.List;

public class ReclamationService implements IService<Reclamation> {
    private final ReclamationRepository reclamationRepository = new ReclamationRepository();

    @Override
    public void create(Reclamation reclamation) {
        reclamationRepository.create(reclamation);
    }

    @Override
    public List<Reclamation> getAll() {
        return reclamationRepository.getAll();
    }

    @Override
    public Reclamation getById(Long id) {
        return reclamationRepository.getById(id);
    }

    @Override
    public void update(Reclamation reclamation) {
        reclamationRepository.update(reclamation);
    }

    @Override
    public void delete(Long id) {
        reclamationRepository.delete(id);
    }

}
