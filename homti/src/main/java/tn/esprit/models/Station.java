package tn.esprit.models;




public class Station {
    private int id_station;
    private String nomStation;
    private int capacite;
    private String zone;
    private String status;

    // Constructor
    public Station(int id_station, String nomStation, int capacite, String zone, String status) {
        this.id_station = id_station;
        this.nomStation = nomStation;
        this.capacite = capacite;
        this.zone = zone;
        this.status = status;
    }

    // Getters and Setters
    public int getId_station() { return id_station; }
    public void setId_station(int id_station) { this.id_station = id_station; }
    public String getNomStation() { return nomStation; }
    public void setNomStation(String nomStation) { this.nomStation = nomStation; }
    public int getCapacite() { return capacite; }
    public void setCapacite(int capacite) { this.capacite = capacite; }
    public String getZone() { return zone; }
    public void setZone(String zone) { this.zone = zone; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return "Station{" +
                "id=" + id_station +
                ", name='" + nomStation + '\'' +
                ", capacity=" + capacite +
                ", zone='" + zone + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}