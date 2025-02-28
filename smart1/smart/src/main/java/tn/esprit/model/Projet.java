package tn.esprit.model;

import java.sql.Date;

public class Projet {
    private int id;
    private String nom;
    private String description;
    private double budget;
    private double depense;
    private Date dateDebut;
    private Date dateFin;
    private String statut;
    private Fournisseur fournisseur;

    // Constructeur sans ID (pour l'ajout)
    public Projet(String nom, String description, double budget, double depense, Date dateDebut, Date dateFin, String statut, Fournisseur fournisseur) {
        this.nom = nom;
        this.description = description;
        this.budget = budget;
        this.depense = depense;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.statut = statut;
        this.fournisseur = fournisseur;
    }

    // Constructeur avec ID (pour la mise à jour)
    public Projet(int id, String nom, String description, double budget, double depense, Date dateDebut, Date dateFin, String statut, Fournisseur fournisseur) {
        this.id = id;
        this.nom = nom;
        this.description = description;
        this.budget = budget;
        this.depense = depense;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.statut = statut;
        this.fournisseur = fournisseur;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getBudget() {
        return budget;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }

    public double getDepense() {
        return depense;
    }

    public void setDepense(double depense) {
        this.depense = depense;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public Fournisseur getFournisseur() {
        return fournisseur;
    }

    public void setFournisseur(Fournisseur fournisseur) {
        this.fournisseur = fournisseur;
    }

    @Override
    public String toString() {
        return nom + " - " + (fournisseur != null ? fournisseur.getNom() : "Aucun fournisseur");
    }
}