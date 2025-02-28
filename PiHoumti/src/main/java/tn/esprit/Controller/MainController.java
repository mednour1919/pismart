package tn.esprit.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import tn.esprit.models.Camion;
import tn.esprit.models.Zone_de_collecte;
import tn.esprit.services.ServiceCamion;
import tn.esprit.services.Service_zone_de_collecte;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;

import com.twilio.Twilio;
import com.twilio.rest.verify.v2.service.Verification;
import com.twilio.rest.verify.v2.service.VerificationCheck;

public class MainController {

    @FXML private VBox stationVBox;
    @FXML private TextField stationNameField;
    @FXML private TextField capacityField;
    @FXML private TextField zoneField;
    @FXML private VBox reservationVBox;
    @FXML private TextField placeField;
    @FXML private TextField timeField;
    @FXML private TextField placeField1;
    @FXML private TextField brandField;
    @FXML private TextField searchZoneField;
    @FXML private TextField searchCamionField;
    @FXML private ImageView camionImageView;

    private Service_zone_de_collecte serviceZoneDeCollecte;
    private ServiceCamion serviceCamion;
    private Zone_de_collecte selectedZone;
    private Camion selectedCamion;
    private byte[] selectedImage;

    private static final String ACCOUNT_SID = "ACec96f0bb88a211167535857a14abb0e2";
    private static final String AUTH_TOKEN = "532c587026fe56a08873ec82a3a691f9";
    private static final String VERIFY_SERVICE_SID = "VA820edd0de12d298cee2bace4c8247faf";
    private static final String RECEIVER_PHONE_NUMBER = "+21692636109";

    public MainController() {
        serviceZoneDeCollecte = new Service_zone_de_collecte();
        serviceCamion = new ServiceCamion();
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    }

