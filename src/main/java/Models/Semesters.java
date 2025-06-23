package Models;

import Models.Entities.Semester;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Semesters {
    private static Semesters instance = null;
    private List<Semester> semesters = new ArrayList<>();
    private Connection connection;

    private Semesters() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost/university", "root", "");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Semesters getInstance() {
        if (instance == null) {
            instance = new Semesters();
        }
        return instance;
    }

    public void create(Semester semester) {
        String sql = "INSERT INTO semesters (semester_id, year, semester_number) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, semester.getSemesterId());
            stmt.setInt(2, semester.getYear());
            stmt.setInt(3, semester.getSemesterNumber());
            stmt.executeUpdate();
            semesters.add(semester);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Semester> getAll() {
        semesters.clear();
        String sql = "SELECT * FROM semesters";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                semesters.add(new Semester(
                        rs.getInt("semester_id"),
                        rs.getInt("year"),
                        rs.getInt("semester_number")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return semesters;
    }

    public Semester getById(int semesterId) {
        String sql = "SELECT * FROM semesters WHERE semester_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, semesterId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Semester(
                        rs.getInt("semester_id"),
                        rs.getInt("year"),
                        rs.getInt("semester_number")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void update(Semester semester) {
        String sql = "UPDATE semesters SET year = ?, semester_number = ? WHERE semester_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, semester.getYear());
            stmt.setInt(2, semester.getSemesterNumber());
            stmt.setInt(3, semester.getSemesterId());
            stmt.executeUpdate();
            semesters.removeIf(s -> s.getSemesterId() == semester.getSemesterId());
            semesters.add(semester);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int semesterId) {
        String sql = "DELETE FROM semesters WHERE semester_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, semesterId);
            stmt.executeUpdate();
            semesters.removeIf(s -> s.getSemesterId() == semesterId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}