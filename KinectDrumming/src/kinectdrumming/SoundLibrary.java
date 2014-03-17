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
   private static Name activeLibrary = Name.MARIO;
   private static final Map<Name, String[]> files;

   static
   {
      Map<Name, String[]> tempMap = new HashMap<>();
      tempMap.put(Name.MARIO, new String[]
      {
         "00010001",
         "sounds/mario/smb3_coin.wav",
         "sounds/mario/smb3_fireball.wav",
         "sounds/mario/smb3_frog_mario_walk.wav",
         "sounds/mario/smb3_level_clear.wav",
         "sounds/mario/smb3_coin.wav",
         "sounds/mario/smb3_fireball.wav",
         "sounds/mario/smb3_jump.wav",
         "sounds/mario/smb3_hurry_up.wav"
      //add a picture path for library?
      });
      tempMap.put(Name.SYNTH, new String[]
      {
         "00000100",
         "sounds/beat.wav",
         "sounds/blip.wav",
         "sounds/blip1.wav",
         "sounds/bloop.wav",
         "sounds/bloop2.wav",
         "sounds/synth_loop.wav",
         "sounds/beat.wav",
         "sounds/beat.wav"
      });
      tempMap.put(Name.BIT8, new String[]
      {
         "11111111",
         "sounds/8bit/1.wav",
         "sounds/8bit/2.wav",
         "sounds/8bit/3.wav",
         "sounds/8bit/4.wav",
         "sounds/8bit/5.wav",
         "sounds/8bit/6.wav",
         "sounds/8bit/7.wav",
         "sounds/8bit/8.wav"
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

   //TODO add getPicturePath?
   public static boolean isLooping(int fileNum)
   {
      return files.get(activeLibrary)[0].charAt(fileNum) == '1';
   }
}
