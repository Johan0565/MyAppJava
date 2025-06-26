package myappjava;

import Models.Entities.User;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import myappjava.Controllers.LoginController;
import myappjava.Controllers.RegistrationController;
import myappjava.Controllers.StudentMainController;
import myappjava.Controllers.TeacherMainController;
import myappjava.Controllers.AdminMainController;
import javafx.scene.image.Image;
import java.io.IOException;

public class UniversityApp extends Application {

    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("University");

        try {
            Image icon = new Image(getClass().getResourceAsStream("/images/icon.png"));
            primaryStage.getIcons().add(icon);
        } catch (Exception e) {
            System.err.println("Ошибка загрузки иконки: " + e.getMessage());
        }

        // Загрузка начальной сцены (например, Login)
        showScene("login");
    }

    public void showScene(String fxmlFile, User user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/" + fxmlFile + ".fxml"));
            Pane root = loader.load();

            // Настройка контроллера
            if (fxmlFile.equals("login")) {
                LoginController controller = loader.getController();
                controller.setDependencies(root, Models.Users.getInstance(), this);
            } else if (fxmlFile.equals("adminMain")) {
                AdminMainController controller = loader.getController();
                controller.setDependencies(root, this, user);
            } else if (fxmlFile.equals("studentMain")) {
                StudentMainController controller = loader.getController();
                controller.setDependencies(root, this, user);
            } else if (fxmlFile.equals("teacherMain")) {
                TeacherMainController controller = loader.getController();
                controller.setDependencies(root, this, user);
            } else if (fxmlFile.equals("registration")) {
                RegistrationController controller = loader.getController();
                controller.setDependencies(root, this);
            }

            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Ошибка", "Не удалось загрузить сцену: " + fxmlFile);
        }
    }

    public void showScene(String fxmlFile) {
        showScene(fxmlFile, null);
    }

    private void showAlert(String title, String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
