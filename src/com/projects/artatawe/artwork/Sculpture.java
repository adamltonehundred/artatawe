package com.projects.artatawe.artwork;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.projects.artatawe.user.User;

import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.StringProperty;

/**
 * Implementation of a Sculpture
 * @author Adam Thomas
 *
 */
public class Sculpture extends Artwork
{
   private static final long serialVersionUID = -4142726850418970955L;

   // Implementation Note:
   // Because serialization is used to save the application state, JavaFX Properties
   // and ObservableLists must be marked as transient as they cannot be serialized.
   //
   // This is overcome by overriding the readObject() and writeObject() methods and
   // converting the types to ones which are serializable (i.e. String / ArrayList).
   private transient LongProperty width;
   private transient LongProperty height;
   private transient LongProperty depth;
   private MaterialKind mainMaterial;

   public Sculpture() {
   }

   public Sculpture(User owner, StringProperty title, StringProperty description, StringProperty creatorName, StringProperty artCreationYear,
                    LongProperty width, LongProperty height, LongProperty depth,
                    MaterialKind mainMaterial)
   {
      super(owner, title, description, creatorName, artCreationYear);
      this.width = width;
      this.height = height;
      this.depth = depth;
      this.mainMaterial = mainMaterial;
   }

   /**
    * @return the width
    */
   public long getWidth()
   {
      return width.get();
   }

   /**
    * @return the height
    */
   public long getHeight()
   {
      return height.get();
   }

   /**
    * @return the depth
    */
   public long getDepth()
   {
      return depth.get();
   }

   /**
    * @return the main material
    */
   public String getMainMaterial()
   {
      return mainMaterial.toString();
   }

   private void readObject(ObjectInputStream s) throws ClassNotFoundException, IOException {
      s.defaultReadObject();

      width = new SimpleLongProperty((long) s.readObject());
      height = new SimpleLongProperty((long) s.readObject());
      depth = new SimpleLongProperty((long) s.readObject());
  }

  private void writeObject(ObjectOutputStream s) throws IOException {
      s.defaultWriteObject();

      s.writeObject(getWidth());
      s.writeObject(getHeight());
      s.writeObject(getDepth());
  }

}
