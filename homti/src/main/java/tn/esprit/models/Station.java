package tn.esprit.models;




public class Station {
    private int IdStation;
    private String nomStation;
    private int capacite;
    private String zone;
    private String status;

    // Constructor
    public Station(int IdStation, String nomStation, int capacite, String zone, String status) {
        this.IdStation = IdStation;
        this.nomStation = nomStation;
        this.capacite = capacite;
        this.zone = zone;
        this.status = status;
    }

    // Getters and Setters
    public int getIdStation() { return IdStation; }
    public void setIdStation(int IdStation) { this.IdStation = IdStation; }
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
                "id=" + IdStation +
                ", name='" + nomStation + '\'' +
                ", capacity=" + capacite +
                ", zone='" + zone + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}