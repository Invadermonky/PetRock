package com.invadermonky.petrock.items.modes;

import com.invadermonky.petrock.handlers.ConfigHandlerPR;
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
import net.minecraft.world.World;
import net.minecraftforge.common.IRarity;

import javax.annotation.Nullable;

public class ModeFerociousRock implements IPetRockMode {
    @Override
    public String getName() {
        return LibNames.ferocious_rock;
    }

    @Nullable
    @Override
    public Ingredient getUpgradeIngredient() {
        return Ingredient.fromStacks(new ItemStack(Items.DRAGON_BREATH));
    }

    @Override
    public ActionResult<ItemStack> onRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand) {
        player.getCooldownTracker().setCooldown(stack.getItem(), 200);
        player.playSound(SoundEvents.ENTITY_ENDERDRAGON_GROWL, (float) ConfigHandlerPR.ferocityMultiplier, 1.0f);
        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }

    @Override
    public IRarity getRarity(ItemStack stack) {
        return EnumRarity.RARE;
    }
}
