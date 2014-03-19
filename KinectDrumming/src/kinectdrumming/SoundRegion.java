package kinectdrumming;

import java.io.File;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import static kinectdrumming.Region.parent;
import kinectdrumming.SoundLibrary.LoopMode;
import processing.core.PApplet;

public class SoundRegion extends Region
{
   private Clip clip;
   private boolean isPlayingLoop = false;
   private LoopMode loopMode = LoopMode.NONE;
   private boolean hasEnteredRegion = false;
   //label or picture

   public SoundRegion(PApplet parent, float scale, int x, int y, int width, int height, int regionNum)
   {
      super(parent);
      this.x = (int) (x * scale);
      this.y = (int) (y * scale);
      this.width = (int) (width * scale);
      this.height = (int) (height * scale);
      String soundPath = SoundLibrary.getSoundPath(regionNum);
      String imagePath = SoundLibrary.getImagePath(regionNum);

      if (!"".equals(imagePath))
      {
         image = parent.loadImage(imagePath);
      }

      try
      {
         if (!"".equals(soundPath))
         {
            file = new File(soundPath);
         }
      }
      catch (Exception e)
      {
      }
   }

   public void setLoopMode(LoopMode loopMode)
   {
      this.loopMode = loopMode;
      if (loopMode == LoopMode.NONE) //stop any playing loops
      {
         isPlayingLoop = false;
         stopPlaying();
      }
   }

   private void startPlaying(boolean shouldLoop)
   {
      if (file != null)
      {
         try
         {
            clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(file));
            if (shouldLoop)
            {
               isPlayingLoop = true;
               clip.loop(Clip.LOOP_CONTINUOUSLY);
            }
            else
            {
               clip.start();
            }
         }
         catch (Exception e)
         {
         }
      }
   }

   private void stopPlaying()
   {
      if (clip != null)
      {
         clip.stop();
      }
   }

   @Override
   public void draw() //needs to know if it is being intersected
   {
      parent.stroke(0, 0, 0, 0); //no border

      switch (loopMode)
      {
         case NONE:
            if (isColliding)
            {
               parent.fill(255, 255, 255, 140);
               if (!hasEnteredRegion)
               {
                  hasEnteredRegion = true;
                  startPlaying(false);
               }
            }
            else
            {
               parent.fill(100, 100, 100, 140);
               hasEnteredRegion = false;
            }
            break;
         case ACTIVE:
            if (isColliding)
            {
               parent.fill(255, 255, 255, 140);
               if (!isPlayingLoop)
               {
                  startPlaying(true);
               }
            }
            else
            {
               parent.fill(100, 100, 100, 140);
               isPlayingLoop = false;
               stopPlaying();
            }
            break;
         case TOGGLE:
            if (isColliding)
            {
               if (!hasEnteredRegion)
               {
                  hasEnteredRegion = true;
                  if (isPlayingLoop)
                  {
                     isPlayingLoop = false;
                     stopPlaying();
                  }
                  else
                  {
                     startPlaying(true);
                  }
               }
            }
            else
            {
               hasEnteredRegion = false;
            }

            //Choose colors for region
            if (isPlayingLoop)
            {
               parent.fill(255, 255, 255, 140);
            }
            else
            {
               parent.fill(100, 100, 100, 140);
            }
            break;
      }

      parent.rect(x, y, width, height);
      parent.tint(255, 140);
      parent.image(image, x + 20, y + 20, width - 40, height - 40);

      isColliding = false; //reset
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