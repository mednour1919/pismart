package tn.esprit.services;

import tn.esprit.interfaces.IService;
import tn.esprit.models.Camion;
import tn.esprit.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceCamion implements IService<Camion> {
    private Connection cnx;

    public ServiceCamion() {
        cnx = MyDatabase.getInstance().getCnx();
    }

    @Override
    public void add(Camion camion) {
        String qry = "INSERT INTO `Camion`(`type`, `statut`, `capacity`, `id_zone`) VALUES (?,?,?,?)";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setString(1, camion.getType());
            pstm.setString(2, camion.getStatut());
            pstm.setInt(3, camion.getCapacity());
            if (camion.getId_zone() != null) {
                pstm.setInt(4, camion.getId_zone());
            } else {
                pstm.setNull(4, Types.INTEGER);
            }
            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<Camion> getAll() {
        List<Camion> camions = new ArrayList<>();
        String qry = "SELECT * FROM `Camion`";

        try {
            Statement stm = cnx.createStatement();
            ResultSet rs = stm.executeQuery(qry);

            while (rs.next()) {
                Camion c = new Camion();
                c.setId(rs.getInt("id"));
                c.setType(rs.getString("type"));
                c.setStatut(rs.getString("statut"));
                c.setCapacity(rs.getInt("capacity"));
                c.setId_zone(rs.getInt("id_zone"));

                camions.add(c);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return camions;
    }

    @Override
    public void update(Camion camion) {
        String qry = "UPDATE `Camion` SET `type`=?, `statut`=?, `capacity`=?, `id_zone`=? WHERE `id`=?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setString(1, camion.getType());
            pstm.setString(2, camion.getStatut());
            pstm.setInt(3, camion.getCapacity());
            if (camion.getId_zone() != null) {
                pstm.setInt(4, camion.getId_zone());
            } else {
                pstm.setNull(4, Types.INTEGER);
            }
            pstm.setInt(5, camion.getId());
            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void delete(Camion camion) {
        String qry = "DELETE FROM `Camion` WHERE `id`=?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setInt(1, camion.getId());
            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void create(Camion camion) {
        add(camion);
    }

    @Override
    public void view(Camion camion) {
        System.out.println(camion);
    }
}