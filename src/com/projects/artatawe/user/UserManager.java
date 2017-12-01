package com.projects.artatawe.user;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.projects.artatawe.artwork.Artwork;
import com.projects.artatawe.auction.Bid;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class UserManager implements Serializable
{
   private static final long serialVersionUID = 7949584008269208677L;

   private transient ObservableList<User> users = FXCollections.observableArrayList();

   /*
    * This default constructor populates some dummy data into the system.
    */
   public UserManager()
   {

   }

   /**
    * @return the users
    */
   public ObservableList<User> getUsers()
   {
      // Collections.sort(users);
      return users;
   }

   /*
    * @return a sorted list of users, based on the user number, whose name
    * contains the criteria
    */
   public ObservableList<User> search(String criteria)
   {
      ObservableList<User> results = FXCollections.observableArrayList();

      for (User s : users) {
         if (s.getName().contains(criteria) == true)
            results.add(s);
      }
      Collections.sort(results);

      return results;
   }

   private void readObject(ObjectInputStream s) throws ClassNotFoundException, IOException
   {
      s.defaultReadObject();

      List<User> userList = (List<User>) s.readObject();
      users = FXCollections.observableList(userList);

   }

   private void writeObject(ObjectOutputStream s) throws IOException
   {
      s.defaultWriteObject();
      s.writeObject(new ArrayList<User>(users));
   }

   public void addUser(User user)
   {
      users.add(user);
   }
}
