package tn.esprit.services;

import tn.esprit.interfaces.IService;
import tn.esprit.model.Signalement;
import tn.esprit.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Servicesignalement implements IService<Signalement> {
    private Connection cnx;

    public Servicesignalement() {
        cnx = MyDatabase.getInstance().getCnx();
    }

    @Override
    public void add(Signalement signalement) {
        // Create SQL Query for inserting a signalement
        String qry = "INSERT INTO signalement (type_signalement, description, date_signalement, statut) " +
                "VALUES (?, ?, ?, ?)";

        try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
            pstm.setString(1, signalement.getTypeSignalement());
            pstm.setString(2, signalement.getDescription());
            pstm.setTimestamp(3, signalement.getDateSignalement());
            pstm.setString(4, signalement.getStatut());

            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<Signalement> getAll() {
        // Create SQL query for fetching all signalements
        String qry = "SELECT * FROM signalement";
        List<Signalement> signalements = new ArrayList<>();

        try (Statement stm = cnx.createStatement();
             ResultSet rs = stm.executeQuery(qry)) {

            while (rs.next()) {
                Signalement signalement = new Signalement(
                        rs.getInt("id_signalement"),
                        rs.getString("type_signalement"),
                        rs.getString("description"),
                        rs.getTimestamp("date_signalement"),
                        rs.getString("statut")
                );
                signalements.add(signalement);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return signalements;
    }

    @Override
    public void update(Signalement signalement) {
        // Create SQL query for updating an existing signalement
        String qry = "UPDATE signalement SET type_signalement = ?, description = ?, statut = ? " +
                "WHERE id_signalement = ?";

        try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
            pstm.setString(1, signalement.getTypeSignalement());
            pstm.setString(2, signalement.getDescription());
            pstm.setString(3, signalement.getStatut());
            pstm.setInt(4, signalement.getIdSignalement());

            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void delete(Signalement signalement) {
        // Create SQL query for deleting a signalement
        String qry = "DELETE FROM signalement WHERE id_signalement = ?";

        try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
            pstm.setInt(1, signalement.getIdSignalement());
            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Méthode pour rechercher un signalement par ID avec Stream
    public Signalement getById(int id) {
        List<Signalement> signalements = getAll(); // Récupérer tous les signalements

        // Utiliser Stream pour chercher un signalement par ID
        Optional<Signalement> result = signalements.stream()
                .filter(signalement -> signalement.getIdSignalement() == id) // Filtrer par ID
                .findFirst(); // Récupérer le premier résultat trouvé

        return result.orElse(null); // Retourner le signalement trouvé ou null si aucun trouvé
    }
}
