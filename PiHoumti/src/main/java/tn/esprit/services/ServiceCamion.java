package tn.esprit.services;

import tn.esprit.interfaces.IService;
import tn.esprit.models.Camion;
import tn.esprit.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// Importations pour Apache POI (Excel)
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

// Importation pour les alertes JavaFX
import javafx.scene.control.Alert;

public class ServiceCamion implements IService<Camion> {
    private Connection cnx;

    public ServiceCamion() {
        cnx = MyDatabase.getInstance().getCnx();
    }

    // Ajouter un camion avec zone de type String
    @Override
    public void add(Camion camion) {
        String qry = "INSERT INTO `Camion`(`type`, `statut`, `capacity`, `nom`, `image`) VALUES (?,?,?,?,?)";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setString(1, camion.getType());
            pstm.setString(2, camion.getStatut());
            pstm.setInt(3, camion.getCapacity());

            if (camion.getNom() != null) {
                pstm.setString(4, camion.getNom());  // Changer id_zone par zone (String)
            } else {
                pstm.setNull(4, Types.VARCHAR);  // Utilisation de Types.VARCHAR pour une chaîne
            }

            if (camion.getImage() != null) {
                pstm.setBytes(5, camion.getImage());
            } else {
                pstm.setNull(5, Types.BLOB);
            }

            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Récupérer tous les camions (zone en tant que String)
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
                c.setNom(rs.getString("nom"));  // Récupérer zone en tant que String

                Blob imageBlob = rs.getBlob("image");
                if (imageBlob != null) {
                    c.setImage(imageBlob.getBytes(1, (int) imageBlob.length()));
                }

                camions.add(c);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return camions;
    }

    // Mettre à jour un camion (zone de type String)
    @Override
    public void update(Camion camion) {
        String qry = "UPDATE `Camion` SET `type`=?, `statut`=?, `capacity`=?, `nom`=?, `image`=? WHERE `id`=?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setString(1, camion.getType());
            pstm.setString(2, camion.getStatut());
            pstm.setInt(3, camion.getCapacity());

            if (camion.getNom() != null) {
                pstm.setString(4, camion.getNom());  // Changer id_zone par zone (String)
            } else {
                pstm.setNull(4, Types.VARCHAR);  // Utilisation de Types.VARCHAR pour une chaîne
            }
            if (camion.getImage() != null) {
                pstm.setBytes(5, camion.getImage());
            } else {
                pstm.setNull(5, Types.BLOB);
            }

            pstm.setInt(6, camion.getId());
            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Supprimer un camion
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

    // Méthode pour exporter les camions vers un fichier Excel
    public boolean exportToExcel(String filePath) {
        List<Camion> camions = getAll();  // Récupérer tous les camions

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Camions");

            // Créer l'en-tête
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("ID");
            headerRow.createCell(1).setCellValue("Type");
            headerRow.createCell(2).setCellValue("Statut");
            headerRow.createCell(3).setCellValue("Capacité");
            headerRow.createCell(4).setCellValue("Zone");
            headerRow.createCell(5).setCellValue("Image");

            // Remplir les données
            int rowNum = 1;
            for (Camion camion : camions) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(camion.getId());
                row.createCell(1).setCellValue(camion.getType());
                row.createCell(2).setCellValue(camion.getStatut());
                row.createCell(3).setCellValue(camion.getCapacity());
                row.createCell(4).setCellValue(camion.getNom() != null ? camion.getNom() : "N/A");
                row.createCell(5).setCellValue(camion.getImage() != null ? "Image disponible" : "Aucune image");
            }

            // Écrire le fichier
            try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
                workbook.write(fileOut);
            }
            return true;
        } catch (IOException e) {
            System.err.println("Erreur lors de l'exportation vers Excel : " + e.getMessage());
            return false;
        }
    }

    // Méthode pour importer les camions depuis un fichier Excel
    public List<Camion> importFromExcel(String filePath) {
        List<Camion> camions = new ArrayList<>();

        try (FileInputStream file = new FileInputStream(new File(filePath));
             Workbook workbook = new XSSFWorkbook(file)) {

            Sheet sheet = workbook.getSheetAt(0);  // Supposons que les données sont dans la première feuille

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue;  // Ignorer l'en-tête

                Camion camion = new Camion();
                camion.setId((int) row.getCell(0).getNumericCellValue());
                camion.setType(row.getCell(1).getStringCellValue());
                camion.setStatut(row.getCell(2).getStringCellValue());
                camion.setCapacity((int) row.getCell(3).getNumericCellValue());
                camion.setNom(row.getCell(4).getStringCellValue());

                // L'image n'est pas gérée ici car elle nécessite un traitement spécial
                camion.setImage(null);

                // Vérifier si le camion existe déjà dans la base de données
                if (!camionExists(camion.getId())) {
                    camions.add(camion);  // Ajouter uniquement les nouveaux camions
                }
            }
        } catch (IOException e) {
            System.err.println("Erreur lors de l'importation depuis Excel : " + e.getMessage());
        }

        return camions;
    }

    // Méthode pour vérifier si un camion existe déjà dans la base de données
    private boolean camionExists(int id) {
        String qry = "SELECT COUNT(*) FROM `Camion` WHERE `id` = ?";
        try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
            pstm.setInt(1, id);
            ResultSet rs = pstm.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;  // Retourne true si un camion avec cet ID existe
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la vérification de l'existence du camion : " + e.getMessage());
        }
        return false;
    }

    // Méthode utilitaire pour afficher des alertes
    public void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}