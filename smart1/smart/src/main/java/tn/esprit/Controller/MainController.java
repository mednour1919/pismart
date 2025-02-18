package tn.esprit.Controller;


import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class MainController {

    @FXML
    private Label label1;

    @FXML
    private Label label2;

    @FXML
    private Label label3;

    @FXML
    private Button button1;

    @FXML
    private Button button2;

    @FXML
    private Button button3;

    @FXML
    public void initialize() {
        // Initialisation
        label1.setText("Label 1");
        label2.setText("Label 2");
        label3.setText("Label 3");

        button1.setText("Button 1");
        button2.setText("Button 2");
        button3.setText("Button 3");
    }
}