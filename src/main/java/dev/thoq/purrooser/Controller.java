package dev.thoq.purrooser;

import dev.thoq.purrooser.util.Handling;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;

@SuppressWarnings({"CallToPrintStackTrace", "SameParameterValue"})
public class Controller {
  @FXML
  private TabPane tabPane;
  @FXML
  private Button reloadButton;
  @FXML
  private Button addTabButton;

  private Stage primaryStage;

  public void setPrimaryStage(Stage stage) {
    this.primaryStage = stage;
    updateTitle("Home");
  }

  @FXML
  public void initialize() {
    addNewTab("welcome.html");
    addHoverEffect(reloadButton);
    addHoverEffect(addTabButton);
    setupKeyboardShortcuts();
  }

  private void setupKeyboardShortcuts() {
    tabPane.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
      if (event.isControlDown() || event.isMetaDown()) {
        switch (event.getCode()) {
          case T:
            addNewTab("welcome.html");
            event.consume();
            break;
          case R:
            reloadPage();
            event.consume();
            break;
          case W:
            closeCurrentTab();
            event.consume();
            break;
          case Q:
            primaryStage.close();
            event.consume();
            break;
          default:
            break;
        }
      }
    });
  }

  private void addNewTab(String url) {
    WebView webView = new WebView();
    WebEngine engine = webView.getEngine();
    engine.setUserAgent(headers("User-Agent"));
    engine.setJavaScriptEnabled(true);
    loadPage(engine, url);

    Tab tab = new Tab(url);
    tab.setContent(webView);
    tabPane.getTabs().add(tab);

    engine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
      if (newState == javafx.concurrent.Worker.State.FAILED) loadNotFoundPage(webView);
      else if (newState == javafx.concurrent.Worker.State.RUNNING) setReloadButtonToStop();
      else if (newState == javafx.concurrent.Worker.State.SUCCEEDED) {
        setReloadButtonToReload();
        updateTabTitle(tab, engine);
      }
    });

    tabPane.getSelectionModel().select(tab);
  }

  private void loadPage(WebEngine engine, String url) {
    try {
      URL resource = getClass().getClassLoader().getResource(url);
      if (resource != null) engine.load(resource.toExternalForm());
      else Handling.resourceNotFound(url);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public String headers(String header) {
    String[] headers = {
        "User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36"
    };

    for (int i = 0; i < headers.length; i += 2) {
      if (headers[i].equalsIgnoreCase(header)) return headers[i + 1].trim();
    }
    return "";
  }

  private void loadNotFoundPage(WebView webView) {
    try {
      URL resource = getClass().getClassLoader().getResource("error.html");
      if (resource != null) webView.getEngine().load(resource.toExternalForm());
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
  protected void reloadPage() {
    Tab selectedTab = tabPane.getSelectionModel().getSelectedItem();
    if (selectedTab != null) {
      WebView webView = (WebView) selectedTab.getContent();
      if (webView != null) webView.getEngine().reload();
    }
  }

  @FXML
  protected void goBack() {
    Tab selectedTab = tabPane.getSelectionModel().getSelectedItem();
    if (selectedTab != null) {
      WebView webView = (WebView) selectedTab.getContent();
      if (webView != null) {
        WebEngine engine = webView.getEngine();
        if (engine.getHistory().getCurrentIndex() > 0) engine.getHistory().go(-1);
      }
    }
  }

  @FXML
  protected void goForward() {
    Tab selectedTab = tabPane.getSelectionModel().getSelectedItem();
    if (selectedTab != null) {
      WebView webView = (WebView) selectedTab.getContent();
      if (webView != null) {
        WebEngine engine = webView.getEngine();
        if (engine.getHistory().getCurrentIndex() < engine.getHistory().getEntries().size() - 1) {
          engine.getHistory().go(1);
        }
      }
    }
  }

  @FXML
  protected void goHome() {
    Tab selectedTab = tabPane.getSelectionModel().getSelectedItem();
    if (selectedTab != null) {
      WebView webView = (WebView) selectedTab.getContent();
      if (webView != null) {
        WebEngine engine = webView.getEngine();
        loadPage(engine, "welcome.html");
      }
    }
  }

  @FXML
  protected void addTab() {
    addNewTab("welcome.html");
  }

  @FXML
  protected void closeCurrentTab() {
    Tab selectedTab = tabPane.getSelectionModel().getSelectedItem();
    if (selectedTab != null) {
      tabPane.getTabs().remove(selectedTab);
    }
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

  @SuppressWarnings("unused")
  private void updateTitleFromPage(WebEngine engine) {
    String pageTitle = (String) engine.executeScript("document.title");
    updateTitle(pageTitle != null ? pageTitle : "Untitled");
  }

  private void updateTabTitle(Tab tab, WebEngine engine) {
    String pageTitle = (String) engine.executeScript("document.title");
    tab.setText(pageTitle != null ? pageTitle : "Untitled");
  }
}