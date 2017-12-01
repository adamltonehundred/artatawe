package com.projects.artatawe.auction;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.projects.artatawe.user.User;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;

/**
 * Implementation of a Bid
 *
 * @author Adam Thomas
 *
 */
public class Bid implements Serializable
{
   private static final long serialVersionUID = -5792502034655279595L;

   // Implementation Note:
   // Because serialization is used to save the application state, JavaFX Properties
   // and ObservableLists must be marked as transient as they cannot be serialized.
   //
   // This is overcome by overriding the readObject() and writeObject() methods and
   // converting the types to ones which are serializable (i.e. String / ArrayList).
   private Date timestamp;
   private transient DoubleProperty bidAmount;
   private BidStatusKind status = BidStatusKind.UNPOSTED;
   private transient StringProperty statusReason = new SimpleStringProperty("");
   private final AuctionListing listing;
   private final User user;

   public Bid(User user, AuctionListing listing, DoubleProperty bidAmount)
   {
      this.user = user;
      this.listing = listing;
      this.bidAmount = bidAmount;
   }

   /**
    * @return the bidAmount
    */
   public double getBidAmount()
   {
      return bidAmount.get();
   }

   public DoubleProperty bidAmount()
   {
      return bidAmount;
   }

   public void setBidAmount(double bidAmount) {
      this.bidAmount.set(bidAmount);
   }

   /**
    * @return the status
    */
   public BidStatusKind getStatus()
   {
      return status;
   }

   /**
    * @param status
    *           the status to set
    */
   public void setStatus(BidStatusKind status)
   {
      this.status = status;
      if (timestamp == null) {
         timestamp = new java.util.Date();
      }
   }

   public String getTimestamp()
   {
      if (timestamp == null)
         return "";
      else
         return timestamp.toString();
   }

   /**
    * @return the listing
    */
   public AuctionListing getListing()
   {
      return listing;
   }

   /**
    * @return the user
    */
   public User getUser()
   {
      return user;
   }

   /**
    * @return the statusReason
    */
   public String getStatusReason()
   {
      return statusReason.get();
   }

   /**
    * @param statusReason
    *           the statusReason to set
    */
   public void setStatusReason(String statusReason)
   {
      this.statusReason = new SimpleStringProperty(statusReason);
   }

   private void readObject(ObjectInputStream s) throws ClassNotFoundException, IOException
   {
      s.defaultReadObject();

      bidAmount = new SimpleDoubleProperty((double) s.readObject());
      statusReason = new SimpleStringProperty((String) s.readObject());
   }

   private void writeObject(ObjectOutputStream s) throws IOException
   {
      s.defaultWriteObject();

      s.writeObject(getBidAmount());
      s.writeObject(getStatusReason());
   }

}
