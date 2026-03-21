/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Conexion;

import Logica.Reservacion;
import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 *
 * @author fernan
 */
public class ReservacionDAO {
    
    public boolean crearReservacion(Reservacion r) {

        String sql = "INSERT INTO reservacion(fecha_viaje, paquete, cantidad_personas, agente, costo_total, estado) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, r.getFechaViaje());
            ps.setString(2, r.getPaquete());
            ps.setInt(3, r.getCandidadPersonas());
            ps.setString(4, r.getAgente());
            ps.setDouble(5, r.getCosotTotal());
            ps.setString(6, "PENDIENTE");
            
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
