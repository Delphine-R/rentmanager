package com.epf.rentmanager.servlet.update;

import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Client;
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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@WebServlet("/clients/update")
public class UpdateClientServlet extends HttpServlet {

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
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Retrieve the client ID from the request parameter
        int clientId = Integer.parseInt(request.getParameter("id"));

        try {
            // Retrieve the client's information from the database based on the ID
            Client client = clientService.findById(clientId);

            // Set the client object as an attribute in the request
            request.setAttribute("client", client);
            request.setAttribute("naissance", client.getNaissance().format(formatter));


            // Forward the request to the update page
            this.getServletContext().getRequestDispatcher("/WEB-INF/views/clients/update.jsp").forward(request, response);
        } catch (ServiceException e) {
            // Handle service exception
            // You can forward or redirect to an error page
            response.sendRedirect(request.getContextPath() + "/clients/list");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Retrieve parameters from the form
        int clientId = Integer.parseInt(request.getParameter("id"));
        String nom = request.getParameter("nom");
        String prenom = request.getParameter("prenom");
        String email = request.getParameter("email");
        LocalDate naissance = LocalDate.parse(request.getParameter("naissance"), formatter);

        // Create a new Client object with the updated information
        Client client = new Client(clientId, nom, prenom, email, naissance);

        // Update the client
        try {
            clientService.update(client);
            // Redirect to the list page after updating the client
            response.sendRedirect(request.getContextPath() + "/clients/list");
        } catch (ServiceException e) {
            // Handle service exception
            // You can forward or redirect to an error page
            response.sendRedirect(request.getContextPath() + "/clients/list");
        }
    }
}
