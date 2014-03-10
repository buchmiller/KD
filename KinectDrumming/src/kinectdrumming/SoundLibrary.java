package kinectdrumming;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class SoundLibrary
{
   public static enum Name
   {
      MARIO, SYNTH
   }
   private static Name activeLibrary = Name.MARIO;
   private static final Map<Name, String[]> files;

   static
   {
      Map<Name, String[]> tempMap = new HashMap<>();
      tempMap.put(Name.MARIO, new String[]
      {
         "001001",
         "sounds/mario/smb3_coin.wav",
         "sounds/mario/smb3_fireball.wav",
         "sounds/mario/smb3_level_clear.wav",
         "sounds/mario/smb3_frog_mario_walk.wav",
         "sounds/mario/smb3_jump.wav",
         "sounds/mario/smb3_hurry_up.wav"
      //add a picture path for library?
      });
      tempMap.put(Name.SYNTH, new String[]
      {
         "000001",
         "sounds/beat.wav",
         "sounds/blip.wav",
         "sounds/blip1.wav",
         "sounds/bloop.wav",
         "sounds/bloop2.wav",
         "sounds/synth_loop.wav"
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
      if (fileNum < 0 || fileNum > 5)
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
