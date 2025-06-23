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


import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TeacherMainController {
    private final Users usersModel = Users.getInstance();
    private final Students studentsModel = Students.getInstance();
    private final Enrollments enrollmentsModel = Enrollments.getInstance();
    private final Electives electivesModel = Electives.getInstance();
    private final ElectivePlans electivePlansModel = ElectivePlans.getInstance();
    private final Semesters semestersModel = Semesters.getInstance();
    private final Departments departmentsModel = Departments.getInstance();
    private UniversityApp app;
    private User currentUser;
    private Pane view;

    @FXML private TabPane tabPane;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Button saveProfileButton;
    @FXML private TableView<Enrollment> enrollmentsTable;
    @FXML private TableColumn<Enrollment, String> enrollmentIdColumn;
    @FXML private TableColumn<Enrollment, String> studentNameColumn;
    @FXML private TableColumn<Enrollment, String> electiveNameColumn;
    @FXML private TableColumn<Enrollment, String> semesterColumn;
    @FXML private TableColumn<Enrollment, String> gradeColumn;
    @FXML private TableColumn<Enrollment, String> gradeDateColumn;
    @FXML private Button editEnrollmentButton;
    @FXML private TextField filterElectiveIdField;
    @FXML private TextField filterYearField;
    @FXML private Button filterButton;
    @FXML private TableView<ElectivePlan> electivesTable;
    @FXML private TableColumn<ElectivePlan, String> electiveIdColumn;
    @FXML private TableColumn<ElectivePlan, String> electiveNameAllColumn;
    @FXML private TableColumn<ElectivePlan, String> semesterAllColumn;
    @FXML private TableColumn<ElectivePlan, String> lecturesColumn;
    @FXML private TableColumn<ElectivePlan, String> practicesColumn;
    @FXML private TableColumn<ElectivePlan, String> labsColumn;
    @FXML private TableView<Student> studentsTable;
    @FXML private TableColumn<Student, String> studentIdColumn;
    @FXML private TableColumn<Student, String> studentFullNameColumn;
    @FXML private TableColumn<Student, String> phoneColumn;
    @FXML private TableColumn<Student, String> addressColumn;
    @FXML private TextField electiveIdField;
    @FXML private TextField semesterYearField;
    @FXML private TextField semesterNumberField;
    @FXML private Button searchStudentsButton;
    @FXML private TableView<Student> electiveStudentsTable;
    @FXML private TableColumn<Student, String> electiveStudentIdColumn;
    @FXML private TableColumn<Student, String> electiveStudentNameColumn;
    @FXML private TableView<Department> departmentsTable;
    @FXML private TableColumn<Department, String> departmentNameColumn;
    @FXML private TableColumn<Department, String> electiveNameDeptColumn;

    public TeacherMainController() {
        this.app = null;
    }

    public void setDependencies(Pane view, UniversityApp app, User user) {
        System.out.println("Setting dependencies: user = " + (user != null ? user.getUsername() : "null"));
        this.view = view;
        this.app = app;
        this.currentUser = user;
        initialize();
    }

    @FXML
    private void initialize() {
        if (currentUser == null) {
            showAlert("Ошибка", "Пользователь не инициализирован!");
            return;
        }
        System.out.println("Initializing TeacherMainController for user: " + currentUser.getUsername());

        usernameField.setText(currentUser.getUsername());

        enrollmentIdColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getEnrollmentsId())));
        studentNameColumn.setCellValueFactory(cellData -> {
            Student student = studentsModel.getById(cellData.getValue().getStudentId());
            return new SimpleStringProperty(student != null ? student.getLastName() + " " + student.getName() : "");
        });
        electiveNameColumn.setCellValueFactory(cellData -> {
            Elective elective = electivesModel.getById(cellData.getValue().getElectiveId());
            return new SimpleStringProperty(elective != null ? elective.getElectiveName() : "");
        });
        semesterColumn.setCellValueFactory(cellData -> {
            Semester semester = semestersModel.getById(cellData.getValue().getSemesterId());
            return new SimpleStringProperty(semester != null ? semester.getYear() + "-" + semester.getSemesterNumber() : "");
        });
        gradeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getGrade() > 0 ? String.valueOf(cellData.getValue().getGrade()) : "Нет оценки"
        ));
        gradeDateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getGradeDate() != null ? cellData.getValue().getGradeDate().toString() : ""
        ));
        loadEnrollments();

        // Инициализация таблицы факультативов
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
        loadElectives();

        // Инициализация таблицы всех студентов
        studentIdColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getStudentId())));
        studentFullNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getLastName() + " " + cellData.getValue().getName() + " " +
                        (cellData.getValue().getPatronymic() != null ? cellData.getValue().getPatronymic() : "")
        ));
        phoneColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPhone()));
        addressColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAddress()));
        loadAllStudents();

        // Инициализация таблицы студентов на факультативе
        electiveStudentIdColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getStudentId())));
        electiveStudentNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getLastName() + " " + cellData.getValue().getName() + " " +
                        (cellData.getValue().getPatronymic() != null ? cellData.getValue().getPatronymic() : "")
        ));

        // Инициализация таблицы кафедр
        departmentNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDepartmentName()));
        electiveNameDeptColumn.setCellValueFactory(cellData -> {
            Elective elective = electivesModel.getById(cellData.getValue().getElectiveId());
            return new SimpleStringProperty(elective != null ? elective.getElectiveName() : "");
        });
        loadDepartments();
    }

    @FXML
    private void handleSaveProfile() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty()) {
            showAlert("Ошибка", "Введите имя пользователя!");
            return;
        }

        currentUser.setUsername(username);
        if (!password.isEmpty()) {
            currentUser.setPasswordHash(hashPassword(password));
        }
        usersModel.update(currentUser);
        showAlert("Успех", "Данные успешно обновлены!");
    }

    @FXML
    private void handleEditEnrollment() {
        Enrollment selectedEnrollment = enrollmentsTable.getSelectionModel().getSelectedItem();
        if (selectedEnrollment == null) {
            showAlert("Ошибка", "Выберите запись для редактирования!");
            return;
        }

        Dialog<Enrollment> dialog = new Dialog<>();
        dialog.setTitle("Редактировать запись");
        dialog.setHeaderText("Измените оценку и дату");

        DialogPane dialogPane = dialog.getDialogPane();
        TextField gradeField = new TextField(String.valueOf(selectedEnrollment.getGrade()));
        TextField dateField = new TextField(selectedEnrollment.getGradeDate() != null ? selectedEnrollment.getGradeDate().toString() : "");
        dateField.setPromptText("ГГГГ-ММ-ДД");

        VBox content = new VBox(10, new Label("Оценка (2-5):"), gradeField, new Label("Дата (ГГГГ-ММ-ДД):"), dateField);
        dialogPane.setContent(content);
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(button -> {
            if (button == ButtonType.OK) {
                try {
                    int grade = Integer.parseInt(gradeField.getText());
                    if (grade < 2 || grade > 5) {
                        throw new IllegalArgumentException("Оценка должна быть от 2 до 5!");
                    }
                    Date gradeDate = null;
                    if (!dateField.getText().isEmpty()) {
                        gradeDate = Date.valueOf(dateField.getText());
                    }
                    return new Enrollment(
                            selectedEnrollment.getEnrollmentsId(),
                            selectedEnrollment.getStudentId(),
                            selectedEnrollment.getElectiveId(),
                            selectedEnrollment.getSemesterId(),
                            grade,
                            gradeDate
                    );
                } catch (IllegalArgumentException e) {
                    showAlert("Ошибка", e.getMessage());
                    return null;
                }
            }
            return null;
        });

        Optional<Enrollment> result = dialog.showAndWait();
        result.ifPresent(enrollment -> {
            enrollmentsModel.update(enrollment);
            loadEnrollments();
            showAlert("Успех", "Запись обновлена!");
        });
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

            electivesTable.setItems(FXCollections.observableArrayList(filteredPlans));
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

    private void loadEnrollments() {
        enrollmentsTable.setItems(FXCollections.observableArrayList(enrollmentsModel.getAll()));
    }

    private void loadElectives() {
        electivesTable.setItems(FXCollections.observableArrayList(electivePlansModel.getAll()));
    }

    private void loadAllStudents() {
        studentsTable.setItems(FXCollections.observableArrayList(studentsModel.getAll()));
    }

    private void loadDepartments() {
        List<Department> sortedDepartments = departmentsModel.getAll().stream()
                .sorted((d1, d2) -> d1.getDepartmentName().compareTo(d2.getDepartmentName()))
                .collect(Collectors.toList());
        departmentsTable.setItems(FXCollections.observableArrayList(sortedDepartments));
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