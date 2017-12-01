package com.projects.artatawe.auction;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.projects.artatawe.artwork.*;
import com.projects.artatawe.user.User;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Implementation of an Auction Listing
 *
 * @author Adam Thomas
 *
 */
public class AuctionListing implements Comparable<AuctionListing>, Serializable
{
   private static final long serialVersionUID = 6691534811584714978L;

   // Implementation Note:
   // Because serialization is used to save the application state, JavaFX Properties
   // and ObservableLists must be marked as transient as they cannot be serialized.
   //
   // This is overcome by overriding the readObject() and writeObject() methods and
   // converting the types to ones which are serializable (i.e. String / ArrayList).
   private Artwork artwork;
   private transient ObservableList<Bid> bids = FXCollections.observableArrayList();
   private transient IntegerProperty maxBids;
   private ListingStatusKind status = ListingStatusKind.UNLISTED;
   private transient DoubleProperty reservePrice;
   private User winningBidder;
   private transient DoubleProperty winningPrice;

   public AuctionListing() {

   }

   public AuctionListing(Artwork artwork, IntegerProperty maxBids, DoubleProperty reservePrice)
   {
      this.artwork = artwork;
      this.maxBids = maxBids;
      this.reservePrice = reservePrice;
   }

   @Override
   public int compareTo(AuctionListing o)
   {
      return artwork.getTitle().compareTo(o.getArtwork().getTitle());
   }

   public Artwork getArtwork()
   {
      return artwork;
   }

   public DoubleProperty winningPrice() {
      return this.winningPrice;
   }

   public void setArtwork(Artwork artwork) {
      this.artwork = artwork;
   }

   public void setMaxBids(int maxBids) {
      if(this.maxBids == null) {
         this.maxBids = new SimpleIntegerProperty(maxBids);
      }
      else
         this.maxBids.set(maxBids);
   }

   public void setReservePrice(double reservePrice) {
      if(this.reservePrice == null) {
         this.reservePrice = new SimpleDoubleProperty(reservePrice);
      }
      else
         this.reservePrice.set(reservePrice);
   }

   public DoubleProperty reservePrice()
   {
      return this.reservePrice;
   }

   public StringProperty getArtworkTitleProperty()
   {
      return artwork.titleProperty();
   }

   public StringProperty getArtworkOwnerProperty()
   {
      return artwork.ownerProperty();
   }

   /**
    * @return the bids
    */
   public ObservableList<Bid> getBids()
   {
      return bids;
   }

   /**
    * @return the status
    */
   public ListingStatusKind getStatus()
   {
      return status;
   }

   /**
    * @param status
    *           the status to set
    */
   public void setStatus(ListingStatusKind status)
   {
      this.status = status;
   }

   /**
    * @return the maxBids
    */
   public int getMaxBids()
   {
      return maxBids.get();
   }

   public int getRemainingBids()
   {
      if (this.status == ListingStatusKind.CLOSED)
         return 0;

      int availableBids = maxBids.get();

      for (Bid b : bids) {
         if (b.getStatus() == BidStatusKind.ACCEPTED)
            availableBids--;
      }
      return availableBids;
   }

   /**
    * @return the reservePrice
    */
   public double getReservePrice()
   {
      return reservePrice.get();
   }

   /**
    * This method implements the business logic for processing
    * a bid against and auction listing.
    *
    * @param newbid
    * @return
    */
   public BidStatusKind processBid(Bid newbid)
   {
      // does this listing have any bid slots available?
      if (getRemainingBids() == 0) {
         newbid.setStatus(BidStatusKind.IGNORED);
         newbid.setStatusReason("Listing closed");
         return newbid.getStatus();
      }

      // is the user posting the bid already the highest bidder?
      if (newbid.getUser() == this.winningBidder) {
         newbid.setStatus(BidStatusKind.REJECTED_HIGHEST);
         return newbid.getStatus();
      }

      // determine what the bid status will be

      // initially assume this is the leading bid
      newbid.setStatus(BidStatusKind.ACCEPTED);

      // if there are any higher bids previously placed
      // then change this bid to be rejected
      for (Bid bid : bids) {
         if (bid.getBidAmount() >= newbid.getBidAmount()) {
            newbid.setStatus(BidStatusKind.REJECTED_OUTBID);
            newbid.setStatusReason("Outbid");
            return newbid.getStatus();
         }
      }

      // check this bid is above the reserve for the listing
      if (newbid.getBidAmount() < getReservePrice()) {
         newbid.setStatus(BidStatusKind.REJECTED);
         newbid.setStatusReason("Reserve not met");
         return newbid.getStatus();
      }

      // store the current winning bidder
      if (newbid.getStatus() == BidStatusKind.ACCEPTED) {
         this.winningBidder = newbid.getUser();
         this.winningPrice = new SimpleDoubleProperty(newbid.getBidAmount());
      }

      // add this bid to the auction listing
      bids.add(newbid);

      // close the auction if there are no more bid slots remaining
      if (getRemainingBids() == 0) {
         boolean winnerFound = false;

         for (int j = bids.size() - 1; j >= 0; j--) {
            Bid bid = bids.get(j);
            if ((winnerFound == false) && (bid.getStatus() == BidStatusKind.ACCEPTED)) {
               winnerFound = true;
               bid.setStatus(BidStatusKind.WINNER);
               winningBidder = bid.getUser();
               this.status = ListingStatusKind.CLOSED;
            } else {
               if (bid.getStatus() == BidStatusKind.ACCEPTED) {
                  bid.setStatus(BidStatusKind.LOSER);
                  bid.setStatusReason("Outbid");
               }
            }
         }
      }

      // return this bid status
      return newbid.getStatus();
   }

   public User getWinningBidder()
   {
      return this.winningBidder;
   }

   /**
    * @return the winningPrice
    */
   public double getWinningPrice()
   {
      if(winningPrice == null)
         return 0;
      else
         return winningPrice.get();
   }

   private void readObject(ObjectInputStream s) throws ClassNotFoundException, IOException
   {
      s.defaultReadObject();

      List<Bid> bidList = (List<Bid>) s.readObject();
      bids = FXCollections.observableList(bidList);

      maxBids = new SimpleIntegerProperty((int) s.readObject());
      reservePrice = new SimpleDoubleProperty((double) s.readObject());
      winningPrice = new SimpleDoubleProperty((double) s.readObject());
   }

   private void writeObject(ObjectOutputStream s) throws IOException
   {
      s.defaultWriteObject();
      s.writeObject(new ArrayList<Bid>(bids));
      s.writeObject(getMaxBids());
      s.writeObject(getReservePrice());
      s.writeObject(getWinningPrice());
   }

}
