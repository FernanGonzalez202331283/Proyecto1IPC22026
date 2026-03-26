/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servlet;

import Conexion.ReservacionDAO;
import Logica.Reservacion;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedReader;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;

/**
 *
 * @author fernan
 */
@WebServlet("/ReservacionServlet")
public class ReservacionServlet extends HttpServlet {

    
    private void configurarCORS(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:4200");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        response.setHeader("Access-Control-Allow-Credentials", "true");
    }

    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        configurarCORS(response);
        request.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        configurarCORS(response);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession(false);

        //VALIDACIÓN DE SESIÓN
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
            System.out.println("JSON recibido: " + json);

            if (json == null || json.isEmpty()) {
                out.print("{\"error\":\"JSON vacío\"}");
                return;
            }

            //PARSEAR JSON
            Gson gson = new Gson();
            JsonObject data = gson.fromJson(json, JsonObject.class);

            String fecha = data.get("fecha_viaje").getAsString();
            int paqueteId = data.get("paquete_id").getAsInt();
            int cantidad = data.get("cantidad").getAsInt();
            double costo = data.get("costo").getAsDouble();

            JsonArray dpisJson = data.getAsJsonArray("dpis");
            String[] dpis = new String[dpisJson.size()];

            for (int i = 0; i < dpisJson.size(); i++) {
                dpis[i] = dpisJson.get(i).getAsString();
            }

            //VALIDACIONES
            if (fecha == null || fecha.isEmpty()) {
                System.out.println("DEBUG FECHA: " + fecha);
                out.print("{\"error\":\"Fecha requerida\"}");
                return;
            }

            if (cantidad <= 0) {
                out.print("{\"error\":\"Cantidad inválida\"}");
                return;
            }

            if (dpis == null || dpis.length == 0) {
                out.print("{\"error\":\"Debe ingresar pasajeros\"}");
                return;
            }

            if (dpis.length != cantidad) {
                out.print("{\"error\":\"Cantidad no coincide con pasajeros\"}");
                return;
            }

            int usuarioId = (int) session.getAttribute("usuario_id");

            //CREAR OBJETO
            Reservacion r = new Reservacion();
            r.setFechaViaje(fecha);
            r.setPaqueteId(paqueteId);
            r.setCandidadPersonas(cantidad);
            r.setCosotTotal(costo);
            r.setIdUsurio(usuarioId);
            r.setDpis(dpis);

            ReservacionDAO dao = new ReservacionDAO();

            if (dao.crearReservacion(r)) {
                out.print("{\"status\":\"ok\",\"mensaje\":\"Reservacion creada\"}");
            } else {
                out.print("{\"error\":\"No se pudo crear\"}");
            }

        } catch (Exception e) {
            e.printStackTrace();
            out.print("{\"error\":\"Error interno\"}");
        }
    }
    
   @Override
protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws IOException {

    configurarCORS(response);

    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");

    PrintWriter out = response.getWriter();

    try {

        String accion = request.getParameter("accion");

        ReservacionDAO dao = new ReservacionDAO();
        Gson gson = new Gson();

        // ================= HISTORIAL POR CLIENTE (SIN SESIÓN) =================
        if ("historialCliente".equals(accion)) {

            String dpi = request.getParameter("dpi");

            if (dpi == null || dpi.isEmpty()) {
                out.print("{\"error\":\"DPI requerido\"}");
                return;
            }

            var lista = dao.obtenerPorCliente(dpi);
            out.print(gson.toJson(lista));
            return;
        }

        // ================= DISPONIBLES (SIN SESIÓN) =================
        if ("disponibles".equals(accion)) {

        String fecha = request.getParameter("fecha");
        String destino = request.getParameter("destino");

        if (fecha == null || fecha.isEmpty()) {
            out.print("{\"error\":\"Fecha requerida\"}");
            return;
        }

        var lista = dao.obtenerPorFecha(fecha);

        // si quieres con destino:
        // dao.obtenerPorFechaDestino(fecha, Integer.parseInt(destino));

        out.print(gson.toJson(lista));
        return;
    }

        // ================= A PARTIR DE AQUÍ REQUIERE SESIÓN =================
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("usuario_id") == null) {
            out.print("{\"error\":\"No autorizado\"}");
            return;
        }

        // ================= CONSULTAR POR ID =================
        String idStr = request.getParameter("id");

        if (idStr == null) {
            out.print("{\"error\":\"ID requerido\"}");
            return;
        }

        int id = Integer.parseInt(idStr);

        Reservacion r = dao.obtenerPorId(id);

        if (r == null) {
            out.print("{\"error\":\"No encontrada\"}");
            return;
        }

        out.print(gson.toJson(r));

    } catch (Exception e) {
        e.printStackTrace();
        out.print("{\"error\":\"Error interno\"}");
    }
}
}
