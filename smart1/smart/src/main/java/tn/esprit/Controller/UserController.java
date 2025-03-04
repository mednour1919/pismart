package tn.esprit.Controller;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.chart.BarChart;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.util.Duration;
import org.json.JSONObject;
import tn.esprit.model.Fournisseur;
import tn.esprit.model.Projet;
import tn.esprit.model.ProjetWrapper;
import tn.esprit.services.ServiceProjet;
import tn.esprit.services.ServiceFournisseur;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class UserController implements Initializable {

    @FXML
    private VBox projetCardContainer;
    @FXML
    private VBox fournisseurCardContainer;
    @FXML
    private Label projetCountLabel, fournisseurCountLabel;
    @FXML
    private TextField searchField;
    @FXML
    private TextField cityField;
    @FXML
    private Label weatherLabel;
    @FXML
    private WebView mapView;

    private final ServiceProjet serviceProjet = new ServiceProjet();
    private final ServiceFournisseur serviceFournisseur = new ServiceFournisseur();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        refreshProjetList();
        refreshFournisseurList();

        PauseTransition pause = new PauseTransition(Duration.millis(300));
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            pause.setOnFinished(event -> handleSearch());
            pause.playFromStart();
        });

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
    private void handleSearch() {
        String keyword = searchField.getText().toLowerCase();
        List<ProjetWrapper> filteredProjets = serviceProjet.getAll().stream()
                .map(projet -> new ProjetWrapper(projet, 0.0, 0.0, new double[]{}, new double[]{}))
                .filter(projetWrapper -> projetWrapper.getProjet().getNom().toLowerCase().contains(keyword))
                .collect(Collectors.toList());
        refreshProjetList(filteredProjets);
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
        });

        return card;
    }

    private void refreshDashboard() {
        projetCountLabel.setText(String.valueOf(projetCardContainer.getChildren().size()));
        fournisseurCountLabel.setText(String.valueOf(fournisseurCardContainer.getChildren().size()));
    }

    private double calculerSolde(double budget, double depense) {
        return budget - depense;
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

    private void afficherAlerte(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    private GridPane cardGrid;

    private void addCardToGrid(Projet projet) {
        // Convertir Projet en ProjetWrapper
        ProjetWrapper projetWrapper = new ProjetWrapper(projet, 0.0, 0.0, new double[]{}, new double[]{});
        AnchorPane card = createProjetCard(projetWrapper); // Appel avec ProjetWrapper
        int row = cardGrid.getRowCount();
        int col = cardGrid.getColumnCount();
        cardGrid.add(card, col, row);
    }
}