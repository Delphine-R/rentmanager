package com.epf.rentmanager.service;

import java.util.List;

import com.epf.rentmanager.dao.ReservationDao;
import com.epf.rentmanager.exception.*;
import com.epf.rentmanager.model.Reservation;

public class ReservationService {

    private ReservationDao reservationDao;
    public static ReservationService instance;

    private ReservationService() {
        this.reservationDao = ReservationDao.getInstance();
    }

    public static ReservationService getInstance() {
        if (instance == null) {
            instance = new ReservationService();
        }
        return instance;
    }


    public long create(Reservation reservation) throws ServiceException {
        if (reservation.getId() < 0 || reservation.getClient_id() < 0 || reservation.getVehicle_id() < 0) {
            throw new ServiceException("Error occurred in Service while creating a new reservation.");
        }

        try {
            return reservationDao.create(reservation);
        } catch (DaoException e) {
            throw new ServiceException("Error occurred in Service while creating a new reservation.");
        }

    }

    public List<Reservation> findByClientId(int id) throws ServiceException {
        try {
            return reservationDao.findResaByClientId(id);
        } catch (DaoException e) {
            throw new ServiceException("Error occurred in Service while finding the reservation by Client ID.");
        }
    }

    public List<Reservation> findByVehicleId(int id) throws ServiceException {
        try {
            return reservationDao.findResaByVehicleId(id);
        } catch (DaoException e) {
            throw new ServiceException("Error occurred in Service while finding the reservation by Vehicle ID.");
        }
    }

    public List<Reservation> findAll() throws ServiceException {
        try {
            return reservationDao.findAll();
        } catch (DaoException e) {
            throw new ServiceException("Error occurred in Service while finding all reservations.");
        }
    }

    public int count() throws ServiceException {
        try {
            return reservationDao.count();
        } catch (DaoException e) {
            throw new ServiceException("Error occurred in DAO while counting reservations.");
        }
    }

}
