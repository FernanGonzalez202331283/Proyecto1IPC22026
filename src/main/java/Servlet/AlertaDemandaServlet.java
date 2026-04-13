/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servlet;

import Conexion.AlertaDemanda;
import Conexion.PaqueteDAO;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

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
        response.setCharacterEncoding("UTF-8");

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

        try {
            PaqueteDAO dao = new PaqueteDAO();
            List<AlertaDemanda> lista = dao.obtenerAltaDemanda();

            Gson gson = new Gson();
            out.print(gson.toJson(lista));

        } catch (Exception e) {
            e.printStackTrace();
            out.print("{\"error\":\"Error al obtener alertas\"}");
        }
    }
}
