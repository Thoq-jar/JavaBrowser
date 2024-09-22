package dev.thoq.purrooser;

import dev.thoq.purrooser.util.Handling;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;

@SuppressWarnings("CallToPrintStackTrace")
public class Controller {
  @FXML
  private WebView webView;
  private WebEngine engine;

  @FXML
  private Button reloadButton;

  private Stage primaryStage;

  public void setPrimaryStage(Stage stage) {
    this.primaryStage = stage;
    updateTitle("Home");
  }

  @FXML
  public void initialize() {
    engine = webView.getEngine();
    engine.setUserAgent(headers("User-Agent"));
    engine.setJavaScriptEnabled(Boolean.parseBoolean(headers("SetJavaScriptEnabled")));

    welcomeScreen();
    addHoverEffect(reloadButton);

    engine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
      if (newState == javafx.concurrent.Worker.State.FAILED) loadNotFoundPage();
      else if (newState == javafx.concurrent.Worker.State.RUNNING) setReloadButtonToStop();
      else if (newState == javafx.concurrent.Worker.State.SUCCEEDED) {
        setReloadButtonToReload();
        updateTitleFromPage();
      }
    });
  }

  public String headers(String header) {
    String[] headers = {
        "User-Agent", "Mozilla/5.0 {Purrooser Browser: 0.1.0; Java 22; https://github.com/Thoq-jar/Purrooser.git} (Windows NT 10.0; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/129.0.0.0 Safari/537.36 Edg/129.0.0.0",
        "SetJavaScriptEnabled", String.valueOf(true)
    };

    for (int i = 0; i < headers.length; i += 2) {
      if (headers[i].equalsIgnoreCase(header)) return headers[i + 1].trim();
    }
    return "";
  }

  private void welcomeScreen() {
    try {
      URL resource = getClass().getClassLoader().getResource("welcome.html");
      if (resource != null) engine.load(resource.toExternalForm());
      else Handling.resourceNotFound("welcome.html");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void loadNotFoundPage() {
    try {
      URL resource = getClass().getClassLoader().getResource("error.html");
      if (resource != null) engine.load(resource.toExternalForm());
      else Handling.resourceNotFound("error.html");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void setReloadButtonToStop() {
    reloadButton.setText("◼");
  }

  private void setReloadButtonToReload() {
    reloadButton.setText("↻");
  }

  @FXML
  protected void goBack() {
    if (engine.getHistory().getCurrentIndex() > 0) engine.getHistory().go(-1);
  }

  @FXML
  protected void goForward() {
    if (engine.getHistory().getCurrentIndex() < engine.getHistory().getEntries().size() - 1) engine.getHistory().go(1);
  }

  @FXML
  protected void reloadPage() {
    if (engine.getLoadWorker().getState() == javafx.concurrent.Worker.State.RUNNING) {
      engine.load("about:blank");
      setReloadButtonToReload();
    } else {
      engine.reload();
    }
  }

  @FXML
  protected void goHome() {
    welcomeScreen();
  }

  private void addHoverEffect(Button button) {
    button.addEventFilter(MouseEvent.MOUSE_ENTERED, event -> {
      FadeTransition fade = new FadeTransition(Duration.millis(200), button);
      fade.setFromValue(1.0);
      fade.setToValue(0.8);
      fade.play();
    });

    button.addEventFilter(MouseEvent.MOUSE_EXITED, event -> {
      FadeTransition fade = new FadeTransition(Duration.millis(200), button);
      fade.setFromValue(0.8);
      fade.setToValue(1.0);
      fade.play();
    });
  }

  private void updateTitle(String websiteTitle) {
    primaryStage.setTitle("Purrooser - " + websiteTitle);
  }

  private void updateTitleFromPage() {
    engine.executeScript("document.title");
    String pageTitle = (String) engine.executeScript("document.title");
    updateTitle(pageTitle != null ? pageTitle : "Untitled");
  }
}