/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servlet;

import Conexion.PagoDAO;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

/**
 *
 * @author fernan
 */
@WebServlet("/PagoServlet")
public class PagoServlet extends HttpServlet{
   
    private final Gson gson = new Gson();

    //CORS
    private void configurarCORS(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:4200");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        response.setHeader("Access-Control-Allow-Credentials", "true");
    }

    //PREFLIGHT (CLAVE)
    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        configurarCORS(response);
        response.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        configurarCORS(response); //ESTO FALTABA

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("rol") == null) {
            out.print("{\"error\":\"No autorizado\"}");
            return;
        }

        String rol = (String) session.getAttribute("rol");

        if (!rol.equals("ATENCION")) {
            out.print("{\"error\":\"Acceso denegado\"}");
            return;
        }

        try {
            //LEER JSON
            BufferedReader reader = request.getReader();
            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            String json = sb.toString();
            System.out.println("JSON Pago: " + json);

            JsonObject data = gson.fromJson(json, JsonObject.class);

            int reservacionId = data.get("reservacion_id").getAsInt();
            double monto = data.get("monto").getAsDouble();
            String metodo = data.get("metodo").getAsString();

            PagoDAO dao = new PagoDAO();

            if (dao.registrarPago(reservacionId, monto, metodo)) {
                out.print("{\"status\":\"ok\",\"mensaje\":\"Pago registrado\"}");
            } else {
                out.print("{\"error\":\"No se pudo registrar\"}");
            }

        } catch (Exception e) {
            e.printStackTrace();
            out.print("{\"error\":\"Error interno\"}");
        }
    }
}
