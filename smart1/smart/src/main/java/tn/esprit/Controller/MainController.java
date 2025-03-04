package tn.esprit.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainController {

    @FXML
    private void goToAdminInterface() {
        try {
            // Charge le fichier FXML de l'interface Admin
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/admin_interface.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Interface Admin");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Erreur lors du chargement de l'interface Admin : " + e.getMessage());
        }
    }

    @FXML
    private void goToUserInterface() {
        try {
            // Charge le fichier FXML de l'interface Utilisateur
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/user_interface.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Interface Utilisateur");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Erreur lors du chargement de l'interface Utilisateur : " + e.getMessage());
        }
    }
}