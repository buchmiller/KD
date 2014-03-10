package kinectdrumming;

import static kinectdrumming.Region.parent;
import java.io.File;
import processing.core.PApplet;

public class SwitchRegion extends Region
{
   private boolean hasEnteredRegion = false;
   private boolean hasChanged = false;

   public SwitchRegion(PApplet parent, float scale, int x, int y, int width, int height, String picturePath)
   {
      super(parent);
      this.x = (int) (x * scale);
      this.y = (int) (y * scale);
      this.width = (int) (width * scale);
      this.height = (int) (height * scale);
      try
      {
         if (!"".equals(picturePath))
         {
            this.file = new File(picturePath);
         }
      }
      catch (Exception e)
      {
      }
   }

   @Override
   public void draw() //needs to know if it is being intersected
   {
      parent.stroke(0, 0, 0, 0); //no border

      if (isColliding)
      {
         parent.fill(0, 255, 255, 140);
         if (!hasEnteredRegion)
         {
            hasEnteredRegion = true;
            SoundLibrary.nextLibrary();
            hasChanged = true;
            //TODO set picture path

            //else do nothing - still in region
         }
      }
      else
      {
         parent.fill(0, 0, 255, 140);
         hasEnteredRegion = false;
      }

      parent.rect(x, y, width, height);

      isColliding = false; //reset
   }

   public boolean hasChanged()
   {
      if (hasChanged)
      {
         hasChanged = false;
         return true;
      }
      return false;
   }

   @Override
   public void determineCollision(float px, float py)
   {
      if (isInside(px, py))
      {
         isColliding = true;
      }
   }
}