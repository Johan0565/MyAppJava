package myappjava.Controllers;

import Models.*;
import Models.Entities.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import myappjava.UniversityApp;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class StudentMainController {
    private final Users usersModel = Users.getInstance();
    private final Students studentsModel = Students.getInstance();
    private final Enrollments enrollmentsModel = Enrollments.getInstance();
    private final Electives electivesModel = Electives.getInstance();
    private final ElectivePlans electivePlansModel = ElectivePlans.getInstance();
    private final Semesters semestersModel = Semesters.getInstance();
    private UniversityApp app;
    private User currentUser;
    private Student currentStudent;
    private Pane view;

    @FXML private TabPane tabPane;
    @FXML private TextField usernameField;
    @FXML private TextField nameField;
    @FXML private TextField lastNameField;
    @FXML private TextField patronymicField;
    @FXML private TextField phoneField;
    @FXML private TextField addressField;
    @FXML private PasswordField passwordField;
    @FXML private Button saveProfileButton;
    @FXML private Button expelButton;
    @FXML private TableView<Enrollment> myElectivesTable;
    @FXML private TableColumn<Enrollment, String> electiveNameColumn;
    @FXML private TableColumn<Enrollment, String> semesterColumn;
    @FXML private TableColumn<Enrollment, String> gradeColumn;
    @FXML private TextField enrollElectiveIdField;
    @FXML private Button enrollButton;
    @FXML private TableView<ElectivePlan> allElectivesTable;
    @FXML private TableColumn<ElectivePlan, String> electiveIdColumn;
    @FXML private TableColumn<ElectivePlan, String> electiveNameAllColumn;
    @FXML private TableColumn<ElectivePlan, String> semesterAllColumn;
    @FXML private TableColumn<ElectivePlan, String> lecturesColumn;
    @FXML private TableColumn<ElectivePlan, String> practicesColumn;
    @FXML private TableColumn<ElectivePlan, String> labsColumn;
    @FXML private TextField filterElectiveIdField;
    @FXML private TextField filterYearField;
    @FXML private Button filterButton;
    @FXML private TableView<Student> studentsTable;
    @FXML private TableColumn<Student, String> studentIdColumn;
    @FXML private TableColumn<Student, String> studentNameColumn;
    @FXML private TextField electiveIdField;
    @FXML private TextField semesterYearField;
    @FXML private TextField semesterNumberField;
    @FXML private Button searchStudentsButton;
    @FXML private TableView<Student> electiveStudentsTable;
    @FXML private TableColumn<Student, String> electiveStudentIdColumn;
    @FXML private TableColumn<Student, String> electiveStudentNameColumn;

    public StudentMainController() {
        this.app = null;
    }

    public void setDependencies(Pane view, UniversityApp app, User user) {
        System.out.println("Setting dependencies: user = " + (user != null ? user.getUsername() : "null"));
        this.view = view;
        this.app = app;
        this.currentUser = user;
        this.currentStudent = user != null ? studentsModel.getById(user.getStudentId()) : null;
        System.out.println("Current student: " + (currentStudent != null ? currentStudent.getName() : "null"));
        initialize();
    }

    @FXML
    private void initialize() {
        if (currentUser == null || currentStudent == null) {
            showAlert("Ошибка", "Пользователь или студент не инициализированы!");
            return;
        }
        System.out.println("Initializing StudentMainController for user: " + currentUser.getUsername());

        usernameField.setText(currentUser.getUsername());
        nameField.setText(currentStudent.getName());
        lastNameField.setText(currentStudent.getLastName());
        patronymicField.setText(currentStudent.getPatronymic());
        phoneField.setText(currentStudent.getPhone());
        addressField.setText(currentStudent.getAddress());

        electiveNameColumn.setCellValueFactory(cellData -> {
            Elective elective = electivesModel.getById(cellData.getValue().getElectiveId());
            return new SimpleStringProperty(elective != null ? elective.getElectiveName() : "");
        });
        semesterColumn.setCellValueFactory(cellData -> {
            Semester semester = semestersModel.getById(cellData.getValue().getSemesterId());
            return new SimpleStringProperty(semester != null ? semester.getYear() + "-" + semester.getSemesterNumber() : "");
        });
        gradeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getGrade() > 0 ? String.valueOf(cellData.getValue().getGrade()) : "Пока оценки нет"
        ));
        loadMyElectives();

        electiveIdColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getElectiveId())));
        electiveNameAllColumn.setCellValueFactory(cellData -> {
            Elective elective = electivesModel.getById(cellData.getValue().getElectiveId());
            return new SimpleStringProperty(elective != null ? elective.getElectiveName() : "");
        });
        semesterAllColumn.setCellValueFactory(cellData -> {
            Semester semester = semestersModel.getById(cellData.getValue().getSemesterId());
            return new SimpleStringProperty(semester != null ? semester.getYear() + "-" + semester.getSemesterNumber() : "");
        });
        lecturesColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getNumberLectures())));
        practicesColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getNumberPractices())));
        labsColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getNumberLaboratoryWork())));
        loadAllElectives();

        studentIdColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getStudentId())));
        studentNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getLastName() + " " + cellData.getValue().getName() + " " +
                        (cellData.getValue().getPatronymic() != null ? cellData.getValue().getPatronymic() : "")
        ));
        loadAllStudents();

        // Инициализация таблицы "Студенты на факультативе"
        electiveStudentIdColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getStudentId())));
        electiveStudentNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getLastName() + " " + cellData.getValue().getName() + " " +
                        (cellData.getValue().getPatronymic() != null ? cellData.getValue().getPatronymic() : "")
        ));
    }

    @FXML
    private void handleSaveProfile() {
        String username = usernameField.getText();
        String name = nameField.getText();
        String lastName = lastNameField.getText();
        String patronymic = patronymicField.getText();
        String phone = phoneField.getText();
        String address = addressField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || name.isEmpty() || lastName.isEmpty() || phone.isEmpty() || address.isEmpty()) {
            showAlert("Ошибка", "Заполните все обязательные поля!");
            return;
        }

        currentStudent.setName(name);
        currentStudent.setLastName(lastName);
        currentStudent.setPatronymic(patronymic);
        currentStudent.setPhone(phone);
        currentStudent.setAddress(address);
        studentsModel.update(currentStudent);

        currentUser.setUsername(username);
        if (!password.isEmpty()) {
            currentUser.setPasswordHash(hashPassword(password));
        }
        usersModel.update(currentUser);

        showAlert("Успех", "Данные успешно обновлены!");
    }

    @FXML
    private void handleExpel() {
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Подтверждение");
        confirmAlert.setHeaderText("Вы уверены, что хотите отчислиться?");
        confirmAlert.setContentText("Это действие удалит все ваши данные из системы!");

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Connection conn = null;
            try {
                conn = studentsModel.getConnection(); // Предполагается, что есть метод getConnection в Students
                conn.setAutoCommit(false);

                String sqlEnrollments = "DELETE FROM enrollments WHERE student_id = ?";
                try (PreparedStatement stmt = conn.prepareStatement(sqlEnrollments)) {
                    stmt.setInt(1, currentStudent.getStudentId());
                    stmt.executeUpdate();
                }

                String sqlUser = "DELETE FROM users WHERE user_id = ?";
                try (PreparedStatement stmt = conn.prepareStatement(sqlUser)) {
                    stmt.setInt(1, currentUser.getUserId());
                    stmt.executeUpdate();
                }

                String sqlStudent = "DELETE FROM students WHERE student_id = ?";
                try (PreparedStatement stmt = conn.prepareStatement(sqlStudent)) {
                    stmt.setInt(1, currentStudent.getStudentId());
                    stmt.executeUpdate();
                }

                conn.commit();
                showAlert("Успех", "Вы успешно отчислены!");
                app.showScene("registration");
            } catch (SQLException e) {
                try {
                    if (conn != null) conn.rollback();
                } catch (SQLException ex) {
                    System.err.println("Rollback failed: " + ex.getMessage());
                }
                showAlert("Ошибка", "Не удалось отчислиться: " + e.getMessage());
            } finally {
                try {
                    if (conn != null) conn.setAutoCommit(true);
                } catch (SQLException e) {
                    System.err.println("Failed to reset auto-commit: " + e.getMessage());
                }
            }
        }
    }

    @FXML
    private void handleEnroll() {
        String electiveIdText = enrollElectiveIdField.getText();
        if (electiveIdText.isEmpty()) {
            showAlert("Ошибка", "Введите ID факультатива!");
            return;
        }

        try {
            int electiveId = Integer.parseInt(electiveIdText);
            Elective elective = electivesModel.getById(electiveId);
            if (elective == null) {
                showAlert("Ошибка", "Факультатив с ID " + electiveId + " не найден!");
                return;
            }

            List<Semester> availableSemesters = electivePlansModel.getAll().stream()
                    .filter(plan -> plan.getElectiveId() == electiveId)
                    .map(plan -> semestersModel.getById(plan.getSemesterId()))
                    .filter(semester -> semester != null)
                    .distinct()
                    .collect(Collectors.toList());

            if (availableSemesters.isEmpty()) {
                showAlert("Ошибка", "Для этого факультатива нет доступных семестров!");
                return;
            }

            Dialog<Enrollment> dialog = new Dialog<>();
            dialog.setTitle("Запись на факультатив");
            dialog.setHeaderText("Выберите семестр для факультатива: " + elective.getElectiveName());

            DialogPane dialogPane = dialog.getDialogPane();
            ComboBox<Semester> semesterComboBox = new ComboBox<>();
            semesterComboBox.setItems(FXCollections.observableArrayList(availableSemesters));
            semesterComboBox.setConverter(new javafx.util.StringConverter<Semester>() {
                @Override
                public String toString(Semester semester) {
                    return semester != null ? semester.getYear() + "-" + semester.getSemesterNumber() : "";
                }
                @Override
                public Semester fromString(String string) {
                    return null;
                }
            });

            VBox content = new VBox(10, new Label("Семестр:"), semesterComboBox);
            dialogPane.setContent(content);
            dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

            dialog.setResultConverter(button -> {
                if (button == ButtonType.OK) {
                    Semester semester = semesterComboBox.getValue();
                    if (semester != null) {
                        return new Enrollment(0, currentStudent.getStudentId(), electiveId, semester.getSemesterId(), 0, null);
                    }
                }
                return null;
            });

            Optional<Enrollment> result = dialog.showAndWait();
            result.ifPresent(enrollment -> {
                enrollmentsModel.create(enrollment);
                loadMyElectives();
                showAlert("Успех", "Запись на факультатив прошла успешно!");
            });
        } catch (NumberFormatException e) {
            showAlert("Ошибка", "Введите корректный ID факультатива!");
        }
    }

    @FXML
    private void handleFilterElectives() {
        String electiveIdText = filterElectiveIdField.getText();
        String yearText = filterYearField.getText();

        try {
            Integer electiveId = electiveIdText.isEmpty() ? null : Integer.parseInt(electiveIdText);
            Integer year = yearText.isEmpty() ? null : Integer.parseInt(yearText);

            List<ElectivePlan> filteredPlans = electivePlansModel.getAll().stream()
                    .filter(plan -> {
                        boolean matchesId = electiveId == null || plan.getElectiveId() == electiveId;
                        Semester semester = semestersModel.getById(plan.getSemesterId());
                        boolean matchesYear = year == null || (semester != null && semester.getYear() == year);
                        return matchesId && matchesYear;
                    })
                    .collect(Collectors.toList());

            allElectivesTable.setItems(FXCollections.observableArrayList(filteredPlans));
        } catch (NumberFormatException e) {
            showAlert("Ошибка", "Введите корректные числовые значения для ID или года!");
        }
    }

    @FXML
    private void handleSearchStudents() {
        String electiveIdText = electiveIdField.getText();
        String yearText = semesterYearField.getText();
        String semesterNumberText = semesterNumberField.getText();

        if (electiveIdText.isEmpty() || yearText.isEmpty() || semesterNumberText.isEmpty()) {
            showAlert("Ошибка", "Заполните все поля!");
            return;
        }

        try {
            int electiveId = Integer.parseInt(electiveIdText);
            int year = Integer.parseInt(yearText);
            int semesterNumber = Integer.parseInt(semesterNumberText);

            Semester semester = semestersModel.getAll().stream()
                    .filter(s -> s.getYear() == year && s.getSemesterNumber() == semesterNumber)
                    .findFirst().orElse(null);

            if (semester == null) {
                showAlert("Ошибка", "Семестр не найден!");
                return;
            }

            List<Enrollment> enrollments = enrollmentsModel.getAll().stream()
                    .filter(e -> e.getElectiveId() == electiveId && e.getSemesterId() == semester.getSemesterId())
                    .toList();

            List<Student> students = enrollments.stream()
                    .map(e -> studentsModel.getById(e.getStudentId()))
                    .filter(s -> s != null)
                    .toList();

            electiveStudentsTable.setItems(FXCollections.observableArrayList(students));
        } catch (NumberFormatException e) {
            showAlert("Ошибка", "Введите корректные числовые значения!");
        }
    }

    private void loadMyElectives() {
        List<Enrollment> myEnrollments = enrollmentsModel.getAll().stream()
                .filter(e -> e.getStudentId() == currentStudent.getStudentId())
                .toList();
        myElectivesTable.setItems(FXCollections.observableArrayList(myEnrollments));
    }

    private void loadAllElectives() {
        allElectivesTable.setItems(FXCollections.observableArrayList(electivePlansModel.getAll()));
    }

    private void loadAllStudents() {
        studentsTable.setItems(FXCollections.observableArrayList(studentsModel.getAll()));
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