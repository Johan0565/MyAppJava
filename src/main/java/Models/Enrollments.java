package Models;

import Models.Entities.Enrollment;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Enrollments {
    private static Enrollments instance = null;
    private List<Enrollment> enrollments = new ArrayList<>();
    private Connection connection;

    private Enrollments() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost/university", "root", "");
        } catch (SQLException e) {
            System.err.println("Failed to connect to database: " + e.getMessage());
        }
    }

    public static Enrollments getInstance() {
        if (instance == null) {
            instance = new Enrollments();
        }
        return instance;
    }

    public int create(Enrollment enrollment) {
        String sql = "INSERT INTO enrollments (student_id, elective_id, semester_id, grade, grade_date) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, enrollment.getStudentId());
            stmt.setInt(2, enrollment.getElectiveId());
            stmt.setInt(3, enrollment.getSemesterId());
            if (enrollment.getGrade() >= 2 && enrollment.getGrade() <= 5) {
                stmt.setInt(4, enrollment.getGrade());
            } else {
                stmt.setNull(4, Types.INTEGER);
            }
            if (enrollment.getGradeDate() != null) {
                stmt.setDate(5, new java.sql.Date(enrollment.getGradeDate().getTime()));
            } else {
                stmt.setNull(5, Types.DATE);
            }
            int rowsAffected = stmt.executeUpdate();
            System.out.println("Enrollments.create: Rows affected = " + rowsAffected);

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                int generatedId = rs.getInt(1);
                System.out.println("Enrollments.create: Generated enrollments_id = " + generatedId);
                enrollments.add(new Enrollment(
                        generatedId,
                        enrollment.getStudentId(),
                        enrollment.getElectiveId(),
                        enrollment.getSemesterId(),
                        enrollment.getGrade(),
                        enrollment.getGradeDate()
                ));
                return generatedId;
            } else {
                System.err.println("Enrollments.create: No ID generated");
                return -1;
            }
        } catch (SQLException e) {
            System.err.println("Enrollments.create failed: " + e.getMessage());
            throw new RuntimeException("Failed to create enrollment", e);
        }
    }

    public List<Enrollment> getAll() {
        enrollments.clear();
        String sql = "SELECT * FROM enrollments";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                enrollments.add(new Enrollment(
                        rs.getInt("enrollments_id"),
                        rs.getInt("student_id"),
                        rs.getInt("elective_id"),
                        rs.getInt("semester_id"),
                        rs.getInt("grade"),
                        rs.getDate("grade_date")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Enrollments.getAll failed: " + e.getMessage());
        }
        return enrollments;
    }

    public Enrollment getById(int enrollmentsId) {
        String sql = "SELECT * FROM enrollments WHERE enrollments_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, enrollmentsId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Enrollment(
                        rs.getInt("enrollments_id"),
                        rs.getInt("student_id"),
                        rs.getInt("elective_id"),
                        rs.getInt("semester_id"),
                        rs.getInt("grade"),
                        rs.getDate("grade_date")
                );
            }
        } catch (SQLException e) {
            System.err.println("Enrollments.getById failed: " + e.getMessage());
        }
        return null;
    }

    public void update(Enrollment enrollment) {
        String sql = "UPDATE enrollments SET student_id = ?, elective_id = ?, semester_id = ?, grade = ?, grade_date = ? WHERE enrollments_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, enrollment.getStudentId());
            stmt.setInt(2, enrollment.getElectiveId());
            stmt.setInt(3, enrollment.getSemesterId());
            if (enrollment.getGrade() >= 2 && enrollment.getGrade() <= 5) {
                stmt.setInt(4, enrollment.getGrade());
            } else {
                stmt.setNull(4, Types.INTEGER);
            }
            if (enrollment.getGradeDate() != null) {
                stmt.setDate(5, new java.sql.Date(enrollment.getGradeDate().getTime()));
            } else {
                stmt.setNull(5, Types.DATE);
            }
            stmt.setInt(6, enrollment.getEnrollmentsId());
            stmt.executeUpdate();
            enrollments.removeIf(e -> e.getEnrollmentsId() == enrollment.getEnrollmentsId());
            enrollments.add(enrollment);
        } catch (SQLException e) {
            System.err.println("Enrollments.update failed: " + e.getMessage());
        }
    }

    public void delete(int enrollmentsId) {
        String sql = "DELETE FROM enrollments WHERE enrollments_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, enrollmentsId);
            stmt.executeUpdate();
            enrollments.removeIf(e -> e.getEnrollmentsId() == enrollmentsId);
        } catch (SQLException e) {
            System.err.println("Enrollments.delete failed: " + e.getMessage());
        }
    }
}