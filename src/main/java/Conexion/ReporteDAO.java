/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Conexion;

import java.sql.Connection;
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

        String sql = "SELECT " +
                     "p.nombre AS paquete, " +
                     "r.cantidad_personas AS pasajeros, " +
                     "u.username AS agente, " +
                     "r.costo_total AS total " +
                     "FROM reservacion r " +
                     "JOIN paquete p ON r.paquete_id = p.id " +
                     "JOIN usuario u ON r.usuario_id = u.id " +
                     "WHERE r.estado = 'CONFIRMADA' " +
                     "AND DATE(r.fecha_creacion) BETWEEN ? AND ?";

        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

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
}
