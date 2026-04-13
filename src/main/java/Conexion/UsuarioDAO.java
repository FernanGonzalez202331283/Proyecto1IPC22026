/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Conexion;

import Logica.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 *
 * @author fernan
 */
public class UsuarioDAO {

    public boolean crearUsuario(Usuario u) {
        String sql = "INSERT INTO usuario(username, password, rol, estado) VALUES (?, ?, ?, true)";

        try (Connection con = ConexionBD.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, u.getUsername());
            ps.setString(2, u.getPassword());
            ps.setString(3, u.getRol());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public ArrayList<Usuario> listarUsuarios() {
        ArrayList<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM usuario";

        try (Connection con = ConexionBD.getConnection(); 
         PreparedStatement ps = con.prepareStatement(sql); 
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            Usuario u = new Usuario();
            u.setId(rs.getInt("id"));
            u.setUsername(rs.getString("username"));
            u.setRol(rs.getString("rol"));
            u.setEstado(rs.getBoolean("estado"));

            lista.add(u);
        }

    } catch (Exception e) {
        e.printStackTrace();
    }
    return lista;
    }

    public boolean existeUsuario(String username) {
        String sql = "SELECT 1 FROM usuario WHERE username = ?";

        try (Connection con = ConexionBD.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            return rs.next();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public int obtenerIdPorUsername(String username) {
        String sql = "SELECT id FROM usuario WHERE username=?";
        try (Connection con = ConexionBD.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("id");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }
    public boolean cambiarRol(int id, String nuevoRol) {

    String sql = "UPDATE usuario SET rol=? WHERE id=?";

    try (Connection con = ConexionBD.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setString(1, nuevoRol);
        ps.setInt(2, id);

        return ps.executeUpdate() > 0;

    } catch (Exception e) {
        e.printStackTrace();
    }
    return false;
}
    public boolean cambiarEstado(int id, boolean estado) {

    String sql = "UPDATE usuario SET estado=? WHERE id=?";

    try (Connection con = ConexionBD.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setBoolean(1, estado);
        ps.setInt(2, id);

        return ps.executeUpdate() > 0;

    } catch (Exception e) {
        e.printStackTrace();
    }
    return false;
}
}
