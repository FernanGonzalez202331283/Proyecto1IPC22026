/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Conexion;

import Logica.Paquete;
import com.google.gson.JsonObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author fernan
 */
public class PaqueteDAO {

    public boolean crearPaquete(Paquete p) {

        String sql = "INSERT INTO paquete(nombre, destino_id, duracion, descripcion, precio, capacidad, estado) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = ConexionBD.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, p.getNombre());
            ps.setInt(2, p.getDestinoId());
            ps.setInt(3, p.getDuracion());
            ps.setString(4, p.getDescripcion());
            ps.setDouble(5, p.getPrecio());
            ps.setInt(6, p.getCapacidad());
            ps.setString(7, "activo");

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public List<AlertaDemanda> obtenerAltaDemanda() {

        List<AlertaDemanda> lista = new ArrayList<>();

        String sql = "SELECT p.nombre, p.capacidad, COUNT(r.id) AS ocupados "
                + "FROM paquete p "
                + "JOIN reservacion r ON p.id = r.paquete_id "
                + "WHERE r.estado IN ('PENDIENTE','CONFIRMADA') "
                + "GROUP BY p.id "
                + "HAVING ocupados >= (p.capacidad * 0.8)";

        try (Connection con = ConexionBD.getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                AlertaDemanda a = new AlertaDemanda();

                a.setPaquete(rs.getString("nombre"));
                a.setOcupados(rs.getInt("ocupados"));
                a.setCapacidad(rs.getInt("capacidad"));
                a.setEstado("ALTA DEMANDA");

                lista.add(a);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    public List<Paquete> listarPaquetes() {

        List<Paquete> lista = new ArrayList<>();

        String sql = "SELECT * FROM paquete WHERE estado='activo'";

        try (Connection con = ConexionBD.getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Paquete p = new Paquete();
                p.setId(rs.getInt("id"));
                p.setNombre(rs.getString("nombre"));
                p.setDestinoId(rs.getInt("destino_id"));   
                p.setDuracion(rs.getInt("duracion"));      
                p.setDescripcion(rs.getString("descripcion"));
                p.setPrecio(rs.getDouble("precio"));
                p.setCapacidad(rs.getInt("capacidad"));

                lista.add(p);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }
    public List<Paquete> listarPaquetesPorEstado(String estado) {

    List<Paquete> lista = new ArrayList<>();

    String sql = "SELECT * FROM paquete WHERE estado=?";

    try (Connection con = ConexionBD.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setString(1, estado);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Paquete p = new Paquete();
            p.setId(rs.getInt("id"));
            p.setNombre(rs.getString("nombre"));
            p.setDestinoId(rs.getInt("destino_id"));
            p.setDuracion(rs.getInt("duracion"));
            p.setDescripcion(rs.getString("descripcion"));
            p.setPrecio(rs.getDouble("precio"));
            p.setCapacidad(rs.getInt("capacidad"));

            lista.add(p);
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return lista;
}

    public boolean actualizarPaquete(Paquete p) {

        String sql = "UPDATE paquete SET nombre=?, destino_id=?, duracion=?, descripcion=?, precio=?, capacidad=? WHERE id=?";

        try (Connection con = ConexionBD.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, p.getNombre());
            ps.setInt(2, p.getDestinoId());
            ps.setInt(3, p.getDuracion());
            ps.setString(4, p.getDescripcion());
            ps.setDouble(5, p.getPrecio());
            ps.setInt(6, p.getCapacidad());
            ps.setInt(7, p.getId());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean cambiarEstadoPaquete(int id, String estado) {

        String sql = "UPDATE paquete SET estado=? WHERE id=?";

        try (Connection con = ConexionBD.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, estado);
            ps.setInt(2, id);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public List<JsonObject> obtenerDetallePaquete(int paqueteId) {

        List<JsonObject> lista = new ArrayList<>();

        String sql = "SELECT p.nombre AS paquete, p.precio, p.duracion, "
                + "d.nombre AS destino, d.imagen_url, s.nombre AS servicio, s.costo, pr.nombre AS proveedor "
                + "FROM paquete p "
                + "JOIN destino d ON p.destino_id = d.id "
                + "LEFT JOIN servicio s ON p.id = s.paquete_id "
                + "LEFT JOIN proveedor pr ON s.proveedor_id = pr.id "
                + "WHERE p.id = ?";

        try (Connection con = ConexionBD.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, paqueteId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                JsonObject obj = new JsonObject();

                obj.addProperty("paquete", rs.getString("paquete"));
                obj.addProperty("precio", rs.getDouble("precio"));
                obj.addProperty("duracion", rs.getInt("duracion"));
                obj.addProperty("destino", rs.getString("destino"));
                obj.addProperty("imagen", rs.getString("imagen_url"));
                obj.addProperty("servicio", rs.getString("servicio"));
                obj.addProperty("costo", rs.getDouble("costo"));
                obj.addProperty("proveedor", rs.getString("proveedor"));

                lista.add(obj);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    public boolean existePaquete(String nombre) {

        String sql = "SELECT id FROM paquete WHERE nombre = ?";

        try (Connection con = ConexionBD.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nombre);

            ResultSet rs = ps.executeQuery();

            return rs.next();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public int obtenerIdPorNombre(String nombre) {
        String sql = "SELECT id FROM paquete WHERE nombre = ? AND estado='activo'";

        try (Connection con = ConexionBD.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nombre);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("id");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1;
    }

    public double obtenerPrecio(int id) {

        String sql = "SELECT precio FROM paquete WHERE id=?";

        try (Connection con = ConexionBD.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getDouble("precio");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }
}
