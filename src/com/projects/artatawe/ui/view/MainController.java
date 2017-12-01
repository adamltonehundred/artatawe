package com.projects.artatawe.ui.view;

import java.util.Date;

import com.projects.artatawe.artwork.Artwork;
import com.projects.artatawe.artwork.Painting;
import com.projects.artatawe.auction.AuctionListing;
import com.projects.artatawe.auction.Auctioneer;
import com.projects.artatawe.auction.Bid;
import com.projects.artatawe.ui.LoginManager;
import com.projects.artatawe.user.User;
import com.projects.artatawe.user.UserManager;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 * This class implements the controller logic for the Main Screen.
 *
 * @author Adam Thomas
 *
 */
public class MainController
{
   private LoginManager loginManager;
   private UserManager userManager;
   private Auctioneer auctioneer;

   @FXML
   private Button bidHistoryButton;
   @FXML
   private Button bidButton;
   @FXML
   private Button favouriteSellerButton;

   @FXML
   private TableView<AuctionListing> myArtworkTable;
   @FXML
   private TableColumn<AuctionListing, String> myTitleColumn;
   @FXML
   private TableColumn<AuctionListing, Double> myReserveColumn;
   @FXML
   private TableColumn<AuctionListing, String> myListingStatus;
   @FXML
   private TableColumn<AuctionListing, String> myRemainingBids;

   @FXML
   private Label artDescriptionLabel;
   @FXML
   private Label yearCreatedLabel;
   @FXML
   private Label createdByLabel;
   @FXML
   private Label heightLabel;
   @FXML
   private Label widthLabel;

   @FXML
   private TableView<AuctionListing> browseArtworkTable;
   @FXML
   private TableColumn<AuctionListing, String> browseTitleColumn;
   @FXML
   private TableColumn<AuctionListing, String> browseSellerColumn;

   @FXML
   private Label artDescriptionLabel2;
   @FXML
   private Label yearCreatedLabel2;
   @FXML
   private Label createdByLabel2;
   @FXML
   private Label heightLabel2;
   @FXML
   private Label widthLabel2;

   @FXML
   private TableView<Bid> bidTable;
   @FXML
   private TableColumn<Bid, String> bidDateColumn;
   @FXML
   private TableColumn<Bid, String> bidTitleColumn;
   @FXML
   private TableColumn<Bid, String> bidSellerColumn;
   @FXML
   private TableColumn<Bid, Double> bidAmountColumn;
   @FXML
   private TableColumn<Bid, String> bidStatusColumn;

   @FXML
   private TableView<AuctionListing> wonTable;
   @FXML
   private TableColumn<AuctionListing, String> wonTitleColumn;
   @FXML
   private TableColumn<AuctionListing, String> wonSellerColumn;
   @FXML
   private TableColumn<AuctionListing, Double> wonAmountColumn;

   @FXML
   private TableView<AuctionListing> doneTable;
   @FXML
   private TableColumn<AuctionListing, String> doneTitleColumn;
   @FXML
   private TableColumn<AuctionListing, String> doneBuyerColumn;
   @FXML
   private TableColumn<AuctionListing, Double> doneAmountColumn;

   @FXML
   private Label dateLastLoginLabel;

   private AuctionListing selectedAuctionListing;



