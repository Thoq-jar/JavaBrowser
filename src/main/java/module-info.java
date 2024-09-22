module dev.thoq.purrooser {
  requires javafx.fxml;
  requires javafx.web;
  requires static lombok;


  opens dev.thoq.purrooser to javafx.fxml;
  exports dev.thoq.purrooser;
}