package tn.esprit.services;

import tn.esprit.interfaces.IService;
import tn.esprit.models.zone_de_collecte;
import tn.esprit.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceZoneDeCollecte extends zone_de_collecte implements IService<zone_de_collecte> {
    private Connection cnx;

    public ServiceZoneDeCollecte() {
        cnx = MyDatabase.getInstance().getCnx();
    }

    @Override
    public void add(zone_de_collecte zone) {
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
    public List<zone_de_collecte> getAll() {
        List<zone_de_collecte> zones = new ArrayList<>();
        String qry = "SELECT * FROM `zone_de_collecte`";

        try {
            Statement stm = cnx.createStatement();
            ResultSet rs = stm.executeQuery(qry);

            while (rs.next()) {
                zone_de_collecte z = new zone_de_collecte();
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
    public void update(zone_de_collecte zone) {
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
    public void delete(zone_de_collecte zone) {
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
    public void create(zone_de_collecte zone) {
        add(zone);
    }

    @Override
    public void view(zone_de_collecte zone) {
        System.out.println(zone);
    }
}