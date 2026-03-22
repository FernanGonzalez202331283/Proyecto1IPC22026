/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servlet;

import Conexion.PagoDAO;
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
@WebServlet("/PagoServlet")
public class PagoServlet extends HttpServlet{
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

        int reservacionId = Integer.parseInt(request.getParameter("reservacion_id"));
        double monto = Double.parseDouble(request.getParameter("monto"));
        String metodo = request.getParameter("metodo");

        PagoDAO dao = new PagoDAO();

        if (dao.registrarPago(reservacionId, monto, metodo)) {
            out.print("{\"status\":\"ok\",\"mensaje\":\"Pago registrado\"}");
        } else {
            out.print("{\"error\":\"No se pudo registrar\"}");
        }
    }
}
