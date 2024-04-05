package com.epf.rentmanager.dao;

import com.epf.rentmanager.exception.DaoException;
import com.epf.rentmanager.model.Reservation;
import com.epf.rentmanager.persistence.ConnectionManager;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ReservationDao {

    //private static ReservationDao instance = null;

    private static final String CREATE_RESERVATION_QUERY = "INSERT INTO Reservation(client_id, vehicle_id, debut, fin) VALUES(?, ?, ?, ?);";
    private static final String DELETE_RESERVATION_QUERY = "DELETE FROM Reservation WHERE id=?;";
    private static final String FIND_RESERVATION_QUERY = "SELECT id, client_id, vehicle_id, debut, fin FROM Reservation WHERE id=?;";
    private static final String FIND_RESERVATIONS_BY_CLIENT_QUERY = "SELECT id, vehicle_id, debut, fin FROM Reservation WHERE client_id=?;";
    private static final String FIND_RESERVATIONS_BY_VEHICLE_QUERY = "SELECT id, client_id, debut, fin FROM Reservation WHERE vehicle_id=?;";
    private static final String FIND_RESERVATIONS_QUERY = "SELECT id, client_id, vehicle_id, debut, fin FROM Reservation;";
    private static final String COUNT_RESERVATIONS_QUERY = "SELECT COUNT(id) AS count FROM Reservation;";
    private static final String UPDATE_RESERVATIONS_QUERY = "UPDATE Reservation SET client_id = ?, vehicle_id = ?, debut = ?, fin = ? WHERE id = ?;";

    private ReservationDao() {}

    public static int count() throws DaoException {
        int count = 0;
        try {
            Connection connection = ConnectionManager.getConnection();
            PreparedStatement ps = connection.prepareStatement(COUNT_RESERVATIONS_QUERY);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                count = rs.getInt("count");
            }

            rs.close();
            ps.close();
            connection.close();
        } catch (SQLException e) {
            throw new DaoException("Error occurred in DAO while counting reservations.");
        }
        return count;
    }

    public void update(Reservation reservation) throws DaoException {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement ps = connection.prepareStatement(UPDATE_RESERVATIONS_QUERY)) {
            ps.setInt(1, reservation.getClient_id());
            ps.setInt(2, reservation.getVehicle_id());
            ps.setDate(3, java.sql.Date.valueOf(reservation.getDebut()));
            ps.setDate(4, java.sql.Date.valueOf(reservation.getFin()));
            ps.setInt(5, reservation.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException("Error occurred in DAO while updating the reservation.");
        }
    }

    public int create(Reservation reservation) throws DaoException {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement ps = connection.prepareStatement(CREATE_RESERVATION_QUERY, PreparedStatement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, reservation.getClient_id());
            ps.setInt(2, reservation.getVehicle_id());
            ps.setDate(3, Date.valueOf(reservation.getDebut()));
            ps.setDate(4, Date.valueOf(reservation.getFin()));

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected == 0) {
                throw new DaoException("Error occurred in DAO while creating a new reservation.");
            }

            try (ResultSet resultSet = ps.getGeneratedKeys()) {
                if (resultSet.next()) {
                    int id = resultSet.getInt(1);
                    reservation.setId(id);
                    return id;
                } else {
                    throw new DaoException("Error occurred in DAO while creating a new reservation.");
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Error occurred in DAO while creating a new reservation.");
        }
    }

    public Reservation findById(int id) throws DaoException {
        try {
            Connection connection = ConnectionManager.getConnection();
            PreparedStatement ps = connection.prepareStatement(FIND_RESERVATION_QUERY);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int clientId = rs.getInt("client_id");
                int vehicleId = rs.getInt("vehicle_id");
                java.sql.Date debut = rs.getDate("debut");
                java.sql.Date fin = rs.getDate("fin");

                return new Reservation(id, clientId, vehicleId, debut.toLocalDate(), fin.toLocalDate());
            }

            rs.close();
            ps.close();
            connection.close();

            return null;
        } catch (SQLException e) {
            throw new DaoException("Error occurred in DAO while finding the reservation by ID.");
        }
    }

    public void deleteById(int reservationId) throws DaoException {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement ps = connection.prepareStatement(DELETE_RESERVATION_QUERY)) {

            ps.setInt(1, reservationId);

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new DaoException("Error occurred in DAO while deleting the reservation.");
        }
    }

    public List<Reservation> findResaByClientId(int clientId) throws DaoException {
        List<Reservation> reservations = new ArrayList<>();
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement ps = connection.prepareStatement(FIND_RESERVATIONS_BY_CLIENT_QUERY)) {

            ps.setInt(1, clientId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    int vehicleId = rs.getInt("vehicle_id");
                    java.sql.Date debut = rs.getDate("debut");
                    java.sql.Date fin = rs.getDate("fin");

                    Reservation reservation = new Reservation(id, clientId, vehicleId, debut.toLocalDate(), fin.toLocalDate());
                    reservations.add(reservation);
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Error occurred in DAO while finding the reservation by Client ID.");
        }

        return reservations;
    }

    public List<Reservation> findResaByVehicleId(int vehicleId) throws DaoException {
        List<Reservation> reservations = new ArrayList<>();
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement ps = connection.prepareStatement(FIND_RESERVATIONS_BY_VEHICLE_QUERY)) {

            ps.setInt(1, vehicleId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    int clientId = rs.getInt("client_id");
                    java.sql.Date debut = rs.getDate("debut");
                    java.sql.Date fin = rs.getDate("fin");

                    Reservation reservation = new Reservation(id, clientId, vehicleId, debut.toLocalDate(), fin.toLocalDate());
                    reservations.add(reservation);
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Error occurred in DAO while finding the reservation by Vehicle ID.");
        }

        return reservations;
    }

    public List<Reservation> findAll() throws DaoException {
        List<Reservation> reservations = new ArrayList<>();
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement ps = connection.prepareStatement(FIND_RESERVATIONS_QUERY);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                int clientId = rs.getInt("client_id");
                int vehicleId = rs.getInt("vehicle_id");
                java.sql.Date debut = rs.getDate("debut");
                java.sql.Date fin = rs.getDate("fin");

                Reservation reservation = new Reservation(id, clientId, vehicleId, debut.toLocalDate(), fin.toLocalDate());
                reservations.add(reservation);
            }
        } catch (SQLException e) {
            throw new DaoException("Error occurred in DAO while finding all reservations.");
        }

        return reservations;
    }
}
