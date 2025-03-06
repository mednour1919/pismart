package tn.esprit.models;

public class Camion {
    private int id;
    private int capacity;
    private String nom;  // Modification : id_zone devient zone de type String
    private String type;
    private String statut;
    private byte[] image;

    public Camion() {
    }

    public Camion(String type, String statut, int capacity, String nom, byte[] image) {
        this.type = type;
        this.statut = statut;
        this.capacity = capacity;
        this.nom = nom;  // zone est bien de type String
        this.image = image;
    }

    public Camion(String type, String statut, int capacity, String nom) {
        this(type, statut, capacity, nom, null);  // Appel du constructeur principal
    }

    public Camion(String type, String statut, int capacity) {
        this(type, statut, capacity, null, null);  // zone est null par défaut
    }

    // Getters & Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {  // Modification du getter
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;  // Accepte une String
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
                ", zone='" + nom + '\'' +  // Changement de id_zone à zone
                ", type='" + type + '\'' +
                ", statut='" + statut + '\'' +
                ", image=" + (image != null ? "[Image disponible]" : "[Aucune image]") +
                '}';
    }

    // Méthode pour l'exportation
    public String[] toStringForExport() {
        return new String[]{
                String.valueOf(id),
                type,
                statut,
                String.valueOf(capacity),
                nom != null ? nom : "N/A",  // Zone (nom) peut être null
                image != null ? "Image disponible" : "Aucune image"
        };
    }

    // Méthode pour l'importation
    public static Camion fromStringArray(String[] data) {
        Camion camion = new Camion();
        camion.setId(Integer.parseInt(data[0]));
        camion.setType(data[1]);
        camion.setStatut(data[2]);
        camion.setCapacity(Integer.parseInt(data[3]));
        camion.setNom(data[4].equals("N/A") ? null : data[4]);  // Gestion de la zone (nom)
        // L'image n'est pas gérée ici car elle nécessite un traitement spécial
        return camion;
    }
}