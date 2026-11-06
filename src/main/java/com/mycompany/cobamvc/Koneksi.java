package com.mycompany.cobamvc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class Koneksi {
    private static Connection koneksi;

    public static Connection getKoneksi() {
        if (koneksi == null) {
            try {
                // db_login adalah nama database di phpMyAdmin
                String url = "jdbc:mysql://localhost:3306/db_login";
                String user = "root";  
                String pass = "";      
                
                DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
                koneksi = DriverManager.getConnection(url, user, pass);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Gagal koneksi ke database: " + e.getMessage());
            }
        }
        return koneksi;
    }
}