package com.projects.artatawe.artwork;

/**
 * Implementation of the Material enumeration
 *
 * @author Adam Thomas
 *
 */
public enum MaterialKind {
   STONE("Stone"),
   MARBLE("Marble"),
   WAX("Wax"),
   ICE("Ice");

   private String description;

   private MaterialKind(String description) {
       this.description = description;
   }

   @Override
   public String toString() {
       return description;
   }
}
