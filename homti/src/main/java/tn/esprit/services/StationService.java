package tn.esprit.services;

import tn.esprit.models.Station;
import tn.esprit.utils.DataBaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StationService {


    public void addStation(Station station) {
        String sql = "INSERT INTO station (nomStation, capacite, zone, status) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pst = DataBaseConnection.getConnection().prepareStatement(sql)) {
            pst.setString(1, station.getNomStation());
            pst.setInt(2, station.getCapacite());
            pst.setString(3, station.getZone());
            pst.setString(4, station.getStatus());
            pst.executeUpdate();
            System.out.println("Station ajoutée !");
        } catch (SQLException e) {
            System.err.println("Erreur d'ajout: " + e.getMessage());
        }
    }

    // Méthode pour mettre à jour une station
    public void updateStation(Station station) {
        String sql = "UPDATE station SET nomStation=?, capacite=?, zone=?, status=? WHERE id_station=?";
        try (PreparedStatement pst = DataBaseConnection.getConnection().prepareStatement(sql)) {
            pst.setString(1, station.getNomStation());
            pst.setInt(2, station.getCapacite());
            pst.setString(3, station.getZone());
            pst.setString(4, station.getStatus());
            pst.setInt(5, station.getIdStation());
            pst.executeUpdate();
            System.out.println("Station mise à jour !");
        } catch (SQLException e) {
            System.err.println("Erreur de mise à jour: " + e.getMessage());
        }
    }


    public void deleteStation(int id) {
        String sql = "DELETE FROM station WHERE id_station=?";
        try (PreparedStatement pst = DataBaseConnection.getConnection().prepareStatement(sql)) {
            pst.setInt(1, id);
            pst.executeUpdate();
            System.out.println("Station supprimée !");
        } catch (SQLException e) {
            System.err.println("Erreur de suppression: " + e.getMessage());
        }
    }


    public List<Station> findAll() {
        List<Station> stations = new ArrayList<>();
        String sql = "SELECT * FROM station";
        try (Statement st = DataBaseConnection.getConnection().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                stations.add(new Station(
                        rs.getInt("id_station"),
                        rs.getString("nomStation"),
                        rs.getInt("capacite"),
                        rs.getString("zone"),
                        rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Erreur de récupération: " + e.getMessage());
        }
        return stations;
    }

    public List<Station> findByZone(String zone) {
        List<Station> stations = new ArrayList<>();
        String query = "SELECT * FROM station WHERE zone = ?";
        try (PreparedStatement stmt = DataBaseConnection.getConnection().prepareStatement(query)) {
            stmt.setString(1, zone);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Station station = new Station(
                        rs.getInt("id_station"), // Correction ici: id_station
                        rs.getString("nomStation"), // Correction ici: nomStation
                        rs.getInt("capacite"), // Correction ici: capacite
                        rs.getString("zone"), // Correction ici: zone
                        rs.getString("status") // Correction ici: status
                );
                stations.add(station);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stations;
    }
}
