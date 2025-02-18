package services;

import models.Chat;
import repositories.ChatRepository;

import java.util.List;

public class ChatService implements IService<Chat> {
    private final ChatRepository chatRepository = new ChatRepository();

    @Override
    public void create(Chat chat) {
        chatRepository.create(chat);
    }

    @Override
    public List<Chat> getAll() {
        return chatRepository.getAll();
    }

    @Override
    public Chat getById(Long id) {
        return chatRepository.getById(id);
    }

    @Override
    public void update(Chat chat) {
        chatRepository.update(chat);
    }

    @Override
    public void delete(Long id) {
        chatRepository.delete(id);
    }

}
