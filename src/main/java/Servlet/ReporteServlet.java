/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servlet;

import Conexion.ReporteDAO;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 *
 * @author fernan
 */
@WebServlet("/ReporteServlet")
public class ReporteServlet extends HttpServlet{
    @Override
protected void doOptions(HttpServletRequest request, HttpServletResponse response)
        throws IOException {

    response.setHeader("Access-Control-Allow-Origin", "http://localhost:4200");
    response.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
    response.setHeader("Access-Control-Allow-Headers", "Content-Type");
    response.setHeader("Access-Control-Allow-Credentials", "true");
    response.setStatus(HttpServletResponse.SC_OK);
}
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:4200");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        String tipo = request.getParameter("tipo");
        String inicio = request.getParameter("inicio");
        String fin = request.getParameter("fin");

        System.out.println("TIPO RECIBIDO: " + tipo);
        ReporteDAO dao = new ReporteDAO();
        Gson gson = new Gson();
        try {

            if ("ventas".equals(tipo)) {
                response.setContentType("application/json");
                List<Map<String, Object>> lista = dao.reporteVentas(inicio, fin);
                response.getWriter().write(gson.toJson(lista));
            }else if ("cancelaciones".equals(tipo)) {
                response.setContentType("application/json");
                List<Map<String, Object>> lista = dao.reporteCancelaciones(inicio, fin);
                response.getWriter().write(gson.toJson(lista));
            }else if ("ganancias".equals(tipo)) {
                response.setContentType("application/json");
                Map<String, Object> datos = dao.reporteGanancias(inicio, fin);
                response.setContentType("application/json");
                response.getWriter().write(new Gson().toJson(datos));
            }else if ("mejor-agente".equalsIgnoreCase(tipo)
                    || "topAgenteVentas".equalsIgnoreCase(tipo)
                    || "mejorAgente".equalsIgnoreCase(tipo)) {

                Map<String, Object> datos = dao.reporteMejorAgente(inicio, fin);

                response.setContentType("application/json");
                response.getWriter().write(gson.toJson(datos));
            }else if ("topAgenteGanancias".equalsIgnoreCase(tipo)) {
                Map<String, Object> datos = dao.topAgenteGanancias(inicio, fin);

                response.setContentType("application/json");
                response.getWriter().write(gson.toJson(datos));
            }else if ("paquete-top".equalsIgnoreCase(tipo) || "paqueteMas".equalsIgnoreCase(tipo)) {
                Map<String, Object> datos = dao.reportePaqueteMasVendido(inicio, fin);

                response.setContentType("application/json");
                response.getWriter().write(gson.toJson(datos));
            }
            else if ("paqueteMin".equalsIgnoreCase(tipo) || "paqueteMenos".equalsIgnoreCase(tipo)) {
                Map<String, Object> datos = dao.reportePaqueteMenosVendido(inicio, fin);
                System.out.println("DEBUG: Entró al reporte de menos vendido correctamente");
                response.setContentType("application/json");
                response.getWriter().write(gson.toJson(datos));
            }
            else if (tipo != null && (tipo.equalsIgnoreCase("ocupacion-destino") || tipo.toLowerCase().contains("ocupacion"))) {
                List<Map<String, Object>> resultado = dao.reporteOcupacionDestinos(inicio, fin);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(new Gson().toJson(resultado));
            }
            else{
                System.out.println("ADVERTENCIA: No se reconoció el tipo [" + tipo + "]");
                response.getWriter().write("{}");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
