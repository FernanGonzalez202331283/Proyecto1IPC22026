/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servlet;

import Conexion.PaqueteDAO;
import Logica.Paquete;
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
@WebServlet("/PaqueteServlet")
public class PaqueteServlet extends HttpServlet{
    
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

        if (!rol.equals("OPERACIONES") && !rol.equals("ADMIN")) {
            out.print("{\"error\":\"Acceso denegado\"}");
            return;
        }

        Paquete p = new Paquete();
    
        p.setNombre(request.getParameter("nombre"));
        p.setDestinoId(Integer.parseInt(request.getParameter("destino_id")));
        p.setDuracion(Integer.parseInt(request.getParameter("duracion")));
        p.setDescripcion(request.getParameter("descripcion"));
        p.setPrecio(Double.parseDouble(request.getParameter("precio")));
        p.setCapacidad(Integer.parseInt(request.getParameter("capacidad")));

        PaqueteDAO dao = new PaqueteDAO();

        if (dao.crearPaquete(p)) {
            out.print("{\"status\":\"ok\",\"mensaje\":\"Paquete creado\"}");
        } else {
            out.print("{\"error\":\"No se pudo crear\"}");
        }
    }
}
