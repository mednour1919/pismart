package tn.esprit.models;

public class Camion {

    private int id;
    private int capacity;
    private int id_zone; // Changer idZone en id_zone
    private String type;
    private String statut;

    public Camion() {
    }

    public Camion(String type, String statut, int capacity, int id_zone) {
        this.type = type;
        this.statut = statut;
        this.capacity = capacity;
        this.id_zone = id_zone;
    }

    public Camion(String type, String statut, int capacity) {
        this.type = type;
        this.statut = statut;
        this.capacity = capacity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_zone() {
        return id_zone;
    }

    public void setId_zone(int id_zone) {
        this.id_zone = id_zone;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    @Override
    public String toString() {
        return "Camion{" +
                "id=" + id +
                ", id_zone=" + id_zone +
                ", capacity=" + capacity +
                ", type='" + type + '\'' +
                ", statut='" + statut + '\'' +
                "}\n";
    }
}