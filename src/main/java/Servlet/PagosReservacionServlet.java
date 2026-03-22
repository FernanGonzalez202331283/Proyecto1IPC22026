/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servlet;

import Conexion.ConsultaDAO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 *
 * @author fernan
 */
@WebServlet("/PagosPorReservacion")
public class PagosReservacionServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession(false);

        if (session == null) {
            out.print("{\"error\":\"No autorizado\"}");
            return;
        }

        int id = Integer.parseInt(request.getParameter("id"));

        ConsultaDAO dao = new ConsultaDAO();
        ArrayList<String> lista = dao.pagosPorReservacion(id);

        out.print("[");
        for (int i = 0; i < lista.size(); i++) {
            out.print(lista.get(i));
            if (i < lista.size() - 1) out.print(",");
        }
        out.print("]");
    }
}
