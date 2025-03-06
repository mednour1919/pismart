package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import tn.esprit.models.Camion;
import tn.esprit.models.Zone_de_collecte;
import tn.esprit.services.ServiceCamion;
import tn.esprit.services.Service_zone_de_collecte;

import java.util.List;
import java.util.stream.Collectors;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import javafx.embed.swing.SwingFXUtils;

import java.awt.image.BufferedImage;

public class ClientView {

    @FXML
    private VBox stationVBox; // Conteneur pour les cartes des zones de collecte
    @FXML
    private VBox reservationVBox; // Conteneur pour les cartes des camions
    @FXML
    private TextField searchZoneField; // Champ de recherche pour les zones
    @FXML
    private TextField searchCamionField; // Champ de recherche pour les camions
    @FXML
    private ImageView qrCodeLogo; // ImageView pour afficher le code QR

    private Service_zone_de_collecte serviceZoneDeCollecte;
    private ServiceCamion serviceCamion;

    public ClientView() {
        serviceZoneDeCollecte = new Service_zone_de_collecte();
        serviceCamion = new ServiceCamion();
    }

    @FXML
    private void switchToAdminInterface() {
        try {
            // Charger l'interface admin
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/admin_interface.fxml"));
            Stage stage = (Stage) searchZoneField.getScene().getWindow(); // Récupérer la fenêtre actuelle
            stage.setScene(new Scene(root)); // Changer la scène
            stage.setTitle("Interface Admin"); // Changer le titre de la fenêtre
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger l'interface admin.");
        }
    }

    @FXML
    private void initialize() {
        // Charger les données initiales
        loadZones();
        loadCamions();

        // Configurer les écouteurs de recherche
        setupSearchListeners();

        // Générer et afficher le code QR
        generateAndDisplayQRCode();
    }

    // Génère et affiche un code QR avec les informations de la zone de collecte et du camion
    private void generateAndDisplayQRCode() {
        try {
            // Récupérer les données de la zone de collecte et du camion
            Zone_de_collecte zone = serviceZoneDeCollecte.getAll().get(0); // Exemple: première zone de collecte
            Camion camion = serviceCamion.getAll().get(0); // Exemple: premier camion

            // Créer une chaîne de caractères avec les informations de la zone de collecte et du camion
            String qrText = "Zone de Collecte:\n" +
                    "Nom: " + zone.getNom() + "\n" +
                    "Population: " + zone.getPopulation() + "\n" +
                    "Temps de Collecte: " + zone.getTempsDeCollecte() + "\n\n" +
                    "Camion:\n" +
                    "Type: " + camion.getType() + "\n" +
                    "Statut: " + camion.getStatut() + "\n" +
                    "Capacité: " + camion.getCapacity() + "\n" +
                    "Zone: " + camion.getNom();

            // Générer le QR code avec une taille plus grande
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(qrText, BarcodeFormat.QR_CODE, 300, 300); // Taille augmentée à 300x300

            // Convertir le BitMatrix en BufferedImage
            BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);

            // Convertir BufferedImage en Image JavaFX
            Image qrImage = SwingFXUtils.toFXImage(bufferedImage, null);

            // Afficher le QR code dans l'ImageView
            qrCodeLogo.setImage(qrImage);
        } catch (WriterException e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Erreur", "Impossible de générer le code QR.");
        }
    }

    // Configure les écouteurs pour les champs de recherche
    private void setupSearchListeners() {
        // Écouteur pour la recherche des zones
        searchZoneField.textProperty().addListener((observable, oldValue, newValue) -> {
            List<Zone_de_collecte> filteredZones = serviceZoneDeCollecte.getAll().stream()
                    .filter(zone -> zone.getNom().toLowerCase().contains(newValue.toLowerCase()))
                    .collect(Collectors.toList());
            updateZoneList(filteredZones);
        });

        // Écouteur pour la recherche des camions
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

    // Charge les zones de collecte depuis le service et met à jour l'interface
    private void loadZones() {
        updateZoneList(serviceZoneDeCollecte.getAll());
    }

    // Charge les camions depuis le service et met à jour l'interface
    private void loadCamions() {
        updateCamionList(serviceCamion.getAll());
    }

    // Met à jour la liste des zones de collecte dans l'interface
    private void updateZoneList(List<Zone_de_collecte> zones) {
        stationVBox.getChildren().clear(); // Vider le conteneur actuel

        for (Zone_de_collecte zone : zones) {
            Node card = createZoneCard(zone); // Créer une carte pour la zone
            stationVBox.getChildren().add(card); // Ajouter la carte au conteneur
        }
    }

    // Met à jour la liste des camions dans l'interface
    private void updateCamionList(List<Camion> camions) {
        reservationVBox.getChildren().clear(); // Vider le conteneur actuel

        for (Camion camion : camions) {
            Node card = createCamionCard(camion); // Créer une carte pour le camion
            reservationVBox.getChildren().add(card); // Ajouter la carte au conteneur
        }
    }

    // Crée une carte pour afficher les détails d'une zone de collecte
    private Node createZoneCard(Zone_de_collecte zone) {
        VBox card = new VBox();
        card.getStyleClass().add("card-view");
        card.setPadding(new Insets(10));

        // Ajouter les informations de la zone
        Label nameLabel = new Label("Nom: " + zone.getNom());
        Label capacityLabel = new Label("Population: " + zone.getPopulation());
        Label zoneLabel = new Label("Temps: " + zone.getTempsDeCollecte());

        card.getChildren().addAll(nameLabel, capacityLabel, zoneLabel);
        return card;
    }

    // Crée une carte pour afficher les détails d'un camion
    private Node createCamionCard(Camion camion) {
        VBox card = new VBox();
        card.getStyleClass().add("card-view");
        card.setPadding(new Insets(10));

        // Affichage de l'image si disponible
        if (camion.getImage() != null && camion.getImage().length > 0) {
            ImageView imgView = new ImageView(new Image(new java.io.ByteArrayInputStream(camion.getImage())));
            imgView.setFitWidth(100);
            imgView.setPreserveRatio(true);
            card.getChildren().add(imgView);
        }

        // Ajouter les informations du camion
        Label typeLabel = new Label("Type: " + camion.getType());
        Label statusLabel = new Label("Statut: " + camion.getStatut());
        Label capacityLabel = new Label("Capacité: " + camion.getCapacity());
        Label zoneLabel = new Label("Zone: " + camion.getNom());

        card.getChildren().addAll(typeLabel, statusLabel, capacityLabel, zoneLabel);
        return card;
    }

    // Affiche une alerte à l'utilisateur
    private void showAlert(AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}