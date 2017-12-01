package com.projects.artatawe.ui;

import com.projects.artatawe.auction.Auctioneer;
import com.projects.artatawe.data.ArtataweStateWrapper;
import com.projects.artatawe.data.FileHandler;
import com.projects.artatawe.user.UserManager;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * This class launches the JavaFX GUI for artatawe. The JavaFX runtime does the
 * following, in order, whenever an application is launched:
 *
 * 1. Constructs an instance of the specified Application class
 * 2. Calls the init() method
 * 3. Calls the start(javafx.stage.Stage) method
 * 4. Waits for the application to finish, which happens when either of the following occur:
 *    - the application calls Platform.exit()
 *    - the last window has been closed and the implicit Exit attribute on Platform is true
 * 5. Calls the stop() method (which has been overridden here to save the current state)
 *
 * @author Adam Thomas
 *
 */
public class Main extends Application
{
   /**
    * The wrapper object for the application state.
    */
   private final ArtataweStateWrapper state;

   private final UserManager userManager;
   private final Auctioneer auctioneer;

   /*
    * In the constructor, load the previously saved state of the application.
    */
   public Main()
   {
      state = new FileHandler().load();

      this.auctioneer = state.getAuctioneer();
      this.userManager = state.getUserManager();
   }

   public static void main(String[] args)
   {
      launch(args);
   }

   /*
    * When the application is started, hand control to the LoginManager, which
    * then controls which screens / dialogs get displayed.
    *
    * @see javafx.application.Application#start(javafx.stage.Stage)
    */
   public void start(Stage stage) throws Exception
   {
      LoginManager loginManager = new LoginManager(stage, this);
      loginManager.showLoginScreen();
   }

   /*
    * The stop() method is called when the JavaFX application finishes. By overriding
    * this method, we automatically save the application's state.
    *
    * @see javafx.application.Application#stop()
    */
   @Override
   public void stop()
   {
      new FileHandler().save(state);
   }

   public UserManager getUserManager()
   {
      return userManager;
   }

   public Auctioneer getAuctioneer()
   {
      return auctioneer;
   }
}
