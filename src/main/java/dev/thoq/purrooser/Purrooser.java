package dev.thoq.purrooser;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Purrooser extends Application {
  @Override
  public void start(Stage stage) throws IOException {
    FXMLLoader fxmlLoader = new FXMLLoader(Purrooser.class.getResource("layout.fxml"));
    Scene scene = new Scene(fxmlLoader.load(), 1600, 900);
    stage.setTitle("Purrooser");
    stage.setScene(scene);
    stage.show();
  }

  public static void main(String[] args) {
    launch();
  }
}
