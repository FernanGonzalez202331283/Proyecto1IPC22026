/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servlet;

import Conexion.UsuarioDAO;
import Logica.Usuario;
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
@WebServlet("/UsuarioServlet")
public class UsuarioServlet extends HttpServlet {

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
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        UsuarioDAO dao = new UsuarioDAO();
        var lista = dao.listarUsuarios();
        Gson gson = new Gson();
        out.print(gson.toJson(lista));
    }

    // CREAR USUARIO
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        configurarCORS(response);
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("rol") == null) {
            out.print("{\"status\":\"error\",\"mensaje\":\"No autorizado\"}");
            return;
        }

        String rol = (String) session.getAttribute("rol");

        if (!"ADMIN".equals(rol)) {
            out.print("{\"status\":\"error\",\"mensaje\":\"Acceso denegado\"}");
            return;
        }

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String rolNuevo = request.getParameter("rol");

        //VALIDACIONES
        if (username == null || username.trim().isEmpty()) {
            out.print("{\"status\":\"error\",\"mensaje\":\"Username requerido\"}");
            return;
        }

        if (password == null || password.length() < 6) {
            out.print("{\"status\":\"error\",\"mensaje\":\"Password minimo 6 caracteres\"}");
            return;
        }

        if (rolNuevo == null || (!rolNuevo.equals("ADMIN")
                && !rolNuevo.equals("OPERACIONES")
                && !rolNuevo.equals("ATENCION"))) {
            out.print("{\"status\":\"error\",\"mensaje\":\"Rol invalido\"}");
            return;
        }

        UsuarioDAO dao = new UsuarioDAO();

        if (dao.existeUsuario(username)) {
            out.print("{\"status\":\"error\",\"mensaje\":\"Usuario ya existe\"}");
            return;
        }

        Usuario u = new Usuario();
        u.setUsername(username);
        u.setPassword(password);
        u.setRol(rolNuevo);

        if (dao.crearUsuario(u)) {
            out.print("{\"status\":\"ok\",\"mensaje\":\"Usuario creado\"}");
        } else {
            out.print("{\"status\":\"error\",\"mensaje\":\"No se pudo crear\"}");
        }
    }

}
