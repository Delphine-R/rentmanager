package com.epf.rentmanager.ui.cli;

import java.time.LocalDate;
import java.util.List;

import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Client;
import com.epf.rentmanager.model.Reservation;
import com.epf.rentmanager.service.ReservationService;
import com.epf.rentmanager.utils.IOUtils;

public class CLIReservation {

    private ReservationService reservationService;

    public CLIReservation() {
        this.reservationService = ReservationService.getInstance();
    }

    public void createReservation() {
        IOUtils.print("Création d'une nouvelle réservation :");

        int clientId = IOUtils.readInt("ID du client : ");
        int vehicleId = IOUtils.readInt("ID du véhicule : ");
        LocalDate debut = IOUtils.readDate("Date de début (format dd/MM/yyyy) : ", true);
        LocalDate fin = IOUtils.readDate("Date de fin (format dd/MM/yyyy) : ", true);

        Reservation reservation = new Reservation();
        reservation.setClient_id(clientId);
        reservation.setVehicle_id(vehicleId);
        reservation.setDebut(debut);
        reservation.setFin(fin);

        try {
            long reservationId = reservationService.create(reservation);
            IOUtils.print("La réservation a été créée avec succès !");
            IOUtils.print("ID de la réservation : " + reservationId);
            IOUtils.print("ID du client : " + reservation.getClient_id());
            IOUtils.print("ID du véhicule : " + reservation.getVehicle_id());
            IOUtils.print("Date de début : " + reservation.getDebut());
            IOUtils.print("Date de fin : " + reservation.getFin());
        } catch (ServiceException e) {
            IOUtils.print("Erreur lors de la création de la réservation : " + e.getMessage());
        }
    }

    public void listReservations() {
        try {
            List<Reservation> reservations = reservationService.findAll();
            for (Reservation reservation : reservations) {
                IOUtils.print(reservation.toString());
            }
        } catch (ServiceException e) {
            IOUtils.print("Erreur lors de la récupération des réservations : " + e.getMessage());
        }
    }

    public void listReservationsByClientId() {
        int clientId = IOUtils.readInt("ID du client : ");
        try {
            List<Reservation> reservations = reservationService.findByClientId(clientId);
            for (Reservation reservation : reservations) {
                IOUtils.print(reservation.toString());
            }
        } catch (ServiceException e) {
            IOUtils.print("Erreur lors de la récupération des réservations pour le client " + clientId + " : " + e.getMessage());
        }
    }

    public void listReservationsByVehicleId() {
        int vehicleId = IOUtils.readInt("ID du véhicule : ");
        try {
            List<Reservation> reservations = reservationService.findByVehicleId(vehicleId);
            for (Reservation reservation : reservations) {
                IOUtils.print(reservation.toString());
            }
        } catch (ServiceException e) {
            IOUtils.print("Erreur lors de la récupération des réservations pour le véhicule " + vehicleId + " : " + e.getMessage());
        }
    }

    private void displayReservations(List<Reservation> reservations) {
        if (reservations.isEmpty()) {
            IOUtils.print("Aucune réservation trouvée.");
        } else {
            IOUtils.print("Liste des réservations :");
            for (Reservation reservation : reservations) {
                IOUtils.print(reservation.toString());
            }
        }
    }

    public static void main(String[] args) {
        CLIReservation cliReservation = new CLIReservation();
        boolean running = true;
        while (running) {
            IOUtils.print("1. Créer une réservation");
            IOUtils.print("2. Lister les réservations");
            IOUtils.print("3. Lister les réservations par ID de client");
            IOUtils.print("4. Lister les réservations par ID de véhicule");
            IOUtils.print("5. Quitter");
            int choice = IOUtils.readInt("Votre choix : ");
            switch (choice) {
                case 1:
                    cliReservation.createReservation();
                    break;
                case 2:
                    cliReservation.listReservations();
                    break;
                case 3:
                    cliReservation.listReservationsByClientId();
                    break;
                case 4:
                    cliReservation.listReservationsByVehicleId();
                    break;
                case 5:
                    running = false;
                    IOUtils.print("Au revoir !");
                    break;
                default:
                    IOUtils.print("Choix invalide !");
                    break;
            }
        }
    }
}
