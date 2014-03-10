package kinectdrumming;

import java.io.File;
import processing.core.PApplet;

public abstract class Region
{
   int x; //0 is on left
   int y; //0 is at top
   int width;
   int height;
   boolean isColliding = false;
   File file;
   //label or picture
   protected static PApplet parent;

   public Region(PApplet parent)
   {
      Region.parent = parent;
   }

   public abstract void draw();

   public abstract void determineCollision(float px, float py);

   protected boolean isInside(float px, float py)
   {
      return (px > x && px < x + width && py > y && py < y + height);
   }

   public void setPath(String path)
   {
      try
      {
         if (!"".equals(path))
         {
            file = new File(path);
         }
      }
      catch (Exception e)
      {
      }
   }
}