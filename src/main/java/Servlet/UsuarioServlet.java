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
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("rol") == null) {
            out.print("{\"status\":\"error\",\"mensaje\":\"No autorizado\"}");
            return;
        }

        String rol = (String) session.getAttribute("rol");
        
        if (!rol.equals("ADMIN")) {
            out.print("{\"status\":\"error\",\"mensaje\":\"Acceso denegado\"}");
            return;
        }

        // Crear usuario
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
