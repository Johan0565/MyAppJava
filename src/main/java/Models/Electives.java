package Models;

import Models.Entities.Elective;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Electives {
    private static Electives instance = null;
    private List<Elective> electives = new ArrayList<>();
    private Connection connection;

    private Electives() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost/university", "root", "");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Electives getInstance() {
        if (instance == null) {
            instance = new Electives();
        }
        return instance;
    }

    public void create(Elective elective) {
        String sql = "INSERT INTO electives (elective_id, elective_name) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, elective.getElectiveId());
            stmt.setString(2, elective.getElectiveName());
            stmt.executeUpdate();
            electives.add(elective);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Elective> getAll() {
        electives.clear();
        String sql = "SELECT * FROM electives";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                electives.add(new Elective(
                        rs.getInt("elective_id"),
                        rs.getString("elective_name")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return electives;
    }

    public Elective getById(int electiveId) {
        String sql = "SELECT * FROM electives WHERE elective_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, electiveId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Elective(
                        rs.getInt("elective_id"),
                        rs.getString("elective_name")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void update(Elective elective) {
        String sql = "UPDATE electives SET elective_name = ? WHERE elective_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, elective.getElectiveName());
            stmt.setInt(2, elective.getElectiveId());
            stmt.executeUpdate();
            electives.removeIf(e -> e.getElectiveId() == elective.getElectiveId());
            electives.add(elective);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int electiveId) {
        String sql = "DELETE FROM electives WHERE elective_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, electiveId);
            stmt.executeUpdate();
            electives.removeIf(e -> e.getElectiveId() == electiveId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}