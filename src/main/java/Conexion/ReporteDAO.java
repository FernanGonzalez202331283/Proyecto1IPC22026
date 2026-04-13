/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Conexion;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author fernan
 */
public class ReporteDAO {

    public List<Map<String, Object>> reporteVentas(String inicio, String fin) {

        List<Map<String, Object>> lista = new ArrayList<>();

        String sql = "SELECT "
                + "p.nombre AS paquete, "
                + "r.cantidad_personas AS pasajeros, "
                + "u.username AS agente, "
                + "r.costo_total AS total "
                + "FROM reservacion r "
                + "JOIN paquete p ON r.paquete_id = p.id "
                + "JOIN usuario u ON r.usuario_id = u.id "
                + "WHERE r.estado = 'CONFIRMADA' "
                + "AND DATE(r.fecha_creacion) BETWEEN ? AND ?";

        try (Connection con = ConexionBD.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, inicio);
            ps.setString(2, fin);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Map<String, Object> fila = new HashMap<>();

                fila.put("paquete", rs.getString("paquete"));
                fila.put("pasajeros", rs.getInt("pasajeros"));
                fila.put("agente", rs.getString("agente"));
                fila.put("total", rs.getDouble("total"));

                lista.add(fila);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    public Map<String, Object> topAgenteGanancias(String inicio, String fin) {

        Map<String, Object> resultado = new HashMap<>();

        String sql = "SELECT u.username AS agente, SUM(r.costo_total) AS total "
                + "FROM reservacion r "
                + "JOIN usuario u ON r.usuario_id = u.id "
                + "WHERE r.estado IN ('CONFIRMADA','COMPLETADA') "
                + "AND DATE(r.fecha_creacion) BETWEEN ? AND ? "
                + "GROUP BY u.username "
                + "ORDER BY total DESC LIMIT 1";

        try (Connection con = ConexionBD.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, inicio);
            ps.setString(2, fin);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                resultado.put("agente", rs.getString("agente"));
                resultado.put("total", rs.getDouble("total"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultado;
    }

    public List<Map<String, Object>> reporteCancelaciones(String inicio, String fin) {

        List<Map<String, Object>> lista = new ArrayList<>();

        try (Connection con = ConexionBD.getConnection()) {

            String sql = """
            SELECT 
                c.reservacion_id,
                c.fecha,
                c.monto_reembolso,
                c.perdida
            FROM cancelacion c
            WHERE DATE(c.fecha) BETWEEN ? AND ?
        """;

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, inicio);
            ps.setString(2, fin);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Map<String, Object> map = new HashMap<>();

                map.put("reservacion", rs.getInt("reservacion_id"));
                map.put("fecha", rs.getDate("fecha"));
                map.put("reembolso", rs.getDouble("monto_reembolso"));
                map.put("perdida", rs.getDouble("perdida"));

                lista.add(map);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    public Map<String, Object> reporteGanancias(String inicio, String fin) {

        Map<String, Object> datos = new HashMap<>();

        String sql = """
        SELECT 
            IFNULL((SELECT SUM(p.monto)
                    FROM pago p
                    WHERE DATE(p.fecha) BETWEEN ? AND ?),0) AS bruto,

            IFNULL((SELECT SUM(c.monto_reembolso)
                    FROM cancelacion c
                    WHERE DATE(c.fecha) BETWEEN ? AND ?),0) AS reembolsos
    """;

        try (Connection con = ConexionBD.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, inicio);
            ps.setString(2, fin);
            ps.setString(3, inicio);
            ps.setString(4, fin);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                double bruto = rs.getDouble("bruto");
                double reembolsos = rs.getDouble("reembolsos");
                double neto = bruto - reembolsos;

                datos.put("bruto", bruto);
                datos.put("reembolsos", reembolsos);
                datos.put("neto", neto);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return datos;
    }

    public Map<String, Object> reporteMejorAgente(String inicio, String fin) {

        Map<String, Object> resultado = new HashMap<>();

        try (Connection con = ConexionBD.getConnection()) {

            //Mejor agente por ventas
            String sql = """
            SELECT 
                u.id,
                u.username,
                SUM(p.monto) AS total_ventas
            FROM pago p
            JOIN reservacion r ON p.reservacion_id = r.id
            JOIN usuario u ON r.usuario_id = u.id
            WHERE p.fecha BETWEEN ? AND ?
              AND r.usuario_id IS NOT NULL
            GROUP BY u.id, u.username
            ORDER BY total_ventas DESC
            LIMIT 1
        """;

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, inicio + " 00:00:00");
            ps.setString(2, fin + " 23:59:59");

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                int agenteId = rs.getInt("id");

                resultado.put("agente", rs.getString("username"));
                resultado.put("total", rs.getDouble("total_ventas"));

                //Reservaciones del agente
                String sql2 = """
                SELECT 
                    r.id AS reservacion,
                    pk.nombre AS paquete,
                    r.cantidad_personas,
                    SUM(pg.monto) AS monto
                FROM reservacion r
                JOIN pago pg ON r.id = pg.reservacion_id
                JOIN paquete pk ON r.paquete_id = pk.id
                WHERE r.usuario_id = ?
                  AND pg.fecha BETWEEN ? AND ?
                GROUP BY r.id, pk.nombre, r.cantidad_personas
            """;

                PreparedStatement ps2 = con.prepareStatement(sql2);
                ps2.setInt(1, agenteId);
                ps2.setString(2, inicio + " 00:00:00");
                ps2.setString(3, fin + " 23:59:59");

                ResultSet rs2 = ps2.executeQuery();

                List<Map<String, Object>> lista = new ArrayList<>();

                while (rs2.next()) {

                    Map<String, Object> row = new HashMap<>();

                    row.put("reservacion", rs2.getInt("reservacion"));
                    row.put("paquete", rs2.getString("paquete"));
                    row.put("personas", rs2.getInt("cantidad_personas"));
                    row.put("monto", rs2.getDouble("monto"));

                    lista.add(row);
                }

                resultado.put("reservaciones", lista);

            } else {
                resultado.put("agente", null);
                resultado.put("total", 0);
                resultado.put("reservaciones", new ArrayList<>());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultado;
    }
   public Map<String, Object> reportePaqueteMasVendido(String inicio, String fin) {
    Map<String, Object> resultado = new HashMap<>();
    String fechaInicio = inicio.trim() + " 00:00:00";
    String fechaFin = fin.trim() + " 23:59:59";

    System.out.println("--- INICIO REPORTE DAO ---");
    System.out.println("Parámetros recibidos: " + inicio + " a " + fin);
    System.out.println("Fechas formateadas: [" + fechaInicio + "] a [" + fechaFin + "]");

    try (Connection con = ConexionBD.getConnection()) {

        // UERY CORREGIDO (ahora por personas y solo ventas reales)
        String sql1 = """
            SELECT p.id, p.nombre, SUM(r.cantidad_personas) AS total_personas
            FROM paquete p
            JOIN reservacion r ON r.paquete_id = p.id
            WHERE r.estado IN ('CONFIRMADA', 'COMPLETADA')
            AND r.fecha_creacion BETWEEN ? AND ?
            GROUP BY p.id, p.nombre
            ORDER BY total_personas DESC
            LIMIT 1
        """;

        PreparedStatement ps1 = con.prepareStatement(sql1);
        ps1.setString(1, fechaInicio);
        ps1.setString(2, fechaFin);
        ResultSet rs1 = ps1.executeQuery();

        if (rs1.next()) {
            int paqueteId = rs1.getInt("id");
            String nombreP = rs1.getString("nombre");

            System.out.println("¡Paquete encontrado! ID: " + paqueteId + " Nombre: " + nombreP);

            resultado.put("paquete", nombreP);
            resultado.put("total_personas", rs1.getInt("total_personas"));

            // QUERY DETALLE CORREGIDO (mismo filtro de estado)
            String sql2 = """
                SELECT 
                    r.id AS reservacion, 
                    r.fecha_creacion, 
                    r.fecha_viaje,
                    r.cantidad_personas, 
                    r.costo_total, 
                    r.estado, 
                    u.username AS cliente,
                    (SELECT IFNULL(SUM(p.monto), 0) 
                     FROM pago p 
                     WHERE p.reservacion_id = r.id) AS total_pagado
                FROM reservacion r
                LEFT JOIN usuario u ON r.usuario_id = u.id
                WHERE r.paquete_id = ?
                AND r.estado IN ('CONFIRMADA', 'COMPLETADA')
                AND r.fecha_creacion BETWEEN ? AND ?
            """;

            PreparedStatement ps2 = con.prepareStatement(sql2);
            ps2.setInt(1, paqueteId);
            ps2.setString(2, fechaInicio);
            ps2.setString(3, fechaFin);

            ResultSet rs2 = ps2.executeQuery();
            List<Map<String, Object>> lista = new ArrayList<>();

            while (rs2.next()) {
                Map<String, Object> row = new HashMap<>();

                row.put("reservacion", rs2.getInt("reservacion"));
                row.put("fecha_creacion", rs2.getTimestamp("fecha_creacion").toString());

                //  evitar null
                Date fechaViaje = rs2.getDate("fecha_viaje");
                row.put("fecha_viaje", fechaViaje != null ? fechaViaje.toString() : null);

                row.put("personas", rs2.getInt("cantidad_personas"));
                row.put("total", rs2.getDouble("costo_total"));
                row.put("estado", rs2.getString("estado"));
                row.put("cliente", rs2.getString("cliente"));
                row.put("pagado", rs2.getDouble("total_pagado"));

                lista.add(row);
            }

            System.out.println("Total reservaciones encontradas: " + lista.size());
            resultado.put("reservaciones", lista);

        } else {
            System.out.println("No se encontraron ventas en ese rango.");
            resultado.put("paquete", "Sin ventas");
            resultado.put("total_personas", 0);
            resultado.put("reservaciones", new ArrayList<>());
        }

    } catch (Exception e) {
        System.err.println("ERROR EN DAO:");
        e.printStackTrace();
    }

    return resultado;
}
    public Map<String, Object> reportePaqueteMenosVendido(String inicio, String fin) {
    Map<String, Object> resultado = new HashMap<>();
    String fechaInicio = inicio.trim() + " 00:00:00";
    String fechaFin = fin.trim() + " 23:59:59";

    try (Connection con = ConexionBD.getConnection()) {
        //OBTENER EL PAQUETE MENOS VENDIDO (Orden Ascendente)
        String sql1 = """
            SELECT p.id, p.nombre, COUNT(r.id) AS total_reservas
            FROM paquete p
            LEFT JOIN reservacion r ON r.paquete_id = p.id 
                 AND r.estado != 'CANCELADA' 
                 AND r.fecha_creacion BETWEEN ? AND ?
            GROUP BY p.id, p.nombre
            ORDER BY total_reservas ASC
            LIMIT 1
        """;

        PreparedStatement ps1 = con.prepareStatement(sql1);
        ps1.setString(1, fechaInicio);
        ps1.setString(2, fechaFin);
        ResultSet rs1 = ps1.executeQuery();

        if (rs1.next()) {
            int paqueteId = rs1.getInt("id");
            resultado.put("paquete", rs1.getString("nombre"));
            resultado.put("total_reservas", rs1.getInt("total_reservas"));

            //DETALLE DE LAS POCAS RESERVACIONES QUE TENGA
            String sql2 = """
                SELECT r.id AS reservacion, r.fecha_creacion, r.fecha_viaje,
                       r.cantidad_personas, r.costo_total, r.estado, u.username AS cliente
                FROM reservacion r
                JOIN usuario u ON r.usuario_id = u.id
                WHERE r.paquete_id = ? AND r.fecha_creacion BETWEEN ? AND ?
            """;

            PreparedStatement ps2 = con.prepareStatement(sql2);
            ps2.setInt(1, paqueteId);
            ps2.setString(2, fechaInicio);
            ps2.setString(3, fechaFin);
            ResultSet rs2 = ps2.executeQuery();

            List<Map<String, Object>> lista = new ArrayList<>();
            while (rs2.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("reservacion", rs2.getInt("reservacion"));
                row.put("fecha_creacion", rs2.getTimestamp("fecha_creacion").toString());
                row.put("fecha_viaje", rs2.getDate("fecha_viaje").toString());
                row.put("personas", rs2.getInt("cantidad_personas"));
                row.put("total", rs2.getDouble("costo_total"));
                row.put("estado", rs2.getString("estado"));
                row.put("cliente", rs2.getString("cliente"));
                lista.add(row);
            }
            resultado.put("reservaciones", lista);
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return resultado;
}
    public List<Map<String, Object>> reporteOcupacionDestinos(String inicio, String fin) {
    List<Map<String, Object>> lista = new ArrayList<>();
    // Formateamos las fechas para cubrir el día completo
    String fechaInicio = inicio.trim() + " 00:00:00";
    String fechaFin = fin.trim() + " 23:59:59";

    String sql = """
        SELECT 
            d.nombre AS destino, 
            d.pais,
            COUNT(r.id) AS cantidad_reservaciones
        FROM destino d
        LEFT JOIN paquete p ON p.destino_id = d.id
        LEFT JOIN reservacion r ON r.paquete_id = p.id 
            AND r.estado != 'CANCELADA'
            AND r.fecha_creacion BETWEEN ? AND ?
        GROUP BY d.id, d.nombre, d.pais
        ORDER BY cantidad_reservaciones DESC
    """;

    try (Connection con = ConexionBD.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {
        
        ps.setString(1, fechaInicio);
        ps.setString(2, fechaFin);
        
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("destino", rs.getString("destino"));
                row.put("pais", rs.getString("pais"));
                row.put("cantidad", rs.getInt("cantidad_reservaciones"));
                lista.add(row);
            }
        }
    } catch (Exception e) {
        System.err.println("Error en reporteOcupacionDestinos: " + e.getMessage());
        e.printStackTrace();
    }
    return lista;
}
    

}
