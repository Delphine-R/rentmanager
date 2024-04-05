package com.epf.rentmanager.servlet.list;

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

@WebServlet("/reservations/list")
public class ReservationListServlet extends HttpServlet {

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

        List<Reservation> reservations;
        List<Client> clients = new ArrayList<>();;
        List<Vehicle> vehicles = new ArrayList<>();

        try {
            reservations = reservationService.findAll();
        } catch (ServiceException e) {
            return;
        }

        for (Reservation reservation : reservations) {
            try {
                Vehicle vehicle = vehicleService.findById(reservation.getVehicle_id());
                vehicles.add(vehicle);
                Client client = clientService.findById(reservation.getClient_id());
                clients.add(client);
            } catch (ServiceException e) {
                return;
            }
        }

        request.setAttribute("reservations", reservations);
        request.setAttribute("vehicles", vehicles);
        request.setAttribute("clients", clients);

        this.getServletContext().getRequestDispatcher("/WEB-INF/views/reservations/list.jsp").forward(request, response);

    }
}