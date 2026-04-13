/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Conexion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 *
 * @author fernan
 */
public class ConsultaDAO {

    public ArrayList<String> pagosPorReservacion(int id) {

        ArrayList<String> lista = new ArrayList<>();

        String sql = "SELECT monto, metodo, fecha FROM pago WHERE reservacion_id=?";

        try (Connection con = ConexionBD.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String data = "{"
                        + "\"monto\":" + rs.getDouble("monto") + ","
                        + "\"metodo\":\"" + rs.getString("metodo") + "\","
                        + "\"fecha\":\"" + rs.getString("fecha") + "\""
                        + "}";

                lista.add(data);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    public ArrayList<String> reservacionesDelDia() {

        ArrayList<String> lista = new ArrayList<>();

        String sql = "SELECT id, fecha_viaje, paquete, estado, agente "
                + "FROM reservacion "
                + "WHERE DATE(fecha_creacion) = CURDATE()";

        try (Connection con = ConexionBD.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String data = "{"
                        + "\"id\":" + rs.getInt("id") + ","
                        + "\"fecha_viaje\":\"" + rs.getString("fecha_viaje") + "\","
                        + "\"paquete\":\"" + rs.getString("paquete") + "\","
                        + "\"estado\":\"" + rs.getString("estado") + "\","
                        + "\"agente\":\"" + rs.getString("agente") + "\""
                        + "}";

                lista.add(data);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    public ResultSet obtenerDetalle(int paqueteId) {
        String sql = "SELECT p.nombre, p.precio, s.nombre AS servicio, s.costo, pr.nombre AS proveedor "
                + "FROM paquete p "
                + "JOIN servicio s ON p.id = s.paquete_id "
                + "JOIN proveedor pr ON s.proveedor_id = pr.id "
                + "WHERE p.id = ?";

        try {
            Connection con = ConexionBD.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, paqueteId);
            return ps.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public ResultSet obtenerPorDestino(int destinoId) {
        String sql = "SELECT * FROM paquete WHERE destino_id = ? AND estado='ACTIVO'";

        try {
            Connection con = ConexionBD.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, destinoId);
            return ps.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean editarPaquete(int id, String nombre, double precio) {
        String sql = "UPDATE paquete SET nombre=?, precio=? WHERE id=?";

        try (Connection con = ConexionBD.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nombre);
            ps.setDouble(2, precio);
            ps.setInt(3, id);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean desactivarPaquete(int id) {

        String sql = "UPDATE paquete SET estado='INACTIVO' WHERE id=?";

        try (Connection con = ConexionBD.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
