package tn.esprit.gui;



import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Charger le fichier FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Main.fxml"));


        // Créer une scène à partir du fichier FXML
        AnchorPane root = loader.load();

        // Créer une scène et l'ajouter à la fenêtre principale
        Scene scene = new Scene(root, 520, 400);

        // Définir le titre de la fenêtre principale
        primaryStage.setTitle("Ajouter un Signalement");

        // Ajouter la scène à la fenêtre et afficher
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
