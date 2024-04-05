package com.epf.rentmanager.dao;

import com.epf.rentmanager.exception.DaoException;
import com.epf.rentmanager.model.Reservation;
import com.epf.rentmanager.model.Vehicle;
import com.epf.rentmanager.persistence.ConnectionManager;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class VehicleDao {

    //private static VehicleDao instance = null;

    private static final String CREATE_VEHICLE_QUERY = "INSERT INTO Vehicle(constructeur, modele, nb_places) VALUES(?, ?, ?);";
    private static final String DELETE_VEHICLE_QUERY = "DELETE FROM Vehicle WHERE id=?;";
    private static final String FIND_VEHICLE_QUERY = "SELECT constructeur, modele, nb_places FROM Vehicle WHERE id=?;";
    private static final String FIND_VEHICLES_QUERY = "SELECT id, constructeur, modele, nb_places FROM Vehicle;";
    private static final String COUNT_VEHICLES_QUERY = "SELECT COUNT(id) AS count FROM Vehicle;";
    private static final String UPDATE_VEHICLES_QUERY = "UPDATE Vehicle SET constructeur = ?, modele = ?, nb_places = ? WHERE id = ?;";
    private final ConnectionManager connectionManager;
    private final ReservationDao reservationDao;
    public VehicleDao(ConnectionManager connectionManager, ReservationDao reservationDao) {
        this.connectionManager = connectionManager;
        this.reservationDao = reservationDao;
    }

    public void update(Vehicle vehicle) throws DaoException {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement ps = connection.prepareStatement(UPDATE_VEHICLES_QUERY)) {
            ps.setString(1, vehicle.getConstructeur());
            ps.setString(2, vehicle.getModele());
            ps.setInt(3, vehicle.getNb_places());
            ps.setInt(4, vehicle.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException("Error occurred in DAO while updating the vehicle.");
        }
    }

    public long create(Vehicle vehicle) throws DaoException {
        try {
            Connection connection = ConnectionManager.getConnection();
            Statement statement = connection.createStatement();
            PreparedStatement ps = connection.prepareStatement(CREATE_VEHICLE_QUERY, statement.RETURN_GENERATED_KEYS);
            ps.setString(1, vehicle.getConstructeur());
            ps.setString(2, vehicle.getModele());
            ps.setInt(3, vehicle.getNb_places());
            ps.execute();

            ResultSet resultSet = ps.getGeneratedKeys();

            if (resultSet.next()) {
                int id = resultSet.getInt(1);
                vehicle.setId(id);
                return vehicle.getId();
            }
            ps.close();
            connection.close();
        } catch (SQLException e) {
            throw new DaoException("Error occurred in DAO while creating a new vehicle.");
        }
        return vehicle.getId();
    }


    public void deleteById(int vehicleId) throws DaoException {
        try {
            List<Reservation> reservations = reservationDao.findResaByVehicleId(vehicleId);
            for (Reservation reservation : reservations) {
                reservationDao.deleteById(reservation.getId());
            }
            try (Connection connection = ConnectionManager.getConnection();
                 PreparedStatement ps = connection.prepareStatement(DELETE_VEHICLE_QUERY)) {
                ps.setInt(1, vehicleId);
                ps.executeUpdate();
            } catch (SQLException e) {
                throw new DaoException("Error occurred in DAO while deleting the vehicle.");
            }
        } catch (DaoException e) {
            throw new DaoException("Error occurred in DAO while deleting the vehicle's reservations.");
        }
    }

    public Vehicle findById(int id) throws DaoException {
        try {
            Connection connection = ConnectionManager.getConnection();
            PreparedStatement ps = connection.prepareStatement(FIND_VEHICLE_QUERY);
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String constructeur = rs.getString("constructeur");
                String modele = rs.getString("modele");
                int nb_places = rs.getInt("nb_places");

                return new Vehicle(id, constructeur, modele, nb_places);
            }
            rs.close();
            ps.close();
            connection.close();

            return null;
        } catch (SQLException e) {
            throw new DaoException("Error occurred in DAO while finding the vehicle by ID.");
        }
    }

    public List<Vehicle> findAll() throws DaoException {
        List<Vehicle> vehicles = new ArrayList<>();
        try {
            Connection connection = ConnectionManager.getConnection();
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(FIND_VEHICLES_QUERY);

            while (rs.next()) {
                int id = rs.getInt("id");
                String constructeur = rs.getString("constructeur");
                String modele = rs.getString("modele");
                int nb_places = rs.getInt("nb_places");

                Vehicle vehicle = new Vehicle(id, constructeur, modele, nb_places);
                vehicles.add(vehicle);
            }
            rs.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            throw new DaoException("Error occurred in DAO while finding all vehicles.");
        }

        return vehicles;
    }

    public int count() throws DaoException {
        int count = 0;
        try {
            Connection connection = ConnectionManager.getConnection();
            PreparedStatement ps = connection.prepareStatement(COUNT_VEHICLES_QUERY);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                count = rs.getInt("count");
            }

            rs.close();
            ps.close();
            connection.close();
        } catch (SQLException e) {
            throw new DaoException("Error occurred in DAO while counting vehicles.");
        }

        return count;
    }

}
