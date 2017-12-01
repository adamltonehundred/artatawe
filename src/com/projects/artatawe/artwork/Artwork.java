package com.projects.artatawe.artwork;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.projects.artatawe.auction.Bid;
import com.projects.artatawe.user.User;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;

/**
 * Implementation of Artwork
 * @author Adam Thomas
 *
 */
public class Artwork implements Serializable
{
   private static final long serialVersionUID = 4435495359614739435L;

   // Implementation Note:
   // Because serialization is used to save the application state, JavaFX Properties
   // and ObservableLists must be marked as transient as they cannot be serialized.
   //
   // This is overcome by overriding the readObject() and writeObject() methods and
   // converting the types to ones which are serializable (i.e. String / ArrayList).
   private User owner;
   private transient StringProperty title;
   private transient StringProperty description;
   private transient StringProperty creatorName;
   private transient StringProperty artCreationYear;

   public Artwork()
   {
   }

   public Artwork(User owner, StringProperty title, StringProperty description,
         StringProperty creatorName, StringProperty artCreationYear)
   {
      this.owner = owner;
      this.title = title;
      this.description = description;
      this.creatorName = creatorName;
      this.artCreationYear = artCreationYear;
   }

   public final StringProperty titleProperty()
   {
      return this.title;
   }

   public final StringProperty ownerProperty()
   {
      return new SimpleStringProperty(owner.getName());
   }

   public final StringProperty descriptionProperty()
   {
      return this.description;
   }

   public final StringProperty creatorNameProperty()
   {
      return this.creatorName;
   }

   public final StringProperty artCreationYearProperty()
   {
      return this.artCreationYear;
   }

   /**
    * @return the title
    */
   public String getTitle()
   {
      return title.get();
   }

   /**
    * @return the description
    */
   public String getDescription()
   {
      return description.get();
   }

   /**
    * @return the creatorName
    */
   public String getCreatorName()
   {
      return creatorName.get();
   }

   /**
    * @return the artCreationYear
    */
   public String getArtCreationYear()
   {
      return artCreationYear.get();
   }

   /**
    * @return the owner
    */
   public User getOwner()
   {
      return owner;
   }

   private void readObject(ObjectInputStream s) throws ClassNotFoundException, IOException
   {
      s.defaultReadObject();

      title = new SimpleStringProperty((String) s.readObject());
      description = new SimpleStringProperty((String) s.readObject());
      creatorName = new SimpleStringProperty((String) s.readObject());
      artCreationYear = new SimpleStringProperty((String) s.readObject());
   }

   private void writeObject(ObjectOutputStream s) throws IOException
   {
      s.defaultWriteObject();

      s.writeObject(getTitle());
      s.writeObject(getDescription());
      s.writeObject(getCreatorName());
      s.writeObject(getArtCreationYear());
   }

}
