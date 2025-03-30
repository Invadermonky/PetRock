package com.invadermonky.petrock.handlers;

import com.invadermonky.petrock.PetRock;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = PetRock.MOD_ID)
public class ConfigHandlerPR {
    @Config.RequiresMcRestart
    @Config.RangeDouble(min = 1.0, max = 10000000)
    @Config.Comment("https://loadingartist.com/comic/pet-rock/")
    public static double attackRock = 12.0f;

    @Config.RangeInt(min = 0, max = 100)
    @Config.Comment("Awwwwww... It loves you!")
    public static int emotionalSupportRock = 20;

    @Config.RangeDouble(min = 0.1, max = 10.0)
    @Config.Comment("Showing purse dogs who the real boss is. Jokes aside, this is a volume slider. Don't make it too loud.")
    public static double ferocityMultiplier = 2.0;

    @Config.RequiresMcRestart
    @Config.RangeDouble(min = 0, max = 20)
    @Config.Comment("It will always put you first between it and a hard place.")
    public static double guardRock = 6.0;

    @Config.Comment("Now 7% less likely to explode!")
    public static int hotPotato = 60;

    @Config.Comment("Bread and fish...")
    public static boolean jesusRock = true;

    @Config.RequiresMcRestart
    @Config.Comment("Enables the \"Perfectly Balanced\" Broken Rock. Totally won't destroy modpack balance. Trust me.")
    public static boolean perfectlyBalanced = true;

    @Config.Comment("Their numbers have dwindled since humans began harvesting them during the paleolithic era. If the program is\n" +
                    "effective, we will begin releasing them back into their natural habitat!")
    public static boolean rockBreedingProgram = true;

    @Config.Comment("Previous job experience:\n" +
            "  - Mini Nuke    Fallout (various)    2008 - 2018\n" +
            "  - Melta Bomb   Space Marine         2011 - 2024")
    public static float somewhatRadioactive = 8.0f;

    @Config.Comment("Enables the Soviet Rock USSR State Anthem sound effect. You can disable it here if it triggers you.")
    public static boolean sovietRockSoundEffect = true;

    @Config.Comment("In Soviet Russia, rock throws you!")
    public static float whenInRussia = 0.7f;

    @Config.Comment("Gives the pet rock a tooltip describing the different upgrades. For people who like spoilers.")
    public static boolean youAreGonnaMakeMeExplainEverythingArntYou = false;

    @Mod.EventBusSubscriber(modid = PetRock.MOD_ID)
    public static class ConfigChangeListener {
        @SubscribeEvent
        public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
            if(event.getModID().equals(PetRock.MOD_ID)) {
                ConfigManager.sync(PetRock.MOD_ID, Config.Type.INSTANCE);
            }
        }
    }
}
