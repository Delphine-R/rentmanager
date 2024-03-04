package com.epf.rentmanager.service;

import java.util.List;

import com.epf.rentmanager.dao.ClientDao;
import com.epf.rentmanager.exception.*;
import com.epf.rentmanager.model.Client;

public class ClientService {

    private ClientDao clientDao;
    public static ClientService instance;

    private ClientService() {
        this.clientDao = ClientDao.getInstance();
    }

    public static ClientService getInstance() {
        if (instance == null) {
            instance = new ClientService();
        }
        return instance;
    }


    public long create(Client client) throws ServiceException {
        if (client.getNom().isEmpty() || client.getPrenom().isEmpty()) {
            throw new ServiceException("Error occurred in Service while creating a new client.");
        }

        client.setNom(client.getNom().toUpperCase());

        try {
            return clientDao.create(client);
        } catch (DaoException e) {
            throw new ServiceException("Error occurred in Service while creating a new client.");
        }

    }

    public Client findById(int id) throws ServiceException {
        try {
            return clientDao.findById(id);
        } catch (DaoException e) {
            throw new ServiceException("Error occurred in Service while finding the client by ID.");
        }
    }

    public List<Client> findAll() throws ServiceException {
        try {
            return clientDao.findAll();
        } catch (DaoException e) {
            throw new ServiceException("Error occurred in Service while finding all clients.");
        }
    }

}