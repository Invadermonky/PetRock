package com.invadermonky.petrock.crafting.recipes;

import com.invadermonky.petrock.PetRock;
import com.invadermonky.petrock.items.EnumPetRockMode;
import com.invadermonky.petrock.registry.Registrar;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class RecipeJesusRockUpgrade extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {
    public RecipeJesusRockUpgrade() {
        this.setRegistryName(new ResourceLocation(PetRock.MOD_ID, EnumPetRockMode.JESUS.getTagKey()));
    }

    @Override
    public boolean matches(InventoryCrafting inv, World worldIn) {
        int breadFound = 0;         //Needs 5 loaves bread
        int fishFound = 0;          //Needs 2 fish
        boolean rockFound = false;  //Needs rock without jesus nbt

        ItemStack checkStack;
        for(int i = 0; i < inv.getSizeInventory(); i++) {
            checkStack = inv.getStackInSlot(i);
            if(!checkStack.isEmpty()) {
                if(checkStack.getItem() == Items.BREAD) {
                    breadFound++;
                } else if(checkStack.getItem() == Items.FISH) {
                    fishFound++;
                } else if(checkStack.getItem() == Registrar.pet_rock && !rockFound && !EnumPetRockMode.JESUS.hasUpgrade(checkStack)) {
                    rockFound = true;
                } else {
                    return false;
                }
            }
        }
        return rockFound && breadFound == 5 && fishFound == 2;
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
            EnumPetRockMode.JESUS.applyUpgrade(rockStack);
        }
        return rockStack;
    }

    @Override
    public boolean canFit(int width, int height) {
        return width * height > 8;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> ingredients = NonNullList.create();
        ingredients.add(Ingredient.fromStacks(new ItemStack(Registrar.pet_rock)));
        for(int i = 0; i < 5; i++) {
            ingredients.add(Ingredient.fromItem(Items.BREAD));
        }
        for(int i = 0; i < 2; i++) {
            ingredients.add(Ingredient.fromItem(Items.FISH));
        }
        return ingredients;
    }

    @Override
    public ItemStack getRecipeOutput() {
        ItemStack rockStack = new ItemStack(Registrar.pet_rock);
        EnumPetRockMode.JESUS.setAsActiveMode(rockStack);
        return rockStack;
    }

}
