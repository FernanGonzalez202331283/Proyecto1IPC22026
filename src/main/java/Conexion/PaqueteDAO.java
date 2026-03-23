/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Conexion;

import Logica.Paquete;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author fernan
 */
public class PaqueteDAO {
    public boolean crearPaquete(Paquete p) {

        String sql = "INSERT INTO paquete(nombre, destino_id, duracion, descripcion, precio, capacidad, estado) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, p.getNombre());
            ps.setInt(2, p.getDestinoId());
            ps.setInt(3, p.getDuracion());
            ps.setString(4, p.getDescripcion());
            ps.setDouble(5, p.getPrecio());
            ps.setInt(6, p.getCapacidad());
            ps.setString(7, "ACTIVO");

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
    
    public ResultSet obtenerAltaDemanda() {

    String sql = "SELECT p.id, p.nombre, p.capacidad, COUNT(r.id) AS ocupados " +
                 "FROM paquete p " +
                 "JOIN reservacion r ON p.id = r.paquete_id " +
                 "WHERE r.estado IN ('PENDIENTE','CONFIRMADA') " +
                 "GROUP BY p.id " +
                 "HAVING ocupados >= (p.capacidad * 0.8)";

    try {
        Connection con = ConexionBD.getConnection();
        PreparedStatement ps = con.prepareStatement(sql);
        return ps.executeQuery();
    } catch (Exception e) {
        e.printStackTrace();
    }

    return null;
}
}
