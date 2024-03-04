package com.epf.rentmanager.service;

import java.util.List;

import com.epf.rentmanager.exception.*;
import com.epf.rentmanager.model.Vehicle;
import com.epf.rentmanager.dao.VehicleDao;

public class VehicleService {

    private VehicleDao vehicleDao;
    public static VehicleService instance;

    private VehicleService() {
        this.vehicleDao = VehicleDao.getInstance();
    }

    public static VehicleService getInstance() {
        if (instance == null) {
            instance = new VehicleService();
        }
        return instance;
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

}




