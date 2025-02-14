package models;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "reservations")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //kif el client yibda 7adhir lihna twali ManyToOne Client
    //@ManyToOne
    private String client;

    @ManyToOne
    private Voyage voyage;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dateReservation;

    private int nombrePersonnes;

    private double prixTotal;

    @Enumerated(EnumType.STRING)
    private StatutEnum statut;

    public Reservation() {
    }

    public Reservation(String client, Voyage voyage, Date dateReservation, int nombrePersonnes, StatutEnum statut) {
        this.client = client;
        this.voyage = voyage;
        this.dateReservation = dateReservation;
        this.nombrePersonnes = nombrePersonnes;
        this.prixTotal = voyage.getPrix() * nombrePersonnes;
        this.statut = statut;
    }

    public Long getId() { return id; }

    public String getClient() { return client; }
    public void setClient(String client) { this.client = client; }

    public Voyage getVoyage() { return voyage; }
    public void setVoyage(Voyage voyage) { this.voyage = voyage; }

    public Date getDateReservation() { return dateReservation; }
    public void setDateReservation(Date dateReservation) { this.dateReservation = dateReservation; }

    public int getNombrePersonnes() { return nombrePersonnes; }
    public void setNombrePersonnes(int nombrePersonnes) { this.nombrePersonnes = nombrePersonnes; }

    public double getPrixTotal() { return prixTotal; }
    public void setPrixTotal(double prixTotal) { this.prixTotal = prixTotal; }

    public StatutEnum getStatut() { return statut; }
    public void setStatut(StatutEnum statut) { this.statut = statut; }
}
