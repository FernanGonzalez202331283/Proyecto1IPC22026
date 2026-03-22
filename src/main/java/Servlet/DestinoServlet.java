/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servlet;

import Conexion.DestinoDAO;
import Logica.Destino;
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
@WebServlet("/DestinoServlet")
public class DestinoServlet extends HttpServlet{
    
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

        Destino d = new Destino();
        d.setNombre(request.getParameter("nombre"));
        d.setPais(request.getParameter("pais"));
        d.setDescripcion(request.getParameter("descripcion"));
        d.setClima(request.getParameter("clima"));
        d.setImagen(request.getParameter("imagen"));

        DestinoDAO dao = new DestinoDAO();

        if (dao.crearDestino(d)) {
            out.print("{\"status\":\"ok\",\"mensaje\":\"Destino creado\"}");
        } else {
            out.print("{\"error\":\"No se pudo crear\"}");
        }
    }
}
