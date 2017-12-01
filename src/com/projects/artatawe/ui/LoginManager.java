package com.projects.artatawe.ui;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.projects.artatawe.auction.AuctionListing;
import com.projects.artatawe.auction.Bid;
import com.projects.artatawe.ui.view.BidController;
import com.projects.artatawe.ui.view.LoginController;
import com.projects.artatawe.ui.view.MainController;
import com.projects.artatawe.ui.view.PaintingController;
import com.projects.artatawe.ui.view.RegisterUserController;
import com.projects.artatawe.user.User;
import com.projects.artatawe.user.UserManager;

import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * This class manages the screen flow from login to the main
 * artatawe dashboard. The following flow has been implemented:
 *
 * LoginScreen (add user)    -> RegisterUserScreen
 * LoginScreen (login)       -> MainScreen
 * MainScreen (bid)          -> BidDialog
 * MainScreen (add painting) -> PaintingDialog
 * MainScreen (logout)       -> LoginScreen
 *
 * @author Adam Thomas
 *
 */
public class LoginManager
{
   private Stage currentStage;
   private final Main mainApp;
   private User currentUser;

   public LoginManager(Stage stage, Main mainApp)
   {
      this.currentStage = stage;
      this.mainApp = mainApp;
   }

   public LoginController showLoginScreen()
   {
      try {
         Stage stage = new Stage();
         stage.setTitle("Artatawe : Login");

         Scene scene = new Scene(new StackPane());
         stage.setScene(scene);

         FXMLLoader loader = new FXMLLoader(getClass().getResource("view/LoginDialog.fxml"));
         Parent root = loader.load();

         scene.setRoot(root);
         LoginController loginController = loader.getController();
         loginController.initManager(this);

         currentStage.close();
         stage.show();

         this.currentStage = stage;

         loginController.setData(mainApp.getUserManager().getUsers());

         return loginController;
      } catch (IOException ex) {
         Logger.getLogger(LoginManager.class.getName()).log(Level.SEVERE, null, ex);
      }
      return null;
   }

   public void logout()
   {
      showLoginScreen();
   }

   public MainController showMainScreen()
   {

      try {
         Stage stage = new Stage();
         stage.setTitle("Artatawe");

         Scene scene = new Scene(new StackPane());
         stage.setScene(scene);

         FXMLLoader loader = new FXMLLoader(getClass().getResource("view/Main.fxml"));
         Parent root = loader.load();

         scene.setRoot(root);
         MainController mainController = loader.getController();
         mainController.init(this, mainApp.getUserManager(), mainApp.getAuctioneer());

         currentStage.close();
         stage.show();

         this.currentStage = stage;

         return mainController;
      } catch (IOException ex) {
         Logger.getLogger(LoginManager.class.getName()).log(Level.SEVERE, null, ex);
      }
      return null;
   }

   public boolean showRegisterUserScreen()
   {
      try {
         // Load the fxml file and create a new stage for the popup dialog.
         FXMLLoader loader = new FXMLLoader(getClass().getResource("view/RegisterUserDialog.fxml"));
         AnchorPane page = (AnchorPane) loader.load();

         // Create the dialog Stage.
         Stage dialogStage = new Stage();
         dialogStage.setTitle("Artatawe : Register New User");
         dialogStage.initModality(Modality.WINDOW_MODAL);
         dialogStage.initOwner(currentStage);
         Scene scene = new Scene(page);
         dialogStage.setScene(scene);

         // Set the party into the controller.
         RegisterUserController controller = loader.getController();
         controller.setDialogStage(dialogStage);
         controller.setUserManager(mainApp.getUserManager());

         // Show the dialog and wait until the user closes it
         dialogStage.showAndWait();

         return controller.isOkClicked();
      } catch (IOException e) {
         e.printStackTrace();
         return false;
      }
   }

   public void setCurrentUser(User user) {
      this.currentUser = user;
      System.out.println("info: logged in as " + user.getName() + " who has " + user.getAuctionListings().size() + " auctions");
   }

   public User getCurrentUser() {
      return currentUser;
   }

   public boolean showBidDialog(Bid bid)
   {
      try {
      // Load the fxml file and create a new stage for the popup dialog.
         FXMLLoader loader = new FXMLLoader(getClass().getResource("view/BidDialog.fxml"));
         AnchorPane page = (AnchorPane) loader.load();

         // Create the dialog Stage.
         Stage dialogStage = new Stage();
         dialogStage.setTitle("Artatawe : Place Bid");
         dialogStage.initModality(Modality.WINDOW_MODAL);
         dialogStage.initOwner(currentStage);
         Scene scene = new Scene(page);
         dialogStage.setScene(scene);

         // Set the person into the controller.
         BidController controller = loader.getController();
         controller.setDialogStage(dialogStage);
         controller.setBid(bid);

         // Show the dialog and wait until the user closes it
         dialogStage.showAndWait();

         return controller.isOkClicked();
      } catch (IOException e) {
         e.printStackTrace();
         return false;
      }
   }

   public boolean showPaintingDialog(AuctionListing listing)
   {
      try {
      // Load the fxml file and create a new stage for the popup dialog.
         FXMLLoader loader = new FXMLLoader(getClass().getResource("view/PaintingDialog.fxml"));
         AnchorPane page = (AnchorPane) loader.load();

         // Create the dialog Stage.
         Stage dialogStage = new Stage();
         dialogStage.setTitle("Artatawe : Post Painting for Auction");
         dialogStage.initModality(Modality.WINDOW_MODAL);
         dialogStage.initOwner(currentStage);
         Scene scene = new Scene(page);
         dialogStage.setScene(scene);

         // Set the person into the controller.
         PaintingController controller = loader.getController();
         controller.setDialogStage(dialogStage);
         controller.init(listing, this);

         // Show the dialog and wait until the user closes it
         dialogStage.showAndWait();

         return controller.isOkClicked();
      } catch (IOException e) {
         e.printStackTrace();
         return false;
      }
   }

   public Stage getCurrentStage() {
      return this.currentStage;
   }
}
