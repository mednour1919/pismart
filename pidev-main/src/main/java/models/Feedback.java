package models;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "feedbacks")
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String contenu;

    //hedhi titbadil client
    //@ManyToOne
    private String client;

    @ManyToOne
    private Voyage voyage;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dateFeedback;

    private double note;

    private String description;

    public Feedback() {
    }

    public Feedback(String contenu, Voyage voyage, Date dateFeedback, double note, String description) {
        this.contenu = contenu;
        this.voyage = voyage;
        this.dateFeedback = dateFeedback;
        this.note = note;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public Voyage getVoyage() {
        return voyage;
    }

    public void setVoyage(Voyage voyage) {
        this.voyage = voyage;
    }

    public Date getDateFeedback() {
        return dateFeedback;
    }

    public void setDateFeedback(Date dateFeedback) {
        this.dateFeedback = dateFeedback;
    }

    public double getNote() {
        return note;
    }

    public void setNote(double note) {
        this.note = note;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
