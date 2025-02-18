package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import tn.esprit.models.Station;
import tn.esprit.models.Reservation;
import tn.esprit.services.StationService;
import tn.esprit.services.ReservationService;
import java.sql.Date;
import java.time.LocalDate;

public class MainController {
    @FXML private ListView<Station> stationListView;
    @FXML private TextField stationNameField;
    @FXML private TextField capacityField;
    @FXML private TextField zoneField;
    @FXML private TextField statusField;

    @FXML private ListView<Reservation> reservationListView;
    @FXML private TextField placeField;
    @FXML private DatePicker datePicker;
    @FXML private TextField timeField;
    @FXML private TextField brandField;

    private final StationService stationService = new StationService();
    private final ReservationService reservationService = new ReservationService();
    private Station selectedStation;
    private Reservation selectedReservation;

    @FXML
    public void initialize() {
        refreshStationList();
        refreshReservationList();

        // Station list selection listener
        stationListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedStation = newSelection;
                stationNameField.setText(newSelection.getNomStation());
                capacityField.setText(String.valueOf(newSelection.getCapacite()));
                zoneField.setText(newSelection.getZone());
                statusField.setText(newSelection.getStatus());
            }
        });

        // Reservation list selection listener
        reservationListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedReservation = newSelection;
                placeField.setText(newSelection.getNumPLACE());
                datePicker.setValue(newSelection.getDate_Reservation().toLocalDate());
                timeField.setText(newSelection.getTemps());
                brandField.setText(newSelection.getMarque());
            }
        });
    }

    // Station CRUD operations
    @FXML
    private void handleAddStation() {
        try {
            Station station = new Station(
                    0,
                    stationNameField.getText(),
                    Integer.parseInt(capacityField.getText()),
                    zoneField.getText(),
                    statusField.getText()
            );
            stationService.addStation(station);
            refreshStationList();
            clearStationFields();
            showAlert("Success", "Station added successfully!", Alert.AlertType.INFORMATION);
        } catch (Exception e) {
            showAlert("Error", "Error adding station: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleUpdateStation() {
        if (selectedStation == null) {
            showAlert("Error", "Please select a station to update", Alert.AlertType.WARNING);
            return;
        }

        try {
            selectedStation.setNomStation(stationNameField.getText());
            selectedStation.setCapacite(Integer.parseInt(capacityField.getText()));
            selectedStation.setZone(zoneField.getText());
            selectedStation.setStatus(statusField.getText());

            stationService.updateStation(selectedStation);
            refreshStationList();
            clearStationFields();
            showAlert("Success", "Station updated successfully!", Alert.AlertType.INFORMATION);
        } catch (Exception e) {
            showAlert("Error", "Error updating station: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleDeleteStation() {
        if (selectedStation == null) {
            showAlert("Error", "Please select a station to delete", Alert.AlertType.WARNING);
            return;
        }

        try {
            stationService.deleteStation(selectedStation.getId_station());
            refreshStationList();
            clearStationFields();
            showAlert("Success", "Station deleted successfully!", Alert.AlertType.INFORMATION);
        } catch (Exception e) {
            showAlert("Error", "Error deleting station: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleClearStation() {
        clearStationFields();
    }

    // Reservation CRUD operations
    @FXML
    private void handleAddReservation() {
        try {
            Reservation reservation = new Reservation(
                    0,
                    placeField.getText(),
                    Date.valueOf(datePicker.getValue()),
                    timeField.getText(),
                    brandField.getText()
            );
            reservationService.addReservation(reservation);
            refreshReservationList();
            clearReservationFields();
            showAlert("Success", "Reservation added successfully!", Alert.AlertType.INFORMATION);
        } catch (Exception e) {
            showAlert("Error", "Error adding reservation: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleUpdateReservation() {
        if (selectedReservation == null) {
            showAlert("Error", "Please select a reservation to update", Alert.AlertType.WARNING);
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
            showAlert("Error", "Please select a reservation to delete", Alert.AlertType.WARNING);
            return;
        }

        try {
            reservationService.deleteReservation(selectedReservation.getIdReservation());
            refreshReservationList();
            clearReservationFields();
            showAlert("Success", "Reservation deleted successfully!", Alert.AlertType.INFORMATION);
        } catch (Exception e) {
            showAlert("Error", "Error deleting reservation: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleClearReservation() {
        clearReservationFields();
    }

    // Helper methods
    private void refreshStationList() {
        ObservableList<Station> stations = FXCollections.observableArrayList(stationService.findAll());
        stationListView.setItems(stations);
    }

    private void refreshReservationList() {
        ObservableList<Reservation> reservations = FXCollections.observableArrayList(reservationService.findAll());
        reservationListView.setItems(reservations);
    }

    private void clearStationFields() {
        stationNameField.clear();
        capacityField.clear();
        zoneField.clear();
        statusField.clear();
        selectedStation = null;
        stationListView.getSelectionModel().clearSelection();
    }

    private void clearReservationFields() {
        placeField.clear();
        datePicker.setValue(null);
        timeField.clear();
        brandField.clear();
        selectedReservation = null;
        reservationListView.getSelectionModel().clearSelection();
    }

    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}