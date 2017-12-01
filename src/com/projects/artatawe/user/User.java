package com.projects.artatawe.user;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.projects.artatawe.artwork.Artwork;
import com.projects.artatawe.auction.AuctionListing;
import com.projects.artatawe.auction.Bid;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Implementation of User.
 *
 * @author Adam Thomas
 */
public class User extends PublicProfile
{
   private static final long serialVersionUID = 319622321215332209L;

   // Implementation Note:
   // Because serialization is used to save the application state, JavaFX Properties
   // and ObservableLists must be marked as transient as they cannot be serialized.
   //
   // This is overcome by overriding the readObject() and writeObject() methods and
   // converting the types to ones which are serializable (i.e. String / ArrayList).
   private transient StringProperty firstName;
   private transient StringProperty lastName;
   private transient StringProperty telephoneNumber;
   private transient StringProperty addressLineOne;
   private transient StringProperty addressLineTwo;
   private transient StringProperty townOrCity;
   private transient StringProperty county;
   private transient StringProperty postcode;
   private Date lastLogin;

   private transient ObservableList<Artwork> artCollection = FXCollections.observableArrayList();

   private transient ObservableList<Bid> bids = FXCollections.observableArrayList();

   private transient ObservableList<AuctionListing> auctionListings = FXCollections.observableArrayList();

   public User()
   {
      this.firstName = new SimpleStringProperty();
      this.lastName = new SimpleStringProperty();
   }

   public User(StringProperty username, StringProperty firstName, StringProperty lastName)
   {
      super(username);

      this.firstName = firstName;
      this.lastName = lastName;
   }

   public String getName()
   {
      return this.firstName.get() + " " + this.lastName.get();
   }

   public void addArtwork(Artwork artwork)
   {
      artCollection.add(artwork);
   }

   public ObservableList<Artwork> getArtCollection()
   {
      return this.artCollection;
   }

   public ObservableList<Bid> getBids()
   {
      return this.bids;
   }

   public ObservableList<AuctionListing> getAuctionListings()
   {
      return this.auctionListings;
   }

   public final StringProperty firstNameProperty()
   {
      return this.firstName;
   }

   public final StringProperty lastNameProperty()
   {
      return this.lastName;
   }

   public final StringProperty telephoneNumberProperty()
   {
      return this.telephoneNumber;
   }

   public final StringProperty addressLineOneProperty()
   {
      return this.addressLineOne;
   }

   public final StringProperty addressLineTwoProperty()
   {
      return this.addressLineTwo;
   }

   public final StringProperty townOrCityProperty()
   {
      return this.townOrCity;
   }

   public final StringProperty countyProperty()
   {
      return this.county;
   }

   public final StringProperty postcodeProperty()
   {
      return this.postcode;
   }

   public void addBid(Bid bid)
   {
      bids.add(bid);
   }

   public void addAuctionListing(AuctionListing listing)
   {
      auctionListings.add(listing);
   }

   public String getFirstName()
   {
      return firstName.get();
   }

   public String getLastName()
   {
      return lastName.get();
   }

   public String getTelephoneNumber()
   {
      return telephoneNumber.get();
   }

   public void setTelephoneNumber(StringProperty telephoneNumber)
   {
      this.telephoneNumber = telephoneNumber;
   }

   public String getAddressLineOne()
   {
      return addressLineOne.get();
   }

   public void setAddressLineOne(StringProperty addressLineOne)
   {
      this.addressLineOne = addressLineOne;
   }

   public String getAddressLineTwo()
   {
      return addressLineTwo.get();
   }

   public void setAddressLineTwo(StringProperty addressLineTwo)
   {
      this.addressLineTwo = addressLineTwo;
   }

   public String getTownOrCity()
   {
      return townOrCity.get();
   }

   public void setTownOrCity(StringProperty townOrCity)
   {
      this.townOrCity = townOrCity;
   }

   public String getCounty()
   {
      return county.get();
   }

   public void setCounty(StringProperty county)
   {
      this.county = county;
   }

   public String getPostcode()
   {
      return postcode.get();
   }

   public void setPostcode(StringProperty postcode)
   {
      this.postcode = postcode;
   }

   public Date getLastLogin()
   {
      return lastLogin;
   }

   public void setLastLogin(Date lastLogin)
   {
      this.lastLogin = lastLogin;
   }

   @Override
   public String toString()
   {
      return getName();
   }

   private void readObject(ObjectInputStream s) throws ClassNotFoundException, IOException {
      s.defaultReadObject();

      List<Artwork> artList = (List<Artwork>) s.readObject();
      artCollection = FXCollections.observableList(artList);

      List<Bid> bidList = (List<Bid>) s.readObject();
      bids = FXCollections.observableList(bidList);

      List<AuctionListing> auctionListingsList = (List<AuctionListing>) s.readObject();
      auctionListings = FXCollections.observableList(auctionListingsList);

      firstName = new SimpleStringProperty((String) s.readObject());
      lastName = new SimpleStringProperty((String) s.readObject());
  }

  private void writeObject(ObjectOutputStream s) throws IOException {
      s.defaultWriteObject();
      s.writeObject(new ArrayList<Artwork>(artCollection));
      s.writeObject(new ArrayList<Bid>(bids));
      s.writeObject(new ArrayList<AuctionListing>(auctionListings));
      s.writeObject(getFirstName());
      s.writeObject(getLastName());
  }

}
