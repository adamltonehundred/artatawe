package com.projects.artatawe.auction;

/**
 * Implementation of a Listing Status enumeration
 *
 * @author Adam Thomas
 *
 */
public enum ListingStatusKind {
   UNLISTED("Unlisted"),
   ACTIVE("Active"),
   CLOSED("Closed");

   private String description;

   private ListingStatusKind(String description) {
       this.description = description;
   }

   @Override
   public String toString() {
       return description;
   }
}
