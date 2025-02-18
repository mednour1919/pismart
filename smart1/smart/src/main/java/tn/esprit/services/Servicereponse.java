package tn.esprit.services;



import tn.esprit.interfaces.IService;
import tn.esprit.model.reponse;
import tn.esprit.utils.MyDatabase;

import java.sql.*;
        import java.util.ArrayList;
import java.util.List;

public class Servicereponse implements IService<reponse> {
    private Connection cnx;

    public Servicereponse() {
        cnx = MyDatabase.getInstance().getCnx();
    }

    @Override
    public void add(reponse reponse) {
        String qry = "INSERT INTO reponse (id_signalement, reponse, date_reponse, statut) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
            pstm.setInt(1, reponse.getIdSignalement());
            pstm.setString(2, reponse.getReponse());
            pstm.setTimestamp(3, reponse.getDateReponse());
            pstm.setString(4, reponse.getStatut());
            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<reponse> getAll() {
        String qry = "SELECT * FROM reponse";
        List<reponse> reponses = new ArrayList<>();
        try (Statement stm = cnx.createStatement();
             ResultSet rs = stm.executeQuery(qry)) {
            while (rs.next()) {
                reponse reponse = new reponse(
                        rs.getInt("id_reponse"),
                        rs.getInt("id_signalement"),
                        rs.getString("reponse"),
                        rs.getTimestamp("date_reponse"),
                        rs.getString("statut")
                );
                reponses.add(reponse);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return reponses;
    }

    @Override
    public void update(reponse reponse) {
        String qry = "UPDATE reponse SET reponse = ?, statut = ? WHERE id_reponse = ?";
        try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
            pstm.setString(1, reponse.getReponse());
            pstm.setString(2, reponse.getStatut());
            pstm.setInt(3, reponse.getIdReponse());
            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void delete(reponse reponse) {
        String qry = "DELETE FROM reponse WHERE id_reponse = ?";
        try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
            pstm.setInt(1, reponse.getIdReponse());
            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
