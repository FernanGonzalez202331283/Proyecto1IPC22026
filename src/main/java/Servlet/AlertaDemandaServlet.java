/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servlet;

import Conexion.PaqueteDAO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;

/**
 *
 * @author fernan
 */
@WebServlet("/AlertaDemandaServlet")
public class AlertaDemandaServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("rol") == null) {
            out.print("{\"error\":\"No autorizado\"}");
            return;
        }

        String rol = (String) session.getAttribute("rol");

        if (!rol.equals("ADMIN") && !rol.equals("OPERACIONES")) {
            out.print("{\"error\":\"Acceso denegado\"}");
            return;
        }

        PaqueteDAO dao = new PaqueteDAO();
        ResultSet rs = dao.obtenerAltaDemanda();

        out.print("[");

        boolean primero = true;

        try {
            while (rs.next()) {

                if (!primero) out.print(",");
                primero = false;

                out.print("{");
                out.print("\"paquete\":\"" + rs.getString("nombre") + "\",");
                out.print("\"ocupados\":" + rs.getInt("ocupados") + ",");
                out.print("\"capacidad\":" + rs.getInt("capacidad") + ",");
                out.print("\"estado\":\"ALTA DEMANDA\"");
                out.print("}");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        out.print("]");
    }
}
