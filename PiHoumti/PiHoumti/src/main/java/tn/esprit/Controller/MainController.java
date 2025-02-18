package tn.esprit.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import tn.esprit.models.Camion;
import tn.esprit.models.zone_de_collecte;
import tn.esprit.services.ServiceCamion;
import tn.esprit.services.Service_zone_de_collecte;

public class MainController {

    @FXML
    private ListView<zone_de_collecte> stationListView;
    @FXML
    private TextField stationNameField;
    @FXML
    private TextField capacityField;
    @FXML
    private TextField zoneField;

    @FXML
    private ListView<Camion> reservationListView;
    @FXML
    private TextField placeField;
    @FXML
    private TextField timeField;
    @FXML
    private TextField placeField1;
    @FXML
    private TextField brandField;

    private Service_zone_de_collecte serviceZoneDeCollecte;
    private ServiceCamion serviceCamion;

    public MainController() {
        serviceZoneDeCollecte = new Service_zone_de_collecte();
        serviceCamion = new ServiceCamion();
    }

    @FXML
    private void initialize() {
        loadZones();
        loadCamions();
    }

    private void loadZones() {
        stationListView.getItems().setAll(serviceZoneDeCollecte.getAll());
    }

    private void loadCamions() {
        reservationListView.getItems().setAll(serviceCamion.getAll());
    }

    @FXML
    private void handleAddZone() {
        try {
            zone_de_collecte zone = new zone_de_collecte(stationNameField.getText(), Integer.parseInt(capacityField.getText()), zoneField.getText());
            serviceZoneDeCollecte.add(zone);
            loadZones();
        } catch (NumberFormatException e) {
            System.out.println("Erreur de format de nombre : " + e.getMessage());
        }
    }

    @FXML
    private void handleUpdateZone() {
        zone_de_collecte selectedZone = stationListView.getSelectionModel().getSelectedItem();
        if (selectedZone != null) {
            try {
                selectedZone.setNom(stationNameField.getText());
                selectedZone.setPopulation(Integer.parseInt(capacityField.getText()));
                selectedZone.setTempsDeCollecte(zoneField.getText());
                serviceZoneDeCollecte.update(selectedZone);
                loadZones();
            } catch (NumberFormatException e) {
                System.out.println("Erreur de format de nombre : " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleDeleteZone() {
        zone_de_collecte selectedZone = stationListView.getSelectionModel().getSelectedItem();
        if (selectedZone != null) {
            // Supprimer les camions associés avant de supprimer la zone de collecte
            for (Camion camion : serviceCamion.getAll()) {
                if (camion.getId_zone() != null && camion.getId_zone().equals(selectedZone.getId())) {
                    serviceCamion.delete(camion);
                }
            }
            serviceZoneDeCollecte.delete(selectedZone);
            loadZones();
            loadCamions(); // Mettre à jour la liste des camions
        }
    }

    @FXML
    private void handleClearZone() {
        stationNameField.clear();
        capacityField.clear();
        zoneField.clear();
    }

    @FXML
    private void handleAddCamion() {
        try {
            Integer idZone = brandField.getText().isEmpty() ? null : Integer.parseInt(brandField.getText());
            Camion camion = new Camion(placeField.getText(), placeField1.getText(), Integer.parseInt(timeField.getText()), idZone);
            serviceCamion.add(camion);
            loadCamions();
        } catch (NumberFormatException e) {
            System.out.println("Erreur de format de nombre : " + e.getMessage());
        }
    }

    @FXML
    private void handleUpdateCamion() {
        Camion selectedCamion = reservationListView.getSelectionModel().getSelectedItem();
        if (selectedCamion != null) {
            try {
                selectedCamion.setType(placeField.getText());
                selectedCamion.setStatut(placeField1.getText());
                selectedCamion.setCapacity(Integer.parseInt(timeField.getText()));
                Integer idZone = brandField.getText().isEmpty() ? null : Integer.parseInt(brandField.getText());
                selectedCamion.setId_zone(idZone);
                serviceCamion.update(selectedCamion);
                loadCamions();
            } catch (NumberFormatException e) {
                System.out.println("Erreur de format de nombre : " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleDeleteCamion() {
        Camion selectedCamion = reservationListView.getSelectionModel().getSelectedItem();
        if (selectedCamion != null) {
            serviceCamion.delete(selectedCamion);
            loadCamions();
        }
    }

    @FXML
    private void handleClearCamion() {
        placeField.clear();
        placeField1.clear();
        timeField.clear();
        brandField.clear();
    }
}