package com.epf.rentmanager.ui.cli;

import java.util.List;

import com.epf.rentmanager.configuration.AppConfiguration;
import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Vehicle;
import com.epf.rentmanager.service.VehicleService;
import com.epf.rentmanager.utils.IOUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class CLIVehicle {

    private VehicleService vehicleService;

    @Autowired
    public CLIVehicle(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    public void createVehicle() {
        IOUtils.print("Création d'un nouveau véhicule :");
        String constructeur = IOUtils.readString("Constructeur du véhicule : ", true);
        String modele = IOUtils.readString("Modele du véhicule : ", true);
        int nb_places = IOUtils.readInt("Nombre de places du véhicule : ");

        Vehicle vehicle = new Vehicle();
        vehicle.setConstructeur(constructeur);
        vehicle.setModele(modele);
        vehicle.setNb_places(nb_places);

        try {
            long vehicleId = vehicleService.create(vehicle);
            IOUtils.print("Le véhicule a été créé avec succès !");
            IOUtils.print("ID du client : " + vehicleId);
            IOUtils.print("Nom du vehicle : " + vehicle.getConstructeur());
            IOUtils.print("Prénom du vehicle : " + vehicle.getModele());
            IOUtils.print("Email du vehicle : " + vehicle.getNb_places());
        } catch (ServiceException e) {
            IOUtils.print("Erreur lors de la création du véhicule : " + e.getMessage());
        }
    }

    public void listVehicles() {
        try {
            List<Vehicle> vehicles = vehicleService.findAll();
            IOUtils.print("Liste des véhicules :");
            for (Vehicle vehicle : vehicles) {
                IOUtils.print(vehicle.toString());
            }
        } catch (ServiceException e) {
            IOUtils.print("Erreur lors de la récupération des véhicules : " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfiguration.class);
        CLIVehicle cliVehicle = context.getBean(CLIVehicle.class);

        boolean running = true;
        while (running) {
            IOUtils.print("1. Créer un vehicule");
            IOUtils.print("2. Lister les vehicules");
            IOUtils.print("3. Quitter");
            int choice = IOUtils.readInt("Votre choix : ");
            switch (choice) {
                case 1:
                    cliVehicle.createVehicle();
                    break;
                case 2:
                    cliVehicle.listVehicles();
                    break;
                case 3:
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
