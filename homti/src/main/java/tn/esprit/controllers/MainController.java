package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import tn.esprit.models.Station;
import tn.esprit.models.Reservation;
import tn.esprit.services.StationService;
import tn.esprit.services.ReservationService;

import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

public class MainController {

    @FXML private FlowPane stationCardView;
    @FXML private TextField stationNameField;
    @FXML private TextField capacityField;
    @FXML private TextField zoneField;
    @FXML private TextField statusField;
    @FXML private TextField searchZoneField;
    @FXML private ComboBox<Station> stationComboBox;

    @FXML private FlowPane reservationCardView;
    @FXML private TextField placeField;
    @FXML private DatePicker datePicker;
    @FXML private TextField timeField;
    @FXML private TextField brandField;
    @FXML private DatePicker searchDateField;


    private final StationService stationService = new StationService();
    private final ReservationService reservationService = new ReservationService();
    private Station selectedStation;
    private Reservation selectedReservation;

    @FXML
    public void initialize() {
        refreshStationList();
        refreshReservationList();
        loadStationComboBox();
    }

    private void loadStationComboBox() {
        stationComboBox.getItems().clear();
        stationComboBox.getItems().addAll(stationService.findAll());
    }

    @FXML
    private void handleAddStation() {
        try {
            Station station = new Station(0, stationNameField.getText(), Integer.parseInt(capacityField.getText()), zoneField.getText(), statusField.getText());
            stationService.addStation(station);
            refreshStationList();
            loadStationComboBox();
            clearStationFields();
            showAlert("Success", "Station added successfully!", Alert.AlertType.INFORMATION);
        } catch (Exception e) {
            showAlert("Error", "Error adding station: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleAddReservation() {
        try {
            if (stationComboBox.getValue() == null) {
                showAlert("Error", "Please select a station", Alert.AlertType.WARNING);
                return;}
            // Récupération de l'ID de la station à partir du combo box
            int stationId = stationComboBox.getValue().getIdStation();

            // Création de la réservation avec l'ID de la station et le nom de la station
            Reservation reservation = new Reservation(
                    0,  // ID de réservation (0 signifie que l'ID sera généré automatiquement par la base de données)
                    placeField.getText(),
                    Date.valueOf(datePicker.getValue()),
                    timeField.getText(),
                    brandField.getText(),
                    stationComboBox.getValue().getNomStation(), // Nom de la station
                    stationId  // ID de la station
            );

            // Appel du service pour ajouter la réservation
            reservationService.addReservation(reservation);

            // Rafraîchissement de la liste des réservations et réinitialisation des champs
            refreshReservationList();
            clearReservationFields();

            // Affichage du message de succès
            showAlert("Success", "Reservation added successfully!", Alert.AlertType.INFORMATION);

        } catch (Exception e) {
            // Gestion des erreurs et affichage du message d'erreur
            showAlert("Error", "Error adding reservation: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void refreshStationList() {
        List<Station> stations = stationService.findAll();
        updateStationCardView(stations);
    }

    private void refreshReservationList() {
        List<Reservation> reservations = reservationService.findAll();
            updateReservationCardView(reservations);

    }

    private void updateStationCardView(List<Station> stations) {
        stationCardView.getChildren().clear();
        for (Station station : stations) {
            VBox stationCard = new VBox(10);
            stationCard.setStyle("-fx-padding: 10; -fx-border-style: solid inside; -fx-border-width: 2; -fx-border-color: black;");
            stationCard.getChildren().addAll(
                    new Text("Name: " + station.getNomStation()),
                    new Text("Capacity: " + station.getCapacite()),
                    new Text("Zone: " + station.getZone()),
                    new Text("Status: " + station.getStatus())
            );
            stationCard.setOnMouseClicked(event -> {
                selectedStation = station;
                stationNameField.setText(station.getNomStation());
                capacityField.setText(String.valueOf(station.getCapacite()));
                zoneField.setText(station.getZone());
                statusField.setText(station.getStatus());
            });
            stationCardView.getChildren().add(stationCard);
        }
    }

    private void updateReservationCardView(List<Reservation> reservations) {
        reservationCardView.getChildren().clear();  // Efface les anciens éléments
        for (Reservation reservation : reservations) {
            VBox reservationCard = new VBox(10);
            reservationCard.setStyle("-fx-padding: 10; -fx-border-style: solid inside; -fx-border-width: 2; -fx-border-color: black;");
            reservationCard.getChildren().addAll(
                    new Text("Place: " + reservation.getNumPLACE()),
                    new Text("Date: " + reservation.getDate_Reservation()),
                    new Text("Time: " + reservation.getTemps()),
                    new Text("Brand: " + reservation.getMarque()),
                    new Text("Station ID: " + reservation.getid_station())
            );
            reservationCard.setOnMouseClicked(event -> {
                selectedReservation = reservation;
                placeField.setText(reservation.getNumPLACE());
                datePicker.setValue(reservation.getDate_Reservation().toLocalDate());
                timeField.setText(reservation.getTemps());
                brandField.setText(reservation.getMarque());
                //stationComboBox.setValue(stationService.findById(reservation.getIdStation()));
            });
            reservationCardView.getChildren().add(reservationCard);
        }
    }


    private void clearReservationFields() {
        placeField.clear();
        datePicker.setValue(null);
        timeField.clear();
        brandField.clear();
        stationComboBox.setValue(null);
        selectedReservation = null;
    }

    private void clearStationFields() {
        stationNameField.clear();
        capacityField.clear();
        zoneField.clear();
        statusField.clear();
        selectedStation = null;
    }

    @FXML
    private void handleSearchByZone() {
        String zone = searchZoneField.getText();
        if (zone.isEmpty()) {
            showAlert("Error", "Please enter a zone to search.", Alert.AlertType.WARNING);
            return;
        }
        List<Station> filteredStations = stationService.findAll().stream()
                .filter(station -> station.getZone().equalsIgnoreCase(zone))
                .collect(Collectors.toList());
        updateStationCardView(filteredStations);
    }

    @FXML
    private void handleUpdateStation() {
        if (selectedStation == null) {
            showAlert("Error", "Please select a station to update.", Alert.AlertType.WARNING);
            return;
        }
        try {
            selectedStation.setNomStation(stationNameField.getText());
            selectedStation.setCapacite(Integer.parseInt(capacityField.getText()));
            selectedStation.setZone(zoneField.getText());
            selectedStation.setStatus(statusField.getText());

            stationService.updateStation(selectedStation);
            refreshStationList();
            loadStationComboBox();
            clearStationFields();
            showAlert("Success", "Station updated successfully!", Alert.AlertType.INFORMATION);
        } catch (Exception e) {
            showAlert("Error", "Error updating station: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleDeleteStation() {
        if (selectedStation == null) {
            showAlert("Error", "Please select a station to delete.", Alert.AlertType.WARNING);
            return;
        }
        stationService.deleteStation(selectedStation.getIdStation());
        refreshStationList();
        loadStationComboBox();
        clearStationFields();
        showAlert("Success", "Station deleted successfully!", Alert.AlertType.INFORMATION);
    }

    @FXML
    private void handleSearchByDate() {
        if (searchDateField.getValue() == null) {
            showAlert("Error", "Please select a date to search.", Alert.AlertType.WARNING);
            return;
        }
        Date date = Date.valueOf(searchDateField.getValue());
        List<Reservation> filteredReservations = reservationService.findAll().stream()
                .filter(reservation -> reservation.getDate_Reservation().equals(date))
                .collect(Collectors.toList());
        updateReservationCardView(filteredReservations);
    }

    @FXML
    private void handleUpdateReservation() {
        if (selectedReservation == null) {
            showAlert("Error", "Please select a reservation to update.", Alert.AlertType.WARNING);
            return;
        }
        try {
            selectedReservation.setNumPLACE(placeField.getText());
            selectedReservation.setDate_Reservation(Date.valueOf(datePicker.getValue()));
            selectedReservation.setTemps(timeField.getText());
            selectedReservation.setMarque(brandField.getText());

            reservationService.updateReservation(selectedReservation);
            refreshReservationList();
            clearReservationFields();
            showAlert("Success", "Reservation updated successfully!", Alert.AlertType.INFORMATION);
        } catch (Exception e) {
            showAlert("Error", "Error updating reservation: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleDeleteReservation() {
        if (selectedReservation == null) {
            showAlert("Error", "Please select a reservation to delete.", Alert.AlertType.WARNING);
            return;
        }
        reservationService.deleteReservation(selectedReservation.getIdReservation());
        refreshReservationList();
        clearReservationFields();
        showAlert("Success", "Reservation deleted successfully!", Alert.AlertType.INFORMATION);
    }
    @FXML
    private void handleClearReservation() {
        // Réinitialisation des champs liés à la réservation
        placeField.clear();
        datePicker.setValue(null);
        timeField.clear();
        brandField.clear();
        stationComboBox.setValue(null);

        // Réinitialisation de la sélection de réservation
        selectedReservation = null;
    }


    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
