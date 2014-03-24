package kinectdrumming;

import SimpleOpenNI.SimpleOpenNI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import processing.core.*;
import static processing.core.PApplet.concat;
import static processing.core.PApplet.println;

@SuppressWarnings("serial")
public class KinectDrumming extends PApplet
{
   SimpleOpenNI context;
   int imageWidth = 640;
   int imageHeight = 480;
   float scale = 1.5f;
   float scaledWidth;
   float scaledHeight;
   private List<Region> regions = new ArrayList<>();
   SwitchRegion switchRegion;
   private Map<Integer, PVector> handPositions = new HashMap<>();
   private static final int maxNumHands = 2;
   private boolean hasKinect = false;

   @Override
   public boolean sketchFullScreen()
   {
      return false;
   }

   @Override
   public void setup()
   {
      frameRate(30);
      System.out.println("displayWidth: " + displayWidth);
      System.out.println("displayHeight: " + displayHeight);
      System.out.println("imageWidth: " + imageWidth);
      System.out.println("imageHeight: " + imageHeight);
      scale = (float) displayHeight / (float) imageHeight;
      System.out.println("Scale: " + scale);
      scaledWidth = imageWidth * scale;
      scaledHeight = imageHeight * scale;
      System.out.println("scaledWidth: " + scaledWidth);
      System.out.println("scaledHeight: " + scaledHeight);
      size((int) scaledWidth, (int) scaledHeight);
//      frame.setResizable(false);

      context = new SimpleOpenNI(this);
      hasKinect = context.isInit();
      if (hasKinect == false)
      {
         println("Can't init SimpleOpenNI, maybe the camera is not connected!");
      }

      context.enableDepth(); // enable depthMap generation

      // disable mirror
      context.setMirror(true);

      // enable hands + gesture generation
      context.enableHand();
      context.startGesture(SimpleOpenNI.GESTURE_CLICK);

//      context.enableUser();
//      context.enableRGB();

      background(200, 0, 0);

      stroke(0, 0, 255);
      strokeWeight(3);
      smooth();

      SoundRegion region;
      region = new SoundRegion(this, scale, 0, 50, 100, 100, 0);
      region.setLoopMode(SoundLibrary.getLoopMode(0));
      regions.add(region);
      region = new SoundRegion(this, scale, 0, 152, 100, 100, 1);
      region.setLoopMode(SoundLibrary.getLoopMode(1));
      regions.add(region);
      region = new SoundRegion(this, scale, 0, 254, 100, 100, 2);
      region.setLoopMode(SoundLibrary.getLoopMode(2));
      regions.add(region);
      region = new SoundRegion(this, scale, 100, 356, 100, 100, 3);
      region.setLoopMode(SoundLibrary.getLoopMode(3));
      regions.add(region);

      switchRegion = new SwitchRegion(this, scale, 360, 0, 60, 60, "images/note.png");
      regions.add(switchRegion);

      region = new SoundRegion(this, scale, imageWidth - 100, 50, 100, 100, 4);
      region.setLoopMode(SoundLibrary.getLoopMode(4));
      regions.add(region);
      region = new SoundRegion(this, scale, imageWidth - 100, 152, 100, 100, 5);
      region.setLoopMode(SoundLibrary.getLoopMode(5));
      regions.add(region);
      region = new SoundRegion(this, scale, imageWidth - 100, 254, 100, 100, 6);
      region.setLoopMode(SoundLibrary.getLoopMode(6));
      regions.add(region);
      region = new SoundRegion(this, scale, imageWidth - 200, 356, 100, 100, 7);
      region.setLoopMode(SoundLibrary.getLoopMode(7));
      regions.add(region);
   }

   @Override
   public void draw()
   {
      // update the cam
      context.update();

      // draw depthImageMap
      if (hasKinect)
      {
         image(context.depthImage(), 0, 0, scaledWidth, scaledHeight);

         if (handPositions.size() > 0)
         {
            for (Map.Entry mapEntry : handPositions.entrySet())
            {
//            int handId = (Integer) mapEntry.getKey();
               PVector pVec = (PVector) mapEntry.getValue();

               // convert real world point to projective space
               PVector projPos = new PVector();
               context.convertRealWorldToProjective(pVec, projPos);
               float handPosX = projPos.x * scale;
               float handPosY = projPos.y * scale;

               //determine region collisions
               calcCollisions(handPosX, handPosY);

               //determine if library has been switched
               determineLibrary();

               // draw the circles on the hands
               drawHand(handPosX, handPosY);
            }
         }
      }
      else
      {
         //Use this code block for testing with mouse cursor
         calcCollisions(mouseX, mouseY);
         drawHand(mouseX, mouseY);
         determineLibrary();
      }

      drawRegions(); //draw interactive regions
   }

   private void determineLibrary()
   {
      if (switchRegion.hasChanged()) //change the sound library
      {
         int i = 0;
         for (Region r : regions)
         {
            if (r instanceof SoundRegion)
            {
               SoundRegion sr = (SoundRegion) r;
               sr.setPath(SoundLibrary.getSoundPath(i));
               sr.setImagePath(SoundLibrary.getImagePath(i));
               sr.setLoopMode(SoundLibrary.getLoopMode(i));
               i++;
            }
            else if (r instanceof SwitchRegion)
            {
               //change to picture of current library
               //r.setPicture(SoundLibrary.getLibraryImage());
            }
         }
      }
   }

   private void calcCollisions(float x, float y)
   {
      //loop through region locations to determine which is being intersected
      for (Region r : regions)
      {
         r.determineCollision(x, y);
      }
   }

   private void drawRegions()
   {
      for (Region r : regions)
      {
         r.draw();
      }
   }

   private void drawHand(float x, float y)
   {
      fill(255, 0, 0);
      stroke(255, 0, 0);
      ellipse(x, y, 25, 25);
   }

   @Override
   public void keyPressed()
   {
      switch (key)
      {
         case ' ':
            context.setMirror(!context.mirror());
            break;
      }
   }

   // -----------------------------------------------------------------
// hand events
   public void onNewHand(SimpleOpenNI curContext, int handId, PVector pos)
   {
      handPositions.put(handId, pos);
      println("onNewHand - handId: " + handId + ", pos: " + pos);
   }

   public void onTrackedHand(SimpleOpenNI curContext, int handId, PVector pos)
   {
      handPositions.put(handId, pos);
//      println("onTrackedHand - handId: " + handId + ", pos: " + pos );
   }

   public void onLostHand(SimpleOpenNI curContext, int handId)
   {
      handPositions.remove(handId);
      println("onLostHand - handId: " + handId);
   }

// -----------------------------------------------------------------
// gesture events
   public void onCompletedGesture(SimpleOpenNI curContext, int gestureType, PVector pos)
   {
      println("onCompletedGesture - gestureType: " + gestureType + ", pos: " + pos);
      if (handPositions.size() < maxNumHands)
      {
         int handId = context.startTrackingHand(pos);
         println("hand tracked: " + handId);
      }
   }

   static public void main(String[] passedArgs)
   {
      String[] appletArgs = new String[]
      {
         KinectDrumming.class
         .getName()
      };
      if (passedArgs != null)
      {
         PApplet.main(concat(appletArgs, passedArgs));
      }
      else
      {
         PApplet.main(appletArgs);
      }
   }
}
