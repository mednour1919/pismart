package services;

import models.Feedback;
import repositories.FeedbackRepository;

import java.util.List;

public class FeedbackService implements IService<Feedback> {
    private final FeedbackRepository feedbackRepository = new FeedbackRepository();

    @Override
    public void create(Feedback feedback) {
        feedbackRepository.create(feedback);
    }

    @Override
    public List<Feedback> getAll() {
        return feedbackRepository.getAll();
    }

    @Override
    public Feedback getById(Long id) {
        return feedbackRepository.getById(id);
    }

    @Override
    public void update(Feedback feedback) {
        feedbackRepository.update(feedback);
    }

    @Override
    public void delete(Long id) {
        feedbackRepository.delete(id);
    }

}
