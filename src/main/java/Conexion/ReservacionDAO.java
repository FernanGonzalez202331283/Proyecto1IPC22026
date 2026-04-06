/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Conexion;

import Logica.Reservacion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author fernan
 */
public class ReservacionDAO {

    public int crearReservacion(Reservacion r) {

        String sql = "INSERT INTO reservacion(fecha_viaje, paquete_id, cantidad_personas, usuario_id, costo_total, estado) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = ConexionBD.getConnection(); PreparedStatement ps = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);) {

            ps.setString(1, r.getFechaViaje());
            ps.setInt(2, r.getPaqueteId());
            ps.setInt(3, r.getCandidadPersonas());
            ps.setInt(4, r.getIdUsurio());
            ps.setDouble(5, r.getCosotTotal());
            ps.setString(6, "PENDIENTE");

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            int idReservacion = 0;

            if (rs.next()) {
                idReservacion = rs.getInt(1);
            }

            String sql2 = "INSERT INTO reservacion_cliente(reservacion_id, cliente_dpi) VALUES (?,?)";
            PreparedStatement ps2 = con.prepareStatement(sql2);

            for (String dpi : r.getDpis()) {
                ps2.setInt(1, idReservacion);
                ps2.setString(2, dpi);
                ps2.executeUpdate();
            }
            return idReservacion;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1;
    }

