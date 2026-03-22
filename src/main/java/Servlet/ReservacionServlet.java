/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servlet;

import Conexion.ReservacionDAO;
import Logica.Reservacion;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

/**
 *
 * @author fernan
 */
@WebServlet("/ReservacionServlet")
public class ReservacionServlet extends HttpServlet{
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("rol") == null) {
            out.print("{\"error\":\"No autorizado\"}");
            return;
        }

        String rol = (String) session.getAttribute("rol");
        
        if (!rol.equals("ATENCION")) {
            out.print("{\"error\":\"Acceso denegado\"}");
            return;
        }
        
        String fecha = request.getParameter("fecha_viaje");
        int paqueteId = Integer.parseInt(request.getParameter("paquete_id"));
        int cantidad = Integer.parseInt(request.getParameter("cantidad"));
        double costo = Double.parseDouble(request.getParameter("costo"));
        String dpi = request.getParameter("dpi");
        
        String agente = (String) session.getAttribute("usuario");

        Reservacion r = new Reservacion();
        r.setFechaViaje(fecha);
        r.setPaqueteId(paqueteId);
        r.setCandidadPersonas(cantidad);
        r.setCosotTotal(costo);
        r.setAgente(agente);
        r.setDpiCliente(dpi);
        ReservacionDAO dao = new ReservacionDAO();

        if (dao.crearReservacion(r)) {
            out.print("{\"status\":\"ok\",\"mensaje\":\"Reservacion creada\"}");
        } else {
            out.print("{\"error\":\"No se pudo crear\"}");
        }
    }
}
