package com.epf.rentmanager.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.service.ClientService;
import com.epf.rentmanager.service.ReservationService;
import com.epf.rentmanager.service.VehicleService;

@WebServlet("/home")
public class HomeServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private VehicleService vehicleService; // Instance variable to hold the VehicleService instance
    private ClientService clientService;
    private ReservationService reservationService;

    @Override
    public void init() throws ServletException {
        super.init();
        vehicleService = VehicleService.getInstance();
        clientService = ClientService.getInstance();
        reservationService = ReservationService.getInstance();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int vehicleCount = 0;
        int clientCount = 0;
        int reservationCount = 0;

        try {
            vehicleCount = vehicleService.count(); // Call count() method on the instance
            clientCount = clientService.count();
            reservationCount = reservationService.count();
        } catch (ServiceException e) {
            throw new RuntimeException(e);
        }

        request.setAttribute("vehicleCount", vehicleCount);
        request.setAttribute("clientCount", clientCount);
        request.setAttribute("reservationCount", reservationCount);

        this.getServletContext().getRequestDispatcher("/WEB-INF/views/home.jsp").forward(request, response);
    }

}
