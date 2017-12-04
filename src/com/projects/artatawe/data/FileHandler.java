package com.projects.artatawe.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.prefs.Preferences;

import com.projects.artatawe.ui.Main;

/**
 * Implements the data persistence subsystem. This class saves the state of the
 * artatawe application and stores the file location as a user preference.
 *
 * @author Adam Thomas
 *
 */
public class FileHandler
{

   /**
    * Returns the file, or null if the file doesn't exist.
    *
    * @return
    */
   private File getFilePath()
   {
      File f = new File("artatawe.ser");
      if (f.exists() && !f.isDirectory()) {
         return f;
      }
      return null;
   }

   /**
    * Deserialize the artatawe state
    *
    * @return
    */
   public ArtataweStateWrapper load()
   {
      File file = getFilePath();
      if (file == null) {
         return new ArtataweStateWrapper();
      }

      ArtataweStateWrapper state = null;
      FileInputStream fis = null;
      ObjectInputStream in = null;
      try {
         fis = new FileInputStream(file);
         in = new ObjectInputStream(fis);
         state = (ArtataweStateWrapper) in.readObject();
         in.close();
      } catch (IOException ex) {
         return new ArtataweStateWrapper();
      } catch (ClassNotFoundException ex) {
         ex.printStackTrace();
      }
      return state;
   }

   /**
    * Save the state of the artatawe objects using serialization
    *
    * @param state
    */
   public void save(ArtataweStateWrapper state)
   {
      FileOutputStream fos = null;
      ObjectOutputStream out = null;
      try {
         fos = new FileOutputStream("artatawe.ser");
         out = new ObjectOutputStream(fos);
         out.writeObject(state);
         out.close();

         // setFilePath("artatawe.ser");
      } catch (IOException ex) {
         ex.printStackTrace();
      }
   }
}
