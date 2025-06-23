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

public class AdminMainController {
    private final Users usersModel = Users.getInstance();
    private final Students studentsModel = Students.getInstance();
    private final Enrollments enrollmentsModel = Enrollments.getInstance();
    private final Electives electivesModel = Electives.getInstance();
    private final ElectivePlans electivePlansModel = ElectivePlans.getInstance();
    private final Semesters semestersModel = Semesters.getInstance();
    private final Departments departmentsModel = Departments.getInstance();
    private final Roles rolesModel = Roles.getInstance();
    private UniversityApp app;
    private User currentUser;
    private Pane view;

    @FXML private TabPane tabPane;
    @FXML private TextField masterKeyField;
    @FXML private Button changeMasterKeyButton;
    @FXML private TextField personalPasswordField;
    @FXML private Button changePersonalPasswordButton;
    @FXML private TextField userIdField;
    @FXML private TextField userPasswordField;
    @FXML private Button changeUserPasswordButton;
    @FXML private TextField filterUserIdField;
    @FXML private TextField filterUsernameField;
    @FXML private Button filterUsersButton;
    @FXML private TableView<User> usersTable;
    @FXML private TableColumn<User, String> userIdColumn;
    @FXML private TableColumn<User, String> usernameColumn;
    @FXML private TableColumn<User, String> roleIdColumn;
    @FXML private TableColumn<User, String> studentIdColumn;
    @FXML private Button createUserButton;
    @FXML private Button editUserButton;
    @FXML private Button deleteUserButton;
    @FXML private TextField filterStudentIdField;
    @FXML private TextField filterStudentNameField;
    @FXML private Button filterStudentsButton;
    @FXML private TableView<Student> studentsTable;
    @FXML private TableColumn<Student, String> studentIdTableColumn;
    @FXML private TableColumn<Student, String> nameColumn;
    @FXML private TableColumn<Student, String> lastNameColumn;
    @FXML private TableColumn<Student, String> patronymicColumn;
    @FXML private TableColumn<Student, String> phoneColumn;
    @FXML private TableColumn<Student, String> addressColumn;
    @FXML private Button createStudentButton;
    @FXML private Button editStudentButton;
    @FXML private Button deleteStudentButton;
    @FXML private TextField filterEnrollmentIdField;
    @FXML private TextField filterEnrollmentStudentIdField;
    @FXML private Button filterEnrollmentsButton;
    @FXML private TableView<Enrollment> enrollmentsTable;
    @FXML private TableColumn<Enrollment, String> enrollmentIdColumn;
    @FXML private TableColumn<Enrollment, String> enrollmentStudentIdColumn;
    @FXML private TableColumn<Enrollment, String> enrollmentElectiveIdColumn;
    @FXML private TableColumn<Enrollment, String> enrollmentSemesterIdColumn;
    @FXML private TableColumn<Enrollment, String> gradeColumn;
    @FXML private TableColumn<Enrollment, String> gradeDateColumn;
    @FXML private Button createEnrollmentButton;
    @FXML private Button editEnrollmentButton;
    @FXML private Button deleteEnrollmentButton;
    @FXML private TextField filterElectiveIdField;
    @FXML private TextField filterElectiveNameField;
    @FXML private Button filterElectivesButton;
    @FXML private TableView<Elective> electivesTable;
    @FXML private TableColumn<Elective, String> electiveIdTableColumn;
    @FXML private TableColumn<Elective, String> electiveNameColumn;
    @FXML private Button createElectiveButton;
    @FXML private Button editElectiveButton;
    @FXML private Button deleteElectiveButton;
    @FXML private TextField filterPlanIdField;
    @FXML private TextField filterPlanElectiveIdField;
    @FXML private Button filterElectivePlansButton;
    @FXML private TableView<ElectivePlan> electivePlansTable;
    @FXML private TableColumn<ElectivePlan, String> planIdColumn;
    @FXML private TableColumn<ElectivePlan, String> planElectiveIdColumn;
    @FXML private TableColumn<ElectivePlan, String> planSemesterIdColumn;
    @FXML private TableColumn<ElectivePlan, String> lecturesColumn;
    @FXML private TableColumn<ElectivePlan, String> practicesColumn;
    @FXML private TableColumn<ElectivePlan, String> labsColumn;
    @FXML private Button createElectivePlanButton;
    @FXML private Button editElectivePlanButton;
    @FXML private Button deleteElectivePlanButton;
    @FXML private TextField filterSemesterIdField;
    @FXML private TextField filterSemesterYearField;
    @FXML private Button filterSemestersButton;
    @FXML private TableView<Semester> semestersTable;
    @FXML private TableColumn<Semester, String> semesterIdColumn;
    @FXML private TableColumn<Semester, String> yearColumn;
    @FXML private TableColumn<Semester, String> semesterNumberColumn;
    @FXML private Button createSemesterButton;
    @FXML private Button editSemesterButton;
    @FXML private Button deleteSemesterButton;
    @FXML private TextField filterDepartmentNameField;
    @FXML private Button filterDepartmentsButton;
    @FXML private TableView<Department> departmentsTable;
    @FXML private TableColumn<Department, String> departmentNameTableColumn;
    @FXML private TableColumn<Department, String> departmentElectiveIdColumn;
    @FXML private Button createDepartmentButton;
    @FXML private Button editDepartmentButton;
    @FXML private Button deleteDepartmentButton;
    @FXML private TextField filterPostIdField;
    @FXML private TextField filterPostNameField;
    @FXML private Button filterPostsButton;
    @FXML private TableView<Post> postsTable;
    @FXML private TableColumn<Post, String> postIdColumn;
    @FXML private TableColumn<Post, String> postNameColumn;
    @FXML private Button createPostButton;
    @FXML private Button editPostButton;
    @FXML private Button deletePostButton;

