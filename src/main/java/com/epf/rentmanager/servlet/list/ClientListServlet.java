package com.epf.rentmanager.servlet.list;

import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Client;
import com.epf.rentmanager.service.ClientService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/clients/list")
public class ClientListServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Client> clients;
        try {
            clients = ClientService.getInstance().findAll();
        } catch (ServiceException e) {
            return;
        }

        // Set the list of clients as an attribute in the request object
        request.setAttribute("clients", clients);

        this.getServletContext().getRequestDispatcher("/WEB-INF/views/clients/list.jsp").forward(request, response);

    }
}
