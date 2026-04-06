/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servlet;

import Conexion.ProveedorDAO;
import Logica.Proveedor;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 *
 * @author fernan
 */
@WebServlet("/ProveedorServlet")
public class ProveedorServlet extends HttpServlet{
    private void configurarCORS(HttpServletResponse response) {
    response.setHeader("Access-Control-Allow-Origin", "http://localhost:4200");
    response.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
    response.setHeader("Access-Control-Allow-Headers", "Content-Type");
    response.setHeader("Access-Control-Allow-Credentials", "true");
}
      @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        configurarCORS(response);
        response.setStatus(HttpServletResponse.SC_OK);
    }
    
    // Validación de sesión y rol
    private boolean validar(HttpServletRequest request, HttpServletResponse response) throws IOException {
        configurarCORS(response);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
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

    configurarCORS(response);
    response.setContentType("application/json");
    PrintWriter out = response.getWriter();

    if (!validar(request, response)) return;

    try {
        //LEER JSON
        StringBuilder sb = new StringBuilder();
        String line;

        try (var reader = request.getReader()) {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }

        Gson gson = new Gson();
        Proveedor p = gson.fromJson(sb.toString(), Proveedor.class);

        if (p == null || p.getAccion() == null) {
            out.print("{\"error\":\"Acción requerida\"}");
            return;
        }

        ProveedorDAO dao = new ProveedorDAO();
        String accion = p.getAccion();

        // ===== CREAR =====
        if ("crear".equals(accion)) {

            if (dao.crearProveedor(p)) {
                out.print("{\"status\":\"ok\",\"mensaje\":\"Proveedor creado\"}");
            } else {
                out.print("{\"error\":\"No se pudo crear\"}");
            }

        // ===== EDITAR =====
        } else if ("editar".equals(accion)) {

            if (p.getId() == 0) {
                out.print("{\"error\":\"ID requerido\"}");
                return;
            }

            if (dao.actualizarProveedor(p)) {
                out.print("{\"status\":\"ok\",\"mensaje\":\"Proveedor actualizado\"}");
            } else {
                out.print("{\"error\":\"No se pudo actualizar\"}");
            }

        // ===== ELIMINAR =====
        } else if ("eliminar".equals(accion)) {

            if (p.getId() == 0) {
                out.print("{\"error\":\"ID requerido\"}");
                return;
            }

            if (dao.eliminarProveedor(p.getId())) {
                out.print("{\"status\":\"ok\",\"mensaje\":\"Proveedor eliminado\"}");
            } else {
                out.print("{\"error\":\"No se pudo eliminar\"}");
            }

        } else {
            out.print("{\"error\":\"Acción inválida\"}");
        }

    } catch (Exception e) {
        e.printStackTrace();
        out.print("{\"error\":\"Datos inválidos\"}");
    }
}
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        configurarCORS(response);
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        ProveedorDAO dao = new ProveedorDAO();
        Gson gson = new Gson();

        try {
            // Ahora dao devuelve List<Proveedor>
            List<Proveedor> lista = dao.listarProveedores();
            out.print(gson.toJson(lista));
        } catch (Exception e) {
            e.printStackTrace();
            out.print("{\"error\":\"Error al listar proveedores\"}");
        }
    }
}
