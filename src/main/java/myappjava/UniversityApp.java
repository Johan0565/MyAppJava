package myappjava;

import Models.Users;
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

import java.util.HashMap;
import java.util.Map;

public class UniversityApp extends Application {
    private Stage primaryStage;
    private Map<String, Scene> scenes = new HashMap<>();
    private Users usersModel = Users.getInstance();

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("University App");

        // Загрузка Login.fxml
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Login.fxml"));
        Pane view = loader.load();
        LoginController loginController = loader.getController();
        loginController.setDependencies(view, usersModel, this);
        scenes.put("login", new Scene(view));

        // Загрузка Registration.fxml
        loader = new FXMLLoader(getClass().getResource("/fxml/Registration.fxml"));
        view = loader.load();
        RegistrationController regController = loader.getController();
        regController.setDependencies(view, this);
        scenes.put("registration", new Scene(view));

        showScene("login");
    }

    public void showScene(String sceneName, User user) {
        Scene scene = scenes.get(sceneName);
        if (sceneName.equals("studentMain") && user != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/StudentMain.fxml"));
                System.out.println("Loading StudentMain.fxml: " + getClass().getResource("/fxml/StudentMain.fxml"));
                Pane view = loader.load();
                StudentMainController controller = loader.getController();
                controller.setDependencies(view, this, user);
                scene = new Scene(view);
                scenes.put("studentMain", scene);
            } catch (Exception e) {
                System.err.println("Failed to load studentMain: " + e.getMessage());
                e.printStackTrace();
                return;
            }
        } else if (sceneName.equals("teacherMain") && user != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/TeacherMain.fxml"));
                System.out.println("Loading TeacherMain.fxml: " + getClass().getResource("/fxml/TeacherMain.fxml"));
                Pane view = loader.load();
                TeacherMainController controller = loader.getController();
                controller.setDependencies(view, this, user);
                scene = new Scene(view);
                scenes.put("teacherMain", scene);
            } catch (Exception e) {
                System.err.println("Failed to load teacherMain: " + e.getMessage());
                e.printStackTrace();
                return;
            }
        } else if (sceneName.equals("adminMain") && user != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AdminMain.fxml"));
                System.out.println("Loading AdminMain.fxml: " + getClass().getResource("/fxml/AdminMain.fxml"));
                Pane view = loader.load();
                AdminMainController controller = loader.getController();
                controller.setDependencies(view, this, user);
                scene = new Scene(view);
                scenes.put("adminMain", scene);
            } catch (Exception e) {
                System.err.println("Failed to load adminMain: " + e.getMessage());
                e.printStackTrace();
                return;
            }
        }
        if (scene != null) {
            primaryStage.setScene(scene);
            primaryStage.show();
        } else {
            System.err.println("Scene not found: " + sceneName);
        }
    }

    public void showScene(String sceneName) {
        showScene(sceneName, null);
    }

    public static void main(String[] args) {
        launch(args);
    }
}