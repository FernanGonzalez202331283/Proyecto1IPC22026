/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Conexion;

import Logica.Destino;
import java.sql.Connection;
import java.sql.PreparedStatement;

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
}
