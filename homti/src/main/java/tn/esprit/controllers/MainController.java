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
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Hashtable;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.fxml.FXML;
import javafx.scene.web.WebView;
import javafx.scene.web.WebEngine;

import org.controlsfx.control.Notifications;
import javafx.geometry.Pos;
import javafx.util.Duration;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

import javafx.scene.control.TextField; // Correct pour JavaFX
import java.awt.TrayIcon.MessageType;



import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.AWTException;
import java.awt.Toolkit;

import javafx.scene.image.Image;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.AWTException;

import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import java.util.Map;
import java.util.HashMap;


public class MainController {

    @FXML
    private FlowPane stationCardView;
    @FXML
    private TextField stationNameField;
    @FXML
    private TextField capacityField;
    @FXML
    private TextField zoneField;
    @FXML
    private TextField statusField;
    @FXML
    private TextField searchZoneField;
    @FXML
    private ComboBox<Station> stationComboBox;

    @FXML
    private FlowPane reservationCardView;
    @FXML
    private TextField placeField;
    @FXML
    private DatePicker datePicker;
    @FXML
    private TextField timeField;
    @FXML
    private TextField brandField;
    @FXML
    private DatePicker searchDateField;
    @FXML
    private WebView mapView;


    @FXML
    private BarChart<String, Number> reservationChart;
    @FXML
    private CategoryAxis xAxis;
    @FXML
    private NumberAxis yAxis;

    private final StationService stationService = new StationService();
    private final ReservationService reservationService = new ReservationService();
    private Station selectedStation;
    private Reservation selectedReservation;

    @FXML
    public void initialize() {
        refreshStationList();
        refreshReservationList();
        loadStationComboBox();
        loadMap();
        loadReservationStatistics();
    }

    private void loadStationComboBox() {
        stationComboBox.getItems().clear();
        stationComboBox.getItems().addAll(stationService.findAll());
    }

    private void loadMap() {
        WebEngine webEngine = mapView.getEngine();
        String mapUrl = "https://www.google.com/maps"; // URL de Google Maps
        webEngine.load(mapUrl);
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
                return;
            }

            int stationId = stationComboBox.getValue().getIdStation();
            Reservation reservation = new Reservation(
                    0,
                    placeField.getText(),
                    Date.valueOf(datePicker.getValue()),
                    timeField.getText(),
                    brandField.getText(),
                    stationComboBox.getValue().getNomStation(),
                    stationId
            );

            reservationService.addReservation(reservation);

            // Détails de la réservation à afficher dans le QR code
            String reservationData = "Reservation Details:\n" +
                    "Place: " + reservation.getNumPLACE() + "\n" +
                    "Date: " + reservation.getDate_Reservation() + "\n" +
                    "Time: " + reservation.getTemps() + "\n" +
                    "Brand: " + reservation.getMarque() + "\n" +
                    "Station: " + reservation.getNomStation();
            String qrCodePath = "QR_Codes/reservation_" + reservation.getIdReservation() + ".png";

            File directory = new File("QR_Codes");
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // Générer le QR code avec les nouveaux détails
            generateQRCode(reservationData, qrCodePath);

            // Afficher le QR code dans l'ImageView
            displayQRCode(qrCodePath);

            refreshReservationList();
            clearReservationFields();

            showAlert("Success", "Reservation added successfully!", Alert.AlertType.INFORMATION);

        } catch (Exception e) {
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

            String status = statusField.getText();
            if (status == null || status.trim().isEmpty()) {
                showAlert("Error", "Status cannot be empty.", Alert.AlertType.WARNING);
                return;
            }

            selectedStation.setStatus(status);
            stationService.updateStation(selectedStation);

            refreshStationList();
            loadStationComboBox();
            clearStationFields();

            // Afficher la notification système
            //showSystemTrayNotification("Station Updated", "Status updated to: " + selectedStation.getStatus());

            // Ajout de la notification JavaFX
            Notifications.create()
                    .title("Station Updated")
                    .text("Station " + selectedStation.getNomStation() + " updated successfully!")
                    .position(Pos.BOTTOM_RIGHT)
                    .hideAfter(Duration.seconds(5))
                    .showInformation();

            showAlert("Success", "Station updated successfully!", Alert.AlertType.INFORMATION);
        } catch (Exception e) {
            e.printStackTrace();
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

    public static void generateQRCode(String data, String filePath) {
        int width = 300;
        int height = 300;
        String fileType = "png";

        Hashtable<EncodeHintType, Object> hintMap = new Hashtable<>();
        hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);

        try {
            BitMatrix matrix = new MultiFormatWriter().encode(data, BarcodeFormat.QR_CODE, width, height, hintMap);
            Path path = FileSystems.getDefault().getPath(filePath);
            MatrixToImageWriter.writeToPath(matrix, fileType, path);
            System.out.println("QR Code généré avec succès : " + filePath);
        } catch (WriterException | IOException e) {
            System.err.println("Erreur lors de la génération du QR Code : " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        // Exemple d'utilisation : Générer un QR Code pour une réservation avec ID 123
        String reservationData = "Reservation ID: 123";
        String qrCodePath = "QR_Codes/reservation_123.png";

        // Vérifier si le dossier existe, sinon le créer
        File directory = new File("QR_Codes");
        if (!directory.exists()) {
            directory.mkdirs();
        }

        generateQRCode(reservationData, qrCodePath);
    }

    @FXML
    private ImageView qrCodeImageView;

    public void displayQRCode(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            Image qrCodeImage = new Image(file.toURI().toString());
            qrCodeImageView.setImage(qrCodeImage);
        } else {
            System.out.println("QR Code file not found at: " + filePath);
        }
    }

    private void loadReservationStatistics() {
        List<Reservation> reservations = reservationService.findAll();
        Map<String, Integer> stationStats = new HashMap<>();

        for (Reservation reservation : reservations) {
            String station = reservation.getNomStation();
            stationStats.put(station, stationStats.getOrDefault(station, 0) + 1);
        }

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Réservations par station");

        for (Map.Entry<String, Integer> entry : stationStats.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }

        reservationChart.getData().clear();
        reservationChart.getData().add(series);
    }


}


