package myappjava.Controllers;

import Models.Users;
import Models.Entities.User;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import myappjava.UniversityApp;

public class LoginController {
    private static String MASTER_KEY = "admin123";
    private Users usersModel;
    private UniversityApp app;
    private Pane view;

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private ComboBox<String> roleComboBox;
    @FXML private TextField masterKeyField;
    @FXML private Button loginButton;
    @FXML private Button registerButton;

    public LoginController() {
        // Пустой конструктор для FXML
    }
    public static void setMasterKey(String newMasterKey) {
        MASTER_KEY = newMasterKey;
    }
    public void setDependencies(Pane view, Users usersModel, UniversityApp app) {
        this.view = view;
        this.usersModel = usersModel;
        this.app = app;
    }

    @FXML
    private void initialize() {
        if (roleComboBox != null) {
            roleComboBox.setItems(FXCollections.observableArrayList("Студент", "Учитель", "Админ"));
            roleComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
                if (masterKeyField != null) {
                    masterKeyField.setVisible("Админ".equals(newValue));
                }
            });
        }
    }

    @FXML
    private void handleLogin() {
        if (app == null || usersModel == null) {
            showAlert("Ошибка", "Контроллер не инициализирован!");
            return;
        }
        String username = usernameField.getText();
        String password = passwordField.getText();
        String selectedRole = roleComboBox.getValue();

        if (username.isEmpty() || password.isEmpty() || selectedRole == null) {
            showAlert("Ошибка", "Заполните все поля!");
            return;
        }

        int expectedRoleId;
        switch (selectedRole) {
            case "Студент":
                expectedRoleId = 2;
                break;
            case "Учитель":
                expectedRoleId = 3;
                break;
            case "Админ":
                expectedRoleId = 1;
                if (!masterKeyField.getText().equals(MASTER_KEY)) {
                    showAlert("Ошибка", "Неверный мастер-ключ!");
                    return;
                }
                break;
            default:
                showAlert("Ошибка", "Неверная роль!");
                return;
        }

        User user = usersModel.authenticate(username, password);
        if (user != null && user.getRoleId() == expectedRoleId) {
            switch (user.getRoleId()) {
                case 1:
                    app.showScene("adminMain", user);
                    break;
                case 2:
                    app.showScene("studentMain", user);
                    break;
                case 3:
                    app.showScene("teacherMain", user);
                    break;
            }
        } else {
            showAlert("Ошибка", "Неверное имя пользователя, пароль или роль!");
        }
    }

    @FXML
    private void handleRegister() {
        if (app == null) {
            showAlert("Ошибка", "Контроллер не инициализирован!");
            return;
        }
        app.showScene("registration");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}