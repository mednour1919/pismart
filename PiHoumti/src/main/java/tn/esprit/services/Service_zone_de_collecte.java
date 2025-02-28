package tn.esprit.services;

import tn.esprit.interfaces.IService;
import tn.esprit.models.Zone_de_collecte;
import tn.esprit.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Service_zone_de_collecte implements IService<Zone_de_collecte> {
    private Connection cnx;

    public Service_zone_de_collecte() {
        cnx = MyDatabase.getInstance().getCnx();
    }

    @Override
    public void add(Zone_de_collecte zone) {
        String qry = "INSERT INTO `zone_de_collecte`(`nom`, `population`, `temps_de_collecte`) VALUES (?,?,?)";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setString(1, zone.getNom());
            pstm.setInt(2, zone.getPopulation());
            pstm.setString(3, zone.getTempsDeCollecte());
            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<Zone_de_collecte> getAll() {
        List<Zone_de_collecte> zones = new ArrayList<>();
        String qry = "SELECT * FROM `zone_de_collecte`";

        try {
            Statement stm = cnx.createStatement();
            ResultSet rs = stm.executeQuery(qry);

            while (rs.next()) {
                Zone_de_collecte z = new Zone_de_collecte();
                z.setId(rs.getInt("id"));
                z.setNom(rs.getString("nom"));
                z.setPopulation(rs.getInt("population"));
                z.setTempsDeCollecte(rs.getString("temps_de_collecte"));

                zones.add(z);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return zones;
    }

    @Override
    public void update(Zone_de_collecte zone) {
        String qry = "UPDATE `zone_de_collecte` SET `nom`=?, `population`=?, `temps_de_collecte`=? WHERE `id`=?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setString(1, zone.getNom());
            pstm.setInt(2, zone.getPopulation());
            pstm.setString(3, zone.getTempsDeCollecte());
            pstm.setInt(4, zone.getId());
            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void delete(Zone_de_collecte zone) {
        String qry = "DELETE FROM `zone_de_collecte` WHERE `id`=?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setInt(1, zone.getId());
            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void create(Zone_de_collecte zone) {
        add(zone);
    }

    @Override
    public void view(Zone_de_collecte zone) {
        System.out.println(zone);
    }
}