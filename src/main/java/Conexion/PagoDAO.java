/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Conexion;

import com.google.gson.JsonObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 *
 * @author fernan
 */
public class PagoDAO {

    public boolean registrarPago(int reservacionId, double monto, String metodo, java.sql.Timestamp fecha) {

        try (Connection con = ConexionBD.getConnection()) {

            // VALIDAR LA EXISTENCIA
            String costoSQL = "SELECT costo_total, estado FROM reservacion WHERE id=?";
            PreparedStatement ps3 = con.prepareStatement(costoSQL);
            ps3.setInt(1, reservacionId);
            ResultSet rs2 = ps3.executeQuery();

            if (!rs2.next()) {
                System.out.println("Reservación no existe");
                return false;
            }

            double costoTotal = rs2.getDouble("costo_total");
            String estado = rs2.getString("estado");

            // TOTAL DE PAGO 
            String sumaSQL = "SELECT COALESCE(SUM(monto),0) FROM pago WHERE reservacion_id=?";
            PreparedStatement ps2 = con.prepareStatement(sumaSQL);
            ps2.setInt(1, reservacionId);
            ResultSet rs = ps2.executeQuery();

            double totalPagado = 0;
            if (rs.next()) {
                totalPagado = rs.getDouble(1);
            }

            // VALIDACIONES
            if (estado.equals("CONFIRMADA") || totalPagado >= costoTotal) {
                System.out.println("Pago ya completado");
                return false;
            }

            if ((totalPagado + monto) > costoTotal) {
                System.out.println("El pago excede el total");
                return false;
            }
            String sql = "INSERT INTO pago(reservacion_id, monto, metodo, fecha) VALUES (?,?,?,?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, reservacionId);
            ps.setDouble(2, monto);
            ps.setString(3, metodo);
            ps.setTimestamp(4, fecha);
            ps.executeUpdate();

            totalPagado += monto;

            //ACTUALIZAR ESTADO 
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

    public ArrayList<JsonObject> pagosPorReservacion(int id) {

        ArrayList<JsonObject> lista = new ArrayList<>();

        String sql = "SELECT * FROM pago WHERE reservacion_id=?";

        try (Connection con = ConexionBD.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                JsonObject obj = new JsonObject();
                obj.addProperty("id", rs.getInt("id"));
                obj.addProperty("monto", rs.getDouble("monto"));
                obj.addProperty("metodo", rs.getString("metodo"));
                obj.addProperty("fecha", rs.getString("fecha"));

                lista.add(obj);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }
}
