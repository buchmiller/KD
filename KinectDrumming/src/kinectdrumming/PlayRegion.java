package kinectdrumming;

import processing.core.PApplet;

public class PlayRegion extends SoundRegion
{

   public PlayRegion(PApplet parent, float scale, int x, int y, int width, int height)
   {
      super(parent, scale, x, y, width, height, "sounds/recorded.wav");
   }
}
