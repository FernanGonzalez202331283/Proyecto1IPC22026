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
     private boolean validar(HttpServletRequest request, HttpServletResponse response) throws IOException {
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

        if (!validar(request, response)) return;

        String accion = request.getParameter("accion");

        ProveedorDAO dao = new ProveedorDAO();
        
        if (accion == null || accion.equals("crear")) {

            Proveedor p = new Proveedor();
            p.setNombre(request.getParameter("nombre"));
            p.setTipo(request.getParameter("tipo"));
            p.setPais(request.getParameter("pais"));
            p.setContacto(request.getParameter("contacto"));

            if (dao.crearProveedor(p)) {
                out.print("{\"status\":\"ok\",\"mensaje\":\"Proveedor creado\"}");
            } else {
                out.print("{\"error\":\"No se pudo crear\"}");
            }
        }
        else if (accion.equals("editar")) {

            Proveedor p = new Proveedor();
            p.setId(Integer.parseInt(request.getParameter("id")));
            p.setNombre(request.getParameter("nombre"));
            p.setTipo(request.getParameter("tipo"));
            p.setPais(request.getParameter("pais"));
            p.setContacto(request.getParameter("contacto"));

            if (dao.actualizarProveedor(p)) {
                out.print("{\"status\":\"ok\",\"mensaje\":\"Proveedor actualizado\"}");
            } else {
                out.print("{\"error\":\"No se pudo actualizar\"}");
            }
        }
        else if (accion.equals("eliminar")) {

            int id = Integer.parseInt(request.getParameter("id"));

            if (dao.eliminarProveedor(id)) {
                out.print("{\"status\":\"ok\",\"mensaje\":\"Proveedor eliminado\"}");
            } else {
                out.print("{\"error\":\"No se pudo eliminar\"}");
            }
        }
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        ProveedorDAO dao = new ProveedorDAO();

        try {
            var rs = dao.listarProveedores();

            out.print("[");

            boolean primero = true;

            while (rs.next()) {

                if (!primero) out.print(",");
                primero = false;

                out.print("{");
                out.print("\"id\":" + rs.getInt("id") + ",");
                out.print("\"nombre\":\"" + rs.getString("nombre") + "\",");
                out.print("\"tipo\":\"" + rs.getString("tipo") + "\",");
                out.print("\"pais\":\"" + rs.getString("pais") + "\",");
                out.print("\"contacto\":\"" + rs.getString("contacto") + "\"");
                out.print("}");
            }

            out.print("]");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