    public AdminMainController() {
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
        System.out.println("Initializing AdminMainController for user: " + currentUser.getUsername());

        userIdColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getUserId())));
        usernameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUsername()));
        roleIdColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getRoleId())));
        studentIdColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStudentId() != null ? String.valueOf(cellData.getValue().getStudentId()) : ""));
        loadUsers();

        studentIdTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getStudentId())));
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        lastNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLastName()));
        patronymicColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPatronymic() != null ? cellData.getValue().getPatronymic() : ""));
        phoneColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPhone()));
        addressColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAddress()));
        loadStudents();

        enrollmentIdColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getEnrollmentsId())));
        enrollmentStudentIdColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getStudentId())));
        enrollmentElectiveIdColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getElectiveId())));
        enrollmentSemesterIdColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getSemesterId())));
        gradeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getGrade() > 0 ? String.valueOf(cellData.getValue().getGrade()) : "Нет оценки"));
        gradeDateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getGradeDate() != null ? cellData.getValue().getGradeDate().toString() : ""));
        loadEnrollments();

        electiveIdTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getElectiveId())));
        electiveNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getElectiveName()));
        loadElectives();

        planIdColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getPlanId())));
        planElectiveIdColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getElectiveId())));
        planSemesterIdColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getSemesterId())));
        lecturesColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getNumberLectures())));
        practicesColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getNumberPractices())));
        labsColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getNumberLaboratoryWork())));
        loadElectivePlans();

        semesterIdColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getSemesterId())));
        yearColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getYear())));
        semesterNumberColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getSemesterNumber())));
        loadSemesters();

        departmentNameTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDepartmentName()));
        departmentElectiveIdColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getElectiveId())));
        loadDepartments();

        postIdColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getPostId())));
        postNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPostName()));
        loadPosts();
    }

    @FXML
    private void handleChangeMasterKey() {
        String newMasterKey = masterKeyField.getText();
        if (newMasterKey.isEmpty()) {
            showAlert("Ошибка", "Введите новый мастер-ключ!");
            return;
        }
        myappjava.Controllers.LoginController.setMasterKey(newMasterKey);
        myappjava.Controllers.RegistrationController.setMasterKey(newMasterKey);
        showAlert("Успех", "Мастер-ключ успешно изменен!");
        masterKeyField.clear();
    }

    @FXML
    private void handleChangePersonalPassword() {
        String newPassword = personalPasswordField.getText();
        if (newPassword.isEmpty()) {
            showAlert("Ошибка", "Введите новый пароль!");
            return;
        }
        currentUser.setPasswordHash(hashPassword(newPassword));
        usersModel.update(currentUser);
        showAlert("Успех", "Личный пароль успешно изменен!");
        personalPasswordField.clear();
    }

    @FXML
    private void handleChangeUserPassword() {
        String userIdText = userIdField.getText();
        String newPassword = userPasswordField.getText();
        if (userIdText.isEmpty() || newPassword.isEmpty()) {
            showAlert("Ошибка", "Введите ID пользователя и новый пароль!");
            return;
        }
        try {
            int userId = Integer.parseInt(userIdText);
            User user = usersModel.getById(userId);
            if (user == null) {
                showAlert("Ошибка", "Пользователь с ID " + userId + " не найден!");
                return;
            }
            user.setPasswordHash(hashPassword(newPassword));
            usersModel.update(user);
            showAlert("Успех", "Пароль пользователя успешно изменен!");
            userIdField.clear();
            userPasswordField.clear();
        } catch (NumberFormatException e) {
            showAlert("Ошибка", "Введите корректный ID пользователя!");
        }
    }

    @FXML
    private void handleCreateUser() {
        Dialog<User> dialog = new Dialog<>();
        dialog.setTitle("Создать пользователя");
        dialog.setHeaderText("Введите данные нового пользователя");

        DialogPane dialogPane = dialog.getDialogPane();
        TextField usernameField = new TextField();
        TextField passwordField = new TextField();
        TextField roleIdField = new TextField();
        TextField studentIdField = new TextField();
        studentIdField.setPromptText("Опционально");
        VBox content = new VBox(10, new Label("Имя пользователя:"), usernameField, new Label("Пароль:"), passwordField, new Label("ID роли:"), roleIdField, new Label("ID студента (опционально):"), studentIdField);
        dialogPane.setContent(content);
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(button -> {
            if (button == ButtonType.OK) {
                try {
                    String username = usernameField.getText();
                    String password = passwordField.getText();
                    int roleId = Integer.parseInt(roleIdField.getText());
                    Integer studentId = studentIdField.getText().isEmpty() ? null : Integer.parseInt(studentIdField.getText());
                    if (username.isEmpty() || password.isEmpty()) {
                        throw new IllegalArgumentException("Имя пользователя и пароль не могут быть пустыми!");
                    }
                    return new User(0, username, hashPassword(password), roleId, studentId);
                } catch (IllegalArgumentException e) {
                    showAlert("Ошибка", "Введите корректные данные: " + e.getMessage());
                    return null;
                }
            }
            return null;
        });

        Optional<User> result = dialog.showAndWait();
        result.ifPresent(user -> {
            usersModel.create(user);
            loadUsers();
            showAlert("Успех", "Пользователь создан!");
        });
    }

    @FXML
    private void handleEditUser() {
        User selectedUser = usersTable.getSelectionModel().getSelectedItem();
        if (selectedUser == null) {
            showAlert("Ошибка", "Выберите пользователя для редактирования!");
            return;
        }
        Dialog<User> dialog = new Dialog<>();
        dialog.setTitle("Редактировать пользователя");
        dialog.setHeaderText("Измените данные пользователя");

        DialogPane dialogPane = dialog.getDialogPane();
        TextField usernameField = new TextField(selectedUser.getUsername());
        TextField roleIdField = new TextField(String.valueOf(selectedUser.getRoleId()));
        TextField studentIdField = new TextField(selectedUser.getStudentId() != null ? String.valueOf(selectedUser.getStudentId()) : "");
        VBox content = new VBox(10, new Label("Имя пользователя:"), usernameField, new Label("ID роли:"), roleIdField, new Label("ID студента (опционально):"), studentIdField);
        dialogPane.setContent(content);
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(button -> {
            if (button == ButtonType.OK) {
                try {
                    String username = usernameField.getText();
                    int roleId = Integer.parseInt(roleIdField.getText());
                    Integer studentId = studentIdField.getText().isEmpty() ? null : Integer.parseInt(studentIdField.getText());
                    if (username.isEmpty()) {
                        throw new IllegalArgumentException("Имя пользователя не может быть пустым!");
                    }
                    return new User(selectedUser.getUserId(), username, selectedUser.getPasswordHash(), roleId, studentId);
                } catch (IllegalArgumentException e) {
                    showAlert("Ошибка", "Введите корректные данные: " + e.getMessage());
                    return null;
                }
            }
            return null;
        });

        Optional<User> result = dialog.showAndWait();
        result.ifPresent(user -> {
            usersModel.update(user);
            loadUsers();
            showAlert("Успех", "Пользователь обновлен!");
        });
    }

    @FXML
    private void handleDeleteUser() {
        User selectedUser = usersTable.getSelectionModel().getSelectedItem();
        if (selectedUser == null) {
            showAlert("Ошибка", "Выберите пользователя для удаления!");
            return;
        }
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Подтверждение");
        confirmAlert.setHeaderText("Вы уверены, что хотите удалить пользователя?");
        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            usersModel.delete(selectedUser.getUserId());
            loadUsers();
            showAlert("Успех", "Пользователь удален!");
        }
    }

    @FXML
    private void handleFilterUsers() {
        String userIdText = filterUserIdField.getText();
        String usernameText = filterUsernameField.getText();

        try {
            Integer userId = userIdText.isEmpty() ? null : Integer.parseInt(userIdText);
            List<User> filteredUsers = usersModel.getAll().stream()
                    .filter(user -> {
                        boolean matchesId = userId == null || user.getUserId() == userId;
                        boolean matchesUsername = usernameText.isEmpty() || user.getUsername().toLowerCase().contains(usernameText.toLowerCase());
                        return matchesId && matchesUsername;
                    })
                    .collect(Collectors.toList());
            usersTable.setItems(FXCollections.observableArrayList(filteredUsers));
        } catch (NumberFormatException e) {
            showAlert("Ошибка", "Введите корректный ID пользователя!");
        }
    }

    @FXML
    private void handleCreateStudent() {
        Dialog<Student> dialog = new Dialog<>();
        dialog.setTitle("Создать студента");
        dialog.setHeaderText("Введите данные нового студента");

        DialogPane dialogPane = dialog.getDialogPane();
        TextField nameField = new TextField();
        TextField lastNameField = new TextField();
        TextField patronymicField = new TextField();
        TextField phoneField = new TextField();
        TextField addressField = new TextField();
        patronymicField.setPromptText("Опционально");
        VBox content = new VBox(10, new Label("Имя:"), nameField, new Label("Фамилия:"), lastNameField, new Label("Отчество (опционально):"), patronymicField, new Label("Телефон:"), phoneField, new Label("Адрес:"), addressField);
        dialogPane.setContent(content);
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(button -> {
            if (button == ButtonType.OK) {
                try {
                    String name = nameField.getText();
                    String lastName = lastNameField.getText();
                    String patronymic = patronymicField.getText();
                    String phone = phoneField.getText();
                    String address = addressField.getText();
                    if (name.isEmpty() || lastName.isEmpty() || phone.isEmpty() || address.isEmpty()) {
                        throw new IllegalArgumentException("Обязательные поля не могут быть пустыми!");
                    }
                    return new Student(0, name, lastName, patronymic, phone, address);
                } catch (IllegalArgumentException e) {
                    showAlert("Ошибка", e.getMessage());
                    return null;
                }
            }
            return null;
        });

        Optional<Student> result = dialog.showAndWait();
        result.ifPresent(student -> {
            studentsModel.create(student);
            loadStudents();
            showAlert("Успех", "Студент создан!");
        });
    }

    @FXML
    private void handleEditStudent() {
        Student selectedStudent = studentsTable.getSelectionModel().getSelectedItem();
        if (selectedStudent == null) {
            showAlert("Ошибка", "Выберите студента для редактирования!");
            return;
        }
        Dialog<Student> dialog = new Dialog<>();
        dialog.setTitle("Редактировать студента");
        dialog.setHeaderText("Измените данные студента");

        DialogPane dialogPane = dialog.getDialogPane();
        TextField nameField = new TextField(selectedStudent.getName());
        TextField lastNameField = new TextField(selectedStudent.getLastName());
        TextField patronymicField = new TextField(selectedStudent.getPatronymic());
        TextField phoneField = new TextField(selectedStudent.getPhone());
        TextField addressField = new TextField(selectedStudent.getAddress());
        VBox content = new VBox(10, new Label("Имя:"), nameField, new Label("Фамилия:"), lastNameField, new Label("Отчество (опционально):"), patronymicField, new Label("Телефон:"), phoneField, new Label("Адрес:"), addressField);
        dialogPane.setContent(content);
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(button -> {
            if (button == ButtonType.OK) {
                try {
                    String name = nameField.getText();
                    String lastName = lastNameField.getText();
                    String patronymic = patronymicField.getText();
                    String phone = phoneField.getText();
                    String address = addressField.getText();
                    if (name.isEmpty() || lastName.isEmpty() || phone.isEmpty() || address.isEmpty()) {
                        throw new IllegalArgumentException("Обязательные поля не могут быть пустыми!");
                    }
                    return new Student(selectedStudent.getStudentId(), name, lastName, patronymic, phone, address);
                } catch (IllegalArgumentException e) {
                    showAlert("Ошибка", e.getMessage());
                    return null;
                }
            }
            return null;
        });

        Optional<Student> result = dialog.showAndWait();
        result.ifPresent(student -> {
            studentsModel.update(student);
            loadStudents();
            showAlert("Успех", "Студент обновлен!");
        });
    }

    @FXML
    private void handleDeleteStudent() {
        Student selectedStudent = studentsTable.getSelectionModel().getSelectedItem();
        if (selectedStudent == null) {
            showAlert("Ошибка", "Выберите студента для удаления!");
            return;
        }
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Подтверждение");
        confirmAlert.setHeaderText("Вы уверены, что хотите удалить студента?");
        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            studentsModel.delete(selectedStudent.getStudentId());
            loadStudents();
            showAlert("Успех", "Студент удален!");
        }
    }

    @FXML
    private void handleFilterStudents() {
        String studentIdText = filterStudentIdField.getText();
        String nameText = filterStudentNameField.getText();

        try {
            Integer studentId = studentIdText.isEmpty() ? null : Integer.parseInt(studentIdText);
            List<Student> filteredStudents = studentsModel.getAll().stream()
                    .filter(student -> {
                        boolean matchesId = studentId == null || student.getStudentId() == studentId;
                        boolean matchesName = nameText.isEmpty() ||
                                student.getName().toLowerCase().contains(nameText.toLowerCase()) ||
                                student.getLastName().toLowerCase().contains(nameText.toLowerCase());
                        return matchesId && matchesName;
                    })
                    .collect(Collectors.toList());
            studentsTable.setItems(FXCollections.observableArrayList(filteredStudents));
        } catch (NumberFormatException e) {
            showAlert("Ошибка", "Введите корректный ID студента!");
        }
    }

    @FXML
    private void handleCreateEnrollment() {
        Dialog<Enrollment> dialog = new Dialog<>();
        dialog.setTitle("Создать запись");
        dialog.setHeaderText("Введите данные новой записи");

        DialogPane dialogPane = dialog.getDialogPane();
        TextField studentIdField = new TextField();
        TextField electiveIdField = new TextField();
        TextField semesterIdField = new TextField();
        TextField gradeField = new TextField("0");
        TextField gradeDateField = new TextField();
        gradeDateField.setPromptText("ГГГГ-ММ-ДД, опционально");
        VBox content = new VBox(10, new Label("ID студента:"), studentIdField, new Label("ID факультатива:"), electiveIdField, new Label("ID семестра:"), semesterIdField, new Label("Оценка (2-5, 0 для отсутствия):"), gradeField, new Label("Дата оценки (ГГГГ-ММ-ДД, опционально):"), gradeDateField);
        dialogPane.setContent(content);
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(button -> {
            if (button == ButtonType.OK) {
                try {
                    int studentId = Integer.parseInt(studentIdField.getText());
                    int electiveId = Integer.parseInt(electiveIdField.getText());
                    int semesterId = Integer.parseInt(semesterIdField.getText());
                    int grade = Integer.parseInt(gradeField.getText());
                    if (grade != 0 && (grade < 2 || grade > 5)) {
                        throw new IllegalArgumentException("Оценка должна быть от 2 до 5 или 0!");
                    }
                    Date gradeDate = gradeDateField.getText().isEmpty() ? null : Date.valueOf(gradeDateField.getText());
                    return new Enrollment(0, studentId, electiveId, semesterId, grade, gradeDate);
                } catch (IllegalArgumentException e) {
                    showAlert("Ошибка", "Введите корректные данные: " + e.getMessage());
                    return null;
                }
            }
            return null;
        });

        Optional<Enrollment> result = dialog.showAndWait();
        result.ifPresent(enrollment -> {
            enrollmentsModel.create(enrollment);
            loadEnrollments();
            showAlert("Успех", "Запись создана!");
        });
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
        dialog.setHeaderText("Измените данные записи");

        DialogPane dialogPane = dialog.getDialogPane();
        TextField studentIdField = new TextField(String.valueOf(selectedEnrollment.getStudentId()));
        TextField electiveIdField = new TextField(String.valueOf(selectedEnrollment.getElectiveId()));
        TextField semesterIdField = new TextField(String.valueOf(selectedEnrollment.getSemesterId()));
        TextField gradeField = new TextField(String.valueOf(selectedEnrollment.getGrade()));
        TextField gradeDateField = new TextField(selectedEnrollment.getGradeDate() != null ? selectedEnrollment.getGradeDate().toString() : "");
        gradeDateField.setPromptText("ГГГГ-ММ-ДД");
        VBox content = new VBox(10, new Label("ID студента:"), studentIdField, new Label("ID факультатива:"), electiveIdField, new Label("ID семестра:"), semesterIdField, new Label("Оценка (2-5, 0 для отсутствия):"), gradeField, new Label("Дата оценки (ГГГГ-ММ-ДД, опционально):"), gradeDateField);
        dialogPane.setContent(content);
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(button -> {
            if (button == ButtonType.OK) {
                try {
                    int studentId = Integer.parseInt(studentIdField.getText());
                    int electiveId = Integer.parseInt(electiveIdField.getText());
                    int semesterId = Integer.parseInt(semesterIdField.getText());
                    int grade = Integer.parseInt(gradeField.getText());
                    if (grade != 0 && (grade < 2 || grade > 5)) {
                        throw new IllegalArgumentException("Оценка должна быть от 2 до 5 или 0!");
                    }
                    Date gradeDate = gradeDateField.getText().isEmpty() ? null : Date.valueOf(gradeDateField.getText());
                    return new Enrollment(selectedEnrollment.getEnrollmentsId(), studentId, electiveId, semesterId, grade, gradeDate);
                } catch (IllegalArgumentException e) {
                    showAlert("Ошибка", "Введите корректные данные: " + e.getMessage());
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
    private void handleDeleteEnrollment() {
        Enrollment selectedEnrollment = enrollmentsTable.getSelectionModel().getSelectedItem();
        if (selectedEnrollment == null) {
            showAlert("Ошибка", "Выберите запись для удаления!");
            return;
        }
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Подтверждение");
        confirmAlert.setHeaderText("Вы уверены, что хотите удалить запись?");
        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            enrollmentsModel.delete(selectedEnrollment.getEnrollmentsId());
            loadEnrollments();
            showAlert("Успех", "Запись удалена!");
        }
    }

    @FXML
    private void handleFilterEnrollments() {
        String enrollmentIdText = filterEnrollmentIdField.getText();
        String studentIdText = filterEnrollmentStudentIdField.getText();

        try {
            Integer enrollmentId = enrollmentIdText.isEmpty() ? null : Integer.parseInt(enrollmentIdText);
            Integer studentId = studentIdText.isEmpty() ? null : Integer.parseInt(studentIdText);
            List<Enrollment> filteredEnrollments = enrollmentsModel.getAll().stream()
                    .filter(enrollment -> {
                        boolean matchesId = enrollmentId == null || enrollment.getEnrollmentsId() == enrollmentId;
                        boolean matchesStudentId = studentId == null || enrollment.getStudentId() == studentId;
                        return matchesId && matchesStudentId;
                    })
                    .collect(Collectors.toList());
            enrollmentsTable.setItems(FXCollections.observableArrayList(filteredEnrollments));
        } catch (NumberFormatException e) {
            showAlert("Ошибка", "Введите корректные ID!");
        }
    }

    @FXML
    private void handleCreateElective() {
        Dialog<Elective> dialog = new Dialog<>();
        dialog.setTitle("Создать факультатив");
        dialog.setHeaderText("Введите данные нового факультатива");

        DialogPane dialogPane = dialog.getDialogPane();
        TextField nameField = new TextField();
        VBox content = new VBox(10, new Label("Название:"), nameField);
        dialogPane.setContent(content);
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(button -> {
            if (button == ButtonType.OK) {
                String name = nameField.getText();
                if (name.isEmpty()) {
                    showAlert("Ошибка", "Название не может быть пустым!");
                    return null;
                }
                return new Elective(0, name);
            }
            return null;
        });

        Optional<Elective> result = dialog.showAndWait();
        result.ifPresent(elective -> {
            electivesModel.create(elective);
            loadElectives();
            showAlert("Успех", "Факультатив создан!");
        });
    }

    @FXML
    private void handleEditElective() {
        Elective selectedElective = electivesTable.getSelectionModel().getSelectedItem();
        if (selectedElective == null) {
            showAlert("Ошибка", "Выберите факультатив для редактирования!");
            return;
        }
        Dialog<Elective> dialog = new Dialog<>();
        dialog.setTitle("Редактировать факультатив");
        dialog.setHeaderText("Измените данные факультатива");

        DialogPane dialogPane = dialog.getDialogPane();
        TextField nameField = new TextField(selectedElective.getElectiveName());
        VBox content = new VBox(10, new Label("Название:"), nameField);
        dialogPane.setContent(content);
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(button -> {
            if (button == ButtonType.OK) {
                String name = nameField.getText();
                if (name.isEmpty()) {
                    showAlert("Ошибка", "Название не может быть пустым!");
                    return null;
                }
                return new Elective(selectedElective.getElectiveId(), name);
            }
            return null;
        });

        Optional<Elective> result = dialog.showAndWait();
        result.ifPresent(elective -> {
            electivesModel.update(elective);
            loadElectives();
            showAlert("Успех", "Факультатив обновлен!");
        });
    }

    @FXML
    private void handleDeleteElective() {
        Elective selectedElective = electivesTable.getSelectionModel().getSelectedItem();
        if (selectedElective == null) {
            showAlert("Ошибка", "Выберите факультатив для удаления!");
            return;
        }
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Подтверждение");
        confirmAlert.setHeaderText("Вы уверены, что хотите удалить факультатив?");
        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            electivesModel.delete(selectedElective.getElectiveId());
            loadElectives();
            showAlert("Успех", "Факультатив удален!");
        }
    }

    @FXML
    private void handleFilterElectives() {
        String electiveIdText = filterElectiveIdField.getText();
        String nameText = filterElectiveNameField.getText();

        try {
            Integer electiveId = electiveIdText.isEmpty() ? null : Integer.parseInt(electiveIdText);
            List<Elective> filteredElectives = electivesModel.getAll().stream()
                    .filter(elective -> {
                        boolean matchesId = electiveId == null || elective.getElectiveId() == electiveId;
                        boolean matchesName = nameText.isEmpty() || elective.getElectiveName().toLowerCase().contains(nameText.toLowerCase());
                        return matchesId && matchesName;
                    })
                    .collect(Collectors.toList());
            electivesTable.setItems(FXCollections.observableArrayList(filteredElectives));
        } catch (NumberFormatException e) {
            showAlert("Ошибка", "Введите корректный ID факультатива!");
        }
    }

    @FXML
    private void handleCreateElectivePlan() {
        Dialog<ElectivePlan> dialog = new Dialog<>();
        dialog.setTitle("Создать план факультатива");
        dialog.setHeaderText("Введите данные нового плана");

        DialogPane dialogPane = dialog.getDialogPane();
        TextField electiveIdField = new TextField();
        TextField semesterIdField = new TextField();
        TextField lecturesField = new TextField();
        TextField practicesField = new TextField();
        TextField labsField = new TextField();
        VBox content = new VBox(10, new Label("ID факультатива:"), electiveIdField, new Label("ID семестра:"), semesterIdField, new Label("Лекции:"), lecturesField, new Label("Практики:"), practicesField, new Label("Лабы:"), labsField);
        dialogPane.setContent(content);
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(button -> {
            if (button == ButtonType.OK) {
                try {
                    int electiveId = Integer.parseInt(electiveIdField.getText());
                    int semesterId = Integer.parseInt(semesterIdField.getText());
                    int lectures = Integer.parseInt(lecturesField.getText());
                    int practices = Integer.parseInt(practicesField.getText());
                    int labs = Integer.parseInt(labsField.getText());
                    return new ElectivePlan(0, electiveId, semesterId, lectures, practices, labs);
                } catch (NumberFormatException e) {
                    showAlert("Ошибка", "Введите корректные числовые значения!");
                    return null;
                }
            }
            return null;
        });

        Optional<ElectivePlan> result = dialog.showAndWait();
        result.ifPresent(plan -> {
            electivePlansModel.create(plan);
            loadElectivePlans();
            showAlert("Успех", "План факультатива создан!");
        });
    }

    @FXML
    private void handleEditElectivePlan() {
        ElectivePlan selectedPlan = electivePlansTable.getSelectionModel().getSelectedItem();
        if (selectedPlan == null) {
            showAlert("Ошибка", "Выберите план для редактирования!");
            return;
        }
        Dialog<ElectivePlan> dialog = new Dialog<>();
        dialog.setTitle("Редактировать план факультатива");
        dialog.setHeaderText("Измените данные плана");

        DialogPane dialogPane = dialog.getDialogPane();
        TextField electiveIdField = new TextField(String.valueOf(selectedPlan.getElectiveId()));
        TextField semesterIdField = new TextField(String.valueOf(selectedPlan.getSemesterId()));
        TextField lecturesField = new TextField(String.valueOf(selectedPlan.getNumberLectures()));
        TextField practicesField = new TextField(String.valueOf(selectedPlan.getNumberPractices()));
        TextField labsField = new TextField(String.valueOf(selectedPlan.getNumberLaboratoryWork()));
        VBox content = new VBox(10, new Label("ID факультатива:"), electiveIdField, new Label("ID семестра:"), semesterIdField, new Label("Лекции:"), lecturesField, new Label("Практики:"), practicesField, new Label("Лабы:"), labsField);
        dialogPane.setContent(content);
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(button -> {
            if (button == ButtonType.OK) {
                try {
                    int electiveId = Integer.parseInt(electiveIdField.getText());
                    int semesterId = Integer.parseInt(semesterIdField.getText());
                    int lectures = Integer.parseInt(lecturesField.getText());
                    int practices = Integer.parseInt(practicesField.getText());
                    int labs = Integer.parseInt(labsField.getText());
                    return new ElectivePlan(selectedPlan.getPlanId(), electiveId, semesterId, lectures, practices, labs);
                } catch (NumberFormatException e) {
                    showAlert("Ошибка", "Введите корректные числовые значения!");
                    return null;
                }
            }
            return null;
        });

        Optional<ElectivePlan> result = dialog.showAndWait();
        result.ifPresent(plan -> {
            electivePlansModel.update(plan);
            loadElectivePlans();
            showAlert("Успех", "План факультатива обновлен!");
        });
    }

    @FXML
    private void handleDeleteElectivePlan() {
        ElectivePlan selectedPlan = electivePlansTable.getSelectionModel().getSelectedItem();
        if (selectedPlan == null) {
            showAlert("Ошибка", "Выберите план для удаления!");
            return;
        }
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Подтверждение");
        confirmAlert.setHeaderText("Вы уверены, что хотите удалить план?");
        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            electivePlansModel.delete(selectedPlan.getPlanId());
            loadElectivePlans();
            showAlert("Успех", "План факультатива удален!");
        }
    }

    @FXML
    private void handleFilterElectivePlans() {
        String planIdText = filterPlanIdField.getText();
        String electiveIdText = filterPlanElectiveIdField.getText();

        try {
            Integer planId = planIdText.isEmpty() ? null : Integer.parseInt(planIdText);
            Integer electiveId = electiveIdText.isEmpty() ? null : Integer.parseInt(electiveIdText);
            List<ElectivePlan> filteredPlans = electivePlansModel.getAll().stream()
                    .filter(plan -> {
                        boolean matchesPlanId = planId == null || plan.getPlanId() == planId;
                        boolean matchesElectiveId = electiveId == null || plan.getElectiveId() == electiveId;
                        return matchesPlanId && matchesElectiveId;
                    })
                    .collect(Collectors.toList());
            electivePlansTable.setItems(FXCollections.observableArrayList(filteredPlans));
        } catch (NumberFormatException e) {
            showAlert("Ошибка", "Введите корректные ID!");
        }
    }

    @FXML
    private void handleCreateSemester() {
        Dialog<Semester> dialog = new Dialog<>();
        dialog.setTitle("Создать семестр");
        dialog.setHeaderText("Введите данные нового семестра");

        DialogPane dialogPane = dialog.getDialogPane();
        TextField yearField = new TextField();
        TextField semesterNumberField = new TextField();
        VBox content = new VBox(10, new Label("Год:"), yearField, new Label("Номер семестра:"), semesterNumberField);
        dialogPane.setContent(content);
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(button -> {
            if (button == ButtonType.OK) {
                try {
                    int year = Integer.parseInt(yearField.getText());
                    int semesterNumber = Integer.parseInt(semesterNumberField.getText());
                    return new Semester(0, year, semesterNumber);
                } catch (NumberFormatException e) {
                    showAlert("Ошибка", "Введите корректные числовые значения!");
                    return null;
                }
            }
            return null;
        });

        Optional<Semester> result = dialog.showAndWait();
        result.ifPresent(semester -> {
            semestersModel.create(semester);
            loadSemesters();
            showAlert("Успех", "Семестр создан!");
        });
    }

    @FXML
    private void handleEditSemester() {
        Semester selectedSemester = semestersTable.getSelectionModel().getSelectedItem();
        if (selectedSemester == null) {
            showAlert("Ошибка", "Выберите семестр для редактирования!");
            return;
        }
        Dialog<Semester> dialog = new Dialog<>();
        dialog.setTitle("Редактировать семестр");
        dialog.setHeaderText("Измените данные семестра");

        DialogPane dialogPane = dialog.getDialogPane();
        TextField yearField = new TextField(String.valueOf(selectedSemester.getYear()));
        TextField semesterNumberField = new TextField(String.valueOf(selectedSemester.getSemesterNumber()));
        VBox content = new VBox(10, new Label("Год:"), yearField, new Label("Номер семестра:"), semesterNumberField);
        dialogPane.setContent(content);
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(button -> {
            if (button == ButtonType.OK) {
                try {
                    int year = Integer.parseInt(yearField.getText());
                    int semesterNumber = Integer.parseInt(semesterNumberField.getText());
                    return new Semester(selectedSemester.getSemesterId(), year, semesterNumber);
                } catch (NumberFormatException e) {
                    showAlert("Ошибка", "Введите корректные числовые значения!");
                    return null;
                }
            }
            return null;
        });

        Optional<Semester> result = dialog.showAndWait();
        result.ifPresent(semester -> {
            semestersModel.update(semester);
            loadSemesters();
            showAlert("Успех", "Семестр обновлен!");
        });
    }

    @FXML
    private void handleDeleteSemester() {
        Semester selectedSemester = semestersTable.getSelectionModel().getSelectedItem();
        if (selectedSemester == null) {
            showAlert("Ошибка", "Выберите семестр для удаления!");
            return;
        }
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Подтверждение");
        confirmAlert.setHeaderText("Вы уверены, что хотите удалить семестр?");
        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            semestersModel.delete(selectedSemester.getSemesterId());
            loadSemesters();
            showAlert("Успех", "Семестр удален!");
        }
    }

    @FXML
    private void handleFilterSemesters() {
        String semesterIdText = filterSemesterIdField.getText();
        String yearText = filterSemesterYearField.getText();

        try {
            Integer semesterId = semesterIdText.isEmpty() ? null : Integer.parseInt(semesterIdText);
            Integer year = yearText.isEmpty() ? null : Integer.parseInt(yearText);
            List<Semester> filteredSemesters = semestersModel.getAll().stream()
                    .filter(semester -> {
                        boolean matchesId = semesterId == null || semester.getSemesterId() == semesterId;
                        boolean matchesYear = year == null || semester.getYear() == year;
                        return matchesId && matchesYear;
                    })
                    .collect(Collectors.toList());
            semestersTable.setItems(FXCollections.observableArrayList(filteredSemesters));
        } catch (NumberFormatException e) {
            showAlert("Ошибка", "Введите корректные числовые значения!");
        }
    }

    @FXML
    private void handleCreateDepartment() {
        Dialog<Department> dialog = new Dialog<>();
        dialog.setTitle("Создать кафедру");
        dialog.setHeaderText("Введите данные новой кафедры");

        DialogPane dialogPane = dialog.getDialogPane();
        TextField nameField = new TextField();
        TextField electiveIdField = new TextField();
        VBox content = new VBox(10, new Label("Название:"), nameField, new Label("ID факультатива:"), electiveIdField);
        dialogPane.setContent(content);
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(button -> {
            if (button == ButtonType.OK) {
                try {
                    String name = nameField.getText();
                    int electiveId = Integer.parseInt(electiveIdField.getText());
                    if (name.isEmpty()) {
                        throw new IllegalArgumentException("Название не может быть пустым!");
                    }
                    return new Department(name, electiveId);
                } catch (IllegalArgumentException e) {
                    showAlert("Ошибка", "Введите корректные данные: " + e.getMessage());
                    return null;
                }
            }
            return null;
        });

        Optional<Department> result = dialog.showAndWait();
        result.ifPresent(department -> {
            departmentsModel.create(department);
            loadDepartments();
            showAlert("Успех", "Кафедра создана!");
        });
    }

    @FXML
    private void handleEditDepartment() {
        Department selectedDepartment = departmentsTable.getSelectionModel().getSelectedItem();
        if (selectedDepartment == null) {
            showAlert("Ошибка", "Выберите кафедру для редактирования!");
            return;
        }
        Dialog<Department> dialog = new Dialog<>();
        dialog.setTitle("Редактировать кафедру");
        dialog.setHeaderText("Измените данные кафедры");

        DialogPane dialogPane = dialog.getDialogPane();
        TextField nameField = new TextField(selectedDepartment.getDepartmentName());
        TextField electiveIdField = new TextField(String.valueOf(selectedDepartment.getElectiveId()));
        VBox content = new VBox(10, new Label("Название:"), nameField, new Label("ID факультатива:"), electiveIdField);
        dialogPane.setContent(content);
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(button -> {
            if (button == ButtonType.OK) {
                try {
                    String name = nameField.getText();
                    int electiveId = Integer.parseInt(electiveIdField.getText());
                    if (name.isEmpty()) {
                        throw new IllegalArgumentException("Название не может быть пустым!");
                    }
                    return new Department(name, electiveId);
                } catch (IllegalArgumentException e) {
                    showAlert("Ошибка", "Введите корректные данные: " + e.getMessage());
                    return null;
                }
            }
            return null;
        });

        Optional<Department> result = dialog.showAndWait();
        result.ifPresent(department -> {
            departmentsModel.update(department);
            loadDepartments();
            showAlert("Успех", "Кафедра обновлена!");
        });
    }

    @FXML
    private void handleDeleteDepartment() {
        Department selectedDepartment = departmentsTable.getSelectionModel().getSelectedItem();
        if (selectedDepartment == null) {
            showAlert("Ошибка", "Выберите кафедру для удаления!");
            return;
        }
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Подтверждение");
        confirmAlert.setHeaderText("Вы уверены, что хотите удалить кафедру?");
        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            departmentsModel.delete(selectedDepartment.getDepartmentName());
            loadDepartments();
            showAlert("Успех", "Кафедра удалена!");
        }
    }

    @FXML
    private void handleFilterDepartments() {
        String nameText = filterDepartmentNameField.getText();
        List<Department> filteredDepartments = departmentsModel.getAll().stream()
                .filter(department -> nameText.isEmpty() || department.getDepartmentName().toLowerCase().contains(nameText.toLowerCase()))
                .collect(Collectors.toList());
        departmentsTable.setItems(FXCollections.observableArrayList(filteredDepartments));
    }

    @FXML
    private void handleCreatePost() {
        Dialog<Post> dialog = new Dialog<>();
        dialog.setTitle("Создать роль");
        dialog.setHeaderText("Введите данные новой роли");

        DialogPane dialogPane = dialog.getDialogPane();
        TextField nameField = new TextField();
        VBox content = new VBox(10, new Label("Название роли:"), nameField);
        dialogPane.setContent(content);
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(button -> {
            if (button == ButtonType.OK) {
                String name = nameField.getText();
                if (name.isEmpty()) {
                    showAlert("Ошибка", "Название роли не может быть пустым!");
                    return null;
                }
                return new Post(0, name);
            }
            return null;
        });

        Optional<Post> result = dialog.showAndWait();
        result.ifPresent(post -> {
            rolesModel.create(post);
            loadPosts();
            showAlert("Успех", "Роль создана!");
        });
    }

    @FXML
    private void handleEditPost() {
        Post selectedPost = postsTable.getSelectionModel().getSelectedItem();
        if (selectedPost == null) {
            showAlert("Ошибка", "Выберите роль для редактирования!");
            return;
        }
        Dialog<Post> dialog = new Dialog<>();
        dialog.setTitle("Редактировать роль");
        dialog.setHeaderText("Измените данные роли");

        DialogPane dialogPane = dialog.getDialogPane();
        TextField nameField = new TextField(selectedPost.getPostName());
        VBox content = new VBox(10, new Label("Название роли:"), nameField);
        dialogPane.setContent(content);
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(button -> {
            if (button == ButtonType.OK) {
                String name = nameField.getText();
                if (name.isEmpty()) {
                    showAlert("Ошибка", "Название роли не может быть пустым!");
                    return null;
                }
                return new Post(selectedPost.getPostId(), name);
            }
            return null;
        });

        Optional<Post> result = dialog.showAndWait();
        result.ifPresent(post -> {
            rolesModel.update(post);
            loadPosts();
            showAlert("Успех", "Роль обновлена!");
        });
    }

    @FXML
    private void handleDeletePost() {
        Post selectedPost = postsTable.getSelectionModel().getSelectedItem();
        if (selectedPost == null) {
            showAlert("Ошибка", "Выберите роль для удаления!");
            return;
        }
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Подтверждение");
        confirmAlert.setHeaderText("Вы уверены, что хотите удалить роль?");
        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            rolesModel.delete(selectedPost.getPostId());
            loadPosts();
            showAlert("Успех", "Роль удалена!");
        }
    }

    @FXML
    private void handleFilterPosts() {
        String postIdText = filterPostIdField.getText();
        String nameText = filterPostNameField.getText();

        try {
            Integer postId = postIdText.isEmpty() ? null : Integer.parseInt(postIdText);
            List<Post> filteredPosts = rolesModel.getAll().stream()
                    .filter(post -> {
                        boolean matchesId = postId == null || post.getPostId() == postId;
                        boolean matchesName = nameText.isEmpty() || post.getPostName().toLowerCase().contains(nameText.toLowerCase());
                        return matchesId && matchesName;
                    })
                    .collect(Collectors.toList());
            postsTable.setItems(FXCollections.observableArrayList(filteredPosts));
        } catch (NumberFormatException e) {
            showAlert("Ошибка", "Введите корректный ID роли!");
        }
    }

    private void loadUsers() {
        usersTable.setItems(FXCollections.observableArrayList(usersModel.getAll()));
    }

    private void loadStudents() {
        studentsTable.setItems(FXCollections.observableArrayList(studentsModel.getAll()));
    }

    private void loadEnrollments() {
        enrollmentsTable.setItems(FXCollections.observableArrayList(enrollmentsModel.getAll()));
    }

    private void loadElectives() {
        electivesTable.setItems(FXCollections.observableArrayList(electivesModel.getAll()));
    }

    private void loadElectivePlans() {
        electivePlansTable.setItems(FXCollections.observableArrayList(electivePlansModel.getAll()));
    }

    private void loadSemesters() {
        semestersTable.setItems(FXCollections.observableArrayList(semestersModel.getAll()));
    }

    private void loadDepartments() {
        departmentsTable.setItems(FXCollections.observableArrayList(departmentsModel.getAll()));
    }

    private void loadPosts() {
        postsTable.setItems(FXCollections.observableArrayList(rolesModel.getAll()));
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