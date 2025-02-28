package tn.esprit.test;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import tn.esprit.utils.QRCodeGenerator; // Assurez-vous d'importer QRCodeGenerator

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Générer le QR code
        try {
            String data = "https://example.com";
            String filePath = "QRCode.png"; // Le chemin où vous voulez sauvegarder le QR code
            QRCodeGenerator.generateQRCode(data, filePath); // Génère le QR code
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Charger le fichier FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Main.fxml"));
        // Créer une scène à partir du fichier FXML
        Parent root = loader.load();

        // Créer une scène et l'ajouter à la fenêtre principale
        Scene scene = new Scene(root, 520, 400);

        // Définir le titre de la fenêtre principale
        primaryStage.setTitle("gestion des Signalements");

        // Ajouter la scène à la fenêtre et afficher
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
