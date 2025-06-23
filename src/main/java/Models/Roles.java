package Models;

import Models.Entities.Post;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Roles {
    private static Roles instance = null;
    private List<Post> roles = new ArrayList<>();
    private Connection connection;

    private Roles() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost/university", "root", "");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Roles getInstance() {
        if (instance == null) {
            instance = new Roles();
        }
        return instance;
    }

    public void create(Post role) {
        String sql = "INSERT INTO posts (role_id, role_name) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, role.getPostId());
            stmt.setString(2, role.getPostName());
            stmt.executeUpdate();
            roles.add(role);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Post> getAll() {
        roles.clear();
        String sql = "SELECT * FROM posts";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                roles.add(new Post(
                        rs.getInt("role_id"),
                        rs.getString("role_name")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return roles;
    }

    public Post getById(int roleId) {
        String sql = "SELECT * FROM posts WHERE role_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, roleId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Post(
                        rs.getInt("role_id"),
                        rs.getString("role_name")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void update(Post role) {
        String sql = "UPDATE posts SET role_name = ? WHERE role_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, role.getPostName());
            stmt.setInt(2, role.getPostId());
            stmt.executeUpdate();
            roles.removeIf(r -> r.getPostId() == role.getPostId());
            roles.add(role);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int roleId) {
        String sql = "DELETE FROM posts WHERE role_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, roleId);
            stmt.executeUpdate();
            roles.removeIf(r -> r.getPostId() == roleId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}