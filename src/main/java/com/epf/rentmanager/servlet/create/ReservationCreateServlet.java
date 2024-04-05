package com.epf.rentmanager.servlet.create;

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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@WebServlet("/reservations/create")
public class ReservationCreateServlet extends HttpServlet {

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
        List<Vehicle> vehicles = null;
        List<Client> clients = null;
        try {
            vehicles = vehicleService.findAll();
            clients = clientService.findAll();
        } catch (ServiceException e) {
        }

        // Set the list of reservations as an attribute in the request
        request.setAttribute("vehicles", vehicles);
        request.setAttribute("clients", clients);

        // Forward the request to the JSP view
        this.getServletContext().getRequestDispatcher("/WEB-INF/views/reservations/create.jsp").forward(request, response);
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int clientId = Integer.parseInt(request.getParameter("client_id"));
        int vehicleId = Integer.parseInt(request.getParameter("vehicle_id"));
        LocalDate debut = LocalDate.parse(request.getParameter("debut"), formatter);
        LocalDate fin = LocalDate.parse(request.getParameter("fin"), formatter);

        Reservation reservation = new Reservation();
        reservation.setClient_id(clientId);
        reservation.setVehicle_id(vehicleId);
        reservation.setDebut(debut);
        reservation.setFin(fin);

        try {
            reservationService.create(reservation);
            response.sendRedirect(request.getContextPath() + "/reservations/list");
        } catch (ServiceException e) {
        }
    }

}
