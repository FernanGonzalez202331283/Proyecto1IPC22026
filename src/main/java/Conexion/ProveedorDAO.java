/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Conexion;

import Logica.Proveedor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author fernan
 */
public class ProveedorDAO {

    public boolean crearProveedor(Proveedor p) {

        String sql = "INSERT INTO proveedor(nombre, tipo, pais, contacto) VALUES (?, ?, ?, ?)";

        try (Connection con = ConexionBD.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, p.getNombre());
            ps.setString(2, p.getTipo());
            ps.setString(3, p.getPais());
            ps.setString(4, p.getContacto());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean actualizarProveedor(Proveedor p) {

        String sql = "UPDATE proveedor SET nombre=?, tipo=?, pais=?, contacto=? WHERE id=?";

        try (Connection con = ConexionBD.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, p.getNombre());
            ps.setString(2, p.getTipo());
            ps.setString(3, p.getPais());
            ps.setString(4, p.getContacto());
            ps.setInt(5, p.getId());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean eliminarProveedor(int id) {

        String sql = "DELETE FROM proveedor WHERE id=?";

        try (Connection con = ConexionBD.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public List<Proveedor> listarProveedores() {
        String sql = "SELECT * FROM proveedor";
        List<Proveedor> lista = new ArrayList<>();
        try (Connection con = ConexionBD.getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Proveedor p = new Proveedor();
                p.setId(rs.getInt("id"));
                p.setNombre(rs.getString("nombre"));
                p.setTipo(rs.getString("tipo"));
                p.setPais(rs.getString("pais"));
                p.setContacto(rs.getString("contacto"));
                lista.add(p);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    public int obtenerIdPorNombre(String nombre) {
        String sql = "SELECT id FROM proveedor WHERE nombre = ?";

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
}
