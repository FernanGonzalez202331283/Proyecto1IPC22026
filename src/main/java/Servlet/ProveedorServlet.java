/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servlet;

import Conexion.ProveedorDAO;
import Logica.Proveedor;
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
@WebServlet("/ProveedorServlet")
public class ProveedorServlet extends HttpServlet{
    
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
        
        Proveedor p = new Proveedor();
        p.setNombre(request.getParameter("nombre"));
        p.setTipo(request.getParameter("tipo"));
        p.setPais(request.getParameter("pais"));
        p.setContacto(request.getParameter("contacto"));

        ProveedorDAO dao = new ProveedorDAO();

        if (dao.crearProveedor(p)) {
            out.print("{\"status\":\"ok\",\"mensaje\":\"Proveedor creado\"}");
        } else {
            out.print("{\"error\":\"No se pudo crear\"}");
        }
    }
}
