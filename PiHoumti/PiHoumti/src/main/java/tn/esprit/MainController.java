package tn.esprit.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.geometry.Insets;
import tn.esprit.models.Camion;
import tn.esprit.models.Zone_de_collecte;
import tn.esprit.services.ServiceCamion;
import tn.esprit.services.Service_zone_de_collecte;

import java.util.List;
import java.util.stream.Collectors;

public class MainController {

    @FXML
    private VBox stationVBox;
    @FXML
    private TextField stationNameField;
    @FXML
    private TextField capacityField;
    @FXML
    private TextField zoneField;

    @FXML
    private VBox reservationVBox;
    @FXML
    private TextField placeField;
    @FXML
    private TextField timeField;
    @FXML
    private TextField placeField1;
    @FXML
    private TextField brandField;

    @FXML
    private TextField searchZoneField;
    @FXML
    private TextField searchCamionField;

    private Service_zone_de_collecte serviceZoneDeCollecte;
    private ServiceCamion serviceCamion;

    private Zone_de_collecte selectedZone;
    private Camion selectedCamion;

    public MainController() {
        serviceZoneDeCollecte = new Service_zone_de_collecte();
        serviceCamion = new ServiceCamion();
    }

    @FXML
    private void initialize() {
        loadZones();
        loadCamions();
        setupSearchListeners();
    }


    private void setupSearchListeners() {
        searchZoneField.textProperty().addListener((observable, oldValue, newValue) -> {
            List<Zone_de_collecte> filteredZones = serviceZoneDeCollecte.getAll().stream()
                    .filter(zone -> zone.getNom().toLowerCase().contains(newValue.toLowerCase()))
                    .collect(Collectors.toList());
            updateZoneList(filteredZones);
        });

        searchCamionField.textProperty().addListener((observable, oldValue, newValue) -> {
            List<Camion> filteredCamions = serviceCamion.getAll().stream()
                    .filter(camion ->
                            camion.getType().toLowerCase().contains(newValue.toLowerCase()) ||
                                    camion.getStatut().toLowerCase().contains(newValue.toLowerCase()) ||
                                    String.valueOf(camion.getCapacity()).contains(newValue))
                    .collect(Collectors.toList());
            updateCamionList(filteredCamions);
        });
    }

    private void loadZones() {
        updateZoneList(serviceZoneDeCollecte.getAll());
    }

    private void loadCamions() {
        updateCamionList(serviceCamion.getAll());
    }

    private void updateZoneList(List<Zone_de_collecte> zones) {
        stationVBox.getChildren().clear();
        for (Zone_de_collecte zone : zones) {
            Node card = createZoneCard(zone);
            card.setOnMouseClicked(event -> {
                selectedZone = zone;
                stationNameField.setText(zone.getNom());
                capacityField.setText(String.valueOf(zone.getPopulation()));
                zoneField.setText(zone.getTempsDeCollecte());
            });
            stationVBox.getChildren().add(card);
        }
    }

    private void updateCamionList(List<Camion> camions) {
        reservationVBox.getChildren().clear();
        for (Camion camion : camions) {
            Node card = createCamionCard(camion);
            card.setOnMouseClicked(event -> {
                selectedCamion = camion;
                placeField.setText(camion.getType());
                placeField1.setText(camion.getStatut());
                timeField.setText(String.valueOf(camion.getCapacity()));
                brandField.setText(camion.getId_zone() != null ? camion.getId_zone().toString() : "");
            });
            reservationVBox.getChildren().add(card);
        }
    }


    private Node createZoneCard(Zone_de_collecte zone) {
        VBox card = new VBox();
        card.getStyleClass().add("card-view");
        card.setPadding(new Insets(10));

        Label nameLabel = new Label("Nom: " + zone.getNom());
        Label capacityLabel = new Label("Population: " + zone.getPopulation());
        Label zoneLabel = new Label("Temps: " + zone.getTempsDeCollecte());

        card.getChildren().addAll(nameLabel, capacityLabel, zoneLabel);
        return card;
    }

    private Node createCamionCard(Camion camion) {
        VBox card = new VBox();
        card.getStyleClass().add("card-view");
        card.setPadding(new Insets(10));

        Label typeLabel = new Label("Type: " + camion.getType());
        Label statusLabel = new Label("Statut: " + camion.getStatut());
        Label capacityLabel = new Label("Capacité: " + camion.getCapacity());
        Label zoneLabel = new Label("ID Zone: " + camion.getId_zone());

        card.getChildren().addAll(typeLabel, statusLabel, capacityLabel, zoneLabel);
        return card;
    }


    @FXML
    private void handleAddZone() {
        try {
            Zone_de_collecte zone = new Zone_de_collecte(stationNameField.getText(),
                    Integer.parseInt(capacityField.getText()),
                    zoneField.getText());
            serviceZoneDeCollecte.add(zone);
            loadZones();
        } catch (NumberFormatException e) {
            System.out.println("Erreur de format de nombre : " + e.getMessage());
        }
    }

    @FXML
    private void handleUpdateZone() {
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
        if (selectedZone != null) {
            for (Camion camion : serviceCamion.getAll()) {
                if (camion.getId_zone() != null && camion.getId_zone().equals(selectedZone.getId())) {
                    serviceCamion.delete(camion);
                }
            }
            serviceZoneDeCollecte.delete(selectedZone);
            loadZones();
            loadCamions();
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
            Camion camion = new Camion(placeField.getText(),
                    placeField1.getText(),
                    Integer.parseInt(timeField.getText()),
                    idZone);
            serviceCamion.add(camion);
            loadCamions();
        } catch (NumberFormatException e) {
            System.out.println("Erreur de format de nombre : " + e.getMessage());
        }
    }

    @FXML
    private void handleUpdateCamion() {
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