package Models;

import Models.Entities.Student;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Students {
    private static Students instance = null;
    private List<Student> students = new ArrayList<>();
    private Connection connection;

    private Students() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost/university", "root", "");
        } catch (SQLException e) {
            System.err.println("Failed to connect to database: " + e.getMessage());
        }
    }

    public static Students getInstance() {
        if (instance == null) {
            instance = new Students();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

    public int create(Student student) {
        String sql = "INSERT INTO students (name, last_name, patronymic, phone, address) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, student.getName());
            stmt.setString(2, student.getLastName());
            stmt.setString(3, student.getPatronymic());
            stmt.setString(4, student.getPhone());
            stmt.setString(5, student.getAddress());
            int rowsAffected = stmt.executeUpdate();
            System.out.println("Students.create: Rows affected = " + rowsAffected);

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                int generatedId = rs.getInt(1);
                System.out.println("Students.create: Generated student_id = " + generatedId);
                return generatedId;
            } else {
                System.err.println("Students.create: No ID generated");
                return -1;
            }
        } catch (SQLException e) {
            System.err.println("Students.create failed: " + e.getMessage());
            return -1;
        }
    }

    public List<Student> getAll() {
        students.clear();
        String sql = "SELECT * FROM students";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                students.add(new Student(
                        rs.getInt("student_id"),
                        rs.getString("name"),
                        rs.getString("last_name"),
                        rs.getString("patronymic"),
                        rs.getString("phone"),
                        rs.getString("address")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Students.getAll failed: " + e.getMessage());
        }
        return students;
    }

    public Student getById(int studentId) {
        String sql = "SELECT * FROM students WHERE student_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Student(
                        rs.getInt("student_id"),
                        rs.getString("name"),
                        rs.getString("last_name"),
                        rs.getString("patronymic"),
                        rs.getString("phone"),
                        rs.getString("address")
                );
            }
        } catch (SQLException e) {
            System.err.println("Students.getById failed: " + e.getMessage());
        }
        return null;
    }

    public void update(Student student) {
        String sql = "UPDATE students SET name = ?, last_name = ?, patronymic = ?, phone = ?, address = ? WHERE student_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, student.getName());
            stmt.setString(2, student.getLastName());
            stmt.setString(3, student.getPatronymic());
            stmt.setString(4, student.getPhone());
            stmt.setString(5, student.getAddress());
            stmt.setInt(6, student.getStudentId());
            stmt.executeUpdate();
            students.removeIf(s -> s.getStudentId() == student.getStudentId());
            students.add(student);
        } catch (SQLException e) {
            System.err.println("Students.update failed: " + e.getMessage());
        }
    }

    public void delete(int studentId) {
        String sql = "DELETE FROM students WHERE student_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
            stmt.executeUpdate();
            students.removeIf(s -> s.getStudentId() == studentId);
        } catch (SQLException e) {
            System.err.println("Students.delete failed: " + e.getMessage());
        }
    }
}