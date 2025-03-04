package tn.esprit.model;
public class ProjetWrapper {
    private Projet projet;
    private double latitude;
    private double longitude;
    private double[] latitudesZoneImpact;
    private double[] longitudesZoneImpact;

    public ProjetWrapper(Projet projet, double latitude, double longitude, double[] latitudesZoneImpact, double[] longitudesZoneImpact) {
        this.projet = projet;
        this.latitude = latitude;
        this.longitude = longitude;
        this.latitudesZoneImpact = latitudesZoneImpact;
        this.longitudesZoneImpact = longitudesZoneImpact;
    }

    public Projet getProjet() {
        return projet;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double[] getLatitudesZoneImpact() {
        return latitudesZoneImpact;
    }

    public double[] getLongitudesZoneImpact() {
        return longitudesZoneImpact;
    }
}