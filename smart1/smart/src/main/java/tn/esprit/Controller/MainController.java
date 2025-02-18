package tn.esprit.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import tn.esprit.model.Signalement;
import tn.esprit.model.reponse;
import tn.esprit.services.Servicesignalement;
import tn.esprit.services.Servicereponse;
import java.sql.Timestamp;
import java.time.LocalDate;

public class MainController {
    @FXML private ListView<Signalement> signalementListView;
    @FXML private TextField typeSignalementField;
    @FXML private TextField descriptionField;
    @FXML private DatePicker dateSignalementPicker;
    @FXML private TextField statutSignalementField;

    @FXML private ListView<reponse> reponseListView;
    @FXML private TextField idSignalementField;
    @FXML private TextField reponseField;
    @FXML private DatePicker dateReponsePicker;
    @FXML private TextField statutReponseField;

    private final Servicesignalement signalementService = new Servicesignalement();
    private final Servicereponse reponseService = new Servicereponse();
    private Signalement selectedSignalement;
    private reponse selectedReponse;

    @FXML
    public void initialize() {
        refreshSignalementList();
        refreshReponseList();

        // Signalement list selection listener
        signalementListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedSignalement = newSelection;
                typeSignalementField.setText(newSelection.getTypeSignalement());
                descriptionField.setText(newSelection.getDescription());
                dateSignalementPicker.setValue(newSelection.getDateSignalement().toLocalDateTime().toLocalDate());
                statutSignalementField.setText(newSelection.getStatut());
            }
        });

        // Reponse list selection listener
        reponseListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedReponse = newSelection;
                idSignalementField.setText(String.valueOf(newSelection.getIdSignalement()));
                reponseField.setText(newSelection.getReponse());
                dateReponsePicker.setValue(newSelection.getDateReponse().toLocalDateTime().toLocalDate());
                statutReponseField.setText(newSelection.getStatut());
            }
        });
    }

    // Signalement CRUD operations
    @FXML
    private void handleAddSignalement() {
        try {
            Signalement signalement = new Signalement(
                    typeSignalementField.getText(),
                    descriptionField.getText(),
                    Timestamp.valueOf(dateSignalementPicker.getValue().atStartOfDay()),
                    statutSignalementField.getText()
            );
            signalementService.add(signalement);
            refreshSignalementList();
            clearSignalementFields();
            showAlert("Success", "Signalement ajouté avec succès !", Alert.AlertType.INFORMATION);
        } catch (Exception e) {
            showAlert("Erreur", "Erreur lors de l'ajout du signalement : " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleUpdateSignalement() {
        if (selectedSignalement == null) {
            showAlert("Erreur", "Veuillez sélectionner un signalement à mettre à jour", Alert.AlertType.WARNING);
            return;
        }

        try {
            selectedSignalement.setTypeSignalement(typeSignalementField.getText());
            selectedSignalement.setDescription(descriptionField.getText());
            selectedSignalement.setDateSignalement(Timestamp.valueOf(dateSignalementPicker.getValue().atStartOfDay()));
            selectedSignalement.setStatut(statutSignalementField.getText());

            signalementService.update(selectedSignalement);
            refreshSignalementList();
            clearSignalementFields();
            showAlert("Success", "Signalement mis à jour avec succès !", Alert.AlertType.INFORMATION);
        } catch (Exception e) {
            showAlert("Erreur", "Erreur lors de la mise à jour du signalement : " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleDeleteSignalement() {
        if (selectedSignalement == null) {
            showAlert("Erreur", "Veuillez sélectionner un signalement à supprimer", Alert.AlertType.WARNING);
            return;
        }

        try {
            signalementService.delete(selectedSignalement);
            refreshSignalementList();
            clearSignalementFields();
            showAlert("Success", "Signalement supprimé avec succès !", Alert.AlertType.INFORMATION);
        } catch (Exception e) {
            showAlert("Erreur", "Erreur lors de la suppression du signalement : " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleClearSignalement() {
        clearSignalementFields();
    }

    // Reponse CRUD operations
    @FXML
    private void handleAddReponse() {
        try {
            reponse reponseInstance = new reponse(
                    Integer.parseInt(idSignalementField.getText()),
                    reponseField.getText(),
                    Timestamp.valueOf(dateReponsePicker.getValue().atStartOfDay()),
                    statutReponseField.getText()
            );
            reponseService.add(reponseInstance);
            refreshReponseList();
            clearReponseFields();
            showAlert("Success", "Réponse ajoutée avec succès !", Alert.AlertType.INFORMATION);
        } catch (Exception e) {
            showAlert("Erreur", "Erreur lors de l'ajout de la réponse : " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleUpdateReponse() {
        if (selectedReponse == null) {
            showAlert("Erreur", "Veuillez sélectionner une réponse à mettre à jour", Alert.AlertType.WARNING);
            return;
        }

        try {
            selectedReponse.setIdSignalement(Integer.parseInt(idSignalementField.getText()));
            selectedReponse.setReponse(reponseField.getText());
            selectedReponse.setDateReponse(Timestamp.valueOf(dateReponsePicker.getValue().atStartOfDay()));
            selectedReponse.setStatut(statutReponseField.getText());

            reponseService.update(selectedReponse);
            refreshReponseList();
            clearReponseFields();
            showAlert("Success", "Réponse mise à jour avec succès !", Alert.AlertType.INFORMATION);
        } catch (Exception e) {
            showAlert("Erreur", "Erreur lors de la mise à jour de la réponse : " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleDeleteReponse() {
        if (selectedReponse == null) {
            showAlert("Erreur", "Veuillez sélectionner une réponse à supprimer", Alert.AlertType.WARNING);
            return;
        }

        try {
            reponseService.delete(selectedReponse);
            refreshReponseList();
            clearReponseFields();
            showAlert("Success", "Réponse supprimée avec succès !", Alert.AlertType.INFORMATION);
        } catch (Exception e) {
            showAlert("Erreur", "Erreur lors de la suppression de la réponse : " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleClearReponse() {
        clearReponseFields();
    }

    // Helper methods
    private void refreshSignalementList() {
        ObservableList<Signalement> signalements = FXCollections.observableArrayList(signalementService.getAll());
        signalementListView.setItems(signalements);
    }

    private void refreshReponseList() {
        ObservableList<reponse> reponses = FXCollections.observableArrayList(reponseService.getAll());
        reponseListView.setItems(reponses);
    }

    private void clearSignalementFields() {
        typeSignalementField.clear();
        descriptionField.clear();
        dateSignalementPicker.setValue(null);
        statutSignalementField.clear();
        selectedSignalement = null;
        signalementListView.getSelectionModel().clearSelection();
    }

    private void clearReponseFields() {
        idSignalementField.clear();
        reponseField.clear();
        dateReponsePicker.setValue(null);
        statutReponseField.clear();
        selectedReponse = null;
        reponseListView.getSelectionModel().clearSelection();
    }

    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
