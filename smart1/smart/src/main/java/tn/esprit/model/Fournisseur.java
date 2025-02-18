package tn.esprit.model;

public class Fournisseur {
    private int id;
    private String nom;
    private String adresse;

    // Constructeurs
    public Fournisseur(int id, String nom, String adresse) {
        this.id = id;
        this.nom = nom;
        this.adresse = adresse;
    }

    public Fournisseur(String nom, String adresse) {
        this.nom = nom;
        this.adresse = adresse;
    }

    // Getters et setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    // Redéfinir la méthode toString()
    @Override
    public String toString() {
        return nom; // Retourne le nom du fournisseur pour l'affichage
    }
}