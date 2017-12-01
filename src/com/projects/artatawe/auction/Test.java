package com.projects.artatawe.auction;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import com.projects.artatawe.artwork.Artwork;
import com.projects.artatawe.artwork.Painting;
import com.projects.artatawe.data.ArtataweStateWrapper;
import com.projects.artatawe.data.FileHandler;
import com.projects.artatawe.user.User;
import com.projects.artatawe.user.UserManager;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;

/**
 * A test class to exercise the auction logic.
 *
 * @author Adam Thomas
 *
 */
public class Test
{
   public static void main(String[] args)
   {
      final int bidslots = 10;
      final double reserve = 500.0;

      // load the system state if previously saved
      ArtataweStateWrapper state = new FileHandler().load();

      // get the auctioneer object
      Auctioneer auctioneer = state.getAuctioneer();

      System.out.println("debug: " + auctioneer.getAuctionListings().size() + " listings loaded");

      // get the user manager object
      UserManager um = state.getUserManager();

      // get the users
      List<User> users = um.getUsers();

      System.out.println("debug: " + users.size() + " users loaded");

      // create some users if none exist
      if (users.size() == 0) {
         String[][] names = { { "Lauren", "Elsa" }, { "Julia", "Mildred" }, { "Bernice", "Leona" },
               { "Jan", "Al" }, { "Kathryn", "Oliver" }, { "Antoinette", "Debbie" },
               { "Dale", "Toby" }, { "Randy", "Margarita" }, { "Alexander", "Evan" },
               { "Jeanette", "Desiree" } };

         boolean artloaded = false;
         for (int x = 0; x < names.length; x++) {

            User user = new User(new SimpleStringProperty("user" + (x + 1)),
                                 new SimpleStringProperty(names[x][0]),
                                 new SimpleStringProperty(names[x][1]));

            // add a painting for the first user so we have something to auction
            if (artloaded == false) {
               Artwork artwork;

               artwork = new Painting(user, new SimpleStringProperty("A rearing stallion"),
                     new SimpleStringProperty("A rearing stallion, oil on canvas, 43 x 45 in. (109 x 115.6 cm.)"),
                     new SimpleStringProperty("Sir Anthony van Dyck"), new SimpleStringProperty("1599-1641"), new SimpleLongProperty(43), new SimpleLongProperty(45));

               user.addArtwork(artwork);

               AuctionListing listing = new AuctionListing(artwork,
                           new SimpleIntegerProperty(bidslots),
                           new SimpleDoubleProperty(reserve));

               System.out.println("\ninfo: auction created, status - " + listing.getStatus());

               auctioneer.post(listing);

               System.out.println("\ninfo: auction posted, status - " + listing.getStatus());

               artwork = new Painting(user, new SimpleStringProperty("The Mona Lisa"),
                     new SimpleStringProperty("The Mona Lisa is a half-length portrait painting by the Italian Renaissance artist Leonardo da Vinci that has been described as 'the best known, the most visited, the most written about'."),
                     new SimpleStringProperty("Leonardo da Vinci"), new SimpleStringProperty("1503"), new SimpleLongProperty(77), new SimpleLongProperty(53));

               user.addArtwork(artwork);

               listing = new AuctionListing(artwork,
                           new SimpleIntegerProperty(bidslots),
                           new SimpleDoubleProperty(reserve));

               System.out.println("\ninfo: auction created, status - " + listing.getStatus());

               auctioneer.post(listing);

               System.out.println("\ninfo: auction posted, status - " + listing.getStatus());

               artloaded = true;
            }
            users.add(user);
         }
      }

      // find an auction listing
      ObservableList<AuctionListing> auctionListings = auctioneer.getAuctionListings();
      if(auctionListings.size() > 0) {
         AuctionListing listing = auctionListings.get(auctionListings.size() - 1);

         System.out.println("\ninfo: simulating an auction\n");

         int x = 0;
         // do some bidding against the listing
         while (listing.getRemainingBids() > 0) {
            // select a random user
            User u = users.get(ThreadLocalRandom.current().nextInt(0, users.size()));

            // only bid if this user is not the current winning bidder
            if (u != listing.getWinningBidder()) {
               double bidbase = reserve;
               if (listing.getWinningPrice() > 0)
                  bidbase = listing.getWinningPrice();

               double bidfrom = bidbase * .85;
               double bidto = bidbase * 1.15;

               // generate a random bid amount (85% - 150% of the reserve)
               final double bidAmount = (double) ThreadLocalRandom.current().nextInt((int) bidfrom,
                     (int) bidto);

               // create the Bid object
               Bid b = new Bid(u, listing, new SimpleDoubleProperty(bidAmount));

               // post the bid to the auctioneer
               auctioneer.bidFor(listing, b);
               x++;

               // display the result of the posting
               System.out.println("info: bid " + x + " " + b.getStatus() + " "
                     + b.getUser().getName() + "'s bid of " + b.getBidAmount());
            }
         }

         System.out.println("\ninfo: auction finished, status - " + listing.getStatus());

         System.out.println("\ninfo: bid results:\n");

         List<Bid> bids = listing.getBids();
         x = 1;
         for (Bid bid : bids) {
            System.out.println("info: bid " + x++ + " closing status is " + bid.getStatus()
                  + (bid.getStatus() != BidStatusKind.WINNER ? " - " + bid.getStatusReason()
                        : " - " + bid.getUser().getName()));
         }

         System.out.println("\ninfo: bid history for users:");
         for (User u : users) {
            bids = u.getBids();
            if (bids.size() > 0) {
               System.out.println("\ninfo: printing bids for " + u.getName());

               for (Bid bid : bids) {
                  System.out.println("info: " + bid.getStatus()
                        + (bid.getStatus() != BidStatusKind.WINNER
                              ? " (" + bid.getStatusReason() + ")" : "")
                        + " - " + bid.getTimestamp());
               }
            }
         }
      }
      // save the current state
      new FileHandler().save(state);
   }

}
