package myappjava.Controllers;

import Models.Students;
import Models.Users;
import Models.Entities.Student;
import Models.Entities.User;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import myappjava.UniversityApp;

public class RegistrationController {
    private static String MASTER_KEY = "admin123";
    private final Users usersModel;
    private final Students studentsModel;
    private UniversityApp app;
    private Pane view;

    @FXML private ComboBox<String> roleComboBox;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private TextField nameField;
    @FXML private TextField lastNameField;
    @FXML private TextField patronymicField;
    @FXML private TextField phoneField;
    @FXML private TextField addressField;
    @FXML private TextField masterKeyField;
    @FXML private Button registerButton;
    @FXML private Button backButton;

    public RegistrationController() {
        this.usersModel = Users.getInstance();
        this.studentsModel = Students.getInstance();
        this.app = null;
    }

    public static void setMasterKey(String newMasterKey) {
        MASTER_KEY = newMasterKey;
    }

    public void setDependencies(Pane view, UniversityApp app) {
        this.view = view;
        this.app = app;
    }

    @FXML
    private void initialize() {
        if (roleComboBox != null) {
            roleComboBox.setItems(FXCollections.observableArrayList("Студент", "Админ"));
            roleComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
                boolean isStudent = "Студент".equals(newValue);
                boolean isAdmin = "Админ".equals(newValue);
                nameField.setVisible(isStudent);
                lastNameField.setVisible(isStudent);
                patronymicField.setVisible(isStudent);
                phoneField.setVisible(isStudent);
                addressField.setVisible(isStudent);
                masterKeyField.setVisible(isAdmin);
            });
        }
    }

    @FXML
    private void handleRegister() {
        if (app == null) {
            showAlert("Ошибка", "Контроллер не инициализирован!");
            return;
        }

        String username = usernameField.getText();
        String password = passwordField.getText();
        String selectedRole = roleComboBox.getValue();

        if (username.isEmpty() || password.isEmpty() || selectedRole == null) {
            showAlert("Ошибка", "Заполните обязательные поля!");
            return;
        }

        int roleId;
        switch (selectedRole) {
            case "Студент":
                roleId = 2;
                String name = nameField.getText();
                String lastName = lastNameField.getText();
                String patronymic = patronymicField.getText();
                String phone = phoneField.getText();
                String address = addressField.getText();

                if (name.isEmpty() || lastName.isEmpty() || phone.isEmpty() || address.isEmpty()) {
                    showAlert("Ошибка", "Заполните все поля для студента!");
                    return;
                }

                // Создаем студента
                Student student = new Student(0, name, lastName, patronymic, phone, address); // ID будет сгенерирован
                int studentId = studentsModel.create(student);
                if (studentId == -1) {
                    showAlert("Ошибка", "Не удалось создать студента!");
                    return;
                }
                System.out.println("RegistrationController: Created student with ID = " + studentId);

                User user = new User(0, username, hashPassword(password), roleId, studentId); // ID будет сгенерирован
                int userId = usersModel.create(user);
                if (userId == -1) {
                    showAlert("Ошибка", "Не удалось создать пользователя!");
                    return;
                }
                System.out.println("RegistrationController: Created user with ID = " + userId);
                break;

            case "Админ":
                roleId = 1;
                if (!masterKeyField.getText().equals(MASTER_KEY)) {
                    showAlert("Ошибка", "Неверный мастер-ключ!");
                    return;
                }

                user = new User(0, username, hashPassword(password), roleId, null);
                userId = usersModel.create(user);
                if (userId == -1) {
                    showAlert("Ошибка", "Не удалось создать пользователя!");
                    return;
                }
                System.out.println("RegistrationController: Created admin with ID = " + userId);
                break;

            default:
                showAlert("Ошибка", "Неверная роль!");
                return;
        }

        showAlert("Успех", "Регистрация прошла успешно!");
        app.showScene("login");
    }

    @FXML
    private void handleBack() {
        if (app == null) {
            showAlert("Ошибка", "Контроллер не инициализирован!");
            return;
        }
        app.showScene("login");
    }

    private String hashPassword(String password) {
        return password;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(title.equals("Ошибка") ? Alert.AlertType.ERROR : Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}