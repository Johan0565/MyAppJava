package Models.Entities; // Замени на твой пакет

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class User {
    private IntegerProperty userId;
    private StringProperty username;
    private StringProperty passwordHash;
    private IntegerProperty roleId;
    private IntegerProperty studentId; // Может быть null

    public User(int userId, String username, String passwordHash, int roleId, Integer studentId) {
        this.userId = new SimpleIntegerProperty(userId);
        this.username = new SimpleStringProperty(username);
        this.passwordHash = new SimpleStringProperty(passwordHash);
        this.roleId = new SimpleIntegerProperty(roleId);
        this.studentId = studentId != null ? new SimpleIntegerProperty(studentId) : null;
    }

    // Геттеры свойств
    public IntegerProperty getUserIdProperty() { return userId; }
    public StringProperty getUsernameProperty() { return username; }
    public StringProperty getPasswordHashProperty() { return passwordHash; }
    public IntegerProperty getRoleIdProperty() { return roleId; }
    public IntegerProperty getStudentIdProperty() { return studentId; }

    // Геттеры и сеттеры для совместимости
    public int getUserId() { return userId.get(); }
    public void setUserId(int userId) { this.userId.set(userId); }
    public String getUsername() { return username.get(); }
    public void setUsername(String username) { this.username.set(username); }
    public String getPasswordHash() { return passwordHash.get(); }
    public void setPasswordHash(String passwordHash) { this.passwordHash.set(passwordHash); }
    public int getRoleId() { return roleId.get(); }
    public void setRoleId(int roleId) { this.roleId.set(roleId); }
    public Integer getStudentId() { return studentId != null ? studentId.get() : null; }
    public void setStudentId(Integer studentId) { this.studentId = studentId != null ? new SimpleIntegerProperty(studentId) : null; }

    @Override
    public String toString() {
        return "User{id=" + userId.get() + ", username='" + username.get() + "', roleId=" + roleId.get() + "}";
    }
}