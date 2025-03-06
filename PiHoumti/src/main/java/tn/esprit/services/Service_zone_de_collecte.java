package tn.esprit.services;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import tn.esprit.interfaces.IService;
import tn.esprit.models.Zone_de_collecte;
import tn.esprit.utils.MyDatabase;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
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

    // ✅ Vérification de l'existence de la zone avant d'ajouter
    private boolean zoneExists(String nom, int population, String tempsDeCollecte) {
        String qry = "SELECT COUNT(*) FROM `zone_de_collecte` WHERE `nom` = ? AND `population` = ? AND `temps_de_collecte` = ?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setString(1, nom);
            pstm.setInt(2, population);
            pstm.setString(3, tempsDeCollecte);

            ResultSet rs = pstm.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; // Si la zone existe déjà
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    // ✅ Méthode pour convertir une chaîne de temps en format HH:mm:ss
    private String convertToTimeFormat(String timeValue) {
        if (timeValue == null || timeValue.isEmpty()) {
            return "00:00:00"; // Valeur par défaut si la cellule est vide
        }

        // Exemple 1 : Si la valeur est "30 minutes"
        if (timeValue.contains("minute")) {
            int minutes = Integer.parseInt(timeValue.replaceAll("[^0-9]", "")); // Extraire les chiffres
            return String.format("%02d:%02d:00", minutes / 60, minutes % 60); // Convertir en HH:mm:ss
        }

        // Exemple 2 : Si la valeur est "2 heures 30 minutes"
        if (timeValue.contains("heure") || timeValue.contains("h")) {
            String[] parts = timeValue.split(" ");
            int hours = 0, minutes = 0;
            for (String part : parts) {
                if (part.contains("h") || part.contains("heure")) {
                    hours = Integer.parseInt(part.replaceAll("[^0-9]", ""));
                } else if (part.contains("minute")) {
                    minutes = Integer.parseInt(part.replaceAll("[^0-9]", ""));
                }
            }
            return String.format("%02d:%02d:00", hours, minutes); // Convertir en HH:mm:ss
        }

        // Exemple 3 : Si la valeur est déjà au format HH:mm:ss
        if (timeValue.matches("\\d{2}:\\d{2}:\\d{2}")) {
            return timeValue;
        }

        // Si le format n'est pas reconnu, retourner une valeur par défaut
        return "00:00:00";
    }

    // ✅ IMPORTER LES DONNÉES DEPUIS EXCEL (avec vérification de l'existence)
    public boolean importFromExcel(String filePath) {
        try (FileInputStream file = new FileInputStream(new File(filePath));
             Workbook workbook = WorkbookFactory.create(file)) {

            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();

            if (rowIterator.hasNext()) {
                rowIterator.next(); // Ignorer la ligne d'entête
            }

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();

                // Lire les valeurs des cellules
                String nom = getCellValueAsString(row.getCell(1)); // Colonne 1 : Nom
                int population = (int) getCellValueAsNumeric(row.getCell(2)); // Colonne 2 : Population
                String tempsDeCollecte = convertToTimeFormat(getCellValueAsString(row.getCell(3))); // Colonne 3 : Temps de collecte

                // Vérifier si la zone existe déjà avant de l'ajouter
                if (!zoneExists(nom, population, tempsDeCollecte)) {
                    Zone_de_collecte zone = new Zone_de_collecte();
                    zone.setNom(nom);
                    zone.setPopulation(population);
                    zone.setTempsDeCollecte(tempsDeCollecte);

                    add(zone); // Ajouter la zone à la base de données
                }
            }
            return true;

        } catch (IOException | NullPointerException | IllegalStateException e) {
            System.err.println("Erreur d'importation : " + e.getMessage());
            return false;
        }
    }

    // ✅ Méthode pour lire une cellule comme une chaîne de caractères
    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return ""; // Retourne une chaîne vide si la cellule est nulle
        }
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return String.valueOf((int) cell.getNumericCellValue()); // Convertit le nombre en chaîne
            default:
                return "";
        }
    }

    // ✅ Méthode pour lire une cellule comme un nombre
    private double getCellValueAsNumeric(Cell cell) {
        if (cell == null) {
            return 0; // Retourne 0 si la cellule est nulle
        }
        switch (cell.getCellType()) {
            case NUMERIC:
                return cell.getNumericCellValue();
            case STRING:
                try {
                    return Double.parseDouble(cell.getStringCellValue()); // Convertit la chaîne en nombre
                } catch (NumberFormatException e) {
                    return 0;
                }
            default:
                return 0;
        }
    }

    // ✅ EXPORTER LES DONNÉES VERS EXCEL
    public boolean exportToExcel(String filePath) {
        String[] columns = {"ID", "Nom", "Population", "Temps de collecte"};
        List<Zone_de_collecte> zones = getAll();  // Récupérer les données depuis la base de données

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Zones de Collecte");

            // Création du header
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
            }

            // Remplissage des données
            int rowNum = 1;
            for (Zone_de_collecte zone : zones) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(zone.getId());
                row.createCell(1).setCellValue(zone.getNom());
                row.createCell(2).setCellValue(zone.getPopulation());
                row.createCell(3).setCellValue(zone.getTempsDeCollecte());
            }

            // Sauvegarde du fichier
            try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
                workbook.write(fileOut);
            }
            return true;

        } catch (IOException e) {
            System.err.println("Erreur d'exportation : " + e.getMessage());
            return false;
        }
    }
}