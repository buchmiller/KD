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
   SwitchRegion[] switchRegions = new SwitchRegion[2];
   private Map<Integer, PVector> handPositions = new HashMap<>();
   private static final int maxNumHands = 2;
   private int backColor = color(49, 101, 156);
   private int buttonColor = color(99, 154, 206);
   private int buttonOnColor = color(148, 186, 231);
   private int buttonBorderColor = color(74, 121, 165);
   private int handColor = color(74, 121, 165);
   private int handBorderColor = color(255, 255, 255);
//   private float timer = 0;
//   private float delay = 1000 / 2f; //2 fps
//   private List<Integer> timeData = new ArrayList<>();
//   private int myTime = 0;

   @Override
   public boolean sketchFullScreen()
   {
      return true;
   }

   @Override
   public void setup()
   {
//      frameRate(30);
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
      frame.setResizable(false);

      context = new SimpleOpenNI(this);
      if (context.isInit() == false)
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
      strokeWeight(10);
      smooth();

      SoundRegion region;
      region = new SoundRegion(this, scale, 0, 50, 100, 100, 0);
      region.setLoopMode(SoundLibrary.getLoopMode(0));
      regions.add(region);
      region = new SoundRegion(this, scale, 0, 150, 100, 100, 1);
      region.setLoopMode(SoundLibrary.getLoopMode(1));
      regions.add(region);
      region = new SoundRegion(this, scale, 0, 250, 100, 100, 2);
      region.setLoopMode(SoundLibrary.getLoopMode(2));
      regions.add(region);
      region = new SoundRegion(this, scale, 100, 350, 100, 100, 3);
      region.setLoopMode(SoundLibrary.getLoopMode(3));
      regions.add(region);

      switchRegions[0] = new SwitchRegion(this, scale, 260, 0, 60, 60, SoundLibrary.getImagePath(false));
      switchRegions[0].setRightSwitch(false);
      regions.add(switchRegions[0]);
      switchRegions[1] = new SwitchRegion(this, scale, 320, 0, 60, 60, SoundLibrary.getImagePath(true));
      switchRegions[1].setRightSwitch(true);
      regions.add(switchRegions[1]);

      region = new SoundRegion(this, scale, imageWidth - 100, 50, 100, 100, 4);
      region.setLoopMode(SoundLibrary.getLoopMode(4));
      regions.add(region);
      region = new SoundRegion(this, scale, imageWidth - 100, 150, 100, 100, 5);
      region.setLoopMode(SoundLibrary.getLoopMode(5));
      regions.add(region);
      region = new SoundRegion(this, scale, imageWidth - 100, 250, 100, 100, 6);
      region.setLoopMode(SoundLibrary.getLoopMode(6));
      regions.add(region);
      region = new SoundRegion(this, scale, imageWidth - 200, 350, 100, 100, 7);
      region.setLoopMode(SoundLibrary.getLoopMode(7));
      regions.add(region);

      for (Region r : regions)
      {
         r.setNormalColor(buttonColor);
         r.setActiveColor(buttonOnColor);
         r.setBorderColor(buttonBorderColor);
      }
   }

   @Override
   public void draw()
   {
//      long startTime = 1;
//      long endTime = 1;

      // update the cam
      context.update(); //2-3 ms, 5-6 ms with userImage

      background(backColor);
      strokeWeight(10);
      drawRegions(); //draw interactive regions //6-17 ms

      // draw depthImageMap
      if (context.isInit()) //43-45 ms
      {
//         PImage img = context.userImage(); //7-8 ms
//         image(img, 0, 0, scaledWidth, scaledHeight); //35-37 ms

         for (Map.Entry mapEntry : handPositions.entrySet()) //0.001 ms
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
            strokeWeight(4);
            drawHand(handPosX, handPosY);
         }
      }
      else //1-1.5 ms
      {
         //Use this code block for testing with mouse cursor
         calcCollisions(mouseX, mouseY);
         strokeWeight(4);
         drawHand(mouseX, mouseY);
         determineLibrary();
      }

//      startTime = System.nanoTime();
//      endTime = System.nanoTime();

      //output current framerate
//      fill(255, 0, 0);
//      textSize(32);
//      text("fps: " + (int) frameRate, 10, 30);
//
//      timeData.add((int) (endTime - startTime) / 1000);
//      if (millis() > timer)
//      {
//         int sum = 0;
//         for (int i : timeData)
//         {
//            sum += i;
//         }
//         myTime = sum / timeData.size();
//         timeData.clear();
//         timer += delay;
//      }
//      fill(255, 0, 0);
//      textSize(32);
//      text(myTime + " microseconds", 200, 40);
   }

   private void determineLibrary()
   {
      if (switchRegions[0].hasChanged() || switchRegions[1].hasChanged()) //change the sound library
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
         }

         //change pictures of left/right regions
         switchRegions[0].setImagePath(SoundLibrary.getImagePath(false));
         switchRegions[1].setImagePath(SoundLibrary.getImagePath(true));
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
      fill(handColor);
      stroke(handBorderColor);
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
