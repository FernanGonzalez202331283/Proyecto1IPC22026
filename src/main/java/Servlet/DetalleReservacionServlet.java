
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
import java.io.IOException;
import java.io.PrintWriter;

/**
 *
 * @author fernan
 */
@WebServlet("/DetalleReservacionServlet")
public class DetalleReservacionServlet  extends HttpServlet{
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
       response.setHeader("Access-Control-Allow-Origin", "http://localhost:4200");
    response.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
    response.setHeader("Access-Control-Allow-Headers", "Content-Type");
    response.setHeader("Access-Control-Allow-Credentials", "true");

    response.setContentType("application/json");
    PrintWriter out = response.getWriter();

        String idStr = request.getParameter("id");
        if (idStr == null) {
            out.print("{\"error\":\"ID no proporcionado\"}");
            return;
        }

        int id = Integer.parseInt(idStr);
        ReservacionDAO dao = new ReservacionDAO();
        Reservacion r = dao.obtenerPorId(id);

        if (r == null) {
            out.print("{\"error\":\"Reservacion no encontrada\"}");
            return;
        }

        //DEVOLVEMOS JSON CON INFORMACION 
        out.print("{"
            + "\"id\":" + r.getId() + ","
            + "\"paquete\":\"" + r.getPaquete() + "\","
            + "\"destino\":\"" + r.getDestino() + "\","
            + "\"fechaViaje\":\"" + r.getFechaViaje() + "\","
            + "\"personas\":" + r.getCandidadPersonas() + ","
            + "\"total\":" + r.getCosotTotal() + ","
            + "\"estado\":\"" + r.getEstado() + "\""
            + "}");
    }
}
