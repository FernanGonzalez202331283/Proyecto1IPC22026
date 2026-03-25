/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servlet;

import Conexion.ConexionBD;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author fernan
 */

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {

    private void configurarCORS(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:4200");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
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

        String user = request.getParameter("username");
        String pass = request.getParameter("password");


        if (user != null) user = user.trim();
        if (pass != null) pass = pass.trim();

        System.out.println("Usuario recibido: " + user);
        System.out.println("Password recibido: " + pass);

        try (Connection con = ConexionBD.getConnection()) {

            String sql = "SELECT * FROM usuario WHERE username=? AND password=? AND estado=1";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, user);
            ps.setString(2, pass);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String rol = rs.getString("rol");
                int idUsuario = rs.getInt("id");
                System.out.println("Rol encontrado: " + rol);

                HttpSession session = request.getSession();
                session.setAttribute("usuario", user);
                session.setAttribute("rol", rol);
                session.setAttribute("usuario_id", idUsuario);
                

                out.print("{\"status\":\"ok\",\"usuario\":\"" + user + "\",\"rol\":\"" + rol + "\"}");
            } else {
                out.print("{\"status\":\"error\",\"mensaje\":\"Credenciales incorrectas\"}");
            }

        } catch (Exception e) {
            e.printStackTrace();
            out.print("{\"status\":\"error\",\"mensaje\":\"Error en servidor\"}");
        }
    }
}
