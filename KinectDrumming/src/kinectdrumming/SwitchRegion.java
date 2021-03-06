package kinectdrumming;

import static kinectdrumming.Region.parent;
import processing.core.PApplet;

public class SwitchRegion extends Region
{
   private boolean hasEnteredRegion = false;
   private boolean hasChanged = false;
   private boolean isRightSwitch = true;

   public SwitchRegion(PApplet parent, float scale, int x, int y, int width, int height, String imagePath)
   {
      super(parent);
      this.x = (int) (x * scale);
      this.y = (int) (y * scale);
      this.width = (int) (width * scale);
      this.height = (int) (height * scale);

      if (!"".equals(imagePath))
      {
         image = parent.loadImage(imagePath);
      }
   }

   @Override
   public void draw() //needs to know if it is being intersected
   {
      parent.stroke(borderColor); //no border

      if (isColliding)
      {
         parent.fill(activeColor);
         if (!hasEnteredRegion)
         {
            hasEnteredRegion = true;
            if (isRightSwitch)
            {
               SoundLibrary.nextLibrary();
            }
            else
            {
               SoundLibrary.previousLibrary();
            }
            hasChanged = true;

            //else do nothing - still in region
         }
      }
      else
      {
         parent.fill(normalColor);
         hasEnteredRegion = false;
      }

      parent.rect(x, y, width, height);
//      parent.tint(255, 140);
      parent.image(image, x + 20, y + 20, width - 40, height - 40);

      isColliding = false; //reset
   }

   public void setRightSwitch(boolean isRightSwitch)
   {
      this.isRightSwitch = isRightSwitch;
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