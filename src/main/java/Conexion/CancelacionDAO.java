/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Conexion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 *
 * @author fernan
 */
public class CancelacionDAO {
    public double cancelarReservacion(int reservacionId){
        try (Connection con = ConexionBD.getConnection()){
            String sql = "SELECT fecha_viaje, estado FROM reservacion WHERE id=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, reservacionId);
            ResultSet rs = ps.executeQuery();
            if(!rs.next())return -1;
            String estado = rs.getString("estado");
            
            if(!(estado.equals("PENDIENTE")|| estado.equals("CONFIRMADA"))){
                return -2;
            }
            LocalDate fechaViaje = rs.getDate("fecha_viaje").toLocalDate();
            LocalDate hoy = LocalDate.now();
            
            long dias = ChronoUnit.DAYS.between(hoy,fechaViaje);
            if (dias < 7) return -3;
            
            String sumaSQL = "SELECT SUM(monto) FROM pago WHERE reservacion_id=?";
            PreparedStatement ps2 = con.prepareStatement(sumaSQL);
            ps2.setInt(1, reservacionId);
            ResultSet rs2 = ps2.executeQuery();

            double totalPagado = 0;
            if (rs2.next()) totalPagado = rs2.getDouble(1);
            
            double porcentaje;

            if (dias > 30) porcentaje = 1.0;
            else if (dias >= 15) porcentaje = 0.7;
            else porcentaje = 0.4;
            double reembolso = totalPagado * porcentaje;
            
            //actualizar el estado 
            String update = "UPDATE reservacion SET estado='CANCELADA' WHERE id=?";
            PreparedStatement ps3 = con.prepareStatement(update);
            ps3.setInt(1, reservacionId);
            ps3.executeUpdate();
            
            String insert = "INSERT INTO cancelacion(reservacion_id, monto_reembolso) VALUES (?, ?)";
            PreparedStatement ps4 = con.prepareStatement(insert);
            ps4.setInt(1, reservacionId);
            ps4.setDouble(2, reembolso);
            ps4.executeUpdate();

            return reembolso;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -99;
    }
}