   /**
    * Initializes the controller class. This method is automatically called
    * after the fxml file has been loaded.
    */
   @FXML
   private void initialize()
   {
      // bind the table fields for the My Artwork tab
      myTitleColumn.setCellValueFactory(cellData -> cellData.getValue().getArtworkTitleProperty());
      myReserveColumn
            .setCellValueFactory(cellData -> cellData.getValue().reservePrice().asObject());
      myListingStatus.setCellValueFactory(
            cellData -> new SimpleStringProperty(cellData.getValue().getStatus().toString()));
      myRemainingBids.setCellValueFactory(
            cellData -> new SimpleStringProperty("" + cellData.getValue().getRemainingBids()));

      // bind the table fields for the Browse Artwork tab
      browseTitleColumn
            .setCellValueFactory(cellData -> cellData.getValue().getArtworkTitleProperty());
      browseSellerColumn
            .setCellValueFactory(cellData -> cellData.getValue().getArtworkOwnerProperty());

      // bind the table fields for the My Bids tab
      bidDateColumn.setCellValueFactory(
            cellData -> new SimpleStringProperty(cellData.getValue().getTimestamp()));
      bidTitleColumn.setCellValueFactory(
            cellData -> cellData.getValue().getListing().getArtworkTitleProperty());
      bidSellerColumn.setCellValueFactory(
            cellData -> cellData.getValue().getListing().getArtworkOwnerProperty());
      bidAmountColumn.setCellValueFactory(cellData -> cellData.getValue().bidAmount().asObject());
      bidStatusColumn.setCellValueFactory(
            cellData -> new SimpleStringProperty(cellData.getValue().getStatus().toString()));

      // bind the table fields for the Won Artwork tab
      wonTitleColumn.setCellValueFactory(cellData -> cellData.getValue().getArtworkTitleProperty());
      wonSellerColumn
            .setCellValueFactory(cellData -> cellData.getValue().getArtworkOwnerProperty());
      wonAmountColumn
            .setCellValueFactory(cellData -> cellData.getValue().winningPrice().asObject());

      // bind the table fields for the Won Artwork tab
      doneTitleColumn
            .setCellValueFactory(cellData -> cellData.getValue().getArtworkTitleProperty());
      doneBuyerColumn.setCellValueFactory(
            cellData -> new SimpleStringProperty(cellData.getValue().getWinningBidder().getName()));
      doneAmountColumn
            .setCellValueFactory(cellData -> cellData.getValue().winningPrice().asObject());

      // Clear details when nothing is selected on tables
      showMyArtworkDetails(null);
      showBrowseArtworkDetails(null);

      // Listen for selection changes and show details when changed.
      myArtworkTable.getSelectionModel().selectedItemProperty()
            .addListener((observable, oldValue, newValue) -> showMyArtworkDetails(newValue));
      browseArtworkTable.getSelectionModel().selectedItemProperty()
            .addListener((observable, oldValue, newValue) -> showBrowseArtworkDetails(newValue));

      // Bind buttons to tables
      bidHistoryButton.disableProperty().bind(Bindings.isEmpty(myArtworkTable.getSelectionModel().getSelectedItems()));
      bidButton.disableProperty().bind(Bindings.isEmpty(browseArtworkTable.getSelectionModel().getSelectedItems()));
      favouriteSellerButton.disableProperty().bind(Bindings.isEmpty(browseArtworkTable.getSelectionModel().getSelectedItems()));

   }

   /**
    * Called by the login manager to give a reference back to itself and to the
    * userManager and auctioneer objects.
    *
    * @param loginManager
    * @param userManager
    * @param auctioneer
    */
   public void init(LoginManager loginManager, UserManager userManager, Auctioneer auctioneer)
   {
      this.loginManager = loginManager;
      this.userManager = userManager;
      this.auctioneer = auctioneer;

      User user = loginManager.getCurrentUser();

      if (user != null) {
         bidTable.setItems(user.getBids());

         if (auctioneer != null) {
            myArtworkTable.setItems(auctioneer.getMyOpenAuctionListings(user));

            browseArtworkTable.setItems(auctioneer.getOtherAuctionListings(user));

            wonTable.setItems(auctioneer.getWonListings(user));

            doneTable.setItems(auctioneer.getMyClosedAuctionListings(user));
         }
      }

      Date dateLastLogin = loginManager.getDateLastLogin();
      if(dateLastLogin == null)
         dateLastLoginLabel.setText("You've not logged in before");
      else
         dateLastLoginLabel.setText(dateLastLogin.toString());
   }

   /*
    * Return control to the Login screen once a user clicks Logout
    */
   @FXML
   private void handleLogout()
   {
      loginManager.logout();
   }

