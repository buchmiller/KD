package kinectdrumming;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class SoundLibrary
{
   public static enum Name
   {
      MARIO, SYNTH, BIT8
   }

   public static enum LoopMode
   {
      NONE, ACTIVE, TOGGLE
   }
   private static Name activeLibrary = Name.MARIO;
   private static final Map<Name, String[]> files;

   static
   {
      Map<Name, String[]> tempMap = new HashMap<>();
      tempMap.put(Name.MARIO, new String[]
      {
         "1000T000",
         "sounds/mario/smb3_level_clear.wav",
         "sounds/mario/smb3_1_up.wav",
         "sounds/mario/smb3_jump.wav",
         "sounds/mario/smb3_coin.wav",
         "sounds/mario/star_power.wav",
         "sounds/mario/smb3_power_up.wav",
         "sounds/mario/smb3_pipe.wav",
         "sounds/mario/smb3_fireball.wav",
         "images/mario/coin.png",
         "images/mario/life.png",
         "images/mario/jump.png",
         "images/mario/coin.png",
         "images/mario/star.png",
         "images/mario/powerup.png",
         "images/mario/pipe.png",
         "images/mario/fireball.png"
      //add a picture path for library?
      });
      tempMap.put(Name.SYNTH, new String[]
      {
         "00000T00",
         "sounds/beat.wav",
         "sounds/blip.wav",
         "sounds/blip1.wav",
         "sounds/bloop.wav",
         "sounds/bloop2.wav",
         "sounds/synth_loop.wav",
         "sounds/beat.wav",
         "sounds/beat.wav",
         "images/mario/coin.png",
         "images/mario/coin.png",
         "images/mario/coin.png",
         "images/mario/coin.png",
         "images/mario/coin.png",
         "images/mario/coin.png",
         "images/mario/coin.png",
         "images/mario/coin.png"
      });
      tempMap.put(Name.BIT8, new String[]
      {
         "11111111",
         "sounds/8bit/tone_1.wav",
         "sounds/8bit/tone_2.wav",
         "sounds/8bit/tone_3.wav",
         "sounds/8bit/tone_4.wav",
         "sounds/8bit/tone_5.wav",
         "sounds/8bit/tone_6.wav",
         "sounds/8bit/tone_7.wav",
         "sounds/8bit/tone_8.wav",
         "images/8bit/A.png",
         "images/8bit/B.png",
         "images/8bit/C.png",
         "images/8bit/D.png",
         "images/8bit/E.png",
         "images/8bit/F.png",
         "images/8bit/G.png",
         "images/8bit/A.png"
      });
      files = Collections.unmodifiableMap(tempMap);
   }

   private SoundLibrary()
   {
   }

   public static void switchLibrary(Name name)
   {
      activeLibrary = name;
   }

   public static void nextLibrary()
   {
      activeLibrary = activeLibrary.ordinal() < Name.values().length - 1
            ? Name.values()[activeLibrary.ordinal() + 1]
            : Name.values()[0];

      System.out.println("New library - " + activeLibrary.toString());
   }

   public static String getSoundPath(int fileNum)
   {
      if (fileNum < 0 || fileNum > 7)
      {
         return "";
      }

      return files.get(activeLibrary)[fileNum + 1];
   }

   public static String getImagePath(int fileNum)
   {
      if (fileNum < 0 || fileNum > 7)
      {
         return "";
      }

      return files.get(activeLibrary)[fileNum + 9];
   }

   //TODO add getPicturePath?
   public static LoopMode getLoopMode(int fileNum)
   {
      LoopMode mode = LoopMode.NONE;
      switch (files.get(activeLibrary)[0].charAt(fileNum))
      {
         case '0':
            mode = LoopMode.NONE;
            break;
         case '1':
            mode = LoopMode.ACTIVE;
            break;
         case 'T':
            mode = LoopMode.TOGGLE;
            break;
      }

      return mode;
   }
}
