package com.epf.rentmanager.servlet.update;

import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Reservation;
import com.epf.rentmanager.service.ReservationService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@WebServlet("/reservations/update")
public class UpdateReservationServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int reservationId = Integer.parseInt(request.getParameter("id"));
            Reservation reservation = ReservationService.getInstance().findById(reservationId);
            request.setAttribute("reservation", reservation);
            request.getRequestDispatcher("/WEB-INF/views/reservations/update.jsp").forward(request, response);
        } catch (NumberFormatException | ServiceException e) {

            response.sendRedirect(request.getContextPath() + "/reservations/list");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int reservationId = Integer.parseInt(request.getParameter("id"));
            int clientId = Integer.parseInt(request.getParameter("client_id"));
            int vehicleId = Integer.parseInt(request.getParameter("vehicle_id"));
            LocalDate debut = LocalDate.parse(request.getParameter("debut"), formatter);
            LocalDate fin = LocalDate.parse(request.getParameter("fin"), formatter);

            Reservation reservation = new Reservation(reservationId, clientId, vehicleId, debut, fin);

            ReservationService.getInstance().update(reservation);
            response.sendRedirect(request.getContextPath() + "/reservations/list");

        } catch (NumberFormatException | ServiceException e) {
            response.sendRedirect(request.getContextPath() + "/reservations/list");
        }
    }
}
