package com.epf.rentmanager.dao;

import com.epf.rentmanager.exception.DaoException;
import com.epf.rentmanager.model.Client;
import com.epf.rentmanager.model.Reservation;
import com.epf.rentmanager.persistence.ConnectionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ClientDao {


    private static final String CREATE_CLIENT_QUERY = "INSERT INTO Client(nom, prenom, email, naissance) VALUES(?, ?, ?, ?);";
    private static final String DELETE_CLIENT_QUERY = "DELETE FROM Client WHERE id=?;";
    private static final String FIND_CLIENT_QUERY = "SELECT nom, prenom, email, naissance FROM Client WHERE id=?;";
    private static final String FIND_CLIENTS_QUERY = "SELECT id, nom, prenom, email, naissance FROM Client;";
    private static final String COUNT_CLIENTS_QUERY = "SELECT COUNT(id) AS count FROM Client;";
    private static final String UPDATE_CLIENTS_QUERY = "UPDATE Client SET nom = ?, prenom = ?, email = ?, naissance = ? WHERE id = ?;";

    @Autowired
    ReservationDao reservationDao;
    private ClientDao() {
    }

    public Client findById(int id) throws DaoException {
        try {
            Connection connection = ConnectionManager.getConnection();
            PreparedStatement ps = connection.prepareStatement(FIND_CLIENT_QUERY);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String nom = rs.getString("nom");
                String prenom = rs.getString("prenom");
                String email = rs.getString("email");
                Date naissance = rs.getDate("naissance");

                return new Client(id, nom, prenom, email, naissance.toLocalDate());
            }
            rs.close();
            ps.close();
            connection.close();

            return null;
        } catch (SQLException e) {
            throw new DaoException("Error occurred in DAO while finding the client by ID.");
        }
    }

    public List<Client> findAll() throws DaoException {
        List<Client> clients = new ArrayList<>();
        try {
            Connection connection = ConnectionManager.getConnection();
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(FIND_CLIENTS_QUERY);

            while (rs.next()) {
                int id = rs.getInt("id");
                String nom = rs.getString("nom");
                String prenom = rs.getString("prenom");
                String email = rs.getString("email");
                Date naissance = rs.getDate("naissance");

                Client client = new Client(id, nom, prenom, email, naissance.toLocalDate());
                clients.add(client);
            }
            rs.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            throw new DaoException("Error occurred in DAO while finding all clients.");
        }

        return clients;
    }

    public static int count() throws DaoException {
        int count = 0;
        try {
            Connection connection = ConnectionManager.getConnection();
            PreparedStatement ps = connection.prepareStatement(COUNT_CLIENTS_QUERY);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                count = rs.getInt("count");
            }

            rs.close();
            ps.close();
            connection.close();
        } catch (SQLException e) {
            throw new DaoException("Error occurred in DAO while counting clients.");
        }

        return count;
    }

    public int create(Client client) throws DaoException {
        try {
            Connection connection = ConnectionManager.getConnection();
            Statement statement = connection.createStatement();
            PreparedStatement ps = connection.prepareStatement(CREATE_CLIENT_QUERY, statement.RETURN_GENERATED_KEYS);
            ps.setString(1, client.getNom());
            ps.setString(2, client.getPrenom());
            ps.setString(3, client.getEmail());
            ps.setDate(4, Date.valueOf(client.getNaissance()));
            ps.execute();

            ResultSet resultSet = ps.getGeneratedKeys();

            if (resultSet.next()) {
                int id = resultSet.getInt(1);
                client.setId(id);
                return client.getId();
            }
            ps.close();
            connection.close();
        } catch (SQLException e) {
            throw new DaoException("Error occurred in DAO while creating a new client.");
        }
        return client.getId();
    }

    public void update(Client client) throws DaoException {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement ps = connection.prepareStatement(UPDATE_CLIENTS_QUERY)) {
            ps.setString(1, client.getNom());
            ps.setString(2, client.getPrenom());
            ps.setString(3, client.getEmail());
            ps.setDate(4, Date.valueOf(client.getNaissance()));
            ps.setInt(5, client.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException("Error occurred in DAO while updating the client.");
        }
    }

    public void deleteById(int clientId) throws DaoException {
        try {
            List<Reservation> reservations = reservationDao.findResaByClientId(clientId);
            for (Reservation reservation : reservations) {
                reservationDao.deleteById(reservation.getId());
            }
            try (Connection connection = ConnectionManager.getConnection();
                 PreparedStatement ps = connection.prepareStatement(DELETE_CLIENT_QUERY)) {
                ps.setInt(1, clientId);
                ps.executeUpdate();
            } catch (SQLException e) {
                throw new DaoException("Error occurred in DAO while deleting the client.");
            }
        } catch (DaoException e) {
            throw new DaoException("Error occurred in DAO while deleting the client's reservations.");
        }
    }

}