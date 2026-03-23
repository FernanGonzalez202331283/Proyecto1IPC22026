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
public class DestinoServlet extends HttpServlet {

    private boolean validarAcceso(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("rol") == null) {
            response.getWriter().print("{\"error\":\"No autorizado\"}");
            return false;
        }

        String rol = (String) session.getAttribute("rol");

        if (!rol.equals("OPERACIONES") && !rol.equals("ADMIN")) {
            response.getWriter().print("{\"error\":\"Acceso denegado\"}");
            return false;
        }

        return true;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        if (!validarAcceso(request, response)) {
            return;
        }

        String accion = request.getParameter("accion");

        DestinoDAO dao = new DestinoDAO();

        if (accion == null || accion.equals("crear")) {

            Destino d = new Destino();
            d.setNombre(request.getParameter("nombre"));
            d.setPais(request.getParameter("pais"));
            d.setDescripcion(request.getParameter("descripcion"));
            d.setClima(request.getParameter("clima"));
            d.setImagen(request.getParameter("imagen"));

            if (dao.crearDestino(d)) {
                out.print("{\"status\":\"ok\",\"mensaje\":\"Destino creado\"}");
            } else {
                out.print("{\"error\":\"No se pudo crear\"}");
            }
        } else if (accion.equals("editar")) {

            Destino d = new Destino();
            d.setId(Integer.parseInt(request.getParameter("id")));
            d.setNombre(request.getParameter("nombre"));
            d.setPais(request.getParameter("pais"));
            d.setDescripcion(request.getParameter("descripcion"));
            d.setClima(request.getParameter("clima"));
            d.setImagen(request.getParameter("imagen"));

            if (dao.actualizarDestino(d)) {
                out.print("{\"status\":\"ok\",\"mensaje\":\"Destino actualizado\"}");
            } else {
                out.print("{\"error\":\"No se pudo actualizar\"}");
            }
        } else if (accion.equals("eliminar")) {

            int id = Integer.parseInt(request.getParameter("id"));

            if (dao.eliminarDestino(id)) {
                out.print("{\"status\":\"ok\",\"mensaje\":\"Destino eliminado\"}");
            } else {
                out.print("{\"error\":\"No se pudo eliminar\"}");
            }
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        response.setContentType("application/json");

        if (!validarAcceso(request, response)) {
            return;
        }

        Destino d = new Destino();
        d.setId(Integer.parseInt(request.getParameter("id")));
        d.setNombre(request.getParameter("nombre"));
        d.setPais(request.getParameter("pais"));
        d.setDescripcion(request.getParameter("descripcion"));
        d.setClima(request.getParameter("clima"));
        d.setImagen(request.getParameter("imagen"));

        DestinoDAO dao = new DestinoDAO();

        if (dao.actualizarDestino(d)) {
            response.getWriter().print("{\"status\":\"ok\",\"mensaje\":\"Destino actualizado\"}");
        } else {
            response.getWriter().print("{\"error\":\"No se pudo actualizar\"}");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        response.setContentType("application/json");

        if (!validarAcceso(request, response)) {
            return;
        }

        int id = Integer.parseInt(request.getParameter("id"));

        DestinoDAO dao = new DestinoDAO();

        if (dao.eliminarDestino(id)) {
            response.getWriter().print("{\"status\":\"ok\",\"mensaje\":\"Destino eliminado\"}");
        } else {
            response.getWriter().print("{\"error\":\"No se pudo eliminar\"}");
        }
    }
}
