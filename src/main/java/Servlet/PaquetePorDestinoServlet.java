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
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;

/**
 *
 * @author fernan
 */
@WebServlet("/PaquetePorDestinoServlet")
public class PaquetePorDestinoServlet extends HttpServlet{
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        int destinoId = Integer.parseInt(request.getParameter("destino_id"));

        ConsultaDAO dao = new ConsultaDAO();
        ResultSet rs = dao.obtenerPorDestino(destinoId);

        out.print("[");

        boolean primero = true;

        try {
            while (rs.next()) {

                if (!primero) out.print(",");
                primero = false;

                out.print("{");
                out.print("\"id\":" + rs.getInt("id") + ",");
                out.print("\"nombre\":\"" + rs.getString("nombre") + "\",");
                out.print("\"precio\":" + rs.getDouble("precio"));
                out.print("}");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        out.print("]");
    }
}
