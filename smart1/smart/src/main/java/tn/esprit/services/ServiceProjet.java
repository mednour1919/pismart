package tn.esprit.services;

import tn.esprit.model.Projet;
import tn.esprit.model.Fournisseur;
import tn.esprit.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceProjet {

    private Connection connection;

    public ServiceProjet() {
        this.connection = MyDatabase.getInstance().getCnx();
    }

    public void add(Projet projet) {
        String query = "INSERT INTO projet (nom, description, budget, depense, dateDebut, dateFin, statut, fournisseur_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, projet.getNom());
            ps.setString(2, projet.getDescription());
            ps.setDouble(3, projet.getBudget());
            ps.setDouble(4, projet.getDepense());
            ps.setDate(5, projet.getDateDebut());
            ps.setDate(6, projet.getDateFin());
            ps.setString(7, projet.getStatut());
            ps.setInt(8, projet.getFournisseur().getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Projet> getAll() {
        List<Projet> projets = new ArrayList<>();
        String query = "SELECT p.*, f.nom AS nom_fournisseur FROM projet p LEFT JOIN fournisseur f ON p.fournisseur_id = f.id";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Fournisseur fournisseur = new Fournisseur(
                        rs.getInt("fournisseur_id"),
                        rs.getString("nom_fournisseur"),
                        ""
                );
                Projet projet = new Projet(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("description"),
                        rs.getDouble("budget"),
                        rs.getDouble("depense"),
                        rs.getDate("dateDebut"),
                        rs.getDate("dateFin"),
                        rs.getString("statut"),
                        fournisseur
                );
                projets.add(projet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return projets;
    }

    public void update(Projet projet) {
        String query = "UPDATE projet SET nom = ?, description = ?, budget = ?, depense = ?, dateDebut = ?, dateFin = ?, statut = ?, fournisseur_id = ? WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, projet.getNom());
            ps.setString(2, projet.getDescription());
            ps.setDouble(3, projet.getBudget());
            ps.setDouble(4, projet.getDepense());
            ps.setDate(5, projet.getDateDebut());
            ps.setDate(6, projet.getDateFin());
            ps.setString(7, projet.getStatut());
            ps.setInt(8, projet.getFournisseur().getId());
            ps.setInt(9, projet.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(Projet projet) {
        String query = "DELETE FROM projet WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, projet.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}