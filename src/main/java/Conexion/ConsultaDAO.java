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

    try (Connection con = ConexionBD.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {

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
}
