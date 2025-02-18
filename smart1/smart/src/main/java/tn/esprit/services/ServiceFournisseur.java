package tn.esprit.services;

import tn.esprit.interfaces.IService;
import tn.esprit.model.Fournisseur;
import tn.esprit.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceFournisseur implements IService<Fournisseur> {

    private Connection connection;

    public ServiceFournisseur() {
        // Utilisation de MyDatabase pour obtenir la connexion
        this.connection = MyDatabase.getInstance().getCnx();
    }

    // Ajouter un fournisseur
    @Override
    public void add(Fournisseur fournisseur) {
        String sql = "INSERT INTO fournisseur (nom, adresse) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, fournisseur.getNom());
            statement.setString(2, fournisseur.getAdresse());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Mettre à jour un fournisseur
    @Override
    public void update(Fournisseur fournisseur) {
        String sql = "UPDATE fournisseur SET nom = ?, adresse = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, fournisseur.getNom());
            statement.setString(2, fournisseur.getAdresse());
            statement.setInt(3, fournisseur.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Supprimer un fournisseur
    @Override
    public void delete(Fournisseur fournisseur) {
        String sql = "DELETE FROM fournisseur WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, fournisseur.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Récupérer tous les fournisseurs
    @Override
    public List<Fournisseur> getAll() {
        List<Fournisseur> fournisseurs = new ArrayList<>();
        String sql = "SELECT * FROM fournisseur";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                Fournisseur fournisseur = new Fournisseur(
                        resultSet.getInt("id"),
                        resultSet.getString("nom"),
                        resultSet.getString("adresse")
                );
                fournisseurs.add(fournisseur);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return fournisseurs;
    }

    // Récupérer un fournisseur par son ID
    public Fournisseur getById(int id) {
        Fournisseur fournisseur = null;
        String sql = "SELECT * FROM fournisseur WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                fournisseur = new Fournisseur(
                        resultSet.getInt("id"),
                        resultSet.getString("nom"),
                        resultSet.getString("adresse")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return fournisseur;
    }
}
