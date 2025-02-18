package tn.esprit.services;

import tn.esprit.models.Station;
import tn.esprit.interfaces.IStationService;
import tn.esprit.utils.DataBaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StationService implements IStationService {

    @Override
    public void addStation(Station station) {
        String sql = "INSERT INTO station (nomStation, capacite, zone, status) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pst = DataBaseConnection.getConnection().prepareStatement(sql)) {
            pst.setString(1, station.getNomStation());
            pst.setInt(2, station.getCapacite());
            pst.setString(3, station.getZone());
            pst.setString(4, station.getStatus());
            pst.executeUpdate();
            System.out.println("Station added successfully!");
        } catch (SQLException e) {
            System.err.println("Error adding station: " + e.getMessage());
        }
    }

    @Override
    public void updateStation(Station station) {
        String sql = "UPDATE station SET nomStation=?, capacite=?, zone=?, status=? WHERE id_station=?";
        try (PreparedStatement pst = DataBaseConnection.getConnection().prepareStatement(sql)) {
            pst.setString(1, station.getNomStation());
            pst.setInt(2, station.getCapacite());
            pst.setString(3, station.getZone());
            pst.setString(4, station.getStatus());
            pst.setInt(5, station.getId_station());
            pst.executeUpdate();
            System.out.println("Station updated successfully!");
        } catch (SQLException e) {
            System.err.println("Error updating station: " + e.getMessage());
        }
    }

    @Override
    public void deleteStation(int id) {
        String sql = "DELETE FROM station WHERE id_station=?";
        try (PreparedStatement pst = DataBaseConnection.getConnection().prepareStatement(sql)) {
            pst.setInt(1, id);
            pst.executeUpdate();
            System.out.println("Station deleted successfully!");
        } catch (SQLException e) {
            System.err.println("Error deleting station: " + e.getMessage());
        }
    }

    @Override
    public Station findById(int id) {
        String sql = "SELECT * FROM station WHERE id_station=?";
        try (PreparedStatement pst = DataBaseConnection.getConnection().prepareStatement(sql)) {
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return new Station(
                        rs.getInt("id_station"),
                        rs.getString("nomStation"),
                        rs.getInt("capacite"),
                        rs.getString("zone"),
                        rs.getString("status")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error finding station: " + e.getMessage());
        }
        return null;
    }

    @Override
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
            System.err.println("Error listing stations: " + e.getMessage());
        }
        return stations;
    }
}