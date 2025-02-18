package tests;

import models.*;
import services.*;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TestsSupport {
    public static void main(String[] args) {

        Logger.getLogger("org.hibernate").setLevel(Level.SEVERE);

        ChatService chatService = new ChatService();
        MessageService messageService = new MessageService();
        FeedbackService feedbackService = new FeedbackService();
        DestinationService destinationService = new DestinationService();
        VoyageService voyageService = new VoyageService();
        ReclamationService reclamationService = new ReclamationService();

        Chat chat = new Chat();
        Chat chat2 = new Chat();
        chatService.create(chat);
        chatService.create(chat2);

        Message message1 = new Message("Bonjour", "User1", "User2", new java.util.Date(), chat);
        Message message2 = new Message("Bonsoir", "User2", "User1", new java.util.Date(), chat);
        Message message3 = new Message("test autre chat", "User2", "User1", new java.util.Date(), chat2);

        messageService.create(message1);
        messageService.create(message2);
        messageService.create(message3);

        Destination paris = new Destination("Paris", "France", ClimatEnum.FROID);
        Voyage voyage = new Voyage("Paris Getaway", paris, new java.util.Date(), new java.util.Date(), 1200.50, "Oui oui baguette.", null, FormuleEnum.REPAS_SEUL, 4.5f);
        destinationService.create(paris);
        voyageService.create(voyage);

        Feedback feedback = new Feedback("Super experience!", voyage, new java.util.Date(), 5.0, "J'ai adore chaque moment !");
        feedbackService.create(feedback);

        List<Message> messageTotal = messageService.getAll();
        System.out.println("Total de messages : " + messageTotal.size());
        List<Message> messages = messageService.getByChat(chat);
        if (!messages.isEmpty()) {
            System.out.println(messages.size() + " messages dans le chat "+chat.getId());
            for (Message msg : messages) {
                System.out.println("Message de " + msg.getExpediteur() + " à " + msg.getRecepteur() + " : " + msg.getMessage());
            }
        } else {
            System.out.println("Aucun message dans le chat");
        }

        List<Feedback> feedbacks = feedbackService.getAll();
        if (!feedbacks.isEmpty()) {
            System.out.println(feedbacks.size() + " feedbacks");
        } else {
            System.out.println("Aucun feedback disponible");
        }

        Feedback fb = feedbackService.getById(feedback.getId());
        System.out.println(fb.getVoyage().getNom() + " : " + fb.getContenu() + ", note : " + fb.getNote());

        Reclamation reclamation = new Reclamation("titre reclamation", "etat", "description  reclamation", new java.util.Date(), "client");
        reclamationService.create(reclamation);

        Reclamation r1 = reclamationService.getById(reclamation.getId());
        System.out.println(r1.getTitre() + " : " + r1.getDescription());

        messageService.delete(message1.getId());
        messageService.delete(message2.getId());
        messageService.delete(message3.getId());
        chatService.delete(chat.getId());
        feedbackService.delete(feedback.getId());
        reclamationService.delete(reclamation.getId());

        System.out.println("Chat, messages, reclamations et feedbacks supprimes");
    }
}
