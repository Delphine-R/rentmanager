package com.epf.rentmanager.servlet.details;

import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Client;
import com.epf.rentmanager.model.Reservation;
import com.epf.rentmanager.model.Vehicle;
import com.epf.rentmanager.service.ClientService;
import com.epf.rentmanager.service.ReservationService;
import com.epf.rentmanager.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/clients/details")
public class ClientDetailsServlet extends HttpServlet {
    @Autowired
    VehicleService vehicleService;
    @Autowired
    ClientService clientService;
    @Autowired
    ReservationService reservationService;

    @Override
    public void init() throws ServletException {
        super.init();
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Retrieve client ID from the request
        String clientIdStr = request.getParameter("id");

        try {
            // Parse client ID from string to int
            int clientId = Integer.parseInt(clientIdStr);
            // Get client details from the service
            Client client = clientService.findById(clientId);

            // Get reservations for the client
            List<Reservation> reservations = reservationService.findByClientId(clientId);

            // Get vehicles linked to the client
            List<Vehicle> vehicles = new ArrayList<>();

            // Iterate over reservations to extract vehicle IDs and fetch corresponding vehicle details
            for (Reservation reservation : reservations) {
                int vehicleId = reservation.getVehicle_id();
                // Fetch vehicle details using the vehicle ID
                Vehicle vehicle = vehicleService.findById(vehicleId);
                // Add the vehicle to the list
                vehicles.add(vehicle);
            }

            // Set client details, reservations, and vehicles as request attributes
            request.setAttribute("client", client);
            request.setAttribute("reservations", reservations);
            request.setAttribute("vehicles", vehicles);
            request.setAttribute("nb_vehicles", vehicles.size());
            request.setAttribute("nb_reservations", reservations.size());

            // Forward the request to the details page
            this.getServletContext().getRequestDispatcher("/WEB-INF/views/clients/details.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            // If there's an error in parsing ID, redirect to the list page
            response.sendRedirect(request.getContextPath() + "/clients/list");
        } catch (ServiceException e) {
            // If there's an error in retrieving client, reservations, or vehicles, redirect to the list page
            response.sendRedirect(request.getContextPath() + "/clients/list");
        }
    }
}
