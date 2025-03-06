package tn.esprit.test;

import tn.esprit.models.Camion;
import tn.esprit.models.Zone_de_collecte;
import tn.esprit.services.ServiceCamion;
import tn.esprit.services.Service_zone_de_collecte;

public class Main {

    public static void main(String[] args) {
        ServiceCamion sp = new ServiceCamion();
        sp.add(new Camion("aspirateurX", "En Panne", 1400));

        System.out.println("Liste des camions :");
        System.out.println(sp.getAll());

        Service_zone_de_collecte sz = new Service_zone_de_collecte();
        sz.add(new Zone_de_collecte("Zone Nord", 9500, "03:30:00"));

        System.out.println("Liste des zones de collecte :");
        System.out.println(sz.getAll());
    }
}
