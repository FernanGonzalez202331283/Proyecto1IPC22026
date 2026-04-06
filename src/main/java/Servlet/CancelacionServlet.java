/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servlet;

import Conexion.CancelacionDAO;
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
@WebServlet("/CancelacionServlet")
public class CancelacionServlet extends HttpServlet{
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:4200");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
        response.setHeader("Access-Control-Allow-Credentials", "true");

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("rol") == null) {
            out.print("{\"error\":\"No autorizado\"}");
            return;
        }

        String rol = (String) session.getAttribute("rol");

        if (!rol.equals("ATENCION") && !rol.equals("ADMIN")) {
            out.print("{\"error\":\"Acceso denegado\"}");
            return;
        }

        int id = Integer.parseInt(request.getParameter("reservacion_id"));

        CancelacionDAO dao = new CancelacionDAO();
        double resultado = dao.cancelarReservacion(id);

        if (resultado == -1) {
            out.print("{\"error\":\"Reservacion no existe\"}");
        } else if (resultado == -2) {
            out.print("{\"error\":\"No se puede cancelar en este estado\"}");
        } else if (resultado == -3) {
            out.print("{\"error\":\"No se puede cancelar (menos de 7 dias)\"}");
        } else if (resultado < 0) {
            out.print("{\"error\":\"Error en el servidor\"}");
        } else {
            out.print("{\"status\":\"ok\",\"reembolso\":" + resultado + "}");
        }
    }
}
