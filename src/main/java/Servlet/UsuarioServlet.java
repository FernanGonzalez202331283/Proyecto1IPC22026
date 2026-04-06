/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servlet;

import Conexion.UsuarioDAO;
import Logica.Usuario;
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
@WebServlet("/UsuarioServlet")
public class UsuarioServlet extends HttpServlet{
   
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

    // LISTAR USUARIOS
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        configurarCORS(response);
        response.setContentType("application/json");

        PrintWriter out = response.getWriter();

        UsuarioDAO dao = new UsuarioDAO();
        var lista = dao.listarUsuarios();

        String json = "[";

        for (int i = 0; i < lista.size(); i++) {
            Usuario u = lista.get(i);

            json += "{"
                    + "\"id\":" + u.getId() + ","
                    + "\"username\":\"" + u.getUsername() + "\","
                    + "\"rol\":\"" + u.getRol() + "\""
                    + "}";

            if (i < lista.size() - 1) json += ",";
        }

        json += "]";

        out.print(json);
    }

    // CREAR USUARIO
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        configurarCORS(response);
        response.setContentType("application/json");

        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("rol") == null) {
            out.print("{\"status\":\"error\",\"mensaje\":\"No autorizado\"}");
            return;
        }

        String rol = ((String) session.getAttribute("rol")).toLowerCase();

        // IMPORTANTE PARA ADMIN
        if (!rol.equals("admin")) {
            out.print("{\"status\":\"error\",\"mensaje\":\"Acceso denegado\"}");
            return;
        }

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String rolNuevo = request.getParameter("rol");

        Usuario u = new Usuario();
        u.setUsername(username);
        u.setPassword(password);
        u.setRol(rolNuevo);

        UsuarioDAO dao = new UsuarioDAO();

        if (dao.crearUsuario(u)) {
            out.print("{\"status\":\"ok\",\"mensaje\":\"Usuario creado\"}");
        } else {
            out.print("{\"status\":\"error\",\"mensaje\":\"No se pudo crear\"}");
        }
    }
}
