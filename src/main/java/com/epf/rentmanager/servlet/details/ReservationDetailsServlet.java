package com.epf.rentmanager.servlet.details;

import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Client;
import com.epf.rentmanager.model.Reservation;
import com.epf.rentmanager.model.Vehicle;
import com.epf.rentmanager.service.ClientService;
import com.epf.rentmanager.service.ReservationService;
import com.epf.rentmanager.service.VehicleService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/reservations/details")
public class ReservationDetailsServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String reservationIdStr = request.getParameter("id");

        try {
            int reservationId = Integer.parseInt(reservationIdStr);
            Reservation reservation = ReservationService.getInstance().findById(reservationId);

            // Get client details for the reservation
            int clientId = reservation.getClient_id();
            Client client = ClientService.getInstance().findById(clientId);

            // Get vehicles linked to the reservation
            List<Vehicle> vehicles = new ArrayList<>();
            int vehicleId = reservation.getVehicle_id();
            Vehicle vehicle = VehicleService.getInstance().findById(vehicleId);
            vehicles.add(vehicle);

            request.setAttribute("reservation", reservation);
            request.setAttribute("client", client);
            request.setAttribute("vehicles", vehicles);
            request.setAttribute("nb_vehicles", vehicles.size());

            // Forward the request to the details page
            this.getServletContext().getRequestDispatcher("/WEB-INF/views/reservations/details.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            // If there's an error in parsing ID, redirect to the list page
            response.sendRedirect(request.getContextPath() + "/reservations/list");
        } catch (ServiceException e) {
            // If there's an error in retrieving reservation, client, or vehicle, redirect to the list page
            response.sendRedirect(request.getContextPath() + "/reservations/list");
        }
    }
}
