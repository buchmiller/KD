package kinectdrumming;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class SoundLibrary
{
   public static enum Name
   {
      MARIO, DRUMS1, DRUMS2, TRIBAL, BIT8
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
         "images/mario/mario.png",
         "images/mario/life.png",
         "images/mario/jump.png",
         "images/mario/coin.png",
         "images/mario/star.png",
         "images/mario/powerup.png",
         "images/mario/pipe.png",
         "images/mario/fireball.png"
      //add a picture path for library?
      });
      tempMap.put(Name.DRUMS1, new String[]
      {
         "T0000000",
         "sounds/drumset1/easy_guitar.wav", //replace with loop
         "sounds/drumset1/hat.wav",
         "sounds/drumset1/hat-open-3.wav",
         "sounds/drumset1/kick-001.wav",
         "sounds/drumset1/drum_sticks.wav",
         "sounds/drumset1/snare-rimshot.wav",
         "sounds/drumset1/snare-rim.wav",
         "sounds/drumset1/snare.wav",
         "images/mario/coin.png",
         "images/mario/coin.png",
         "images/mario/coin.png",
         "images/mario/coin.png",
         "images/mario/coin.png",
         "images/mario/coin.png",
         "images/mario/coin.png",
         "images/mario/coin.png"
      });
      tempMap.put(Name.DRUMS2, new String[]
      {
         "00000000",
         "sounds/drumset2/snare-001.wav",
         "sounds/drumset2/snare-006.wav",
         "sounds/drumset2/tom.wav",
         "sounds/drumset2/kick-006.wav",
         "sounds/drumset2/perc-1.wav",
         "sounds/drumset2/ride-bell.wav",
         "sounds/drumset2/ride.wav",
         "sounds/drumset2/sidestick.wav",
         "images/mario/fireball.png",
         "images/mario/fireball.png",
         "images/mario/fireball.png",
         "images/mario/fireball.png",
         "images/mario/fireball.png",
         "images/mario/fireball.png",
         "images/mario/fireball.png",
         "images/mario/fireball.png"
      });
      tempMap.put(Name.TRIBAL, new String[]
      {
         "0000T000",
         "sounds/tribal/tribalfloor.wav",
         "sounds/tribal/bongos.wav",
         "sounds/tribal/bongo_1.wav",
         "sounds/tribal/bongo_2.wav",
         "sounds/tribal/tribal-beat-1.wav",
         "sounds/tribal/bongo-split.wav",
         "sounds/tribal/bongo_3.wav",
         "sounds/tribal/bongo_4.wav",
         "images/tribal/conga.png",
         "images/tribal/bongos.png",
         "images/tribal/bongo1.png",
         "images/tribal/bongo2.png",
         "images/tribal/bongo1.png",
         "images/tribal/steel.png",
         "images/tribal/bongo3.png",
         "images/tribal/bongo4.png"
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

   private static Name getNextLibrary()
   {
      return activeLibrary.ordinal() < Name.values().length - 1
            ? Name.values()[activeLibrary.ordinal() + 1]
            : Name.values()[0];
   }

   private static Name getPreviousLibrary()
   {
      return activeLibrary.ordinal() > 0
            ? Name.values()[activeLibrary.ordinal() - 1]
            : Name.values()[Name.values().length - 1];
   }

   public static void nextLibrary()
   {
      activeLibrary = getNextLibrary();
      System.out.println("New library - " + activeLibrary.toString());
   }

   public static void previousLibrary()
   {
      activeLibrary = getPreviousLibrary();
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

   public static String getImagePath(boolean isRightSwitch)
   {
      Name lib = isRightSwitch ? getNextLibrary() : getPreviousLibrary();
      return files.get(lib)[9];
   }

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
