package com.epf.rentmanager.service;

import java.util.List;

import com.epf.rentmanager.dao.ClientDao;
import com.epf.rentmanager.exception.*;
import com.epf.rentmanager.model.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientService {

    @Autowired
    private ClientDao clientDao;

    public void update(Client client) throws ServiceException {
        try {
            clientDao.update(client);
        } catch (DaoException e) {
            throw new ServiceException("Error occurred in Service while updating client.");
        }
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

    public void deleteById(int clientId) throws ServiceException {
        try {
            clientDao.deleteById(clientId);
        } catch (DaoException e) {
            throw new ServiceException("Error occurred in Service while deleting client.");
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

    public int count() throws ServiceException {
        try {
            return clientDao.count();
        } catch (DaoException e) {
            throw new ServiceException("Error occurred in DAO while counting clients.");
        }
    }
}
