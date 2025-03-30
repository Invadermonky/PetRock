package com.invadermonky.petrock.items.modes;

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
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.common.IRarity;

import javax.annotation.Nullable;

public class ModeAddictiveRock implements IPetRockMode {
    @Override
    public String getName() {
        return LibNames.addictive_rock;
    }

    @Nullable
    @Override
    public Ingredient getUpgradeIngredient() {
        return Ingredient.fromItem(Items.SUGAR);
    }

    @Override
    public void onUpdate(ItemStack stack, World world, EntityLivingBase entityLiving, int itemSlot, boolean isSelected) {
        if(!world.isRemote && world.getTotalWorldTime() % 60L == 0 && world.rand.nextInt(50) == 0) {
            entityLiving.addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, 600, 2, true, false));
            entityLiving.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 600, 1, true, false));
        }
    }

    @Override
    public ActionResult<ItemStack> onRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand) {
        player.setActiveHand(hand);
        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }

    @Override
    public ItemStack onUseFinish(ItemStack stack, World world, EntityLivingBase entityLiving) {
        if(!world.isRemote) {
            entityLiving.removePotionEffect(MobEffects.WEAKNESS);
            entityLiving.removePotionEffect(MobEffects.SLOWNESS);
            entityLiving.addPotionEffect(new PotionEffect(MobEffects.SPEED, 100, 3));
            if(world.rand.nextInt(10) == 0) {
                entityLiving.addPotionEffect(new PotionEffect(MobEffects.NAUSEA, 400, 3));
            }
        }
        if(entityLiving instanceof EntityPlayerMP) {
            CriteriaTriggers.CONSUME_ITEM.trigger((EntityPlayerMP) entityLiving, stack);
        }
        return stack;
    }

    @Override
    public EnumAction getItemUseAction() {
        return EnumAction.DRINK;
    }

    @Override
    public IRarity getRarity(ItemStack stack) {
        return EnumRarity.UNCOMMON;
    }
}
