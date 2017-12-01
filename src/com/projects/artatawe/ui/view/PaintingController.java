package com.projects.artatawe.ui.view;

import java.text.DecimalFormat;
import java.text.ParsePosition;

import com.projects.artatawe.artwork.Painting;
import com.projects.artatawe.auction.AuctionListing;
import com.projects.artatawe.auction.Bid;
import com.projects.artatawe.ui.LoginManager;

import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;

/**
 * This class implements the controller logic for the Painting Screen
 *
 * @author Adam Thomas
 *
 */
public class PaintingController
{
   @FXML
   private TextField titleField;
   @FXML
   private TextField descriptionField;
   @FXML
   private TextField createdByField;
   @FXML
   private TextField yearCreatedField;
   @FXML
   private TextField heightField;
   @FXML
   private TextField widthField;
   @FXML
   private TextField reserveAmountField;
   @FXML
   private TextField maximumBidsField;

   private Stage dialogStage;
   private AuctionListing listing;
   private LoginManager loginManager;
   private boolean okClicked = false;

   public void setDialogStage(Stage dialogStage)
   {
      this.dialogStage = dialogStage;
   }

   public boolean isOkClicked()
   {
      return okClicked;
   }

   public void init(AuctionListing listing, LoginManager loginManager)
   {
      this.listing = listing;
      this.loginManager = loginManager;
   }

   @FXML
   private void handleOk()
   {
      okClicked = true;

      Painting painting = new Painting(loginManager.getCurrentUser(),
            new SimpleStringProperty(titleField.getText()),
            new SimpleStringProperty(descriptionField.getText()),
            new SimpleStringProperty(createdByField.getText()),
            new SimpleStringProperty(yearCreatedField.getText()),
            new SimpleLongProperty(Integer.parseInt(heightField.getText())),
            new SimpleLongProperty(Integer.parseInt(widthField.getText())));

      listing.setArtwork(painting);
      listing.setReservePrice(Double.parseDouble(reserveAmountField.getText()));
      listing.setMaxBids(Integer.parseInt(maximumBidsField.getText()));

      dialogStage.close();
   }

   @FXML
   private void handleCancel()
   {
      okClicked = false;
      dialogStage.close();
   }

   /**
    * Initializes the controller class. This method is automatically called
    * after the fxml file has been loaded.
    */
   @FXML
   private void initialize()
   {
      DecimalFormat doubleformat = new DecimalFormat("#.0");
      DecimalFormat integerformat = new DecimalFormat("#");

      reserveAmountField.setTextFormatter(new TextFormatter<>(c -> {
         if (c.getControlNewText().isEmpty()) {
            return c;
         }

         ParsePosition parsePosition = new ParsePosition(0);
         Object object = doubleformat.parse(c.getControlNewText(), parsePosition);

         if (object == null || parsePosition.getIndex() < c.getControlNewText().length()) {
            return null;
         } else {
            return c;
         }
      }));

      maximumBidsField.setTextFormatter(new TextFormatter<>(c -> {
         if (c.getControlNewText().isEmpty()) {
            return c;
         }

         ParsePosition parsePosition = new ParsePosition(0);
         Object object = integerformat.parse(c.getControlNewText(), parsePosition);

         if (object == null || parsePosition.getIndex() < c.getControlNewText().length()) {
            return null;
         } else {
            return c;
         }
      }));
      heightField.setTextFormatter(new TextFormatter<>(c -> {
         if (c.getControlNewText().isEmpty()) {
            return c;
         }

         ParsePosition parsePosition = new ParsePosition(0);
         Object object = integerformat.parse(c.getControlNewText(), parsePosition);

         if (object == null || parsePosition.getIndex() < c.getControlNewText().length()) {
            return null;
         } else {
            return c;
         }
      }));
      widthField.setTextFormatter(new TextFormatter<>(c -> {
         if (c.getControlNewText().isEmpty()) {
            return c;
         }

         ParsePosition parsePosition = new ParsePosition(0);
         Object object = integerformat.parse(c.getControlNewText(), parsePosition);

         if (object == null || parsePosition.getIndex() < c.getControlNewText().length()) {
            return null;
         } else {
            return c;
         }
      }));
   }

}
