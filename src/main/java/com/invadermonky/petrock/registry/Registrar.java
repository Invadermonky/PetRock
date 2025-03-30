package com.invadermonky.petrock.registry;

import com.invadermonky.petrock.PetRock;
import com.invadermonky.petrock.client.render.RenderPetRock;
import com.invadermonky.petrock.crafting.recipes.RecipeBasicRockUpgrade;
import com.invadermonky.petrock.crafting.recipes.RecipeJesusRockUpgrade;
import com.invadermonky.petrock.entities.EntityPetRock;
import com.invadermonky.petrock.handlers.ConfigHandlerPR;
import com.invadermonky.petrock.items.*;
import com.invadermonky.petrock.items.modes.*;
import com.invadermonky.petrock.util.libs.LibNames;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber(modid = PetRock.MOD_ID)
public class Registrar {
    public static final SoundEvent sound_radioactive_rock = makeSoundEvent(LibNames.radioactive_rock);
    public static final SoundEvent sound_soviet_rock = makeSoundEvent(LibNames.soviet_rock);

    public static final Item broken_rock = new ItemBrokenRock();
    public static final Item pet_rock = new ItemPetRock();
    public static final Item rock_box = new ItemRockBox();

    public static final IPetRockMode mode_addictive_rock = new ModeAddictiveRock();
    public static final IPetRockMode mode_attack_rock = new ModeAttackRock();
    public static final IPetRockMode mode_affectionate_rock = new ModeAffectionateRock();
    public static final IPetRockMode mode_fancy_rock = new ModeFancyRock();
    public static final IPetRockMode mode_ferocious_rock = new ModeFerociousRock();
    public static final IPetRockMode mode_guard_rock = new ModeGuardRock();
    public static final IPetRockMode mode_hot_potato = new ModeHotPotato();
    public static final IPetRockMode mode_jesus_rock = new ModeJesusRock();
    public static final IPetRockMode mode_radioactive_rock = new ModeRadioactiveRock();
    public static final IPetRockMode mode_soviet_rock = new ModeSovietRock();
    public static final IPetRockMode mode_basic_rock = new ModeBasicRock();

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().register(rock_box);
        event.getRegistry().register(pet_rock);
        if(ConfigHandlerPR.perfectlyBalanced) {
            event.getRegistry().register(broken_rock);
        }
    }

    @SubscribeEvent
    public static void registerSounds(RegistryEvent.Register<SoundEvent> event) {
        event.getRegistry().register(sound_radioactive_rock);
        if(ConfigHandlerPR.sovietRockSoundEffect) {
            event.getRegistry().register(sound_soviet_rock);
        }
    }

    private static SoundEvent makeSoundEvent(String unlocName) {
        ResourceLocation loc = new ResourceLocation(PetRock.MOD_ID, unlocName);
        return new SoundEvent(loc).setRegistryName(loc);
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        ModelLoader.setCustomModelResourceLocation(rock_box, 0, new ModelResourceLocation(rock_box.getRegistryName(), "inventory"));
        ModelLoader.setCustomModelResourceLocation(pet_rock, 0, new ModelResourceLocation(pet_rock.getRegistryName(), "inventory"));

        if(ConfigHandlerPR.perfectlyBalanced) {
            ModelLoader.setCustomModelResourceLocation(broken_rock, 0, new ModelResourceLocation(broken_rock.getRegistryName(), "inventory"));
        }

        RenderingRegistry.registerEntityRenderingHandler(EntityPetRock.class, RenderPetRock::new);
    }

    public static void registerEntities() {
        EntityRegistry.registerModEntity(new ResourceLocation(PetRock.MOD_ID, LibNames.pet_rock), EntityPetRock.class,
                LibNames.pet_rock, 100, PetRock.instance, 50, 1, true);
    }

    @SubscribeEvent
    public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {
        IForgeRegistry<IRecipe> registry = event.getRegistry();
        for(EnumPetRockMode mode : EnumPetRockMode.values()) {
            if(mode.hasCustomRecipe()) continue;
            registry.register(new RecipeBasicRockUpgrade(mode, mode.getUpgradeIngredient()));
        }
        registry.register(new RecipeJesusRockUpgrade());
    }

}
