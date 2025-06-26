package Models;

import Models.Entities.ElectivePlan;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ElectivePlans {
    private static ElectivePlans instance = null;
    private List<ElectivePlan> plans = new ArrayList<>();
    private Connection connection;

    private ElectivePlans() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost/university", "root", "");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ElectivePlans getInstance() {
        if (instance == null) {
            instance = new ElectivePlans();
        }
        return instance;
    }

    public void create(ElectivePlan plan) {
        String sql = "INSERT INTO electiveplans (plan_id, elective_id, semester_id, number_lectures, number_practices, number_laboratory_work) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, plan.getPlanId());
            stmt.setInt(2, plan.getElectiveId());
            stmt.setInt(3, plan.getSemesterId());
            stmt.setInt(4, plan.getNumberLectures());
            stmt.setInt(5, plan.getNumberPractices());
            stmt.setInt(6, plan.getNumberLaboratoryWork());
            stmt.executeUpdate();
            plans.add(plan);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<ElectivePlan> getAll() {
        plans.clear();
        String sql = "SELECT * FROM electiveplans";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                plans.add(new ElectivePlan(
                        rs.getInt("plan_id"),
                        rs.getInt("elective_id"),
                        rs.getInt("semester_id"),
                        rs.getInt("number_lectures"),
                        rs.getInt("number_practices"),
                        rs.getInt("number_laboratory_work")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return plans;
    }

    public ElectivePlan getById(int planId) {
        String sql = "SELECT * FROM electiveplans WHERE plan_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, planId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new ElectivePlan(
                        rs.getInt("plan_id"),
                        rs.getInt("elective_id"),
                        rs.getInt("semester_id"),
                        rs.getInt("number_lectures"),
                        rs.getInt("number_practices"),
                        rs.getInt("number_laboratory_work")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void update(ElectivePlan plan) {
        String sql = "UPDATE electiveplans SET elective_id = ?, semester_id = ?, number_lectures = ?, number_practices = ?, number_laboratory_work = ? WHERE plan_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, plan.getElectiveId());
            stmt.setInt(2, plan.getSemesterId());
            stmt.setInt(3, plan.getNumberLectures());
            stmt.setInt(4, plan.getNumberPractices());
            stmt.setInt(5, plan.getNumberLaboratoryWork());
            stmt.setInt(6, plan.getPlanId());
            stmt.executeUpdate();
            plans.removeIf(p -> p.getPlanId() == plan.getPlanId());
            plans.add(plan);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int planId) {
        String sql = "DELETE FROM electiveplans WHERE plan_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, planId);
            stmt.executeUpdate();
            plans.removeIf(p -> p.getPlanId() == planId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}