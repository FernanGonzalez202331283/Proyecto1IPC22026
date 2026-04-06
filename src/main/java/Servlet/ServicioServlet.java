/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servlet;

import Conexion.ServicioDAO;
import Logica.Servicio;
import com.google.gson.Gson;
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
    
@Override
protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws IOException {
   
    configurarCORS(response);
    response.setContentType("application/json");
    PrintWriter out = response.getWriter();

    HttpSession session = request.getSession(false);

    //Validar sesión
    if (session == null || session.getAttribute("rol") == null) {
        out.print("{\"error\":\"No autorizado\"}");
        return;
    }

    String rol = (String) session.getAttribute("rol");

    //Validar rol
    if (!rol.equals("OPERACIONES") && !rol.equals("ADMIN")) {
        out.print("{\"error\":\"Acceso denegado\"}");
        return;
    }

    try {
        StringBuilder sb = new StringBuilder();
        String line;

        try (var reader = request.getReader()) {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }

        Gson gson = new Gson();
        Servicio s = gson.fromJson(sb.toString(), Servicio.class);

        //VALIDACIONES COMPLETAS
        if (s.getNombre() == null || s.getNombre().isEmpty()) {
            out.print("{\"error\":\"Nombre requerido\"}");
            return;
        }

        if (s.getPaqueteId() <= 0) {
            out.print("{\"error\":\"Paquete inválido\"}");
            return;
        }

        if (s.getProveedorId() <= 0) {
            out.print("{\"error\":\"Proveedor inválido\"}");
            return;
        }

        if (s.getCosto() <= 0) {
            out.print("{\"error\":\"Costo inválido\"}");
            return;
        }

        ServicioDAO dao = new ServicioDAO();

        if (dao.crearServicio(s)) {
            out.print("{\"status\":\"ok\",\"mensaje\":\"Servicio agregado\"}");
        } else {
            out.print("{\"error\":\"No se pudo agregar\"}");
        }

    } catch (Exception e) {
        e.printStackTrace();
        out.print("{\"error\":\"Datos inválidos\"}");
    }
}
}
