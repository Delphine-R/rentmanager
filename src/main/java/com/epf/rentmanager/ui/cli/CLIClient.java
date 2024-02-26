package com.epf.rentmanager.ui.cli;

import java.time.LocalDate;
import java.util.List;

import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Client;
import com.epf.rentmanager.service.ClientService;
import com.epf.rentmanager.utils.IOUtils;

public class CLIClient {

    private ClientService clientService;

    public CLIClient() {
        this.clientService = ClientService.getInstance();
    }

    public void createClient() {
        IOUtils.print("Création d'un nouveau client :");
        String nom = IOUtils.readString("Nom du client : ", true);
        String prenom = IOUtils.readString("Prénom du client : ", true);
        String email = IOUtils.readEmail("Email du client : ", true);
        LocalDate naissance = IOUtils.readDate("Date de naissance du client (format dd/MM/yyyy) : ", true);

        Client client = new Client();
        client.setNom(nom);
        client.setPrenom(prenom);
        client.setEmail(email);
        client.setNaissance(naissance);

        try {
            long clientId = clientService.create(client);
            IOUtils.print("Le client a été créé avec succès !");
            IOUtils.print("ID du client : " + clientId);
            IOUtils.print("Nom du client : " + client.getNom());
            IOUtils.print("Prénom du client : " + client.getPrenom());
            IOUtils.print("Email du client : " + client.getEmail());
            IOUtils.print("Date de naissance du client : " + client.getNaissance());
        } catch (ServiceException e) {
            IOUtils.print("Erreur lors de la création du client : " + e.getMessage());
        }
    }

    public void listClients() {
        try {
            List<Client> clients = clientService.findAll();
            IOUtils.print("Liste des clients :");
            for (Client client : clients) {
                IOUtils.print(client.toString());
            }
        } catch (ServiceException e) {
            IOUtils.print("Erreur lors de la récupération des clients : " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        CLIClient cliClient = new CLIClient();
        boolean running = true;
        while (running) {
            IOUtils.print("1. Créer un client");
            IOUtils.print("2. Lister les clients");
            IOUtils.print("3. Quitter");
            int choice = IOUtils.readInt("Votre choix : ");
            switch (choice) {
                case 1:
                    cliClient.createClient();
                    break;
                case 2:
                    cliClient.listClients();
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
