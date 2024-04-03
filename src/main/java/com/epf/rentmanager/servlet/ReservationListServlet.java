package com.epf.rentmanager.servlet;

import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Reservation;
import com.epf.rentmanager.service.ReservationService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/reservations/list")
public class ReservationListServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Reservation> reservations;
        try {
            reservations = ReservationService.getInstance().findAll();
        } catch (ServiceException e) {
            return;
        }

        request.setAttribute("reservations", reservations);

        this.getServletContext().getRequestDispatcher("/WEB-INF/views/reservations/list.jsp").forward(request, response);

    }
}
