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
public class LoginServlet extends HttpServlet{
     @Override
     protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
response.setHeader("Access-Control-Allow-Origin", "*");
    response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS");
    response.setHeader("Access-Control-Allow-Headers", "Content-Type");

    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");

    String user = request.getParameter("username");
    String pass = request.getParameter("password");

    PrintWriter out = response.getWriter();

    try (Connection con = ConexionBD.getConnection()) {

        String sql = "SELECT * FROM usuario WHERE username=? AND password=? AND estado=TRUE";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, user);
        ps.setString(2, pass);

        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            String rol = rs.getString("rol");
            
                HttpSession session = request.getSession();
                session.setAttribute("usuario", user);
                session.setAttribute("rol", rol);
            // 🔥 YA NO usamos redirect
            out.print("{");
            out.print("\"status\":\"ok\",");
            out.print("\"usuario\":\"" + user + "\",");
            out.print("\"rol\":\"" + rol + "\"");
            out.print("}");

        } else {
            out.print("{\"status\":\"error\",\"mensaje\":\"Credenciales incorrectas\"}");
        }

    } catch (Exception e) {
        e.printStackTrace();

        out.print("{\"status\":\"error\",\"mensaje\":\"Error en el servidor\"}");
    }
    }
}