    public Reservacion obtenerPorId(int id) {

       String sql = "SELECT r.*, " +
            "p.nombre AS paquete_nombre, " +
            "d.nombre AS destino_nombre, " +
            "d.imagen_url, " +
            "u.username AS agente " +
            "FROM reservacion r " +
            "JOIN paquete p ON r.paquete_id = p.id " +
            "JOIN destino d ON p.destino_id = d.id " +
            "JOIN usuario u ON r.usuario_id = u.id " +
            "WHERE r.id=?";

    try (Connection con = ConexionBD.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {

            Reservacion r = new Reservacion();

            // DATOS PRINCIPALES
            r.setId(rs.getInt("id"));
            r.setFechaViaje(rs.getString("fecha_viaje"));
            r.setFechaCreacion(rs.getString("fecha_creacion")); // NUEVO
            r.setCandidadPersonas(rs.getInt("cantidad_personas"));
            r.setCosotTotal(rs.getDouble("costo_total"));
            r.setEstado(rs.getString("estado"));
            r.setPaqueteId(rs.getInt("paquete_id"));

            // INFO EXTRA
            r.setPaquete(rs.getString("paquete_nombre"));
            r.setDestino(rs.getString("destino_nombre"));
            r.setImagen(rs.getString("imagen_url"));
            r.setAgente(rs.getString("agente"));

            // =========================
            // AQUÍ AGREGAS PASAJEROS
            // =========================
            String sqlClientes = "SELECT c.nombre, c.dpi " +
                                 "FROM reservacion_cliente rc " +
                                 "JOIN cliente c ON rc.cliente_dpi = c.dpi " +
                                 "WHERE rc.reservacion_id = ?";

            PreparedStatement psClientes = con.prepareStatement(sqlClientes);
            psClientes.setInt(1, id);

            ResultSet rsClientes = psClientes.executeQuery();

            List<String> pasajeros = new ArrayList<>();

            while (rsClientes.next()) {
                String nombre = rsClientes.getString("nombre");
                String dpi = rsClientes.getString("dpi");
                pasajeros.add(nombre + " - " + dpi);
            }

            r.setPasajeros(pasajeros);

            return r;
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return null;
    }

    public List<Reservacion> obtenerPorCliente(String dpi) {

        List<Reservacion> lista = new ArrayList<>();

        String sql = "SELECT r.*, p.nombre AS paquete_nombre, d.nombre AS destino_nombre "
                + "FROM reservacion r "
                + "JOIN paquete p ON r.paquete_id = p.id "
                + "JOIN destino d ON p.destino_id = d.id "
                + "JOIN reservacion_cliente rc ON r.id = rc.reservacion_id "
                + "WHERE rc.cliente_dpi = ? "
                + "ORDER BY r.id DESC";

        try (Connection con = ConexionBD.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, dpi);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Reservacion r = new Reservacion();

                r.setId(rs.getInt("id"));
                r.setFechaViaje(rs.getString("fecha_viaje"));
                r.setCandidadPersonas(rs.getInt("cantidad_personas"));
                r.setCosotTotal(rs.getDouble("costo_total"));
                r.setEstado(rs.getString("estado"));
                r.setPaqueteId(rs.getInt("paquete_id"));

                r.setPaquete(rs.getString("paquete_nombre"));
                r.setDestino(rs.getString("destino_nombre"));

                lista.add(r);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

   public List<Reservacion> obtenerDisponibles(String fecha, int destinoId) {

    List<Reservacion> lista = new ArrayList<>();

    String sql = "SELECT r.*, p.nombre AS paquete_nombre, d.nombre AS destino_nombre "
            + "FROM reservacion r "
            + "JOIN paquete p ON r.paquete_id = p.id "
            + "JOIN destino d ON p.destino_id = d.id "
            + "WHERE r.fecha_viaje = ? AND d.id = ?";

    try (Connection con = ConexionBD.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setString(1, fecha);
        ps.setInt(2, destinoId);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {

            Reservacion r = new Reservacion();

            r.setId(rs.getInt("id"));
            r.setFechaViaje(rs.getString("fecha_viaje"));
            r.setCandidadPersonas(rs.getInt("cantidad_personas"));
            r.setCosotTotal(rs.getDouble("costo_total"));
            r.setEstado(rs.getString("estado"));

            r.setPaquete(rs.getString("paquete_nombre"));
            r.setDestino(rs.getString("destino_nombre"));

            lista.add(r);
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return lista;
}
    public List<Reservacion> obtenerPorFecha(String fecha) {

    List<Reservacion> lista = new ArrayList<>();

    String sql = "SELECT r.*, " +
                 "p.nombre AS paquete_nombre, " +
                 "p.precio, " +
                 "p.capacidad, " +
                 "d.nombre AS destino_nombre " +
                 "FROM reservacion r " +
                 "JOIN paquete p ON r.paquete_id = p.id " +
                 "JOIN destino d ON p.destino_id = d.id " +
                 "WHERE r.fecha_viaje = ? " +
                 "ORDER BY r.id DESC";

    try (Connection con = ConexionBD.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setString(1, fecha);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {

            Reservacion r = new Reservacion();

            int capacidad = rs.getInt("capacidad");
            int ocupados = rs.getInt("cantidad_personas");

            //CALCULAR CUPOS DISPONIBLES
            int cupos = capacidad - ocupados;

            r.setId(rs.getInt("id"));
            r.setFechaViaje(rs.getString("fecha_viaje"));
            r.setCandidadPersonas(ocupados);
            r.setCosotTotal(rs.getDouble("costo_total"));
            r.setEstado(rs.getString("estado"));

            r.setPaquete(rs.getString("paquete_nombre"));
            r.setDestino(rs.getString("destino_nombre"));

            //NUEVOS CAMPOS
            r.setPrecio(rs.getDouble("precio"));
            r.setCupos(cupos);

            lista.add(r);
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return lista;
}
    public List<Reservacion> obtenerReservacionesHoy() {

    List<Reservacion> lista = new ArrayList<>();

    String sql = "SELECT r.*, p.nombre AS paquete_nombre, d.nombre AS destino_nombre " +
            "FROM reservacion r " +
            "JOIN paquete p ON r.paquete_id = p.id " +
            "JOIN destino d ON p.destino_id = d.id " +
            "WHERE DATE(r.fecha_creacion) = CURDATE() " +
            "ORDER BY r.id DESC";

    try (Connection con = ConexionBD.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {

            Reservacion r = new Reservacion();

            r.setId(rs.getInt("id"));
            r.setFechaViaje(rs.getString("fecha_viaje"));
            r.setCandidadPersonas(rs.getInt("cantidad_personas"));
            r.setCosotTotal(rs.getDouble("costo_total"));
            r.setEstado(rs.getString("estado"));

            r.setPaquete(rs.getString("paquete_nombre"));
            r.setDestino(rs.getString("destino_nombre"));

            lista.add(r);
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return lista;
}
}