    @FXML
    private void initialize() {
        loadZones();
        loadCamions();
        setupSearchListeners();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleImageUpload() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg")
        );

        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            try {
                selectedImage = Files.readAllBytes(file.toPath());
                camionImageView.setImage(new Image(file.toURI().toString()));
            } catch (IOException e) {
                System.out.println("Erreur de chargement d'image: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleDeleteImage() {
        if (selectedCamion != null) {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Supprimer l'image");
            alert.setContentText("Êtes-vous sûr de vouloir supprimer cette image ?");

            if (alert.showAndWait().get() == ButtonType.OK) {
                selectedCamion.setImage(null);
                selectedImage = null;
                camionImageView.setImage(null);
                serviceCamion.update(selectedCamion);
                loadCamions();
                showAlert(AlertType.INFORMATION, "Succès", "Image supprimée avec succès !");
            }
        }
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
                if (camion.getImage() != null) {
                    camionImageView.setImage(new Image(new java.io.ByteArrayInputStream(camion.getImage())));
                }
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

    @FXML
    private void handleAddZone() {
        try {
            sendVerificationCode(RECEIVER_PHONE_NUMBER);

            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Vérification Requise");
            dialog.setHeaderText("Un code de vérification a été envoyé au " + RECEIVER_PHONE_NUMBER);
            dialog.setContentText("Entrez le code:");

            Optional<String> result = dialog.showAndWait();
            if (result.isPresent() && !result.get().isEmpty()) {
                if (verifyCode(RECEIVER_PHONE_NUMBER, result.get())) {
                    Zone_de_collecte zone = new Zone_de_collecte(
                            stationNameField.getText(),
                            Integer.parseInt(capacityField.getText()),
                            zoneField.getText()
                    );
                    serviceZoneDeCollecte.add(zone);
                    loadZones();
                    showAlert(AlertType.INFORMATION, "Succès", "Zone vérifiée et ajoutée !");
                } else {
                    showAlert(AlertType.ERROR, "Erreur", "Code de vérification invalide !");
                }
            }
        } catch (NumberFormatException e) {
            showAlert(AlertType.ERROR, "Erreur", "Format numérique invalide !");
        } catch (Exception e) {
            showAlert(AlertType.ERROR, "Erreur", "Échec de l'opération: " + e.getMessage());
        }
    }

    private void sendVerificationCode(String phoneNumber) throws Exception {
        Verification verification = Verification.creator(
                VERIFY_SERVICE_SID,
                phoneNumber,
                "sms"
        ).create();

        System.out.println("Verification SID: " + verification.getSid());
    }

    private boolean verifyCode(String phoneNumber, String code) {
        try {
            System.out.println("Verifying code: " + code + " for phone number: " + phoneNumber);

            VerificationCheck verificationCheck = VerificationCheck.creator(
                            VERIFY_SERVICE_SID
                    )
                    .setCode(code)
                    .setTo(phoneNumber)
                    .create();

            System.out.println("Verification status: " + verificationCheck.getStatus());

            return "approved".equals(verificationCheck.getStatus());
        } catch (Exception e) {
            System.out.println("Erreur de vérification: " + e.getMessage());
            return false;
        }
    }
    @FXML
    private void handleUpdateZone() {
        if (selectedZone != null) {
            try {
                Alert alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Confirmation");
                alert.setHeaderText("Modifier la zone");
                alert.setContentText("Êtes-vous sûr de vouloir modifier cette zone ?");

                if (alert.showAndWait().get() == ButtonType.OK) {
                    selectedZone.setNom(stationNameField.getText());
                    selectedZone.setPopulation(Integer.parseInt(capacityField.getText()));
                    selectedZone.setTempsDeCollecte(zoneField.getText());
                    serviceZoneDeCollecte.update(selectedZone);
                    loadZones();
                    showAlert(AlertType.INFORMATION, "Succès", "Zone modifiée avec succès !");
                }
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Format de nombre invalide pour la population !");
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la modification: " + e.getMessage());
            }
        } else {
            showAlert(AlertType.WARNING, "Avertissement", "Aucune zone sélectionnée !");
        }
    }

    @FXML
    private void handleDeleteZone() {
        if (selectedZone != null) {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Confirmation de suppression");
            alert.setHeaderText("Supprimer la zone");
            alert.setContentText("Êtes-vous sûr de vouloir supprimer cette zone et ses camions associés ?");

            if (alert.showAndWait().get() == ButtonType.OK) {
                try {
                    for (Camion camion : serviceCamion.getAll()) {
                        if (camion.getId_zone() != null && camion.getId_zone().equals(selectedZone.getId())) {
                            serviceCamion.delete(camion);
                        }
                    }
                    serviceZoneDeCollecte.delete(selectedZone);
                    loadZones();
                    loadCamions();
                    showAlert(AlertType.INFORMATION, "Succès", "Zone et camions associés supprimés avec succès !");
                } catch (Exception e) {
                    showAlert(AlertType.ERROR, "Erreur", "Échec de la suppression: " + e.getMessage());
                }
            }
        } else {
            showAlert(AlertType.WARNING, "Avertissement", "Aucune zone sélectionnée !");
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
            Camion camion = new Camion(
                    placeField.getText(),
                    placeField1.getText(),
                    Integer.parseInt(timeField.getText()),
                    idZone,
                    selectedImage
            );
            serviceCamion.add(camion);
            loadCamions();
            handleClearCamion();
            showAlert(AlertType.INFORMATION, "Succès", "Camion ajouté avec succès !");
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Format numérique invalide pour la capacité ou l'ID de zone !");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de l'ajout du camion: " + e.getMessage());
        }
    }

    @FXML
    private void handleUpdateCamion() {
        if (selectedCamion != null) {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Modifier le camion");
            alert.setContentText("Êtes-vous sûr de vouloir modifier ce camion ?");

            if (alert.showAndWait().get() == ButtonType.OK) {
                try {
                    selectedCamion.setType(placeField.getText());
                    selectedCamion.setStatut(placeField1.getText());
                    selectedCamion.setCapacity(Integer.parseInt(timeField.getText()));
                    Integer idZone = brandField.getText().isEmpty() ? null : Integer.parseInt(brandField.getText());
                    selectedCamion.setId_zone(idZone);
                    selectedCamion.setImage(selectedImage);
                    serviceCamion.update(selectedCamion);
                    loadCamions();
                    showAlert(AlertType.INFORMATION, "Succès", "Camion modifié avec succès !");
                } catch (NumberFormatException e) {
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Format numérique invalide !");
                } catch (Exception e) {
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Échec de la modification: " + e.getMessage());
                }
            }
        } else {
            showAlert(AlertType.WARNING, "Avertissement", "Aucun camion sélectionné !");
        }
    }

    @FXML
    private void handleDeleteCamion() {
        if (selectedCamion != null) {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Confirmation de suppression");
            alert.setHeaderText("Supprimer le camion");
            alert.setContentText("Êtes-vous sûr de vouloir supprimer ce camion ?");

            if (alert.showAndWait().get() == ButtonType.OK) {
                try {
                    serviceCamion.delete(selectedCamion);
                    loadCamions();
                    handleClearCamion();
                    showAlert(AlertType.INFORMATION, "Succès", "Camion supprimé avec succès !");
                } catch (Exception e) {
                    showAlert(AlertType.ERROR, "Erreur", "Échec de la suppression: " + e.getMessage());
                }
            }
        } else {
            showAlert(AlertType.WARNING, "Avertissement", "Aucun camion sélectionné !");
        }
    }

    @FXML
    private void handleClearCamion() {
        placeField.clear();
        placeField1.clear();
        timeField.clear();
        brandField.clear();
        camionImageView.setImage(null);
        selectedImage = null;
    }

    private Node createCamionCard(Camion camion) {
        VBox card = new VBox();
        card.getStyleClass().add("card-view");
        card.setPadding(new Insets(10));

        if (camion.getImage() != null && camion.getImage().length > 0) {
            ImageView imgView = new ImageView(new Image(new java.io.ByteArrayInputStream(camion.getImage())));
            imgView.setFitWidth(100);
            imgView.setPreserveRatio(true);
            card.getChildren().add(imgView);
        }

        Label typeLabel = new Label("Type: " + camion.getType());
        Label statusLabel = new Label("Statut: " + camion.getStatut());
        Label capacityLabel = new Label("Capacité: " + camion.getCapacity());
        Label zoneLabel = new Label("ID Zone: " + camion.getId_zone());

        card.getChildren().addAll(typeLabel, statusLabel, capacityLabel, zoneLabel);
        return card;
    }
}