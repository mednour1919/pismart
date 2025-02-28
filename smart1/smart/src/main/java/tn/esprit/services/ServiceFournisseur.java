package tn.esprit.services;

import tn.esprit.model.Fournisseur;
import tn.esprit.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceFournisseur {

    private Connection connection;

    public ServiceFournisseur() {
        this.connection = MyDatabase.getInstance().getCnx();
    }

    public void add(Fournisseur fournisseur) {
        String sql = "INSERT INTO fournisseur (nom, adresse, certifications, risques, performances, email, telephone, siteWeb, secteurActivite, responsable, estActif) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, fournisseur.getNom());
            statement.setString(2, fournisseur.getAdresse());
            statement.setString(3, fournisseur.getCertifications());
            statement.setString(4, fournisseur.getRisques());
            statement.setString(5, fournisseur.getPerformances());
            statement.setString(6, fournisseur.getEmail());
            statement.setString(7, fournisseur.getTelephone());
            statement.setString(8, fournisseur.getSiteWeb());
            statement.setString(9, fournisseur.getSecteurActivite());
            statement.setString(10, fournisseur.getResponsable());
            statement.setBoolean(11, fournisseur.isEstActif());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Fournisseur> getAll() {
        List<Fournisseur> fournisseurs = new ArrayList<>();
        String sql = "SELECT * FROM fournisseur";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                Fournisseur fournisseur = new Fournisseur(
                        resultSet.getInt("id"),
                        resultSet.getString("nom"),
                        resultSet.getString("adresse"),
                        resultSet.getString("certifications"),
                        resultSet.getString("risques"),
                        resultSet.getString("performances"),
                        resultSet.getString("email"),
                        resultSet.getString("telephone"),
                        resultSet.getString("siteWeb"),
                        resultSet.getString("secteurActivite"),
                        resultSet.getString("responsable"),
                        resultSet.getBoolean("estActif")
                );
                fournisseurs.add(fournisseur);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return fournisseurs;
    }

    public void update(Fournisseur fournisseur) {
        String sql = "UPDATE fournisseur SET nom = ?, adresse = ?, certifications = ?, risques = ?, performances = ?, email = ?, telephone = ?, siteWeb = ?, secteurActivite = ?, responsable = ?, estActif = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, fournisseur.getNom());
            statement.setString(2, fournisseur.getAdresse());
            statement.setString(3, fournisseur.getCertifications());
            statement.setString(4, fournisseur.getRisques());
            statement.setString(5, fournisseur.getPerformances());
            statement.setString(6, fournisseur.getEmail());
            statement.setString(7, fournisseur.getTelephone());
            statement.setString(8, fournisseur.getSiteWeb());
            statement.setString(9, fournisseur.getSecteurActivite());
            statement.setString(10, fournisseur.getResponsable());
            statement.setBoolean(11, fournisseur.isEstActif());
            statement.setInt(12, fournisseur.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(Fournisseur fournisseur) {
        String sql = "DELETE FROM fournisseur WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, fournisseur.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}