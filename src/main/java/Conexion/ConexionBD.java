/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Conexion;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author fernan
 */
public class ConexionBD {
    private static final String URL = "jdbc:mysql://localhost:3306/agencia";
    private static final String USER_NAME = "root";
    private static final String PASSWORD = "Fernan16@2026";

    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // 🔥 IMPORTANTE
            return DriverManager.getConnection(URL, USER_NAME, PASSWORD);
        } catch (Exception e) {
            System.out.println("Error al conectar con la base de datos: " + e.getMessage());
        }
        return null;
    }
}
