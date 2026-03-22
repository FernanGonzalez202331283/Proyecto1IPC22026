/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Conexion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author fernan
 */
public class PagoDAO {
    public boolean registrarPago(int reservacionId, double monto, String metodo){
        String sql = "INSERT INTO pago(reservacion_id, monto, metodo) VALUES (?,?,?)"; 
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, reservacionId);
            ps.setDouble(2, monto);
            ps.setString(3, metodo);

            ps.executeUpdate();
            String sumaSQL = "SELECT SUM(monto) FROM pago WHERE reservacion_id=?";
            PreparedStatement ps2 = con.prepareStatement(sumaSQL);
            ps2.setInt(1, reservacionId);
            ResultSet rs = ps2.executeQuery();

            double totalPagado = 0;
            if (rs.next()) {
                totalPagado = rs.getDouble(1);
            }
            
            String costoSQL = "SELECT costo_total FROM reservacion WHERE id=?";
            PreparedStatement ps3 = con.prepareStatement(costoSQL);
            ps3.setInt(1, reservacionId);
            ResultSet rs2 = ps3.executeQuery();

            double costoTotal = 0;
            if (rs2.next()) {
                costoTotal = rs2.getDouble("costo_total");
            }
            
             if (totalPagado >= costoTotal) {
                String updateSQL = "UPDATE reservacion SET estado='CONFIRMADA' WHERE id=?";
                PreparedStatement ps4 = con.prepareStatement(updateSQL);
                ps4.setInt(1, reservacionId);
                ps4.executeUpdate();
            }

            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
