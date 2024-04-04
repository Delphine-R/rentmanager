package com.epf.rentmanager.servlet.delete;

import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.service.ClientService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/clients/delete")
public class ClientDeleteServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String clientIdStr = request.getParameter("id");

        try {
            int clientId = Integer.parseInt(clientIdStr);
            ClientService.getInstance().deleteById(clientId);
            // Redirect to the list page after successful deletion
            response.sendRedirect(request.getContextPath() + "/clients/list");
        } catch (NumberFormatException | ServiceException e) {
            // If there's an error, redirect to the list page
            response.sendRedirect(request.getContextPath() + "/clients/list");
        }
    }
}
