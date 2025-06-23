package Models;

import Models.Entities.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Users {
    private static Users instance = null;
    private List<User> users = new ArrayList<>();
    private Connection connection;

    private Users() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost/university", "root", "");
        } catch (SQLException e) {
            System.err.println("Failed to connect to database: " + e.getMessage());
        }
    }

    public static Users getInstance() {
        if (instance == null) {
            instance = new Users();
        }
        return instance;
    }

    public int create(User user) {
        String sql = "INSERT INTO users (username, password_hash, role_id, student_id) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPasswordHash());
            stmt.setInt(3, user.getRoleId());
            if (user.getStudentId() != null) {
                stmt.setInt(4, user.getStudentId());
            } else {
                stmt.setNull(4, Types.INTEGER);
            }
            int rowsAffected = stmt.executeUpdate();
            System.out.println("Users.create: Rows affected = " + rowsAffected);

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                int generatedId = rs.getInt(1);
                System.out.println("Users.create: Generated user_id = " + generatedId);
                return generatedId;
            } else {
                System.err.println("Users.create: No ID generated");
                return -1;
            }
        } catch (SQLException e) {
            System.err.println("Users.create failed: " + e.getMessage());
            return -1;
        }
    }

    public List<User> getAll() {
        users.clear();
        String sql = "SELECT * FROM users";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                users.add(new User(
                        rs.getInt("user_id"),
                        rs.getString("username"),
                        rs.getString("password_hash"),
                        rs.getInt("role_id"),
                        rs.getObject("student_id") != null ? rs.getInt("student_id") : null
                ));
            }
        } catch (SQLException e) {
            System.err.println("Users.getAll failed: " + e.getMessage());
        }
        return users;
    }

    public User getById(int userId) {
        String sql = "SELECT * FROM users WHERE user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new User(
                        rs.getInt("user_id"),
                        rs.getString("username"),
                        rs.getString("password_hash"),
                        rs.getInt("role_id"),
                        rs.getObject("student_id") != null ? rs.getInt("student_id") : null
                );
            }
        } catch (SQLException e) {
            System.err.println("Users.getById failed: " + e.getMessage());
        }
        return null;
    }

    public void update(User user) {
        String sql = "UPDATE users SET username = ?, password_hash = ?, role_id = ?, student_id = ? WHERE user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPasswordHash());
            stmt.setInt(3, user.getRoleId());
            if (user.getStudentId() != null) {
                stmt.setInt(4, user.getStudentId());
            } else {
                stmt.setNull(4, Types.INTEGER);
            }
            stmt.setInt(5, user.getUserId());
            stmt.executeUpdate();
            users.removeIf(u -> u.getUserId() == user.getUserId());
            users.add(user);
        } catch (SQLException e) {
            System.err.println("Users.update failed: " + e.getMessage());
        }
    }

    public void delete(int userId) {
        String sql = "DELETE FROM users WHERE user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.executeUpdate();
            users.removeIf(u -> u.getUserId() == userId);
        } catch (SQLException e) {
            System.err.println("Users.delete failed: " + e.getMessage());
        }
    }

    public User authenticate(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new User(
                        rs.getInt("user_id"),
                        rs.getString("username"),
                        rs.getString("password_hash"),
                        rs.getInt("role_id"),
                        rs.getObject("student_id") != null ? rs.getInt("student_id") : null
                );
            }
        } catch (SQLException e) {
            System.err.println("Users.authenticate failed: " + e.getMessage());
        }
        return null;
    }
}