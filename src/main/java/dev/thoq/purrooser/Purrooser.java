package dev.thoq.purrooser;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Purrooser extends Application {
  @Override
  public void start(Stage primaryStage) throws IOException {
    FXMLLoader fxmlLoader = new FXMLLoader(Purrooser.class.getResource("layout.fxml"));
    Scene scene = new Scene(fxmlLoader.load(), 1600, 900);

    Controller controller = fxmlLoader.getController();
    controller.setPrimaryStage(primaryStage);

    primaryStage.setTitle("Purrooser - Start");
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  public static void main(String[] args) {
    launch();
  }
}