package services;

import models.Chat;
import models.Message;
import repositories.MessageRepository;

import java.util.List;

public class MessageService implements IService<Message> {
    private final MessageRepository messageRepository = new MessageRepository();

    @Override
    public void create(Message message) {
        messageRepository.create(message);
    }

    @Override
    public List<Message> getAll() {
        return messageRepository.getAll();
    }

    @Override
    public Message getById(Long id) {
        return messageRepository.getById(id);
    }

    @Override
    public void update(Message message) {
        messageRepository.update(message);
    }

    @Override
    public void delete(Long id) {
        messageRepository.delete(id);
    }

    public List<Message> getByChat(Chat chat) {
        return messageRepository.getByChat(chat);
    }
}
