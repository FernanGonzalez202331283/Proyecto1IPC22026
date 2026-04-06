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
    
    private void configurarCORS(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:4200");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        response.setHeader("Access-Control-Allow-Credentials", "true");
    }

    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        configurarCORS(response);
        response.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        configurarCORS(response);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("rol") == null) {
            out.print("{\"error\":\"No autorizado\"}");
            return;
        }

        String rol = (String) session.getAttribute("rol");

        if (!rol.equals("ATENCION") && !rol.equals("ADMIN")) {
            out.print("{\"error\":\"Acceso denegado\"}");
            return;
        }

        String accion = request.getParameter("accion");
        String dpi = request.getParameter("dpi");
        String nombre = request.getParameter("nombre");
        String fecha = request.getParameter("fecha");
        String telefono = request.getParameter("telefono");
        String correo = request.getParameter("correo");
        String nacionalidad = request.getParameter("nacionalidad");

        if (accion != null) accion = accion.trim();
        if (dpi != null) dpi = dpi.trim();
        if (nombre != null) nombre = nombre.trim();
        if (fecha != null) fecha = fecha.trim();
        if (telefono != null) telefono = telefono.trim();
        if (correo != null) correo = correo.trim();
        if (nacionalidad != null) nacionalidad = nacionalidad.trim();

        if (dpi == null || dpi.isEmpty()) {
            out.print("{\"error\":\"El DPI es obligatorio\"}");
            return;
        }

        Cliente c = new Cliente();
        c.setDpi(dpi);
        c.setNombre(nombre);
        c.setFechaNacimiento(fecha);
        c.setTelefono(telefono);
        c.setCorreo(correo);
        c.setNacionalidad(nacionalidad);

        ClienteDAO dao = new ClienteDAO();

        if ("actualizar".equalsIgnoreCase(accion)) {
            if (nombre == null || nombre.isEmpty()) {
                out.print("{\"error\":\"Datos incompletos para actualizar\"}");
                return;
            }

            if (dao.actualizarCliente(c)) {
                out.print("{\"status\":\"ok\",\"mensaje\":\"Cliente actualizado\"}");
            } else {
                out.print("{\"error\":\"No se pudo actualizar\"}");
            }

        } else {
            if (nombre == null || nombre.isEmpty()) {
                out.print("{\"error\":\"Datos incompletos para crear\"}");
                return;
            }

            if (dao.crearCliente(c)) {
                out.print("{\"status\":\"ok\",\"mensaje\":\"Cliente creado\"}");
            } else {
                out.print("{\"error\":\"No se pudo crear\"}");
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        configurarCORS(response);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

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

        if (dpi != null) dpi = dpi.trim();

        if (dpi == null || dpi.isEmpty()) {
            out.print("{\"error\":\"DPI requerido\"}");
            return;
        }

        ClienteDAO dao = new ClienteDAO();
        Cliente c = dao.buscarPorDpi(dpi);

        if (c != null) {
            out.print("{");
            out.print("\"dpi\":\"" + c.getDpi() + "\",");
            out.print("\"nombre\":\"" + c.getNombre() + "\",");
            out.print("\"fecha\":\"" + c.getFechaNacimiento() + "\",");
            out.print("\"telefono\":\"" + c.getTelefono() + "\",");
            out.print("\"correo\":\"" + c.getCorreo() + "\",");
            out.print("\"nacionalidad\":\"" + c.getNacionalidad() + "\"");
            out.print("}");
        } else {
            out.print("{\"mensaje\":\"Cliente no encontrado\"}");
        }
    }
}
