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
@WebServlet("/DetallePaqueteServlet")
public class DetallePaqueteServlet extends HttpServlet{
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        PrintWriter out = response.getWriter();
        response.setContentType("application/json");

        int id = Integer.parseInt(request.getParameter("id"));

        ConsultaDAO dao = new ConsultaDAO();
        ResultSet rs = dao.obtenerDetalle(id);

        double costoTotal = 0;
        double precio = 0;

        out.print("{\"servicios\":[");

        boolean primero = true;

        try {
            while (rs.next()) {
                if (!primero) out.print(",");
                primero = false;

                precio = rs.getDouble("precio");
                double costo = rs.getDouble("costo");
                costoTotal += costo;

                out.print("{\"servicio\":\"" + rs.getString("servicio") +
                          "\",\"costo\":" + costo +
                          ",\"proveedor\":\"" + rs.getString("proveedor") + "\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        double ganancia = precio - costoTotal;

        out.print("],\"precio\":" + precio +
                  ",\"costo_total\":" + costoTotal +
                  ",\"ganancia\":" + ganancia + "}");
    }
}
