package com.epf.rentmanager.service;

import java.util.List;

import com.epf.rentmanager.dao.VehicleDao;
import com.epf.rentmanager.exception.*;
import com.epf.rentmanager.model.Vehicle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VehicleService {

    @Autowired
    private VehicleDao vehicleDao;

    public void update(Vehicle vehicle) throws ServiceException {
        try {
            vehicleDao.update(vehicle);
        } catch (DaoException e) {
            throw new ServiceException("Error occurred in Service while updating vehicle.");
        }
    }

    public long create(Vehicle vehicle) throws ServiceException {
        if (vehicle.getConstructeur().isEmpty() || vehicle.getNb_places() < 0) {
            throw new ServiceException("Error occurred in Service while creating the vehicle.");
        }

        try {
            return vehicleDao.create(vehicle);
        } catch (DaoException e) {
            throw new ServiceException("Error occurred in Service while creating the vehicle.");
        }
    }

    public void deleteById(int vehicleId) throws ServiceException {
        try {
            vehicleDao.deleteById(vehicleId);
        } catch (DaoException e) {
            throw new ServiceException("Error occurred in Service while deleting vehicle.");
        }
    }

    public Vehicle findById(int id) throws ServiceException {
        try {
            return vehicleDao.findById(id);
        } catch (DaoException e) {
            throw new ServiceException("Error occurred in Service while finding the vehicle by ID.");
        }
    }

    public List<Vehicle> findAll() throws ServiceException {
        try {
            return vehicleDao.findAll();
        } catch (DaoException e) {
            throw new ServiceException("Error occurred in DAO while finding all vehicles.");
        }
    }

    public int count() throws ServiceException {
        try {
            return vehicleDao.count();
        } catch (DaoException e) {
            throw new ServiceException("Error occurred in DAO while counting vehicles.");
        }
    }
}
