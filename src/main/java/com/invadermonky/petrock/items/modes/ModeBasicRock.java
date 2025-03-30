package com.invadermonky.petrock.items.modes;

import com.invadermonky.petrock.items.IPetRockMode;
import com.invadermonky.petrock.util.libs.LibNames;
import net.minecraft.item.crafting.Ingredient;

import javax.annotation.Nullable;

public class ModeBasicRock implements IPetRockMode {
    @Override
    public String getName() {
        return LibNames.pet_rock;
    }

    @Nullable
    @Override
    public Ingredient getUpgradeIngredient() {
        return null;
    }
}
