package tn.esprit.test;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import tn.esprit.utils.QRCodeGenerator;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Générer le QR code
            String data = "https://example.com"; // Données à encoder dans le QR code
            String filePath = "QRCode.png";     // Chemin de sortie du fichier QR code
            QRCodeGenerator.generateQRCode(data, filePath);
            System.out.println("QR Code généré avec succès : " + filePath);
        } catch (Exception e) {
            System.err.println("Erreur lors de la génération du QR Code : " + e.getMessage());
            e.printStackTrace();
        }

        try {
            // Charger le fichier FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Main.fxml"));
            Parent root = loader.load();

            // Créer la scène
            Scene scene = new Scene(root, 800, 600); // Taille de la fenêtre

            // Configurer la fenêtre principale
            primaryStage.setTitle("Gestion des Projets Urbains");
            primaryStage.setScene(scene);
            primaryStage.setResizable(true); // Permettre le redimensionnement de la fenêtre
            primaryStage.show();
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement de l'interface : " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args); // Lancer l'application JavaFX
    }
}