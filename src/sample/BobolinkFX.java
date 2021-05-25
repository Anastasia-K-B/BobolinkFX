package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class BobolinkFX extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{ // метод с созданием окна
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml")); // создание объекта приложения

        primaryStage.setTitle("Bobolink FX"); //задаем заголовок приложения
        primaryStage.getIcons().add(new Image("file:C:\\Users\\corob\\IdeaProjects\\Bobolink FX\\icon.png")); // иконка приложения

        primaryStage.setScene(new Scene(root)); // создание новой сцены
        primaryStage.setResizable(false); // запрет на изменение размеров окна
        primaryStage.show(); // показать окно
    }

    public static void main(String[] args) {
        launch(args);
    }
}
