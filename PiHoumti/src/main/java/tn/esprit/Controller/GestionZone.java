package tn.esprit.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import tn.esprit.interfaces.IService;
import tn.esprit.models.zone_de_collecte;
import tn.esprit.services.ServiceZoneDeCollecte;

import java.time.ZoneId;

public class GestionZone {

    @FXML
    private TextField tfNomZone;
    @FXML
    private TextField tfTempsCollecte;
    @FXML
    private TextField tfPopulation;

    @FXML
    private Label lbzonedecollecte;

    IService<zone_de_collecte> sp = new ServiceZoneDeCollecte();

    @FXML
    public void ajouterZone(ActionEvent actionEvent) {
        zone_de_collecte z = new ServiceZoneDeCollecte();
        z.setTempsDeCollecte(tfTempsCollecte.getText());
        z.setNom(tfNomZone.getText());
        z.setPopulation(Integer.parseInt(tfPopulation.getText()));

        sp.add(z);
    }

    @FXML
    public void afficherZone(ActionEvent actionEvent) {

        lbzonedecollecte.setText(sp.getAll().toString());
    }


    public void supprimerZone(ActionEvent actionEvent) {
        int id = Integer.parseInt(tfPopulation.getText());
        zone_de_collecte z = new zone_de_collecte();
        z.setId(id);

        sp.delete(z);
        afficherZone(actionEvent);
    }

    public void mettreAJourZone(ActionEvent actionEvent) {
        int id = Integer.parseInt(tfPopulation.getText());
        zone_de_collecte z = new zone_de_collecte();
        z.setId(id);
        z.setTempsDeCollecte(tfTempsCollecte.getText());
        z.setNom(tfNomZone.getText());
        z.setPopulation(Integer.parseInt(tfPopulation.getText()));

        sp.update(z);
        afficherZone(actionEvent);
    }
}
