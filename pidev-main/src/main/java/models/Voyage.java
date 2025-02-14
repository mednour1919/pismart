package models;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "voyages")
public class Voyage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;

    @ManyToOne
    private Destination destination;

    @Temporal(TemporalType.DATE)
    private Date dateDepart;

    @Temporal(TemporalType.DATE)
    private Date dateArrive;

    private double prix;

    private String description;

    private float note;

    @Enumerated(EnumType.STRING)
    private FormuleEnum formule;

    @Lob
    private byte[] image;

    public Voyage() {
    }

    public Voyage(String nom, Destination destination, Date dateDepart, Date dateArrive, double prix, String description, byte[] image, FormuleEnum formule, float note) {
        this.nom = nom;
        this.destination = destination;
        this.dateDepart = dateDepart;
        this.dateArrive = dateArrive;
        this.prix = prix;
        this.description = description;
        this.image = image;
        this.formule = formule;
        this.note = note;
    }

    public Long getId() { return id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public Destination getDestination() { return destination; }
    public void setDestination(Destination destination) { this.destination = destination; }

    public Date getDateDepart() { return dateDepart; }
    public void setDateDepart(Date dateDepart) { this.dateDepart = dateDepart; }

    public Date getDateArrive() { return dateArrive; }
    public void setDateArrive(Date dateArrive) { this.dateArrive = dateArrive; }

    public double getPrix() { return prix; }
    public void setPrix(double prix) { this.prix = prix; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public byte[] getImage() { return image; }
    public void setImage(byte[] image) { this.image = image; }

    public float getNote() {
        return note;
    }

    public void setNote(float note) {
        this.note = note;
    }

    public FormuleEnum getFormule() {
        return formule;
    }

    public void setFormule(FormuleEnum formule) {
        this.formule = formule;
    }

}
