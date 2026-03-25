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
        String sql = "INSERT INTO cliente (dpi, nombre, fecha_nacimiento, telefono, correo, nacionalidad) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            System.out.println("Insertando cliente...");
            System.out.println("DPI: " + c.getDpi());
            System.out.println("Nombre: " + c.getNombre());
            System.out.println("Fecha: " + c.getFechaNacimiento());
            System.out.println("Telefono: " + c.getTelefono());
            System.out.println("Correo: " + c.getCorreo());
            System.out.println("Nacionalidad: " + c.getNacionalidad());

            ps.setString(1, c.getDpi());
            ps.setString(2, c.getNombre());
            ps.setDate(3, java.sql.Date.valueOf(c.getFechaNacimiento()));
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
                c.setFechaNacimiento(rs.getString("fecha_nacimiento"));
                c.setTelefono(rs.getString("telefono"));
                c.setCorreo(rs.getString("correo"));
                c.setNacionalidad(rs.getString("nacionalidad"));
                return c;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public boolean actualizarCliente(Cliente c) {
        String sql = "UPDATE cliente SET nombre=?, fecha_nacimiento=?, telefono=?, correo=?, nacionalidad=? WHERE dpi=?";

        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, c.getNombre());
            ps.setDate(2, java.sql.Date.valueOf(c.getFechaNacimiento()));
            ps.setString(3, c.getTelefono());
            ps.setString(4, c.getCorreo());
            ps.setString(5, c.getNacionalidad());
            ps.setString(6, c.getDpi());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
