package com.projects.artatawe.ui.view;

import java.text.DecimalFormat;
import java.text.ParsePosition;

import com.projects.artatawe.auction.Bid;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;

/**
 * The controller for the BidDialog dialog. This lets a user place a bid
 * against an open auction.
 *
 * @author Adam Thomas
 *
 */
public class BidController
{
   @FXML
   Label artworkTitleLabel;

   @FXML
   private TextField bidAmountField;

   private Stage dialogStage;
   private Bid bid;
   private boolean okClicked = false;

   public void setDialogStage(Stage dialogStage)
   {
      this.dialogStage = dialogStage;
   }

   public boolean isOkClicked()
   {
      return okClicked;
   }

   public void setBid(Bid bid)
   {
      this.bid = bid;
      artworkTitleLabel.setText(bid.getListing().getArtwork().getTitle());
   }

   @FXML
   private void handleOk()
   {
      okClicked = true;
      bid.setBidAmount(Double.parseDouble(bidAmountField.getText()));
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

      // This lambda function is used to restrict the input
      // from the user to characters that form a valid decimal number
      bidAmountField.setTextFormatter(new TextFormatter<>(c -> {
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
   }

}
