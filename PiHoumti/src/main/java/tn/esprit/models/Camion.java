package tn.esprit.models;

public class Camion {
    private int id;
    private int capacity;
    private Integer id_zone;
    private String type;
    private String statut;
    private byte[] image;


    public Camion() {
    }

    public Camion(String type, String statut, int capacity, Integer id_zone, byte[] image) {
        this.type = type;
        this.statut = statut;
        this.capacity = capacity;
        this.id_zone = id_zone;
        this.image = image;
    }

    public Camion(String type, String statut, int capacity, Integer id_zone) {
        this(type, statut, capacity, id_zone, null);
    }

    public Camion(String type, String statut, int capacity) {
        this(type, statut, capacity, null, null);
    }

    // Getters & Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getId_zone() {
        return id_zone;
    }

    public void setId_zone(Integer id_zone) {
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

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Camion{" +
                "id=" + id +
                ", capacity=" + capacity +
                ", id_zone=" + id_zone +
                ", type='" + type + '\'' +
                ", statut='" + statut + '\'' +
                ", image=" + (image != null ? "[Image disponible]" : "[Aucune image]") +
                '}';
    }
}