/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servlet;

import Conexion.DestinoDAO;
import Logica.Destino;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 *
 * @author fernan
 */
@WebServlet("/DestinoServlet")
public class DestinoServlet extends HttpServlet {

    //CORS
    private void configurarCORS(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:4200");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
        response.setHeader("Access-Control-Allow-Credentials", "true");
    }

    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        configurarCORS(response);
        response.setStatus(HttpServletResponse.SC_OK);
    }

    //VALIDACION
    private boolean validarAcceso(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("rol") == null) {
            response.getWriter().print("{\"error\":\"No autorizado\"}");
            return false;
        }

        String rol = (String) session.getAttribute("rol");

        if (!rol.equals("OPERACIONES") && !rol.equals("ADMIN")) {
            response.getWriter().print("{\"error\":\"Acceso denegado\"}");
            return false;
        }

        return true;
    }

    //GET
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        configurarCORS(response);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();

        try {

            DestinoDAO dao = new DestinoDAO();
            List<Destino> lista = dao.obtenerTodos();

            Gson gson = new Gson();
            out.print(gson.toJson(lista));

        } catch (Exception e) {
            e.printStackTrace();
            out.print("{\"error\":\"Error al obtener destinos\"}");
        }
    }

    //POST
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        configurarCORS(response);

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        if (!validarAcceso(request, response)) return;

        String accion = request.getParameter("accion");
        
        if(accion == null){
            out.print("{\"error\":\"Acción requerida\"}");
            return;
        }

        DestinoDAO dao = new DestinoDAO();

        if ("crear".equals(accion)) {

            Destino d = new Destino();
            d.setNombre(request.getParameter("nombre"));
            d.setPais(request.getParameter("pais"));
            d.setDescripcion(request.getParameter("descripcion"));
            d.setClima(request.getParameter("clima"));
            d.setImagen(request.getParameter("imagen_url"));

            if (dao.crearDestino(d)) {
                out.print("{\"status\":\"ok\",\"mensaje\":\"Destino creado\"}");
            } else {
                out.print("{\"error\":\"No se pudo crear\"}");
            }

        } else if (accion.equals("editar")) {
            String idStr = request.getParameter("id");
            
            if (idStr == null || idStr.isEmpty()) {
                out.print("{\"error\":\"ID requerido\"}");
                return;
            }
            
            int id = Integer.parseInt(idStr.trim());
            Destino d = new Destino();
            d.setId(id);
            d.setNombre(request.getParameter("nombre"));
            d.setPais(request.getParameter("pais"));
            d.setDescripcion(request.getParameter("descripcion"));
            d.setClima(request.getParameter("clima"));
            d.setImagen(request.getParameter("imagen_url"));

            if (dao.actualizarDestino(d)) {
                out.print("{\"status\":\"ok\",\"mensaje\":\"Destino actualizado\"}");
            } else {
                out.print("{\"error\":\"No se pudo actualizar\"}");
            }

        } else if (accion.equals("eliminar")) {
            
             String idStr = request.getParameter("id");

            if (idStr == null || idStr.isEmpty()) {
                out.print("{\"error\":\"ID requerido\"}");
                return;
            }

            int id = Integer.parseInt(idStr.trim());

            if (dao.eliminarDestino(id)) {
                out.print("{\"status\":\"ok\",\"mensaje\":\"Destino eliminado\"}");
            } else {
                out.print("{\"error\":\"No se pudo eliminar\"}");
            }
        }
    }

    //PUT
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        configurarCORS(response);

        response.setContentType("application/json");

        if (!validarAcceso(request, response)) return;

        Destino d = new Destino();
        d.setId(Integer.parseInt(request.getParameter("id")));
        d.setNombre(request.getParameter("nombre"));
        d.setPais(request.getParameter("pais"));
        d.setDescripcion(request.getParameter("descripcion"));
        d.setClima(request.getParameter("clima"));
        d.setImagen(request.getParameter("imagen_url"));

        DestinoDAO dao = new DestinoDAO();

        if (dao.actualizarDestino(d)) {
            response.getWriter().print("{\"status\":\"ok\",\"mensaje\":\"Destino actualizado\"}");
        } else {
            response.getWriter().print("{\"error\":\"No se pudo actualizar\"}");
        }
    }

    //DELETE
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        configurarCORS(response);

        response.setContentType("application/json");

        if (!validarAcceso(request, response)) return;

        int id = Integer.parseInt(request.getParameter("id"));

        DestinoDAO dao = new DestinoDAO();

        if (dao.eliminarDestino(id)) {
            response.getWriter().print("{\"status\":\"ok\",\"mensaje\":\"Destino eliminado\"}");
        } else {
            response.getWriter().print("{\"error\":\"No se pudo eliminar\"}");
        }
    }
}
