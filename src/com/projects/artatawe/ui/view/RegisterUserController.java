package com.projects.artatawe.ui.view;

import java.net.URL;

import java.util.ResourceBundle;

import com.projects.artatawe.user.User;
import com.projects.artatawe.user.UserManager;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

/**
 * This class implements the controller logic for the Register User screen.
 *
 * @author Adam Thomas
 *
 */
public class RegisterUserController implements Initializable
{
   private Stage dialogStage;
   private UserManager userManager;

   private boolean okClicked = false;

   @FXML private TextField username;
   @FXML private TextField firstName;
   @FXML private TextField lastName;
   @FXML private TextField addressLineOne;
   @FXML private TextField addressLineTwo;
   @FXML private TextField townOrCity;
   @FXML private TextField county;
   @FXML private TextField postcode;
   @FXML private TextField telephoneNumber;

   public void setDialogStage(Stage dialogStage)
   {
      this.dialogStage = dialogStage;
   }

   public void setUserManager(UserManager userManager) {
      this.userManager = userManager;
   }

   public boolean isOkClicked()
   {
      return okClicked;
   }

   @Override
   public void initialize(URL location, ResourceBundle resources)
   {

   }

   // TODO: validate user input
   private boolean isInputValid()
   {
      return true;
   }

   @FXML
   private void handleOk()
   {
      if (isInputValid()) {

         User user = new User(username.textProperty(),firstName.textProperty(), lastName.textProperty());
         userManager.addUser(user);

         okClicked = true;
         dialogStage.close();
      }
   }

   @FXML
   private void handleCancel()
   {
      dialogStage.close();
   }
}
