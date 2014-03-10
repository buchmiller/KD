package kinectdrumming;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;
import processing.core.PApplet;

public class RecordRegion extends Region
{
   private boolean isRecording = false;
   private boolean hasEnteredRegion = false;
   private TargetDataLine line; // the line from which audio data is captured

   public RecordRegion(PApplet parent, float scale, int x, int y, int width, int height)
   {
      super(parent);
      this.x = (int) (x * scale);
      this.y = (int) (y * scale);
      this.width = (int) (width * scale);
      this.height = (int) (height * scale);
      try
      {
         this.file = new File("sounds/recorded.wav");
      }
      catch (Exception e)
      {
      }
   }

   private AudioFormat getAudioFormat()
   {
      float sampleRate = 16000;
      int sampleSizeInBits = 8;
      int channels = 2;
      boolean signed = true;
      boolean bigEndian = true;
      AudioFormat format = new AudioFormat(sampleRate, sampleSizeInBits,
                                           channels, signed, bigEndian);
      return format;
   }

   /**
    * Captures the sound and record into a WAV file
    */
   private void startRecording()
   {
      new Thread(new Runnable()
      {
         @Override
         public void run()
         {
            try
            {
               AudioFormat format = getAudioFormat();
               DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

               // checks if system supports the data line
               if (!AudioSystem.isLineSupported(info))
               {
                  System.out.println("Line not supported");
                  return;
               }
               line = (TargetDataLine) AudioSystem.getLine(info);
               line.open(format);
               line.start();   // start capturing

               System.out.println("Start capturing...");

               AudioInputStream ais = new AudioInputStream(line);

               System.out.println("Start recording...");

               // start recording
               AudioSystem.write(ais, AudioFileFormat.Type.WAVE, new File("sounds/recording.wav"));

            }
            catch (LineUnavailableException | IOException ex)
            {
//         ex.printStackTrace();
            }
         }
      }).start();
   }

   /**
    * Closes the target data line to finish capturing and recording
    */
   private void stopRecording()
   {
      try
      {
         line.stop();
         line.close();

         Files.copy(new File("sounds/recording.wav").toPath(), new File("sounds/recorded.wav").toPath(), StandardCopyOption.REPLACE_EXISTING);
         Files.deleteIfExists(new File("sounds/recording.wav").toPath());

         System.out.println("Finished recording");
      }
      catch (Exception e)
      {
         System.out.println("Error:\n" + e);
      }
   }

   @Override
   public void draw() //needs to know if it is being intersected
   {
      if (isColliding)
      {
         if (!hasEnteredRegion)
         {
            hasEnteredRegion = true;
            if (isRecording) //stop recording
            {
               isRecording = false;
               System.out.println("Stopped recording");
               stopRecording();
            }
            else //start recording
            {
               isRecording = true;
               System.out.println("Recording started");
               startRecording();
            }
            //else do nothing - still in region
         }
      }
      else
      {
         hasEnteredRegion = false;
      }

      //Choose colors for region
      parent.stroke(0, 0, 0, 0); //no border
      if (isRecording)
      {
         parent.fill(0, 255, 255, 140);
      }
      else
      {
         parent.fill(0, 0, 255, 140);
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