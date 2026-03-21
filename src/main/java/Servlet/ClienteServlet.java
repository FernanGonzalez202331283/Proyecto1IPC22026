/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servlet;

import Conexion.ClienteDAO;
import Logica.Cliente;
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
@WebServlet("/ClienteServlet")
public class ClienteServlet extends HttpServlet{
    
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
        
        if (!rol.equals("ATENCION")) {
            out.print("{\"error\":\"Acceso denegado\"}");
            return;
        }
        
        String dpi = request.getParameter("dpi");
        String nombre = request.getParameter("nombre");
        String fecha = request.getParameter("fecha");
        String telefono = request.getParameter("telefono");
        String correo = request.getParameter("correo");
        String nacionalidad = request.getParameter("nacionalidad");

        Cliente c = new Cliente();
        c.setDpi(dpi);
        c.setNombre(nombre);
        c.setFechaNacimiento(fecha);
        c.setTelefono(telefono);
        c.setCorreo(correo);
        c.setNacionalidad(nacionalidad);

        ClienteDAO dao = new ClienteDAO();

        if (dao.crearCliente(c)) {
            out.print("{\"status\":\"ok\",\"mensaje\":\"Cliente creado\"}");
        } else {
            out.print("{\"error\":\"No se pudo crear\"}");
        }
    }
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

    if (!rol.equals("ATENCION")) {
        out.print("{\"error\":\"Acceso denegado\"}");
        return;
    }

    String dpi = request.getParameter("dpi");

    ClienteDAO dao = new ClienteDAO();
    Cliente c = dao.buscarPorDpi(dpi);

    if (c != null) {
        out.print("{");
        out.print("\"dpi\":\"" + c.getDpi() + "\",");
        out.print("\"nombre\":\"" + c.getNombre() + "\",");
        out.print("\"telefono\":\"" + c.getTelefono() + "\"");
        out.print("}");
    } else {
        out.print("{\"mensaje\":\"Cliente no encontrado\"}");
    }
}
}
