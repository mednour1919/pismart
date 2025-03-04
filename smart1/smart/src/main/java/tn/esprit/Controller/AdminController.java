package tn.esprit.Controller;

import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.chart.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.util.Duration;
import org.json.JSONArray;
import org.json.JSONObject;
import tn.esprit.model.Fournisseur;
import tn.esprit.model.Projet;
import tn.esprit.model.ProjetWrapper;
import tn.esprit.services.ServiceFournisseur;
import tn.esprit.services.ServiceProjet;
import tn.esprit.utils.QRCodeGenerator;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class AdminController implements Initializable {

    @FXML
    private VBox projetCardContainer;
    @FXML
    private VBox fournisseurCardContainer;
    @FXML
    private TextField projetNomField, projetDescriptionField, projetBudgetField, projetDepenseField, projetStatutField, projetSoldeField;
    @FXML
    private DatePicker projetDateDebutPicker, projetDateFinPicker;
    @FXML
    private ComboBox<Fournisseur> fournisseurComboBox;
    @FXML
    private TextField nomFournisseurField, adresseFournisseurField, certificationsField, risquesField, performancesField, emailField, telephoneField, siteWebField, secteurActiviteField, responsableField;
    @FXML
    private CheckBox estActifCheckBox;
    @FXML
    private BarChart<String, Number> budgetChart;
    @FXML
    private Label projetCountLabel, fournisseurCountLabel;
    @FXML
    private TextField searchField;
    @FXML
    private ImageView qrCodeImageView;
    @FXML
    private TextField cityField;
    @FXML
    private Label weatherLabel;
    @FXML
    private ComboBox<String> sortCriteriaComboBox;
    @FXML
    private WebView mapView;

    private final ServiceProjet serviceProjet = new ServiceProjet();
    private final ServiceFournisseur serviceFournisseur = new ServiceFournisseur();

    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    private static final String PHONE_REGEX = "^\\+?[0-9]{8,15}$";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        refreshProjetList();
        refreshFournisseurList();
        fournisseurComboBox.setItems(FXCollections.observableArrayList(serviceFournisseur.getAll()));

        sortCriteriaComboBox.getItems().addAll("Nom", "Adresse");
        sortCriteriaComboBox.setValue("Nom"); // Valeur par défaut

        PauseTransition pause = new PauseTransition(Duration.millis(300));
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            pause.setOnFinished(event -> handleSearch());
            pause.playFromStart();
        });

        refreshBudgetChart();
        refreshDashboard();

        WebEngine webEngine = mapView.getEngine();
        webEngine.loadContent(
                "<!DOCTYPE html>" +
                        "<html>" +
                        "<head>" +
                        "  <link rel='stylesheet' href='https://unpkg.com/leaflet/dist/leaflet.css' />" +
                        "  <script src='https://unpkg.com/leaflet/dist/leaflet.js'></script>" +
                        "</head>" +
                        "<body>" +
                        "  <div id='map' style='height: 100%; width: 100%;'></div>" +
                        "  <script>" +
                        "    var map = L.map('map').setView([36.8065, 10.1815], 12);" + // Centre sur Tunis
                        "    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {" +
                        "      attribution: '© OpenStreetMap contributors'" +
                        "    }).addTo(map);" +
                        "    L.marker([36.8065, 10.1815]).addTo(map)" + // Ajoute un marqueur
                        "      .bindPopup('Tunis');" +
                        "  </script>" +
                        "</body>" +
                        "</html>"
        );
    }

    @FXML
    private void handleSort() {
        String sortCriteria = sortCriteriaComboBox.getValue();
        if (sortCriteria == null) {
            afficherAlerte("Erreur", "Veuillez sélectionner un critère de tri.");
            return;
        }

        List<Fournisseur> sortedList;
        switch (sortCriteria) {
            case "Nom":
                sortedList = serviceFournisseur.getAll().stream()
                        .sorted(Comparator.comparing(Fournisseur::getNom))
                        .collect(Collectors.toList());
                break;
            case "Adresse":
                sortedList = serviceFournisseur.getAll().stream()
                        .sorted(Comparator.comparing(Fournisseur::getAdresse))
                        .collect(Collectors.toList());
                break;
            default:
                sortedList = serviceFournisseur.getAll();
                break;
        }

        refreshFournisseurList(sortedList);
    }

    @FXML
    private void handleAddProjet() {
        String nom = projetNomField.getText();
        String description = projetDescriptionField.getText();
        double budget = 0.0;
        double depense = 0.0;
        String statut = projetStatutField.getText();
        LocalDate dateDebut = projetDateDebutPicker.getValue();
        LocalDate dateFin = projetDateFinPicker.getValue();
        Fournisseur fournisseur = fournisseurComboBox.getValue();

        try {
            budget = Double.parseDouble(projetBudgetField.getText());
            depense = Double.parseDouble(projetDepenseField.getText());
        } catch (NumberFormatException e) {
            afficherAlerte("Erreur", "Le budget et la dépense doivent être des nombres valides.");
            return;
        }

        if (nom.isEmpty() || description.isEmpty() || statut.isEmpty() || dateDebut == null || dateFin == null || fournisseur == null) {
            afficherAlerte("Erreur", "Tous les champs doivent être remplis.");
            return;
        }

        if (dateFin.isBefore(dateDebut)) {
            afficherAlerte("Erreur", "La date de fin doit être après la date de début.");
            return;
        }

        Projet projet = new Projet(nom, description, budget, depense, Date.valueOf(dateDebut), Date.valueOf(dateFin), statut, fournisseur);
        double latitude = 0.0; // Remplacer par la latitude réelle
        double longitude = 0.0; // Remplacer par la longitude réelle
        double[] latitudesZoneImpact = {}; // Remplacer par les latitudes de la zone d'impact
        double[] longitudesZoneImpact = {}; // Remplacer par les longitudes de la zone d'impact

        ProjetWrapper projetWrapper = new ProjetWrapper(projet, latitude, longitude, latitudesZoneImpact, longitudesZoneImpact);
        serviceProjet.add(projet); // Ajouter le projet au service
        refreshProjetList();

        afficherAlerte("Succès", "Projet ajouté avec succès.");

        double solde = calculerSolde(budget, depense);
        projetSoldeField.setText(String.valueOf(solde));
    }

    private void afficherDetailsProjet(ProjetWrapper projetWrapper) {
        if (projetWrapper != null) {
            Projet projet = projetWrapper.getProjet();
            projetNomField.setText(projet.getNom());
            projetDescriptionField.setText(projet.getDescription());
            projetBudgetField.setText(String.valueOf(projet.getBudget()));
            projetDepenseField.setText(String.valueOf(projet.getDepense()));
            projetDateDebutPicker.setValue(projet.getDateDebut() != null ? projet.getDateDebut().toLocalDate() : null);
            projetDateFinPicker.setValue(projet.getDateFin() != null ? projet.getDateFin().toLocalDate() : null);
            projetStatutField.setText(projet.getStatut());
            fournisseurComboBox.setValue(projet.getFournisseur());

            double solde = calculerSolde(projet.getBudget(), projet.getDepense());
            projetSoldeField.setText(String.valueOf(solde));
        } else {
            handleClearProjet();
        }
    }

    @FXML
    private void handleUpdateProjet() {
        ProjetWrapper selectedProjetWrapper = getSelectedProjetWrapper();
        if (selectedProjetWrapper == null) {
            afficherAlerte("Erreur", "Aucun projet sélectionné.");
            return;
        }

        Projet selectedProjet = selectedProjetWrapper.getProjet();
        String nom = projetNomField.getText();
        String description = projetDescriptionField.getText();
        double budget = 0.0;
        double depense = 0.0;
        String statut = projetStatutField.getText();
        LocalDate dateDebut = projetDateDebutPicker.getValue();
        LocalDate dateFin = projetDateFinPicker.getValue();
        Fournisseur fournisseur = fournisseurComboBox.getValue();

        try {
            budget = Double.parseDouble(projetBudgetField.getText());
            depense = Double.parseDouble(projetDepenseField.getText());
        } catch (NumberFormatException e) {
            afficherAlerte("Erreur", "Le budget et la dépense doivent être des nombres valides.");
            return;
        }

        if (nom.isEmpty() || description.isEmpty() || statut.isEmpty() || dateDebut == null || dateFin == null || fournisseur == null) {
            afficherAlerte("Erreur", "Tous les champs doivent être remplis.");
            return;
        }

        if (dateFin.isBefore(dateDebut)) {
            afficherAlerte("Erreur", "La date de fin doit être après la date de début.");
            return;
        }

        selectedProjet.setNom(nom);
        selectedProjet.setDescription(description);
        selectedProjet.setBudget(budget);
        selectedProjet.setDepense(depense);
        selectedProjet.setDateDebut(Date.valueOf(dateDebut));
        selectedProjet.setDateFin(Date.valueOf(dateFin));
        selectedProjet.setStatut(statut);
        selectedProjet.setFournisseur(fournisseur);

        serviceProjet.update(selectedProjet);
        refreshProjetList();

        afficherAlerte("Succès", "Projet mis à jour avec succès.");

        double solde = calculerSolde(budget, depense);
        projetSoldeField.setText(String.valueOf(solde));
    }

    @FXML
    private void handleDeleteProjet() {
        ProjetWrapper selectedProjetWrapper = getSelectedProjetWrapper();
        if (selectedProjetWrapper == null) {
            afficherAlerte("Erreur", "Aucun projet sélectionné.");
            return;
        }

        Projet selectedProjet = selectedProjetWrapper.getProjet();
        serviceProjet.delete(selectedProjet);
        refreshProjetList();

        afficherAlerte("Succès", "Projet supprimé avec succès.");
    }

    @FXML
    private void handleClearProjet() {
        projetNomField.clear();
        projetDescriptionField.clear();
        projetBudgetField.clear();
        projetDepenseField.clear();
        projetDateDebutPicker.setValue(null);
        projetDateFinPicker.setValue(null);
        projetStatutField.clear();
        projetSoldeField.clear();
        fournisseurComboBox.setValue(null);
    }

    @FXML
    private void handleSearch() {
        String keyword = searchField.getText().toLowerCase();
        List<ProjetWrapper> filteredProjets = serviceProjet.getAll().stream()
                .map(projet -> new ProjetWrapper(projet, 0.0, 0.0, new double[]{}, new double[]{}))
                .filter(projetWrapper -> projetWrapper.getProjet().getNom().toLowerCase().contains(keyword))
                .collect(Collectors.toList());
        refreshProjetList(filteredProjets);
    }

    @FXML
    private void handleAddFournisseur() {
        String nom = nomFournisseurField.getText().trim();
        String adresse = adresseFournisseurField.getText().trim();
        String email = emailField.getText().trim();
        String telephone = telephoneField.getText().trim();

        if (nom.isEmpty() || adresse.isEmpty()) {
            afficherAlerte("Erreur", "Les champs 'Nom' et 'Adresse' sont obligatoires.");
            return;
        }

        if (!email.isEmpty() && !isValidEmail(email)) {
            afficherAlerte("Erreur", "L'email n'est pas valide.");
            return;
        }

        if (!telephone.isEmpty() && !isValidPhone(telephone)) {
            afficherAlerte("Erreur", "Le numéro de téléphone n'est pas valide.");
            return;
        }

        Fournisseur fournisseur = new Fournisseur(nom, adresse, certificationsField.getText(), risquesField.getText(),
                performancesField.getText(), email, telephone, siteWebField.getText(), secteurActiviteField.getText(),
                responsableField.getText(), estActifCheckBox.isSelected());

        serviceFournisseur.add(fournisseur);
        refreshFournisseurList();

        afficherAlerte("Succès", "Fournisseur ajouté avec succès.");
    }

    @FXML
    private void handleUpdateFournisseur() {
        Fournisseur selectedFournisseur = getSelectedFournisseur();
        if (selectedFournisseur == null) {
            afficherAlerte("Erreur", "Aucun fournisseur sélectionné.");
            return;
        }

        String nom = nomFournisseurField.getText().trim();
        String adresse = adresseFournisseurField.getText().trim();
        String email = emailField.getText().trim();
        String telephone = telephoneField.getText().trim();

        if (nom.isEmpty() || adresse.isEmpty()) {
            afficherAlerte("Erreur", "Les champs 'Nom' et 'Adresse' sont obligatoires.");
            return;
        }

        if (!email.isEmpty() && !isValidEmail(email)) {
            afficherAlerte("Erreur", "L'email n'est pas valide.");
            return;
        }

        if (!telephone.isEmpty() && !isValidPhone(telephone)) {
            afficherAlerte("Erreur", "Le numéro de téléphone n'est pas valide.");
            return;
        }

        selectedFournisseur.setNom(nom);
        selectedFournisseur.setAdresse(adresse);
        selectedFournisseur.setCertifications(certificationsField.getText());
        selectedFournisseur.setRisques(risquesField.getText());
        selectedFournisseur.setPerformances(performancesField.getText());
        selectedFournisseur.setEmail(email);
        selectedFournisseur.setTelephone(telephone);
        selectedFournisseur.setSiteWeb(siteWebField.getText());
        selectedFournisseur.setSecteurActivite(secteurActiviteField.getText());
        selectedFournisseur.setResponsable(responsableField.getText());
        selectedFournisseur.setEstActif(estActifCheckBox.isSelected());

        serviceFournisseur.update(selectedFournisseur);
        refreshFournisseurList();

        afficherAlerte("Succès", "Fournisseur mis à jour avec succès.");
    }

    @FXML
    private void handleDeleteFournisseur() {
        Fournisseur selectedFournisseur = getSelectedFournisseur();
        if (selectedFournisseur == null) {
            afficherAlerte("Erreur", "Aucun fournisseur sélectionné.");
            return;
        }

        serviceFournisseur.delete(selectedFournisseur);
        refreshFournisseurList();

        afficherAlerte("Succès", "Fournisseur supprimé avec succès.");
    }

    @FXML
    private void handleClearFournisseur() {
        nomFournisseurField.clear();
        adresseFournisseurField.clear();
        certificationsField.clear();
        risquesField.clear();
        performancesField.clear();
        emailField.clear();
        telephoneField.clear();
        siteWebField.clear();
        secteurActiviteField.clear();
        responsableField.clear();
        estActifCheckBox.setSelected(false);
    }

    private void refreshProjetList() {
        refreshProjetList(serviceProjet.getAll().stream()
                .map(projet -> new ProjetWrapper(projet, 0.0, 0.0, new double[]{}, new double[]{}))
                .collect(Collectors.toList()));
    }

    private void refreshProjetList(List<ProjetWrapper> projets) {
        projetCardContainer.getChildren().clear();
        for (ProjetWrapper projetWrapper : projets) {
            Projet projet = projetWrapper.getProjet();
            AnchorPane card = createProjetCard(projetWrapper);
            projetCardContainer.getChildren().add(card);
        }
    }

    private AnchorPane createProjetCard(ProjetWrapper projetWrapper) {
        AnchorPane card = new AnchorPane();
        card.getStyleClass().add("card");

        card.setUserData(projetWrapper);

        Projet projet = projetWrapper.getProjet();

        Label nomLabel = new Label("Nom du projet: " + projet.getNom());
        nomLabel.getStyleClass().add("card-title");

        Label budgetLabel = new Label("Budget: " + projet.getBudget());
        budgetLabel.getStyleClass().add("card-detail");

        Label depenseLabel = new Label("Dépense: " + projet.getDepense());
        depenseLabel.getStyleClass().add("card-detail");

        double solde = calculerSolde(projet.getBudget(), projet.getDepense());
        Label soldeLabel = new Label("Solde: " + solde);
        soldeLabel.getStyleClass().add("card-detail");

        Label dateDebutLabel = new Label("Date de début: " + (projet.getDateDebut() != null ? projet.getDateDebut().toString() : "N/A"));
        dateDebutLabel.getStyleClass().add("card-detail");

        Label dateFinLabel = new Label("Date de fin: " + (projet.getDateFin() != null ? projet.getDateFin().toString() : "N/A"));
        dateFinLabel.getStyleClass().add("card-detail");

        Label statutLabel = new Label("Statut: " + projet.getStatut());
        statutLabel.getStyleClass().add("card-detail");

        VBox cardContent = new VBox(nomLabel, budgetLabel, depenseLabel, soldeLabel, dateDebutLabel, dateFinLabel, statutLabel);
        cardContent.setSpacing(5);
        cardContent.setPadding(new Insets(10));

        card.getChildren().add(cardContent);

        card.setOnMouseClicked(event -> {
            projetCardContainer.getChildren().forEach(node -> node.getStyleClass().remove("selected"));

            card.getStyleClass().add("selected");

            afficherDetailsProjet(projetWrapper);
        });

        return card;
    }

    private void refreshFournisseurList() {
        refreshFournisseurList(serviceFournisseur.getAll());
    }

    private void refreshFournisseurList(List<Fournisseur> fournisseurs) {
        fournisseurCardContainer.getChildren().clear();
        for (Fournisseur fournisseur : fournisseurs) {
            AnchorPane card = createFournisseurCard(fournisseur);
            fournisseurCardContainer.getChildren().add(card);
        }
    }

    private void afficherDetailsFournisseur(Fournisseur fournisseur) {
        if (fournisseur != null) {
            nomFournisseurField.setText(fournisseur.getNom());
            adresseFournisseurField.setText(fournisseur.getAdresse());
            certificationsField.setText(fournisseur.getCertifications());
            risquesField.setText(fournisseur.getRisques());
            performancesField.setText(fournisseur.getPerformances());
            emailField.setText(fournisseur.getEmail());
            telephoneField.setText(fournisseur.getTelephone());
            siteWebField.setText(fournisseur.getSiteWeb());
            secteurActiviteField.setText(fournisseur.getSecteurActivite());
            responsableField.setText(fournisseur.getResponsable());
            estActifCheckBox.setSelected(fournisseur.isEstActif());
        } else {
            handleClearFournisseur();
        }
    }

    private AnchorPane createFournisseurCard(Fournisseur fournisseur) {
        AnchorPane card = new AnchorPane();
        card.getStyleClass().add("card");

        card.setUserData(fournisseur);

        Label nomLabel = new Label(fournisseur.getNom());
        nomLabel.getStyleClass().add("card-title");

        Label adresseLabel = new Label(fournisseur.getAdresse());
        adresseLabel.getStyleClass().add("card-description");

        Label emailLabel = new Label("Email: " + fournisseur.getEmail());
        emailLabel.getStyleClass().add("card-detail");

        Label telephoneLabel = new Label("Téléphone: " + fournisseur.getTelephone());
        telephoneLabel.getStyleClass().add("card-detail");

        VBox cardContent = new VBox(nomLabel, adresseLabel, emailLabel, telephoneLabel);
        cardContent.setSpacing(5);
        cardContent.setPadding(new Insets(10));

        card.getChildren().add(cardContent);

        card.setOnMouseClicked(event -> {
            fournisseurCardContainer.getChildren().forEach(node -> node.getStyleClass().remove("selected"));

            card.getStyleClass().add("selected");

            afficherDetailsFournisseur(fournisseur);
        });

        return card;
    }

    private void refreshBudgetChart() {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Budget");

        for (Node node : projetCardContainer.getChildren()) {
            if (node instanceof AnchorPane) {
                AnchorPane card = (AnchorPane) node;
                ProjetWrapper projetWrapper = (ProjetWrapper) card.getUserData();

                if (projetWrapper != null) {
                    Projet projet = projetWrapper.getProjet();
                    series.getData().add(new XYChart.Data<>(projet.getNom(), projet.getBudget()));
                }
            }
        }

        budgetChart.getData().clear();
        budgetChart.getData().add(series);
    }

    private void refreshDashboard() {
        projetCountLabel.setText(String.valueOf(projetCardContainer.getChildren().size()));
        fournisseurCountLabel.setText(String.valueOf(fournisseurCardContainer.getChildren().size()));
    }

    private void afficherAlerte(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private double calculerSolde(double budget, double depense) {
        return budget - depense;
    }

    private boolean isValidEmail(String email) {
        return email != null && email.matches(EMAIL_REGEX);
    }

    private boolean isValidPhone(String phone) {
        return phone != null && phone.matches(PHONE_REGEX);
    }

    @FXML
    public void generateQRCode(ActionEvent event) {
        ProjetWrapper selectedProjetWrapper = getSelectedProjetWrapper();
        if (selectedProjetWrapper == null) {
            afficherAlerte("Erreur", "Aucun projet sélectionné.");
            return;
        }

        try {
            String data = convertirProjetEnTexte(selectedProjetWrapper.getProjet());
            String filePath = "QRCode_" + System.currentTimeMillis() + ".png";
            QRCodeGenerator.generateQRCode(data, filePath);

            File file = new File(filePath);
            Image image = new Image(file.toURI().toString());
            qrCodeImageView.setImage(image);
        } catch (Exception e) {
            e.printStackTrace();
            afficherAlerte("Erreur", "Une erreur s'est produite lors de la génération du QR Code.");
        }
    }

    private String convertirProjetEnTexte(Projet projet) {
        return String.format(
                "Nom: %s\nDescription: %s\nBudget: %.2f\nDépense: %.2f\nDate de début: %s\nDate de fin: %s\nStatut: %s\nFournisseur: %s",
                projet.getNom(),
                projet.getDescription(),
                projet.getBudget(),
                projet.getDepense(),
                projet.getDateDebut() != null ? projet.getDateDebut().toString() : "N/A",
                projet.getDateFin() != null ? projet.getDateFin().toString() : "N/A",
                projet.getStatut(),
                projet.getFournisseur() != null ? projet.getFournisseur().getNom() : "Aucun fournisseur"
        );
    }

    @FXML
    private void handleGetWeather() {
        String city = cityField.getText().trim();

        if (city.isEmpty()) {
            afficherAlerte("Erreur", "Veuillez entrer le nom d'une ville.");
            return;
        }

        String apiKey = "db65531e584f33f4e8c6a06701a5e735";
        String apiUrl = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + apiKey + "&units=metric&lang=fr";

        try {
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            JSONObject jsonResponse = new JSONObject(response.toString());
            String cityName = jsonResponse.getString("name");
            String weatherDescription = jsonResponse.getJSONArray("weather").getJSONObject(0).getString("description");
            double temperature = jsonResponse.getJSONObject("main").getDouble("temp");
            int humidity = jsonResponse.getJSONObject("main").getInt("humidity");

            String weatherInfo = String.format("Météo à %s : %s\nTempérature : %.1f°C\nHumidité : %d%%",
                    cityName, weatherDescription, temperature, humidity);
            weatherLabel.setText(weatherInfo);
        } catch (Exception e) {
            e.printStackTrace();
            afficherAlerte("Erreur", "Impossible d'obtenir les données météo. Vérifiez le nom de la ville ou votre connexion Internet.");
        }
    }

    @FXML
    private void afficherProjetsSurCarte() {
        WebEngine webEngine = mapView.getEngine();
        for (Node node : projetCardContainer.getChildren()) {
            if (node instanceof AnchorPane) {
                AnchorPane card = (AnchorPane) node;
                ProjetWrapper projetWrapper = (ProjetWrapper) card.getUserData();

                if (projetWrapper != null) {
                    String script = String.format(
                            "var marker = L.marker([%f, %f]).addTo(map);" +
                                    "marker.bindPopup('%s');",
                            projetWrapper.getLatitude(),
                            projetWrapper.getLongitude(),
                            projetWrapper.getProjet().getNom()
                    );
                    webEngine.executeScript(script);
                }
            }
        }
    }

    @FXML
    private void afficherZonesImpact() {
        List<ProjetWrapper> projets = projetCardContainer.getChildren().stream()
                .map(node -> (AnchorPane) node)
                .map(card -> (ProjetWrapper) card.getUserData())
                .collect(Collectors.toList());
        WebEngine webEngine = mapView.getEngine();
        for (ProjetWrapper projetWrapper : projets) {
            String script = String.format(
                    "var zoneCoords = %s;" +
                            "var zone = L.polygon(zoneCoords, {" +
                            "  color: '#FF0000'," +
                            "  fillOpacity: 0.5" +
                            "}).addTo(map);" +
                            "zone.bindPopup('%s');",
                    convertirCoordonneesEnJSON(projetWrapper.getLatitudesZoneImpact(), projetWrapper.getLongitudesZoneImpact()),
                    projetWrapper.getProjet().getNom()
            );
            webEngine.executeScript(script);
        }
    }

    private String convertirCoordonneesEnJSON(double[] latitudes, double[] longitudes) {
        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < latitudes.length; i++) {
            json.append(String.format("[%f, %f]", latitudes[i], longitudes[i]));
            if (i < latitudes.length - 1) {
                json.append(",");
            }
        }
        json.append("]");
        return json.toString();
    }

    private ProjetWrapper getSelectedProjetWrapper() {
        for (Node node : projetCardContainer.getChildren()) {
            if (node.getStyleClass().contains("selected")) {
                return (ProjetWrapper) node.getUserData();
            }
        }
        return null;
    }

    private Fournisseur getSelectedFournisseur() {
        for (Node node : fournisseurCardContainer.getChildren()) {
            if (node.getStyleClass().contains("selected")) {
                return (Fournisseur) node.getUserData();
            }
        }
        return null;
    }
}