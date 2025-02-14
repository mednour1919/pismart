package tests;

import models.*;
import services.DestinationService;
import services.EventService;
import services.ReclamationService;
import services.ReservationService;
import services.VoyageService;

import java.sql.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainTest {
    public static void main(String[] args) {

        //hedhi juste tna7i logs fil console
        Logger.getLogger("org.hibernate").setLevel(Level.SEVERE);

        VoyageService voyageService = new VoyageService();
        DestinationService destinationService = new DestinationService();
        ReservationService reservationService = new ReservationService();

        Destination paris = new Destination("Paris", "France", ClimatEnum.FROID);
        Voyage voyage = new Voyage("Paris Getaway", paris, new java.util.Date(), new java.util.Date(), 1200.50, "Oui oui baguette.", null, FormuleEnum.REPAS_SEUL, 4.5f);
        Reservation reservation1 = new Reservation("123", voyage, new java.util.Date(), 1, StatutEnum.EN_ATTENTE);
        Reservation reservation2 = new Reservation("456", voyage, new java.util.Date(), 3, StatutEnum.CONFIRME);

        destinationService.create(paris);
        voyageService.create(voyage);
        reservationService.create(reservation1);
        reservationService.create(reservation2);

        List<Voyage> voyages = voyageService.getAll();

        if (!voyages.isEmpty()) {
            System.out.println(voyages.size() + " voyages");
            Voyage fetchedVoyage = voyageService.getById(voyage.getId());
            System.out.println("Description : " + fetchedVoyage.getDescription());
            System.out.println("nom : " + fetchedVoyage.getNom());

            Destination fetchedDestination = destinationService.getById(fetchedVoyage.getDestination().getId());
            System.out.println("Destination : " + fetchedDestination.getVille());

            fetchedVoyage.setDescription("description 2");
            voyageService.update(fetchedVoyage);
            Voyage updatedVoyage = voyageService.getById(fetchedVoyage.getId());
            System.out.println("nouvelle description: " + updatedVoyage.getDescription());

            List<Reservation> reservations = reservationService.getAll().stream().filter(r -> r.getVoyage().getId().equals(updatedVoyage.getId())).toList();
            System.out.println(reservations.size() + " reservations");
            for (Reservation reservation : reservations) {
                System.out.println(reservation.getClient() + ", " + reservation.getNombrePersonnes() + " personnes, total = " + reservation.getPrixTotal());
            }

            reservationService.delete(reservation1.getId());
            reservationService.delete(reservation2.getId());
            voyageService.delete(updatedVoyage.getId());
            System.out.println("voyage et reservations supprimés");

        } else {
            System.out.println("Aucun voyage");
        }

        EventService eventService = new EventService();

        Event test = new Event("Voyage à Paris", paris, new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()), 500.0,"description");
        eventService.create(test);
    }
}