/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Conexion;

import Logica.Servicio;
import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 *
 * @author fernan
 */
public class ServicioDAO {
    public boolean crearServicio(Servicio s){
        String sql = "INSERT INTO servicio(paquete_id, proveedor_id, nombre, costo) VALUES (?,?,?,?)";
        try (Connection con = ConexionBD.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)){
           ps.setInt(1, s.getPaqueteId());
           ps.setInt(2, s.getProveedorId());
           ps.setString(3, s.getNombre());
           ps.setDouble(4, s.getCosto());
          return ps.executeUpdate() > 0;
           
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
