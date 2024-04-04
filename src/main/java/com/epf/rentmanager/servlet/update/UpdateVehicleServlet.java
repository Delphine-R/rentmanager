package com.epf.rentmanager.servlet.update;

import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Vehicle;
import com.epf.rentmanager.service.VehicleService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/vehicles/update")
public class UpdateVehicleServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int vehicleId = Integer.parseInt(request.getParameter("id"));
            Vehicle vehicle = VehicleService.getInstance().findById(vehicleId);
            request.setAttribute("vehicle", vehicle);
            request.getRequestDispatcher("/WEB-INF/views/vehicles/update.jsp").forward(request, response);
        } catch (NumberFormatException | ServiceException e) {
            response.sendRedirect(request.getContextPath() + "/vehicles/list");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int vehicleId = Integer.parseInt(request.getParameter("id"));
            String constructeur = request.getParameter("constructeur");
            String modele = request.getParameter("modele");
            int nbPlaces = Integer.parseInt(request.getParameter("nb_places"));
            Vehicle vehicle = new Vehicle(vehicleId, constructeur, modele, nbPlaces);
            VehicleService.getInstance().update(vehicle);
            response.sendRedirect(request.getContextPath() + "/vehicles/list");
        } catch (NumberFormatException | ServiceException e) {
            response.sendRedirect(request.getContextPath() + "/vehicles/list");
        }
    }
}
