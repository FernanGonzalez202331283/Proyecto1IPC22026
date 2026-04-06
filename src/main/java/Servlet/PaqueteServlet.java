/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servlet;

import Conexion.PaqueteDAO;
import Logica.Paquete;
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
@WebServlet("/PaqueteServlet")
public class PaqueteServlet extends HttpServlet{
    // cors
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

    // ================= VALIDACIÓN =================
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

    // ================= POST (JSON) =================
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        configurarCORS(response);
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        if (!validarAcceso(request, response)) return;

        try {
            // LEER JSON
            StringBuilder sb = new StringBuilder();
            String line;

            try (var reader = request.getReader()) {
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
            }

            Gson gson = new Gson();
            Paquete p = gson.fromJson(sb.toString(), Paquete.class);

            if (p == null || p.getAccion() == null) {
                out.print("{\"error\":\"Acción requerida\"}");
                return;
            }

            PaqueteDAO dao = new PaqueteDAO();
            String accion = p.getAccion();

            // ================= ACCIONES =================

            if ("crear".equals(accion)) {

                if (p.getNombre() == null || p.getNombre().isEmpty()) {
                    out.print("{\"error\":\"Nombre requerido\"}");
                    return;
                }

                if (dao.crearPaquete(p)) {
                    out.print("{\"status\":\"ok\",\"mensaje\":\"Paquete creado\"}");
                } else {
                    out.print("{\"error\":\"No se pudo crear\"}");
                }

            } else if ("editar".equals(accion)) {

                if (p.getId() == 0) {
                    out.print("{\"error\":\"ID requerido\"}");
                    return;
                }

                if (dao.actualizarPaquete(p)) {
                    out.print("{\"status\":\"ok\",\"mensaje\":\"Paquete actualizado\"}");
                } else {
                    out.print("{\"error\":\"No se pudo actualizar\"}");
                }

            } else if ("activar".equals(accion) || "desactivar".equals(accion)) {

                if (p.getId() == 0) {
                    out.print("{\"error\":\"ID requerido\"}");
                    return;
                }

                String estado = accion.equals("activar") ? "activo" : "inactivo";

                if (dao.cambiarEstadoPaquete(p.getId(), estado)) {
                    out.print("{\"status\":\"ok\",\"mensaje\":\"Paquete " + estado + "\"}");
                } else {
                    out.print("{\"error\":\"No se pudo cambiar estado\"}");
                }

            } else {
                out.print("{\"error\":\"Acción inválida\"}");
            }

        } catch (Exception e) {
            e.printStackTrace();
            out.print("{\"error\":\"Datos inválidos\"}");
        }
    }

    // ================= GET =================
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        configurarCORS(response);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();

        try {
            PaqueteDAO dao = new PaqueteDAO();
            List<Paquete> lista = dao.listarPaquetes();

            Gson gson = new Gson();
            out.print(gson.toJson(lista));

        } catch (Exception e) {
            e.printStackTrace();
            out.print("{\"error\":\"Error al obtener paquetes\"}");
        }
    }
}
