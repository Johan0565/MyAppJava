package Models;

import Models.Entities.Department;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Departments {
    private static Departments instance = null;
    private List<Department> departments = new ArrayList<>();
    private Connection connection;

    private Departments() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost/university", "root", "");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Departments getInstance() {
        if (instance == null) {
            instance = new Departments();
        }
        return instance;
    }

    public void create(Department department) {
        String sql = "INSERT INTO departments (department_name, elective_id) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, department.getDepartmentName());
            stmt.setInt(2, department.getElectiveId());
            stmt.executeUpdate();
            departments.add(department);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Department> getAll() {
        departments.clear();
        String sql = "SELECT * FROM departments";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                departments.add(new Department(
                        rs.getString("department_name"),
                        rs.getInt("elective_id")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return departments;
    }

    public Department getByName(String departmentName) {
        String sql = "SELECT * FROM departments WHERE department_name = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, departmentName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Department(
                        rs.getString("department_name"), rs.getInt("elective_id")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void update(Department department) {
        String sql = "UPDATE departments SET elective_id = ? WHERE department_name = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, department.getElectiveId());
            stmt.setString(2, department.getDepartmentName());
            stmt.executeUpdate();
            departments.removeIf(d -> d.getDepartmentName().equals(department.getDepartmentName()));
            departments.add(department);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(String departmentName) {
        String sql = "DELETE FROM departments WHERE department_name = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, departmentName);
            stmt.executeUpdate();
            departments.removeIf(d -> d.getDepartmentName().equals(departmentName));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}