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

                List<Map<String, Object>> lista = dao.reporteVentas(inicio, fin);
                response.getWriter().write(gson.toJson(lista));
            }else if ("cancelaciones".equals(tipo)) {

                List<Map<String, Object>> lista = dao.reporteCancelaciones(inicio, fin);
                response.getWriter().write(gson.toJson(lista));
            }else{
                response.getWriter().write("[]");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
