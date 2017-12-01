package com.projects.artatawe.auction;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.projects.artatawe.artwork.Artwork;
import com.projects.artatawe.user.User;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Implementation of the Auctioneer.
 *
 * @author Adam Thomas
 *
 */
public class Auctioneer implements Serializable
{
   private static final long serialVersionUID = -3230042000411398530L;

   // Implementation Note:
   // Because serialization is used to save the application state, JavaFX Properties
   // and ObservableLists must be marked as transient as they cannot be serialized.
   //
   // This is overcome by overriding the readObject() and writeObject() methods and
   // converting the types to ones which are serializable (i.e. String / ArrayList).
   private transient ObservableList<AuctionListing> auctionListings = FXCollections.observableArrayList();

   public Auctioneer()
   {

   }

   public ObservableList<AuctionListing> search(String criteria)
   {
      ObservableList<AuctionListing> results = FXCollections.observableArrayList();

      for (AuctionListing listing : auctionListings) {
         if (listing.getArtwork().getTitle().contains(criteria) == true)
            results.add(listing);
      }
      Collections.sort(results);

      return results;
   }

   public ObservableList<AuctionListing> getMyOpenAuctionListings(User user)
   {
      ObservableList<AuctionListing> results = FXCollections.observableArrayList();

      for (AuctionListing listing : auctionListings) {
         if ((listing.getArtwork().getOwner() == user)
               && (listing.getStatus() != ListingStatusKind.CLOSED))
            results.add(listing);
      }
      Collections.sort(results);

      return results;
   }

   public ObservableList<AuctionListing> getMyClosedAuctionListings(User user)
   {
      ObservableList<AuctionListing> results = FXCollections.observableArrayList();

      for (AuctionListing listing : auctionListings) {
         if ((listing.getArtwork().getOwner() == user)
               && (listing.getStatus() == ListingStatusKind.CLOSED))
            results.add(listing);
      }
      Collections.sort(results);

      return results;
   }

   public ObservableList<AuctionListing> getOtherAuctionListings(User user)
   {
      ObservableList<AuctionListing> results = FXCollections.observableArrayList();

      for (AuctionListing listing : auctionListings) {
         if ((listing.getArtwork().getOwner() != user)
               && (listing.getStatus() == ListingStatusKind.ACTIVE))
            results.add(listing);
      }
      Collections.sort(results);

      return results;
   }

   public ObservableList<AuctionListing> getWonListings(User user)
   {
      ObservableList<AuctionListing> results = FXCollections.observableArrayList();

      for (AuctionListing listing : auctionListings) {
         if ((listing.getStatus() == ListingStatusKind.CLOSED)
               && (listing.getWinningBidder() == user))
            results.add(listing);
      }
      Collections.sort(results);

      return results;
   }

   /**
    * Post a new auction listing
    *
    * @param listing
    */
   public void post(AuctionListing listing)
   {
      listing.setStatus(ListingStatusKind.ACTIVE);
      auctionListings.add(listing);
      listing.getArtwork().getOwner().addAuctionListing(listing);
   }

   /**
    * Post a bid against an auction listing
    *
    * @param listing
    * @param newbid
    * @return
    */
   public BidStatusKind bidFor(AuctionListing listing, Bid newbid)
   {
      // add this bid to the user's bid history
      newbid.getUser().addBid(newbid);

      // add this bid to the auction listing
      return listing.processBid(newbid);
   }

   public ObservableList<AuctionListing> getAuctionListings()
   {
      return auctionListings;
   }

   private void readObject(ObjectInputStream s) throws ClassNotFoundException, IOException
   {
      s.defaultReadObject();

      List<AuctionListing> auctionList = (List<AuctionListing>) s.readObject();
      auctionListings = FXCollections.observableList(auctionList);

   }

   private void writeObject(ObjectOutputStream s) throws IOException
   {
      s.defaultWriteObject();
      s.writeObject(new ArrayList<AuctionListing>(auctionListings));
   }

}