   /*
    * Show the details for an auction listing on the My Artwork tab
    */
   private void showMyArtworkDetails(AuctionListing auctionListing)
   {
      if (auctionListing != null) {
         Artwork artwork = auctionListing.getArtwork();

         artDescriptionLabel.setText(artwork.getDescription());
         yearCreatedLabel.setText(artwork.getArtCreationYear());
         createdByLabel.setText(artwork.getCreatorName());

         if (artwork instanceof Painting) {
            Painting painting = (Painting) artwork;

            heightLabel.setText("" + painting.getHeight());
            widthLabel.setText("" + painting.getWidth());
         }

      } else {
         // Auction listing is null, remove all the text.
         artDescriptionLabel.setText("");
         yearCreatedLabel.setText("");
         createdByLabel.setText("");
         heightLabel.setText("");
         widthLabel.setText("");

      }
   }

   /*
    * Show the details for an auction listing on the Browse tab
    *
    * @param auctionListing
    */
   private void showBrowseArtworkDetails(AuctionListing auctionListing)
   {
      selectedAuctionListing = auctionListing;

      if (auctionListing != null) {
         Artwork artwork = auctionListing.getArtwork();

         artDescriptionLabel2.setText(artwork.getDescription());
         yearCreatedLabel2.setText(artwork.getArtCreationYear());
         createdByLabel2.setText(artwork.getCreatorName());

         if (artwork instanceof Painting) {
            Painting painting = (Painting) artwork;

            heightLabel2.setText("" + painting.getHeight());
            widthLabel2.setText("" + painting.getWidth());
         }

      } else {
         // Auction listing is null, remove all the text.
         artDescriptionLabel2.setText("");
         yearCreatedLabel2.setText("");
         createdByLabel2.setText("");
         heightLabel2.setText("");
         widthLabel2.setText("");

      }
   }

   /*
    * Allow a user to enter a bid for a selected auction. If the user
    * clicks OK, then post the bid to the auctioneer.
    */
   @FXML
   private void handleNewBid()
   {
      Bid bid = new Bid(loginManager.getCurrentUser(), selectedAuctionListing,
            new SimpleDoubleProperty(0));
      boolean okClicked = loginManager.showBidDialog(bid);
      if (okClicked) {
         auctioneer.bidFor(selectedAuctionListing, bid);
         Alert alert = new Alert(AlertType.INFORMATION);
         alert.initOwner(loginManager.getCurrentStage());
         alert.setTitle("Artatawe : Bid Result");
         alert.setHeaderText("Your bid was submitted");
         alert.setContentText("Status: " + bid.getStatus());

         alert.showAndWait();
      }
   }

   /*
    * Allow a user to post a new paiting for auction. If the user
    * clicks OK, then post the auction to the auctioneer.
    */
   @FXML
   private void handleNewPainting()
   {
      AuctionListing listing = new AuctionListing();
      boolean okClicked = loginManager.showPaintingDialog(listing);
      if (okClicked) {
         auctioneer.post(listing);
      }
   }

   /*
    * TODO: Allow a user to post a new sculpture for auction. If the user
    * clicks OK, then post the auction to the auctioneer.
    */
   @FXML
   private void handleNewSculptured()
   {
      Alert alert = new Alert(AlertType.INFORMATION);
      alert.initOwner(loginManager.getCurrentStage());
      alert.setTitle("Artatawe : New Sculpture");
      alert.setHeaderText("TODO");
      alert.setContentText("TODO - Post a new Painting instead!");

      alert.showAndWait();
   }

   /*
    * TODO: Allow a user to see the bids for an auction.
    */
   @FXML
   private void handleBidHistory()
   {
      Alert alert = new Alert(AlertType.INFORMATION);
      alert.initOwner(loginManager.getCurrentStage());
      alert.setTitle("Artatawe : Bid History");
      alert.setHeaderText("TODO");
      alert.setContentText("TODO");

      alert.showAndWait();
   }

}
