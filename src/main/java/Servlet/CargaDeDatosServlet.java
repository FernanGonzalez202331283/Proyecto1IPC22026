/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servlet;

import CargaDeDatos.CargaDatos;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author fernan
 */
@WebServlet("/carga")
@MultipartConfig
public class CargaDeDatosServlet extends HttpServlet{
      private void configurarCORS(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:4200");
        response.setHeader("Access-Control-Allow-Methods", "POST, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
    }

    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        configurarCORS(response);
        response.setStatus(HttpServletResponse.SC_OK);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        configurarCORS(response);
        Part archivo = request.getPart("archivo");

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(archivo.getInputStream(), "UTF-8")
        );

        String linea;
        CargaDatos cargador = new CargaDatos();

        while ((linea = reader.readLine()) != null) {
            cargador.procesarLinea(linea);
        }

       response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        response.getWriter().write("{\"mensaje\":\"Carga completada\"}");
    }   
}
