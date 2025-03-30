package com.invadermonky.petrock.items.modes;

import com.invadermonky.petrock.items.IPetRockMode;
import com.invadermonky.petrock.util.libs.LibNames;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.common.IRarity;

import javax.annotation.Nullable;

public class ModeFancyRock implements IPetRockMode {
    @Override
    public String getName() {
        return LibNames.fancy_rock;
    }

    @Override
    public ActionResult<ItemStack> onRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand) {
        world.playSound(null, player.getPosition(), SoundEvents.ENTITY_VILLAGER_AMBIENT, SoundCategory.PLAYERS, 1.0f, 1.0f);
        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }

    @Nullable
    @Override
    public Ingredient getUpgradeIngredient() {
        return Ingredient.fromItem(Items.NETHER_STAR);
    }

    @Override
    public IRarity getRarity(ItemStack stack) {
        return EnumRarity.EPIC;
    }
}
