package tn.esprit.services;

import tn.esprit.models.Reservation;
import tn.esprit.interfaces.IReservationService; // Remplacez par le package réel
import tn.esprit.utils.DataBaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.FileSystems;



public class ReservationService implements IReservationService {

    public void addReservation(Reservation reservation) {
        String sql = "INSERT INTO reservation (numPLACE, date_Reservation, temps, marque, idStation) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, reservation.getNumPLACE());
            pst.setDate(2, reservation.getDate_Reservation());
            pst.setString(3, reservation.getTemps());
            pst.setString(4, reservation.getMarque());
            pst.setInt(5, reservation.getid_station()); // Association à la station

            pst.executeUpdate();
            System.out.println("Reservation added successfully!");
        } catch (SQLException e) {
            System.err.println("Error adding reservation: " + e.getMessage());
        }
    }

    private void generateQRCode(Reservation reservation) {
        try {
            String qrCodeData = "Réservation ID: " + reservation.getIdReservation() +
                    "\nNuméro Place: " + reservation.getNumPLACE() +
                    "\nDate: " + reservation.getDate_Reservation() +
                    "\nTemps: " + reservation.getTemps() +
                    "\nMarque: " + reservation.getMarque();

            String qrCodePath = "QR_Codes/";
            File directory = new File(qrCodePath);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            String qrCodeFilePath = qrCodePath + "Reservation_" + reservation.getIdReservation() + ".png";
            int width = 300;
            int height = 300;

            BitMatrix matrix = new MultiFormatWriter().encode(qrCodeData, BarcodeFormat.QR_CODE, width, height);
            Path path = FileSystems.getDefault().getPath(qrCodeFilePath);
            MatrixToImageWriter.writeToPath(matrix, "PNG", path);

            System.out.println("QR Code généré : " + qrCodeFilePath);
        } catch (Exception e) {
            System.err.println("Erreur lors de la génération du QR Code : " + e.getMessage());
        }
    }

    @Override
    public void updateReservation(Reservation reservation) {
        String sql = "UPDATE reservation SET numPLACE=?, date_Reservation=?, temps=?, marque=? WHERE idReservation=?";
        try (PreparedStatement pst = DataBaseConnection.getConnection().prepareStatement(sql)) {
            pst.setString(1, reservation.getNumPLACE());
            pst.setDate(2, reservation.getDate_Reservation());
            pst.setString(3, reservation.getTemps());
            pst.setString(4, reservation.getMarque());
            pst.setInt(5, reservation.getIdReservation());
            pst.executeUpdate();
            System.out.println("Reservation updated successfully!");
        } catch (SQLException e) {
            System.err.println("Error updating reservation: " + e.getMessage());
        }
    }

    @Override
    public void deleteReservation(int id) {
        String sql = "DELETE FROM reservation WHERE idReservation=?";
        try (PreparedStatement pst = DataBaseConnection.getConnection().prepareStatement(sql)) {
            pst.setInt(1, id);
            pst.executeUpdate();
            System.out.println("Reservation deleted successfully!");
        } catch (SQLException e) {
            System.err.println("Error deleting reservation: " + e.getMessage());
        }
    }


    @Override
    public Reservation findById(int id) {
        String sql = "SELECT r.idReservation, r.numPLACE, r.date_Reservation, r.temps, r.marque, s.nomStation, r.idStation " +
                "FROM reservation r " +
                "JOIN station s ON r.idStation = s.id_station " +
                "WHERE r.idReservation = ?";

        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                return new Reservation(
                        rs.getInt("idReservation"),
                        rs.getString("numPLACE"),
                        rs.getDate("date_Reservation"),
                        rs.getString("temps"),
                        rs.getString("marque"),
                        rs.getString("nomStation"), // récupérer le nom de la station
                        rs.getInt("idStation")     // récupérer l'id de la station
                );
            }
        } catch (SQLException e) {
            System.err.println("Error finding reservation: " + e.getMessage());
        }
        return null;
    }





    @Override
    public List<Reservation> findByDate(Date date) {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT r.idReservation, r.numPLACE, r.date_Reservation, r.temps, r.marque, s.nomStation, r.idStation " +
                "FROM reservation r " +
                "JOIN station s ON r.idStation = s.id_station " +
                "WHERE r.date_Reservation = ?";

        try (PreparedStatement pst = DataBaseConnection.getConnection().prepareStatement(sql)) {
            pst.setDate(1, date);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                reservations.add(new Reservation(
                        rs.getInt("idReservation"),
                        rs.getString("numPLACE"),
                        rs.getDate("date_Reservation"),
                        rs.getString("temps"),
                        rs.getString("marque"),
                        rs.getString("nomStation"),  // Récupération de nomStation en tant que String
                        rs.getInt("id_station")       // Récupération de idStation en tant qu'Int
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error finding reservations by date: " + e.getMessage());
        }
        return reservations;
    }


    @Override
    public List<Reservation> findAll() {
        List<Reservation> reservations = new ArrayList<>();
        // Mettre à jour la requête SQL pour inclure l'ID de la station
        String sql = "SELECT r.idReservation, r.numPLACE, r.date_Reservation, r.temps, r.marque, s.nomStation, r.idStation " +
                "FROM reservation r " +
                "JOIN station s ON r.idStation = s.id_station";

        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                // Passer tous les arguments nécessaires au constructeur
                reservations.add(new Reservation(
                        rs.getInt("idReservation"),
                        rs.getString("numPLACE"),
                        rs.getDate("date_Reservation"),
                        rs.getString("temps"),
                        rs.getString("marque"),
                        rs.getString("nomStation"), // On récupère le nom de la station
                        rs.getInt("idStation") // On récupère l'ID de la station
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error listing reservations: " + e.getMessage());
        }
        return reservations;
    }



}
