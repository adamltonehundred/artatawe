package com.projects.artatawe.data;

import java.io.Serializable;

import com.projects.artatawe.auction.Auctioneer;
import com.projects.artatawe.user.UserManager;

/**
 * This class acts as a wrapper for the objects within the Artatawe system.
 *
 * It is used to ensure that duplicate objects aren't created when loading a
 * previously saved state.
 *
 * see http://java.sun.com/javase/8/docs/api/java/io/ObjectOutputStream.html
 *
 * The default serialization mechanism for an object writes the class of the
 * object, the class signature, and the values of all non-transient and
 * non-static fields. References to other objects (except in transient or static
 * fields) cause those objects to be written also. Multiple references to a
 * single object are encoded using a reference sharing mechanism so that graphs
 * of objects can be restored to the same shape as when the original was
 * written.
 *
 * @author Adam Thomas
 *
 */
public class ArtataweStateWrapper implements Serializable
{
   private static final long serialVersionUID = 1L;

   private Auctioneer auctioneer = new Auctioneer();
   private UserManager userManager = new UserManager();

   public ArtataweStateWrapper()
   {
   }

   /**
    * @return the auctioneer
    */
   public Auctioneer getAuctioneer()
   {
      return auctioneer;
   }

   /**
    * @param auctioneer
    *           the auctioneer to set
    */
   public void setAuctioneer(Auctioneer auctioneer)
   {
      this.auctioneer = auctioneer;
   }

   /**
    * @return the usermanager
    */
   public UserManager getUserManager()
   {
      return userManager;
   }

   /**
    * @param usermanager
    *           the usermanager to set
    */
   public void setUserManager(UserManager usermanager)
   {
      this.userManager = usermanager;
   }

}
