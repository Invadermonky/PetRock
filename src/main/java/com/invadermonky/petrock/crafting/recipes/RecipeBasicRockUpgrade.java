package com.invadermonky.petrock.crafting.recipes;

import com.invadermonky.petrock.PetRock;
import com.invadermonky.petrock.items.EnumPetRockMode;
import com.invadermonky.petrock.registry.Registrar;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class RecipeBasicRockUpgrade extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {
    private final Ingredient upgradeIngredient;
    private final EnumPetRockMode rockMode;

    public RecipeBasicRockUpgrade(EnumPetRockMode rockMode, Ingredient upgradeIngredient) {
        this.setRegistryName(new ResourceLocation(PetRock.MOD_ID, rockMode.getTagKey()));
        this.upgradeIngredient = upgradeIngredient;
        this.rockMode = rockMode;
    }

    @Override
    public boolean matches(InventoryCrafting inv, World worldIn) {
        boolean upgradeItemFound = false;
        boolean rockFound = false;
        ItemStack checkStack;
        for(int i = 0; i < inv.getSizeInventory(); i++) {
            checkStack = inv.getStackInSlot(i);
            if(!checkStack.isEmpty()) {
                if(checkStack.getItem() == Registrar.pet_rock && !rockFound && !this.rockMode.hasUpgrade(checkStack)) {
                    rockFound = true;
                } else if(!upgradeItemFound) {
                    for(ItemStack stack : this.upgradeIngredient.getMatchingStacks()) {
                        if(ItemStack.areItemsEqual(stack, checkStack)) {
                            upgradeItemFound = true;
                            break;
                        }
                    }
                    if(!upgradeItemFound) {
                        return false;
                    }
                } else {
                    return false;
                }
            }
        }
        return upgradeItemFound && rockFound;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {
        ItemStack rockStack = ItemStack.EMPTY;
        for(int i = 0; i < inv.getSizeInventory(); i++) {
            rockStack = inv.getStackInSlot(i);
            if(rockStack.getItem() == Registrar.pet_rock)
                break;
        }

        if(!rockStack.isEmpty()) {
            this.rockMode.applyUpgrade(rockStack);
        }

        return rockStack.copy();
    }

    @Override
    public boolean canFit(int width, int height) {
        return width * height > 1;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> ingredients = NonNullList.create();
        ingredients.add(Ingredient.fromStacks(new ItemStack(Registrar.pet_rock)));
        ingredients.add(this.upgradeIngredient);
        return ingredients;
    }

    @Override
    public ItemStack getRecipeOutput() {
        ItemStack stack = new ItemStack(Registrar.pet_rock);
        this.rockMode.setAsActiveMode(stack);
        return stack;
    }
}
