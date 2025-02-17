package tn.esprit.models;

import java.sql.Date;

public class Reservation {
    private int idReservation;
    private String numPLACE;
    private Date date_Reservation;
    private String temps;
    private String marque;

    // Constructor
    public Reservation(int idReservation, String numPLACE, Date date_Reservation, String temps, String marque) {
        this.idReservation = idReservation;
        this.numPLACE = numPLACE;
        this.date_Reservation = date_Reservation;
        this.temps = temps;
        this.marque = marque;
    }

    // Getters and Setters
    public int getIdReservation() { return idReservation; }
    public void setIdReservation(int idReservation) { this.idReservation = idReservation; }
    public String getNumPLACE() { return numPLACE; }
    public void setNumPLACE(String numPLACE) { this.numPLACE = numPLACE; }
    public Date getDate_Reservation() { return date_Reservation; }
    public void setDate_Reservation(Date date_Reservation) { this.date_Reservation = date_Reservation; }
    public String getTemps() { return temps; }
    public void setTemps(String temps) { this.temps = temps; }
    public String getMarque() { return marque; }
    public void setMarque(String marque) { this.marque = marque; }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + idReservation +
                ", place='" + numPLACE + '\'' +
                ", date=" + date_Reservation +
                ", time='" + temps + '\'' +
                ", brand='" + marque + '\'' +
                '}';
    }
}
