/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servlet;

import Conexion.ConsultaDAO;
import Conexion.PagoDAO;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
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
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession(false);

        if (session == null) {
            out.print("{\"error\":\"No autorizado\"}");
            return;
        }

        try {
            int id = Integer.parseInt(request.getParameter("id"));

            PagoDAO dao = new PagoDAO();
            ArrayList<JsonObject> lista = dao.pagosPorReservacion(id);

            Gson gson = new Gson();
            String json = gson.toJson(lista);

            out.print(json);

        } catch (Exception e) {
            e.printStackTrace();
            out.print("{\"error\":\"Error interno\"}");
        }
    }
}
