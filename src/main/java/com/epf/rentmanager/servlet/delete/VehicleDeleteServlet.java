package com.epf.rentmanager.servlet.delete;

import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.service.VehicleService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/vehicles/delete")
public class VehicleDeleteServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String vehicleIdStr = request.getParameter("id");

        try {
            int vehicleId = Integer.parseInt(vehicleIdStr);
            VehicleService.getInstance().deleteById(vehicleId);
            // Redirect to the list page after successful deletion
            response.sendRedirect(request.getContextPath() + "/vehicles/list");
        } catch (NumberFormatException | ServiceException e) {
            // If there's an error, redirect to the list page
            response.sendRedirect(request.getContextPath() + "/vehicles/list");
        }
    }
}
