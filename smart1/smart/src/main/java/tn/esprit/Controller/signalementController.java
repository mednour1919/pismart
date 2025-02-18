package tn.esprit.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import tn.esprit.model.Signalement;
import tn.esprit.services.Servicesignalement;

import java.sql.Timestamp;

public class signalementController {

    // Déclarer les éléments FXML
    @FXML
    private TextField typeSignalementField;

    @FXML
    private TextArea descriptionArea;

    @FXML
    private TextField dateSignalementField;

    @FXML
    private Label statutLabel;

    @FXML
    private Button ajouterButton;

    @FXML
    private Label confirmationLabel;

    // Instance du service pour ajouter un signalement
    private Servicesignalement serviceSignalement;

    public signalementController() {
        // Initialiser le service
        serviceSignalement = new Servicesignalement();
    }

    // Méthode pour gérer l'ajout d'un signalement
    @FXML
    private void handleAjouterSignalement() {
        // Récupérer les valeurs des champs
        String typeSignalement = typeSignalementField.getText();
        String description = descriptionArea.getText();
        String dateSignalementString = dateSignalementField.getText();

        // Vérifier si tous les champs sont remplis
        if (typeSignalement.isEmpty() || description.isEmpty() || dateSignalementString.isEmpty()) {
            confirmationLabel.setText("Tous les champs sont obligatoires.");
            confirmationLabel.setTextFill(javafx.scene.paint.Color.RED);
            return;
        }

        // Convertir la date au format Timestamp
        Timestamp dateSignalement;
        try {
            // Ici, vous pouvez ajouter une logique pour valider et convertir le format de la date.
            // Pour simplifier, on considère ici que la date est au format "jj/mm/aaaa"
            String[] dateParts = dateSignalementString.split("/");
            int day = Integer.parseInt(dateParts[0]);
            int month = Integer.parseInt(dateParts[1]);
            int year = Integer.parseInt(dateParts[2]);
            dateSignalement = Timestamp.valueOf(String.format("%d-%02d-%02d 00:00:00", year, month, day));
        } catch (Exception e) {
            confirmationLabel.setText("Format de la date incorrect.");
            confirmationLabel.setTextFill(javafx.scene.paint.Color.RED);
            return;
        }

        // Créer un nouvel objet Signalement
        Signalement signalement = new Signalement();
        signalement.setTypeSignalement(typeSignalement);
        signalement.setDescription(description);
        signalement.setDateSignalement(dateSignalement);
        signalement.setStatut("En attente");

        // Ajouter le signalement via le service
        serviceSignalement.add(signalement);

        // Afficher un message de confirmation
        confirmationLabel.setText("Signalement ajouté avec succès !");
        confirmationLabel.setTextFill(javafx.scene.paint.Color.GREEN);

        // Réinitialiser les champs
        typeSignalementField.clear();
        descriptionArea.clear();
        dateSignalementField.clear();
    }
}
