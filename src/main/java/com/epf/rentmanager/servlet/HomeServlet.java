package com.epf.rentmanager.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.service.VehicleService;

@WebServlet("/home")
public class HomeServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private VehicleService vehicleService; // Instance variable to hold the VehicleService instance

    @Override
    public void init() throws ServletException {
        super.init();
        vehicleService = VehicleService.getInstance(); // Initialize the VehicleService instance
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int vehicleCount = 0;
        try {
            vehicleCount = vehicleService.count(); // Call count() method on the instance
        } catch (ServiceException e) {
            throw new RuntimeException(e);
        }
        request.setAttribute("vehicleCount", vehicleCount);
        this.getServletContext().getRequestDispatcher("/WEB-INF/views/home.jsp").forward(request, response);
    }

}
