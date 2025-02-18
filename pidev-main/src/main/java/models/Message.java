package models;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String message;

    //id hedhom el zouz yitbadlou Client wala User
    //@ManyToOne
    private String recepteur;

    //@ManyToOne
    private String expediteur;

    @ManyToOne
    private Chat chat;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dateEnvoi;

    public Message() {
    }

    public Message(String message, String recepteur, String expediteur, Date dateEnvoi, Chat chat) {
        this.message = message;
        this.recepteur = recepteur;
        this.expediteur = expediteur;
        this.dateEnvoi = dateEnvoi;
        this.chat = chat;
    }

    public Long getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRecepteur() {
        return recepteur;
    }

    public void setRecepteur(String recepteur) {
        this.recepteur = recepteur;
    }

    public String getExpediteur() {
        return expediteur;
    }

    public void setExpediteur(String expediteur) {
        this.expediteur = expediteur;
    }

    public Date getDateEnvoi() {
        return dateEnvoi;
    }

    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    public void setDateEnvoi(Date dateEnvoi) {
        this.dateEnvoi = dateEnvoi;
    }
}
