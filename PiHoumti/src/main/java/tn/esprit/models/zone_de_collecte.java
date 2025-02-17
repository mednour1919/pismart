package tn.esprit.models;

public class zone_de_collecte {

    private int id;
    private String nom;
    private int population;
    private String tempsDeCollecte;

    public zone_de_collecte() {
    }

    public zone_de_collecte(String nom, int population, String tempsDeCollecte) {
        this.nom = nom;
        this.population = population;
        this.tempsDeCollecte = tempsDeCollecte;
    }

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

    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    public String getTempsDeCollecte() {
        return tempsDeCollecte;
    }

    public void setTempsDeCollecte(String tempsDeCollecte) {
        this.tempsDeCollecte = tempsDeCollecte;
    }

    @Override
    public String toString() {
        return "ZoneDeCollecte{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", population=" + population +
                ", tempsDeCollecte='" + tempsDeCollecte + '\'' +
                "}\n";
    }
}