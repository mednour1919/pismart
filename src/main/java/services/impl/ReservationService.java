package services.impl;

import models.Reservation;
import services.IReservationService;
import utils.DatabaseConnection;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservationService implements IReservationService {

    @Override
    public void addReservation(Reservation reservation) {
        String sql = "INSERT INTO reservation (numPLACE, date_Reservation, temps, marque) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pst = DatabaseConnection.getConnection().prepareStatement(sql)) {
            pst.setString(1, reservation.getNumPLACE());
            pst.setDate(2, reservation.getDate_Reservation());
            pst.setString(3, reservation.getTemps());
            pst.setString(4, reservation.getMarque());
            pst.executeUpdate();
            System.out.println("Reservation added successfully!");
        } catch (SQLException e) {
            System.err.println("Error adding reservation: " + e.getMessage());
        }
    }

    @Override
    public void updateReservation(Reservation reservation) {
        String sql = "UPDATE reservation SET numPLACE=?, date_Reservation=?, temps=?, marque=? WHERE idReservation=?";
        try (PreparedStatement pst = DatabaseConnection.getConnection().prepareStatement(sql)) {
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
        try (PreparedStatement pst = DatabaseConnection.getConnection().prepareStatement(sql)) {
            pst.setInt(1, id);
            pst.executeUpdate();
            System.out.println("Reservation deleted successfully!");
        } catch (SQLException e) {
            System.err.println("Error deleting reservation: " + e.getMessage());
        }
    }

    @Override
    public Reservation findById(int id) {
        String sql = "SELECT * FROM reservation WHERE idReservation=?";
        try (PreparedStatement pst = DatabaseConnection.getConnection().prepareStatement(sql)) {
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return new Reservation(
                        rs.getInt("idReservation"),
                        rs.getString("numPLACE"),
                        rs.getDate("date_Reservation"),
                        rs.getString("temps"),
                        rs.getString("marque")
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
        String sql = "SELECT * FROM reservation WHERE date_Reservation=?";
        try (PreparedStatement pst = DatabaseConnection.getConnection().prepareStatement(sql)) {
            pst.setDate(1, date);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                reservations.add(new Reservation(
                        rs.getInt("idReservation"),
                        rs.getString("numPLACE"),
                        rs.getDate("date_Reservation"),
                        rs.getString("temps"),
                        rs.getString("marque")
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
        String sql = "SELECT * FROM reservation";
        try (Statement st = DatabaseConnection.getConnection().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                reservations.add(new Reservation(
                        rs.getInt("idReservation"),
                        rs.getString("numPLACE"),
                        rs.getDate("date_Reservation"),
                        rs.getString("temps"),
                        rs.getString("marque")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error listing reservations: " + e.getMessage());
        }
        return reservations;
    }
}