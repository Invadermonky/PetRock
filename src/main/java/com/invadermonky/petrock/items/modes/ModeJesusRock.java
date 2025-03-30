package com.invadermonky.petrock.items.modes;

import com.invadermonky.petrock.handlers.ConfigHandlerPR;
import com.invadermonky.petrock.items.IPetRockMode;
import com.invadermonky.petrock.util.libs.LibNames;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.common.IRarity;

import javax.annotation.Nullable;

public class ModeJesusRock implements IPetRockMode {
    @Override
    public String getName() {
        return LibNames.jesus_rock;
    }

    @Nullable
    @Override
    public Ingredient getUpgradeIngredient() {
        return null;
    }

    @Override
    public void onUpdate(ItemStack stack, World world, EntityLivingBase entityLiving, int itemSlot, boolean isSelected) {
        if(!world.isRemote && isSelected && world.getTotalWorldTime() % 60L == 0) {
            entityLiving.removePotionEffect(MobEffects.WITHER);
            entityLiving.removePotionEffect(MobEffects.POISON);
        }
    }

    @Override
    public ActionResult<ItemStack> onRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand) {
        if(ConfigHandlerPR.jesusRock) {
            player.setActiveHand(hand);
            return new ActionResult<>(EnumActionResult.SUCCESS, stack);
        }
        return IPetRockMode.super.onRightClick(stack, world, player, hand);
    }

    @Override
    public ItemStack onUseFinish(ItemStack stack, World world, EntityLivingBase entityLiving) {
        if(!world.isRemote && entityLiving instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entityLiving;
            ItemFood food = (ItemFood) (world.rand.nextInt(7) < 5 ? Items.BREAD : Items.COOKED_FISH);
            ItemStack foodStack = new ItemStack(food);
            player.getFoodStats().addStats(food.getHealAmount(foodStack), food.getSaturationModifier(foodStack));
        }
        if(entityLiving instanceof EntityPlayerMP) {
            CriteriaTriggers.CONSUME_ITEM.trigger((EntityPlayerMP) entityLiving, stack);
        }
        return stack;
    }

    @Override
    public EnumAction getItemUseAction() {
        return ConfigHandlerPR.jesusRock ? EnumAction.EAT : EnumAction.NONE;
    }

    @Override
    public IRarity getRarity(ItemStack stack) {
        return EnumRarity.UNCOMMON;
    }
}
