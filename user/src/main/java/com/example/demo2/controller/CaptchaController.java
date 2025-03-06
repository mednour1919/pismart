package com.example.demo2.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.security.SecureRandom;

public class CaptchaController {

    @FXML
    private Label captchaLabel;

    @FXML
    private TextField captchaInput;

    // Store the generated CAPTCHA code
    private String generatedCaptcha;

    @FXML
    public void initialize() {
        generateCaptcha();
    }

    // Generate a random 6-character alphanumeric CAPTCHA
    private void generateCaptcha() {
        generatedCaptcha = randomAlphaNumeric(6);
        // Optionally, you can add styling or distort the text here.
        captchaLabel.setText(generatedCaptcha);
    }

    // Utility method to generate a random alphanumeric string
    private String randomAlphaNumeric(int count) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder builder = new StringBuilder(count);
        for (int i = 0; i < count; i++) {
            builder.append(characters.charAt(random.nextInt(characters.length())));
        }
        return builder.toString();
    }

    @FXML
    private void handleVerifyCaptcha() {
        String input = captchaInput.getText().trim();
        if (input.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Please enter the CAPTCHA code.");
            return;
        }
        if (input.equals(generatedCaptcha)) {
            showAlert(Alert.AlertType.INFORMATION, "Success", "CAPTCHA verified! Proceeding to dashboard...");
            // Switch to the dashboard (or your next scene)
            switchScene("/com/example/demo2/user-list-view.fxml");
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Incorrect CAPTCHA. Please try again.");
            generateCaptcha(); // Regenerate if the code is wrong
            captchaInput.clear();
        }
    }

    @FXML
    private void handleRefreshCaptcha() {
        generateCaptcha();
        captchaInput.clear();
    }

    private void switchScene(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Scene scene = new Scene(loader.load(), 700, 600);
            Stage stage = (Stage) captchaLabel.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
