package tn.esprit.model;



import java.sql.Timestamp;

public class reponse {

    private int idReponse;
    private int idSignalement;
    private String reponse;
    private Timestamp dateReponse;
    private String statut;

    public reponse() {
    }

    public reponse(int idReponse, int idSignalement, String reponse, Timestamp dateReponse, String statut) {
        this.idReponse = idReponse;
        this.idSignalement = idSignalement;
        this.reponse = reponse;
        this.dateReponse = dateReponse;
        this.statut = statut;
    }

    public reponse(int idSignalement, String reponse, Timestamp dateReponse, String statut) {
        this.idSignalement = idSignalement;
        this.reponse = reponse;
        this.dateReponse = dateReponse;
        this.statut = statut;
    }

    public int getIdReponse() {
        return idReponse;
    }

    public void setIdReponse(int idReponse) {
        this.idReponse = idReponse;
    }

    public int getIdSignalement() {
        return idSignalement;
    }

    public void setIdSignalement(int idSignalement) {
        this.idSignalement = idSignalement;
    }

    public String getReponse() {
        return reponse;
    }

    public void setReponse(String reponse) {
        this.reponse = reponse;
    }

    public Timestamp getDateReponse() {
        return dateReponse;
    }

    public void setDateReponse(Timestamp dateReponse) {
        this.dateReponse = dateReponse;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    @Override
    public String toString() {
        return "Reponse{" +
                "idReponse=" + idReponse +
                ", idSignalement=" + idSignalement +
                ", reponse='" + reponse + '\'' +
                ", dateReponse=" + dateReponse +
                ", statut='" + statut + '\'' +
                '}';
    }
}
