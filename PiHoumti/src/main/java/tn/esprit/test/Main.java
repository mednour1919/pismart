package tn.esprit.test;

import tn.esprit.models.Camion;
import tn.esprit.models.zone_de_collecte;
import tn.esprit.services.ServiceCamion;
import tn.esprit.services.ServiceZoneDeCollecte;

public class Main {

    public static void main(String[] args) {
        // Tester ServiceCamion
        ServiceCamion sp = new ServiceCamion();
        // Créer un objet Camion avec des valeurs valides
        sp.add(new Camion("aspirateurX", "En Panne", 1400, 2));

        System.out.println("Liste des camions :");
        System.out.println(sp.getAll());

        // Tester ServiceZoneDeCollecte
        ServiceZoneDeCollecte sz = new ServiceZoneDeCollecte();
        // Créer un objet ZoneDeCollecte avec des valeurs valides
        sz.add(new zone_de_collecte("Zone Nord", 9500, "03:30:00"));

        System.out.println("Liste des zones de collecte :");
        System.out.println(sz.getAll());
    }
}
