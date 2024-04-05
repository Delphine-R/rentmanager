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

@WebServlet("/vehicles/details")
public class VehicleDetailsServlet extends HttpServlet {
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

        String vehicleIdStr = request.getParameter("id");

        try {
            int vehicleId = Integer.parseInt(vehicleIdStr);
            Vehicle vehicle = vehicleService.findById(vehicleId);

            List<Reservation> reservations = reservationService.findByVehicleId(vehicleId);
            List<Client> clients = new ArrayList<>();

            for (Reservation reservation : reservations) {
                int clientId = reservation.getClient_id();
                Client client = clientService.findById(clientId);
                clients.add(client);
            }

            request.setAttribute("vehicle", vehicle);
            request.setAttribute("clients", clients);
            request.setAttribute("reservations", reservations);
            request.setAttribute("nb_clients", clients.size());
            request.setAttribute("nb_reservations", reservations.size());

            // Forward the request to the details page
            this.getServletContext().getRequestDispatcher("/WEB-INF/views/vehicles/details.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            // If there's an error in parsing ID, redirect to the list page
            response.sendRedirect(request.getContextPath() + "/vehicles/list");
        } catch (ServiceException e) {
            // If there's an error in retrieving vehicle, clients, or reservations, redirect to the list page
            response.sendRedirect(request.getContextPath() + "/vehicles/list");
        }
    }
}
