/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Conexion;

import Logica.Destino;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author fernan
 */
public class DestinoDAO {
    public boolean crearDestino(Destino d) {

        String sql = "INSERT INTO destino(nombre, pais, descripcion, clima, imagen_url) VALUES (?, ?, ?, ?, ?)";

        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, d.getNombre());
            ps.setString(2, d.getPais());
            ps.setString(3, d.getDescripcion());
            ps.setString(4, d.getClima());
            ps.setString(5, d.getImagen());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
        }
    
    public boolean actualizarDestino(Destino d) {

    String sql = "UPDATE destino SET nombre=?, pais=?, descripcion=?, clima=?, imagen_url=? WHERE id=?";

    try (Connection con = ConexionBD.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setString(1, d.getNombre());
        ps.setString(2, d.getPais());
        ps.setString(3, d.getDescripcion());
        ps.setString(4, d.getClima());
        ps.setString(5, d.getImagen());
        ps.setInt(6, d.getId());

        return ps.executeUpdate() > 0;

    } catch (Exception e) {
        e.printStackTrace();
    }

    return false;
}
    public boolean eliminarDestino(int id) {

    String sql = "DELETE FROM destino WHERE id=?";

    try (Connection con = ConexionBD.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setInt(1, id);

        return ps.executeUpdate() > 0;

    } catch (Exception e) {
        e.printStackTrace();
    }

    return false;
}
    
    public List<Destino> obtenerTodos() {

    List<Destino> lista = new ArrayList<>();

    String sql = "SELECT * FROM destino";

    try (Connection con = ConexionBD.getConnection();
         PreparedStatement ps = con.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {

            Destino d = new Destino();

            d.setId(rs.getInt("id"));
            d.setNombre(rs.getString("nombre"));
            d.setPais(rs.getString("pais"));
            d.setDescripcion(rs.getString("descripcion"));
            d.setClima(rs.getString("clima"));
            d.setImagen(rs.getString("imagen_url"));

            lista.add(d);
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return lista;
}
}
