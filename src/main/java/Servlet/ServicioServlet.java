/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servlet;

import Conexion.ServicioDAO;
import Logica.Servicio;
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
@WebServlet("/ServicioServlet")
public class ServicioServlet extends HttpServlet{
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

        Servicio s = new Servicio();

        s.setPaqueteId(Integer.parseInt(request.getParameter("paquete_id")));
        s.setProveedorId(Integer.parseInt(request.getParameter("proveedor_id")));
        s.setNombre(request.getParameter("nombre"));
        s.setCosto(Double.parseDouble(request.getParameter("costo")));

        ServicioDAO dao = new ServicioDAO();
        
        if (dao.crearServicio(s)) {
            out.print("{\"status\":\"ok\",\"mensaje\":\"Servicio agregado\"}");
        } else {
            out.print("{\"error\":\"No se pudo agregar\"}");
        }
    }
}
