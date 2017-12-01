package com.projects.artatawe.auction;

/**
 * Implementation of a Bid Status enumeration
 *
 * @author Adam Thomas
 *
 */
public enum BidStatusKind {
   UNPOSTED("Not posted"),
   ACCEPTED("Accepted - Highest Bidder"),
   REJECTED("Rejected - Lower than Reserve"),
   REJECTED_OUTBID("Rejected - Outbid"),
   REJECTED_HIGHEST("Rejected - Already Current Highest Bidder"),
   WINNER("Winning bid"),
   LOSER("Lost"),
   IGNORED("Ignored");

   private String description;

   private BidStatusKind(String description) {
       this.description = description;
   }

   @Override
   public String toString() {
       return description;
   }
}
