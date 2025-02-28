package tn.esprit.model;

public class Fournisseur {
    private int id;
    private String nom;
    private String adresse;
    private String certifications;
    private String risques;
    private String performances;
    private String email;
    private String telephone;
    private String siteWeb;
    private String secteurActivite;
    private String responsable;
    private boolean estActif;

    // Constructeur avec tous les attributs
    public Fournisseur(int id, String nom, String adresse, String certifications, String risques, String performances, String email, String telephone, String siteWeb, String secteurActivite, String responsable,  boolean estActif) {
        this.id = id;
        this.nom = nom;
        this.adresse = adresse;
        this.certifications = certifications;
        this.risques = risques;
        this.performances = performances;
        this.email = email;
        this.telephone = telephone;
        this.siteWeb = siteWeb;
        this.secteurActivite = secteurActivite;
        this.responsable = responsable;
        this.estActif = estActif;
    }

    // Constructeur sans ID (pour l'ajout)
    public Fournisseur(String nom, String adresse, String certifications, String risques, String performances, String email, String telephone, String siteWeb, String secteurActivite, String responsable,  boolean estActif) {
        this.nom = nom;
        this.adresse = adresse;
        this.certifications = certifications;
        this.risques = risques;
        this.performances = performances;
        this.email = email;
        this.telephone = telephone;
        this.siteWeb = siteWeb;
        this.secteurActivite = secteurActivite;
        this.responsable = responsable;
        this.estActif = estActif;
    }

    public Fournisseur(int fournisseurId, String nomFournisseur, String s) {
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

    public String getCertifications() {
        return certifications;
    }

    public void setCertifications(String certifications) {
        this.certifications = certifications;
    }

    public String getRisques() {
        return risques;
    }

    public void setRisques(String risques) {
        this.risques = risques;
    }

    public String getPerformances() {
        return performances;
    }

    public void setPerformances(String performances) {
        this.performances = performances;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getSiteWeb() {
        return siteWeb;
    }

    public void setSiteWeb(String siteWeb) {
        this.siteWeb = siteWeb;
    }

    public String getSecteurActivite() {
        return secteurActivite;
    }

    public void setSecteurActivite(String secteurActivite) {
        this.secteurActivite = secteurActivite;
    }

    public String getResponsable() {
        return responsable;
    }

    public void setResponsable(String responsable) {
        this.responsable = responsable;
    }



    public boolean isEstActif() {
        return estActif;
    }

    public void setEstActif(boolean estActif) {
        this.estActif = estActif;
    }

    @Override
    public String toString() {
        return nom; // Pour l'affichage dans les ComboBox ou ListView
    }
}