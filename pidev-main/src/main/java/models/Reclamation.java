package models;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "reclamations")
public class Reclamation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titre;

    private String etat;

    private String description;

    @Temporal(TemporalType.DATE)
    private Date dateCreation;

    //hedhi titbadil Client
    //@ManyToOne
    private String client;

    public Reclamation() {
    }

    public Reclamation(String titre, String etat, String description, Date dateCreation, String client) {
        this.titre = titre;
        this.etat = etat;
        this.description = description;
        this.dateCreation = dateCreation;
        this.client = client;
    }

    public Long getId() {
        return id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }
}