package com.myapp;
import java.sql.*;

public class DatabaseManager {
    private static final String URL = "jdbc:sqlite:app_database.db";

    public static Connection getConnection() throws Exception {
        return DriverManager.getConnection(URL);
    }

    public static void initialiseDatabase() {
        String sql = "CREATE TABLE IF NOT EXISTS users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "email TEXT NOT NULL," +
                "password TEXT," +
                "first_name TEXT," +
                "last_name TEXT," +
                "dob TEXT," +
                "gender TEXT" +
                ");";
        try (Connection con = getConnection();
            Statement smt = con.createStatement()) {
                smt.execute(sql);
                System.out.println("Database init");
            }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public static boolean saveToDatabase(User user) {
        String sql = "INSERT INTO users (email, password, first_name, last_name, dob, gender) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = getConnection();
        PreparedStatement psmt = con.prepareStatement(sql)){
            psmt.setString(1,user.getEmail());
            psmt.setString(2,user.getPassword());
            psmt.setString(3, user.getFirstName());
            psmt.setString(4, user.getLastName());
            psmt.setString(5, user.getDob());
            psmt.setString(6, user.getGender());

            psmt.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean validateLogin(String email, String password) {
        String sql = "SELECT count(1) FROM users WHERE email = ? AND password = ?";

        try (Connection con = getConnection();
            PreparedStatement psmt = con.prepareStatement(sql)) {
            psmt.setString(1,email);

            psmt.setString(2,password);
            try (ResultSet rs = psmt.executeQuery()){
                if (rs.next()){
                    return rs.getInt(1) > 0;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
