package com.invadermonky.petrock.compat.jei;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.wrapper.IShapedCraftingRecipeWrapper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RockUpgradeRecipeWrapper implements IShapedCraftingRecipeWrapper {
    private final IRecipe recipe;

    public RockUpgradeRecipeWrapper(IRecipe recipe) {
        this.recipe = recipe;
    }

    @Override
    public int getWidth() {
        return this.recipe.canFit(2, 2) ? 2 : 3;
    }

    @Override
    public int getHeight() {
        return this.recipe.canFit(2, 2) ? 2 : 3;
    }

    @Override
    public void getIngredients(IIngredients iIngredients) {
        List<List<ItemStack>> ingredientList = new ArrayList<>();
        for(Ingredient ingredient : this.recipe.getIngredients()) {
            List<ItemStack> stacks = Arrays.asList(ingredient.getMatchingStacks());
            ingredientList.add(stacks);
        }
        iIngredients.setInputLists(VanillaTypes.ITEM, ingredientList);
        iIngredients.setOutput(VanillaTypes.ITEM, this.recipe.getRecipeOutput());
    }
}
