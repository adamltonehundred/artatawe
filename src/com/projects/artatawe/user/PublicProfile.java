package com.projects.artatawe.user;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Implementation of Public Profile
 * @author Adam Thomas
 *
 */
public class PublicProfile implements Comparable<PublicProfile>, Serializable
{
   private static final long serialVersionUID = -4072733129336851723L;

   // Implementation Note:
   // Because serialization is used to save the application state, JavaFX Properties
   // and ObservableLists must be marked as transient as they cannot be serialized.
   //
   // This is overcome by overriding the readObject() and writeObject() methods and
   // converting the types to ones which are serializable (i.e. String / ArrayList).
   private transient StringProperty username;

   public PublicProfile()
   {
      this.username = new SimpleStringProperty();
   }

   public PublicProfile(StringProperty username)
   {
      this.username = username;
   }

   /**
    * @return the username
    */
   public String getUsername()
   {
      return username.get();
   }

   @Override
   public int compareTo(PublicProfile o)
   {
      return username.get().compareTo(o.getUsername());
   }

   private void readObject(ObjectInputStream s) throws ClassNotFoundException, IOException {
      s.defaultReadObject();

      username = new SimpleStringProperty((String) s.readObject());
  }

  private void writeObject(ObjectOutputStream s) throws IOException {
      s.defaultWriteObject();
      s.writeObject(getUsername());
  }
}
