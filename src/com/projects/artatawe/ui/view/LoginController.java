package com.projects.artatawe.ui.view;

import com.projects.artatawe.ui.LoginManager;
import com.projects.artatawe.user.User;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

/**
 * The controller for the Login dialog.
 *
 * @author Adam Thomas
 *
 */
public class LoginController
{
   private LoginManager loginManager;

   @FXML
   private ComboBox<User> userComboBox;

   @FXML
   private Button loginButton;

   @FXML
   private void initialize()
   {

      userComboBox.getSelectionModel().selectedItemProperty()
            .addListener((observable, oldValue, newValue) -> handleSelectUser());

      loginButton.disableProperty().bind(userComboBox.valueProperty().isNull() );
   }

   public void setData(ObservableList<User> users)
   {
      userComboBox.setItems(users);
   }

   public void initManager(LoginManager loginManager)
   {
      this.loginManager = loginManager;
   }

   @FXML
   private void handleRegister()
   {
      loginManager.showRegisterUserScreen();
   }

   @FXML
   private void handleLogin()
   {
      loginManager.showMainScreen();
   }

   @FXML
   private void handleSelectUser()
   {
      loginManager.setCurrentUser(userComboBox.getValue());
   }

}
