package tn.esprit.models;

import java.sql.Date;

public class Reservation {
    private int idReservation;
    private String numPLACE;
    private Date date_Reservation;
    private String temps;
    private String marque;
    private int idStation;
    private String nomStation; // Ajout de l'attribut pour stocker le nom de la station


    public Reservation(int idReservation, String numPLACE, Date date_Reservation,
                       String temps, String marque, String nomStation, int idStation) {
        this.idReservation = idReservation;
        this.numPLACE = numPLACE;
        this.date_Reservation = date_Reservation;
        this.temps = temps;
        this.marque = marque;
        this.idStation = idStation;
        this.nomStation = nomStation;
    }


    public int getIdReservation() {
        return idReservation;
    }

    public void setIdReservation(int idReservation) {
        this.idReservation = idReservation;
    }

    public String getNumPLACE() {
        return numPLACE;
    }

    public void setNumPLACE(String numPLACE) {
        this.numPLACE = numPLACE;
    }

    public Date getDate_Reservation() {
        return date_Reservation;
    }

    public void setDate_Reservation(Date date_Reservation) {
        this.date_Reservation = date_Reservation;
    }

    public String getTemps() {
        return temps;
    }

    public void setTemps(String temps) {
        this.temps = temps;
    }

    public String getMarque() {
        return marque;
    }

    public void setMarque(String marque) {
        this.marque = marque;
    }

    public int getid_station() {
        return idStation;
    }

    public void setIdStation(int idStation) {
        this.idStation = idStation;
    }

    public String getNomStation() {
        return nomStation;
    }

    public void setNomStation(String nomStation) {
        this.nomStation = nomStation;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "idReservation=" + idReservation +
                ", numPLACE='" + numPLACE + '\'' +
                ", date_Reservation=" + date_Reservation +
                ", temps='" + temps + '\'' +
                ", marque='" + marque + '\'' +
                ", nomStation='" + nomStation + '\'' +  // Affichage du nom de la station
                ", idStation=" + idStation +
                '}';
    }
}
