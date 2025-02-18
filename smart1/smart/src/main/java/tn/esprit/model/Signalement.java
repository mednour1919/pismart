package tn.esprit.model;

import java.sql.Timestamp;

public class Signalement {

    private int idSignalement;
    private String typeSignalement;
    private String description;
    private Timestamp dateSignalement;
    private String statut;

    // Constructeur avec tous les champs sauf latitude et longitude
    public Signalement(int idSignalement, String typeSignalement, String description, Timestamp dateSignalement,
                       String statut) {
        this.idSignalement = idSignalement;
        this.typeSignalement = typeSignalement;
        this.description = description;
        this.dateSignalement = dateSignalement;
        this.statut = statut;
    }

    // Constructeur sans id, pour l'ajout de nouveaux signalements
    public Signalement(String typeSignalement, String description, Timestamp dateSignalement, String statut) {
        this.typeSignalement = typeSignalement;
        this.description = description;
        this.dateSignalement = dateSignalement;
        this.statut = statut;
    }

    public Signalement() {

    }

    public int getIdSignalement() {
        return idSignalement;
    }

    public void setIdSignalement(int idSignalement) {
        this.idSignalement = idSignalement;
    }

    public String getTypeSignalement() {
        return typeSignalement;
    }

    public void setTypeSignalement(String typeSignalement) {
        this.typeSignalement = typeSignalement;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getDateSignalement() {
        return dateSignalement;
    }

    public void setDateSignalement(Timestamp dateSignalement) {
        this.dateSignalement = dateSignalement;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    @Override
    public String toString() {
        return "Signalement{" +
                "idSignalement=" + idSignalement +
                ", typeSignalement='" + typeSignalement + '\'' +
                ", description='" + description + '\'' +
                ", dateSignalement=" + dateSignalement +
                ", statut='" + statut + '\'' +
                '}';
    }
}
