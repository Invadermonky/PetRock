package com.invadermonky.petrock.compat.jei;

import com.google.common.collect.Lists;
import com.invadermonky.petrock.PetRock;
import com.invadermonky.petrock.handlers.ConfigHandlerPR;
import com.invadermonky.petrock.items.EnumPetRockMode;
import com.invadermonky.petrock.registry.Registrar;
import com.invadermonky.petrock.util.StringHelper;
import com.invadermonky.petrock.util.libs.LibNames;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.Collections;

@JEIPlugin
public class JEICompat implements IModPlugin {
    @Override
    public void register(IModRegistry registry) {
        for(EnumPetRockMode rockMode : EnumPetRockMode.values()) {
            if(rockMode == EnumPetRockMode.NONE)
                continue;

            IRecipe recipe = ForgeRegistries.RECIPES.getValue(new ResourceLocation(PetRock.MOD_ID, rockMode.getTagKey()));
            if(recipe != null) {
                registry.addRecipes(Collections.singletonList(new RockUpgradeRecipeWrapper(recipe)) , VanillaRecipeCategoryUid.CRAFTING);
            }
        }

        if(ConfigHandlerPR.rockBreedingProgram) {
            registry.addIngredientInfo(Lists.newArrayList(new ItemStack(Registrar.pet_rock)), VanillaTypes.ITEM, StringHelper.getTranslationKey(LibNames.pet_rock, "jei", "info"));
        }
    }


}
