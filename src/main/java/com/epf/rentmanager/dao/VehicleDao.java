package com.epf.rentmanager.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.epf.rentmanager.exception.DaoException;
import com.epf.rentmanager.model.*;
import com.epf.rentmanager.persistence.ConnectionManager;

public class VehicleDao {

    private static VehicleDao instance = null;

    private VehicleDao() {
    }

    public static VehicleDao getInstance() {
        if (instance == null) {
            instance = new VehicleDao();
        }
        return instance;
    }

    private static final String CREATE_VEHICLE_QUERY = "INSERT INTO Vehicle(constructeur, modele, nb_places) VALUES(?, ?, ?);";
    private static final String DELETE_VEHICLE_QUERY = "DELETE FROM Vehicle WHERE id=?;";
    private static final String FIND_VEHICLE_QUERY = "SELECT constructeur, modele, nb_places FROM Vehicle WHERE id=?;";
    private static final String FIND_VEHICLES_QUERY = "SELECT id, constructeur, modele, nb_places FROM Vehicle;";


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


    public long delete(Vehicle vehicle) throws DaoException {
        try {
            Connection connection = ConnectionManager.getConnection();
            PreparedStatement ps = connection.prepareStatement(DELETE_VEHICLE_QUERY);
            ps.setLong(1, vehicle.getId());
            int rowsAffected = ps.executeUpdate();
            ps.close();
            connection.close();

            return rowsAffected;
        } catch (SQLException e) {
            throw new DaoException("Error occurred in DAO while deleting the vehicle.");
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

}