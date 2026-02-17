package com.myapp;
import javafx.scene.layout.VBox;

import java.sql.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

public class DatabaseManager {
    private static final String URL = "jdbc:sqlite:app_database.db";

    public static Connection getConnection() throws Exception {
        return DriverManager.getConnection(URL);
    }

    public static void initialiseDatabase() {
        String userTable = "CREATE TABLE IF NOT EXISTS users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "email TEXT NOT NULL," +
                "password TEXT," +
                "first_name TEXT," +
                "last_name TEXT," +
                "dob TEXT," +
                "gender TEXT" +
                ");";

        String clinicTable = "CREATE TABLE IF NOT EXISTS clinics (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT," +
                "address TEXT," +
                "lat REAL," +
                "long REAL," +
                "email TEXT," +
                "phone TEXT," +
                "urgentPhone TEXT," +
                "hours TEXT" +
                ");";

        String appTable = "CREATE TABLE IF NOT EXISTS appointments(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "user_id INTEGER," +
                "clinic_id INTEGER," +
                "date TEXT," +
                "time TEXT," +
                "purpose TEXT," +
                "FOREIGN KEY (user_id) REFERENCES users(id)," +
                "FOREIGN KEY (clinic_id) REFERENCES clinics(id)" +
                ");";

        try (Connection con = getConnection();
            Statement smt = con.createStatement()) {
                smt.execute(userTable);
                smt.execute(clinicTable);
                smt.execute(appTable);
                System.out.println("Database init");
            }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public static boolean saveToDatabase(User user) {
        Argon2 argon2 = Argon2Factory.create();
        String hashedPassword = argon2.hash(10,65536,1, user.getPassword());

        String sql = "INSERT INTO users (email, password, first_name, last_name, dob, gender) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = getConnection();
        PreparedStatement psmt = con.prepareStatement(sql)){
            psmt.setString(1,user.getEmail());
            psmt.setString(2,hashedPassword);
            psmt.setString(3, user.getFirstName());
            psmt.setString(4, user.getLastName());
            psmt.setString(5, user.getDob());
            psmt.setString(6, user.getGender());

            psmt.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            argon2.wipeArray(user.getPassword().toCharArray());
        }
    }

    public static boolean validateLogin(String email, String password) {
        String sql = "SELECT password FROM users where email = ?";
        String storedHash = null;

        try (Connection con = getConnection();
            PreparedStatement psmt = con.prepareStatement(sql)) {
            psmt.setString(1,email);

            try (ResultSet rs = psmt.executeQuery()){
                if (rs.next()){
                    storedHash = rs.getString("password");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (storedHash == null){ return false; }
        Argon2 argon2 = Argon2Factory.create();
        try{
            return argon2.verify(storedHash,password.toCharArray());
        } finally {
            argon2.wipeArray(password.toCharArray());
        }
    }

    public static void fetchAndStartSession(String email) {
        String sql = "SELECT id, email, first_name, last_name, dob, gender FROM users WHERE email = ?";

        try (Connection con = getConnection();
             PreparedStatement psmt = con.prepareStatement(sql)) {

            psmt.setString(1, email);
            ResultSet rs = psmt.executeQuery();

            if (rs.next()) {
                userSession.login(
                        rs.getInt("id"),
                        rs.getString("email"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("dob"),
                        rs.getString("gender")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<VBox> pullAppointments() {
        int currentId = userSession.getInstance().getUserId();
        List<VBox> cards = new ArrayList<>();

        String sql = "SELECT appointments.*, clinics.name, clinics.address " +
                "FROM appointments " +
                "JOIN clinics ON appointments.clinic_id = clinics.id " +
                "WHERE appointments.user_id = ? " +
                "ORDER BY appointments.date , appointments.time ";

        try (Connection con = getConnection();
            PreparedStatement psmt = con.prepareStatement(sql)) {
            psmt.setInt(1, currentId);
            ResultSet rs = psmt.executeQuery();
            while (rs.next()) {
                String appDate = rs.getString("date");
                System.out.print(appDate);
                String status = AppointmentSchedulePage.getAppStatus(appDate);
                VBox card = AppointmentSchedulePage.createAppointmentCard(
                    appDate,
                    status,
                    rs.getString("name"),
                    rs.getString("time"),
                    rs.getString("address")
                );
                cards.add(card);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return cards;
    }

    public static boolean insertAppointment(String date, String time, int clinic_id, String purpose) {
        String sql = "INSERT INTO appointments (user_id, clinic_id, date, time, purpose) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection con = getConnection();
            PreparedStatement psmt = con.prepareStatement(sql)) {

            int id = userSession.getInstance().getUserId();

            psmt.setInt(1, id);
            psmt.setInt(2, clinic_id);
            psmt.setString(3, date);
            psmt.setString(4, time);
            psmt.setString(5, purpose);

            psmt.executeUpdate();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String getNextAppt() {
        int currentId = userSession.getInstance().getUserId();

        String sql = "SELECT date, time FROM appointments " +
                "WHERE user_id = ? AND (date > date('now') OR (date = date('now') AND time > time('now'))) " +
                "ORDER BY date , time LIMIT 1";

        try (Connection con = getConnection();
             PreparedStatement psmt = con.prepareStatement(sql)) {

            psmt.setInt(1, currentId);
            ResultSet rs = psmt.executeQuery();

            if (rs.next()) {
                String date = rs.getString("date");
                String time = rs.getString("time");
                return date + " at " + time;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "No upcoming appointments";
    }

        public static List<Clinic> getClinics() {
        List<Clinic> clinics = new ArrayList<>();
        String sql = "SELECT * FROM clinics";
        double userLat = userSession.getInstance().getMyLat();
        double userLon = userSession.getInstance().getMyLon();

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                double dist = calculateDistance(userLat, userLon, rs.getDouble("lat"), rs.getDouble("long"));
                Clinic c = new Clinic(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("address"),
                        rs.getDouble("lat"),
                        rs.getDouble("long"),
                        dist,
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("urgentPhone"),
                        rs.getString("hours")
                );

                clinics.add(c);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        clinics.sort(Comparator.comparingDouble(Clinic::getDistance));

        return clinics;
    }

    public static double calculateDistance(double userLat, double userLon, double clinicLat, double clinicLon) {
        double earthRadius = 6371; // Kilometers
        double dLat = Math.toRadians(clinicLat - userLat);
        double dLon = Math.toRadians(clinicLon - userLon);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(userLat)) * Math.cos(Math.toRadians(clinicLat)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return earthRadius * c;
    }
}
