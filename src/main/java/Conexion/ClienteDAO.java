/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Conexion;

import Logica.Cliente;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author fernan
 */
public class ClienteDAO {
     public boolean crearCliente(Cliente c) {
        String sql = "INSERT INTO cliente VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, c.getDpi());
            ps.setString(2, c.getNombre());
            ps.setString(3, c.getFechaNacimiento());
            ps.setString(4, c.getTelefono());
            ps.setString(5, c.getCorreo());
            ps.setString(6, c.getNacionalidad());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public Cliente buscarPorDpi(String dpi) {
    String sql = "SELECT * FROM cliente WHERE dpi=?";

    try (Connection con = ConexionBD.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setString(1, dpi);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            Cliente c = new Cliente();
            c.setDpi(rs.getString("dpi"));
            c.setNombre(rs.getString("nombre"));
            c.setTelefono(rs.getString("telefono"));
            return c;
        }

    } catch (Exception e) {
        e.printStackTrace();
    }
    return null;
}
}
