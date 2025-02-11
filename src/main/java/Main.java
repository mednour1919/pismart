
import utils.DatabaseConnection;



import utils.DatabaseConnection;
import java.util.Scanner;
import java.util.List;
import services.IStationService;
import services.impl.StationService;
import models.Station;
import services.IReservationService;
import services.impl.ReservationService;
import models.Reservation;
import java.sql.Date;


public class Main {
    private static final IStationService stationService = new StationService();
    private static final IReservationService reservationService = new ReservationService();

    public static void main(String[] args) {
        DatabaseConnection.getConnection();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n=== Parking Management System ===");
            System.out.println("1. Station Management");
            System.out.println("2. Reservation Management");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    stationMenu(scanner);
                    break;
                case 2:
                    reservationMenu(scanner);
                    break;
                case 3:
                    System.out.println("Goodbye!");
                    DatabaseConnection.closeConnection();
                    return;
                default:
                    System.out.println("Invalid option! Please try again.");
            }
        }
    }

    private static void stationMenu(Scanner scanner) {
        while (true) {
            System.out.println("\n=== Station Management ===");
            System.out.println("1. Add Station");
            System.out.println("2. Edit Station");
            System.out.println("3. Delete Station");
            System.out.println("4. Find Station by ID");
            System.out.println("5. List All Stations");
            System.out.println("6. Back to Main Menu");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    addStation(scanner);
                    break;
                case 2:
                    editStation(scanner);
                    break;
                case 3:
                    deleteStation(scanner);
                    break;
                case 4:
                    findStation(scanner);
                    break;
                case 5:
                    listStations();
                    break;
                case 6:
                    return;
                default:
                    System.out.println("Invalid option! Please try again.");
            }
        }
    }

    private static void addStation(Scanner scanner) {
        System.out.println("\nAdding new station:");
        scanner.nextLine(); // Clear buffer

        System.out.print("Enter station name: ");
        String name = scanner.nextLine();

        System.out.print("Enter capacity: ");
        int capacity = scanner.nextInt();
        scanner.nextLine(); // Clear buffer

        System.out.print("Enter zone: ");
        String zone = scanner.nextLine();

        System.out.print("Enter status: ");
        String status = scanner.nextLine();

        Station station = new Station(0, name, capacity, zone, status);
        stationService.addStation(station);
    }

    private static void editStation(Scanner scanner) {
        // First show all available stations
        List<Station> stations = stationService.findAll();
        if (stations.isEmpty()) {
            System.out.println("\nNo stations available to edit!");
            return;
        }

        System.out.println("\nAvailable stations:");
        stations.forEach(System.out::println);

        System.out.print("\nEnter station ID to edit: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Clear buffer

        Station station = stationService.findById(id);
        if (station == null) {
            System.out.println("Station not found!");
            return;
        }

        System.out.print("Enter new name (current: " + station.getNomStation() + "): ");
        String name = scanner.nextLine();
        station.setNomStation(name);

        System.out.print("Enter new capacity (current: " + station.getCapacite() + "): ");
        int capacity = scanner.nextInt();
        station.setCapacite(capacity);
        scanner.nextLine(); // Clear buffer

        System.out.print("Enter new zone (current: " + station.getZone() + "): ");
        String zone = scanner.nextLine();
        station.setZone(zone);

        System.out.print("Enter new status (current: " + station.getStatus() + "): ");
        String status = scanner.nextLine();
        station.setStatus(status);

        stationService.updateStation(station);
    }

    private static void deleteStation(Scanner scanner) {
        // First show all available stations
        List<Station> stations = stationService.findAll();
        if (stations.isEmpty()) {
            System.out.println("\nNo stations available to delete!");
            return;
        }

        System.out.println("\nAvailable stations:");
        stations.forEach(System.out::println);

        System.out.print("\nEnter station ID to delete: ");
        int id = scanner.nextInt();
        stationService.deleteStation(id);
    }

    private static void findStation(Scanner scanner) {
        // First show all available stations
        List<Station> stations = stationService.findAll();
        if (stations.isEmpty()) {
            System.out.println("\nNo stations available to find!");
            return;
        }

        System.out.println("\nAvailable stations:");
        stations.forEach(System.out::println);

        System.out.print("\nEnter station ID to find: ");
        int id = scanner.nextInt();
        Station station = stationService.findById(id);
        if (station != null) {
            System.out.println("\nFound station:");
            System.out.println(station);
        } else {
            System.out.println("Station not found!");
        }
    }

    private static void listStations() {
        List<Station> stations = stationService.findAll();
        if (stations.isEmpty()) {
            System.out.println("\nNo stations found!");
        } else {
            System.out.println("\nAll stations:");
            stations.forEach(System.out::println);
        }
    }

    private static void reservationMenu(Scanner scanner) {
        while (true) {
            System.out.println("\n=== Reservation Management ===");
            System.out.println("1. Add Reservation");
            System.out.println("2. Edit Reservation");
            System.out.println("3. Delete Reservation");
            System.out.println("4. Find Reservation by ID");
            System.out.println("5. Find Reservations by Date");
            System.out.println("6. List All Reservations");
            System.out.println("7. Back to Main Menu");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    addReservation(scanner);
                    break;
                case 2:
                    editReservation(scanner);
                    break;
                case 3:
                    deleteReservation(scanner);
                    break;
                case 4:
                    findReservationById(scanner);
                    break;
                case 5:
                    findReservationsByDate(scanner);
                    break;
                case 6:
                    listReservations();
                    break;
                case 7:
                    return;
                default:
                    System.out.println("Invalid option! Please try again.");
            }
        }
    }

    private static void addReservation(Scanner scanner) {
        System.out.println("\nAdding new reservation:");
        scanner.nextLine(); // Clear buffer

        System.out.print("Enter place number: ");
        String place = scanner.nextLine();

        System.out.print("Enter date (YYYY-MM-DD): ");
        String dateStr = scanner.nextLine();
        Date date = Date.valueOf(dateStr);

        System.out.print("Enter time: ");
        String time = scanner.nextLine();

        System.out.print("Enter car brand: ");
        String brand = scanner.nextLine();

        Reservation reservation = new Reservation(0, place, date, time, brand);
        reservationService.addReservation(reservation);
    }

    private static void editReservation(Scanner scanner) {
        // First show all available reservations
        List<Reservation> reservations = reservationService.findAll();
        if (reservations.isEmpty()) {
            System.out.println("\nNo reservations available to edit!");
            return;
        }

        System.out.println("\nAvailable reservations:");
        reservations.forEach(System.out::println);

        System.out.print("\nEnter reservation ID to edit: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Clear buffer

        Reservation reservation = reservationService.findById(id);
        if (reservation == null) {
            System.out.println("Reservation not found!");
            return;
        }

        System.out.print("Enter new place number (current: " + reservation.getNumPLACE() + "): ");
        String place = scanner.nextLine();
        reservation.setNumPLACE(place);

        System.out.print("Enter new date (YYYY-MM-DD) (current: " + reservation.getDate_Reservation() + "): ");
        String dateStr = scanner.nextLine();
        reservation.setDate_Reservation(Date.valueOf(dateStr));

        System.out.print("Enter new time (current: " + reservation.getTemps() + "): ");
        String time = scanner.nextLine();
        reservation.setTemps(time);

        System.out.print("Enter new car brand (current: " + reservation.getMarque() + "): ");
        String brand = scanner.nextLine();
        reservation.setMarque(brand);

        reservationService.updateReservation(reservation);
    }

    private static void deleteReservation(Scanner scanner) {
        // First show all available reservations
        List<Reservation> reservations = reservationService.findAll();
        if (reservations.isEmpty()) {
            System.out.println("\nNo reservations available to delete!");
            return;
        }

        System.out.println("\nAvailable reservations:");
        reservations.forEach(System.out::println);

        System.out.print("\nEnter reservation ID to delete: ");
        int id = scanner.nextInt();
        reservationService.deleteReservation(id);
    }

    private static void findReservationById(Scanner scanner) {
        // First show all available reservations
        List<Reservation> reservations = reservationService.findAll();
        if (reservations.isEmpty()) {
            System.out.println("\nNo reservations available to find!");
            return;
        }

        System.out.println("\nAvailable reservations:");
        reservations.forEach(System.out::println);

        System.out.print("\nEnter reservation ID to find: ");
        int id = scanner.nextInt();
        Reservation reservation = reservationService.findById(id);
        if (reservation != null) {
            System.out.println("\nFound reservation:");
            System.out.println(reservation);
        } else {
            System.out.println("Reservation not found!");
        }
    }

    private static void findReservationsByDate(Scanner scanner) {
        scanner.nextLine(); // Clear buffer
        System.out.print("\nEnter date (YYYY-MM-DD): ");
        String dateStr = scanner.nextLine();
        Date date = Date.valueOf(dateStr);

        List<Reservation> reservations = reservationService.findByDate(date);
        if (reservations.isEmpty()) {
            System.out.println("No reservations found for this date!");
        } else {
            System.out.println("\nReservations for " + date + ":");
            reservations.forEach(System.out::println);
        }
    }

    private static void listReservations() {
        List<Reservation> reservations = reservationService.findAll();
        if (reservations.isEmpty()) {
            System.out.println("\nNo reservations found!");
        } else {
            System.out.println("\nAll reservations:");
            reservations.forEach(System.out::println);
        }
    }
} 