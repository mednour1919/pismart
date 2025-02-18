package tn.esprit.Controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import tn.esprit.model.Projet;
import tn.esprit.model.Fournisseur;
import tn.esprit.services.ServiceProjet;
import tn.esprit.services.ServiceFournisseur;

import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    // Liste des fournisseurs et projets
    @FXML
    private ListView<Projet> projetListView;
    @FXML
    private ListView<Fournisseur> fournisseurListView;

    // Champs de saisie pour les projets
    @FXML
    private TextField projetNomField;
    @FXML
    private TextField projetDescriptionField;
    @FXML
    private TextField projetBudgetField;
    @FXML
    private TextField projetDepenseField;
    @FXML
    private DatePicker projetDateDebutPicker;
    @FXML
    private DatePicker projetDateFinPicker;
    @FXML
    private TextField projetStatutField;
    @FXML
    private TextField projetSoldeField;
    @FXML
    private ComboBox<Fournisseur> fournisseurComboBox;

    // Champs de saisie pour les fournisseurs
    @FXML
    private TextField nomFournisseurField;
    @FXML
    private TextField adresseFournisseurField;

    // Services pour gérer les projets et les fournisseurs
    private final ServiceProjet serviceProjet = new ServiceProjet();
    private final ServiceFournisseur serviceFournisseur = new ServiceFournisseur();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialiser les ListView et ComboBox
        refreshProjetList();
        refreshFournisseurList();
        fournisseurComboBox.setItems(FXCollections.observableArrayList(serviceFournisseur.getAll()));

        // Ajouter un écouteur pour la sélection d'un projet dans la ListView
        projetListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                remplirChampsAvecProjet(newValue);
            }
        });

        // Ajouter un écouteur pour la sélection d'un fournisseur dans la ListView
        fournisseurListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                remplirChampsAvecFournisseur(newValue);
            }
        });

        // Configurer l'affichage des projets dans la ListView
        projetListView.setCellFactory(param -> new ListCell<Projet>() {
            @Override
            protected void updateItem(Projet projet, boolean empty) {
                super.updateItem(projet, empty);
                if (empty || projet == null) {
                    setText(null);
                } else {
                    setText(projet.getNom() + " - " + (projet.getFournisseur() != null ? projet.getFournisseur().getNom() : "Aucun fournisseur"));
                }
            }
        });
    }

    // Méthodes pour gérer les projets
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
        serviceProjet.add(projet);
        refreshProjetList();

        // Calculer et afficher le solde
        double solde = calculerSolde(budget, depense);
        projetSoldeField.setText(String.valueOf(solde));
    }

    @FXML
    private void handleUpdateProjet() {
        Projet selectedProjet = projetListView.getSelectionModel().getSelectedItem();
        if (selectedProjet == null) {
            afficherAlerte("Erreur", "Aucun projet sélectionné.");
            return;
        }

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

        // Calculer et afficher le solde
        double solde = calculerSolde(budget, depense);
        projetSoldeField.setText(String.valueOf(solde));
    }

    @FXML
    private void handleDeleteProjet() {
        Projet selectedProjet = projetListView.getSelectionModel().getSelectedItem();
        if (selectedProjet == null) {
            afficherAlerte("Erreur", "Aucun projet sélectionné.");
            return;
        }

        serviceProjet.delete(selectedProjet);
        refreshProjetList();
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

    private void refreshProjetList() {
        projetListView.getItems().clear();
        projetListView.getItems().addAll(serviceProjet.getAll());
    }

    // Méthodes pour gérer les fournisseurs
    @FXML
    private void handleAddFournisseur() {
        String nom = nomFournisseurField.getText();
        String adresse = adresseFournisseurField.getText();

        if (nom.isEmpty() || adresse.isEmpty()) {
            afficherAlerte("Erreur", "Tous les champs doivent être remplis.");
            return;
        }

        Fournisseur fournisseur = new Fournisseur(nom, adresse);
        serviceFournisseur.add(fournisseur);
        refreshFournisseurList();
    }

    @FXML
    private void handleUpdateFournisseur() {
        Fournisseur selectedFournisseur = fournisseurListView.getSelectionModel().getSelectedItem();
        if (selectedFournisseur == null) {
            afficherAlerte("Erreur", "Aucun fournisseur sélectionné.");
            return;
        }

        String nom = nomFournisseurField.getText();
        String adresse = adresseFournisseurField.getText();

        if (nom.isEmpty() || adresse.isEmpty()) {
            afficherAlerte("Erreur", "Tous les champs doivent être remplis.");
            return;
        }

        selectedFournisseur.setNom(nom);
        selectedFournisseur.setAdresse(adresse);

        serviceFournisseur.update(selectedFournisseur);
        refreshFournisseurList();
    }

    @FXML
    private void handleDeleteFournisseur() {
        Fournisseur selectedFournisseur = fournisseurListView.getSelectionModel().getSelectedItem();
        if (selectedFournisseur == null) {
            afficherAlerte("Erreur", "Aucun fournisseur sélectionné.");
            return;
        }

        serviceFournisseur.delete(selectedFournisseur);
        refreshFournisseurList();
    }

    @FXML
    private void handleClearFournisseur() {
        nomFournisseurField.clear();
        adresseFournisseurField.clear();
    }

    private void refreshFournisseurList() {
        fournisseurListView.getItems().clear();
        fournisseurListView.getItems().addAll(serviceFournisseur.getAll());
    }

    // Méthodes utilitaires
    private void remplirChampsAvecProjet(Projet projet) {
        projetNomField.setText(projet.getNom());
        projetDescriptionField.setText(projet.getDescription());
        projetBudgetField.setText(String.valueOf(projet.getBudget()));
        projetDepenseField.setText(String.valueOf(projet.getDepense()));
        projetDateDebutPicker.setValue(projet.getDateDebut().toLocalDate());
        projetDateFinPicker.setValue(projet.getDateFin().toLocalDate());
        projetStatutField.setText(projet.getStatut());
        fournisseurComboBox.setValue(projet.getFournisseur());

        // Calculer et afficher le solde
        double solde = calculerSolde(projet.getBudget(), projet.getDepense());
        projetSoldeField.setText(String.valueOf(solde));
    }

    private void remplirChampsAvecFournisseur(Fournisseur fournisseur) {
        nomFournisseurField.setText(fournisseur.getNom());
        adresseFournisseurField.setText(fournisseur.getAdresse());
    }

    private double calculerSolde(double budget, double depense) {
        return budget - depense;
    }

    private void afficherAlerte(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}