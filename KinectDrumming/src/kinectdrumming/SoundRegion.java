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
   private boolean isPlaying = false;
   private boolean loop = false;
   private LoopMode loopMode = LoopMode.NONE;
   private boolean hasEnteredRegion = false;
   //label or picture

   public SoundRegion(PApplet parent, float scale, int x, int y, int width, int height, String soundPath)
   {
      super(parent);
      this.x = (int) (x * scale);
      this.y = (int) (y * scale);
      this.width = (int) (width * scale);
      this.height = (int) (height * scale);

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

   public SoundRegion(PApplet parent, float scale, int x, int y, int width, int height)
   {
      this(parent, scale, x, y, width, height, "");
   }

   public void setLoopMode(LoopMode loopMode)
   {
      if (loopMode == LoopMode.NONE)
      {
         if (clip != null && loop)
         {
            clip.stop();
         }
         this.loop = false;
      }
      else
      {
         this.loop = true;
      }
      this.loopMode = loopMode;
   }

   @Override
   public void draw() //needs to know if it is being intersected
   {
      parent.stroke(0, 0, 0, 0); //no border

      if (isColliding)
      {
         parent.fill(0, 255, 255, 140);
         if (!hasEnteredRegion && file != null)
         {
            try
            {
               hasEnteredRegion = true;
               if (loopMode != LoopMode.TOGGLE || !isPlaying)
               {
                  isPlaying = true;
                  clip = AudioSystem.getClip();
                  clip.open(AudioSystem.getAudioInputStream(file));
                  if (loop)
                  {
                     clip.loop(Clip.LOOP_CONTINUOUSLY);
                  }
                  else
                  {
                     clip.start();
                  }
               }
               else if (loopMode == LoopMode.TOGGLE)
               {
                  isPlaying = false;
                  if (loop && clip != null)
                  {
                     clip.stop();
                  }
               }

            }
            catch (Exception e)
            {
            }
         }
      }
      else
      {
         parent.fill(0, 0, 255, 140);
         hasEnteredRegion = false;
         if (loop && clip != null && loopMode == LoopMode.ACTIVE)
         {
            clip.stop();
         }
      }

      parent.rect(x, y, width, height);

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