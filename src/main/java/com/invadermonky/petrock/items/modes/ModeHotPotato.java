package com.invadermonky.petrock.items.modes;

import com.invadermonky.petrock.handlers.ConfigHandlerPR;
import com.invadermonky.petrock.items.IPetRockMode;
import com.invadermonky.petrock.util.NBTHelper;
import com.invadermonky.petrock.util.libs.LibNames;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class ModeHotPotato implements IPetRockMode {
    @Override
    public String getName() {
        return LibNames.hot_potato;
    }

    @Nullable
    @Override
    public Ingredient getUpgradeIngredient() {
        return Ingredient.fromItem(Items.BLAZE_POWDER);
    }

    @Override
    public void onUpdate(ItemStack stack, World world, EntityLivingBase entity, int itemSlot, boolean isSelected) {
        if(!world.isRemote) {
            boolean held = false;
            for (EnumHand hand : EnumHand.values()) {
                EnumHand tagHand = EnumHand.values()[NBTHelper.getIntegerTag(stack, LibNames.tag_hand)];
                if (entity.getHeldItem(hand) == stack) {
                    held = true;
                    if (tagHand != hand) {
                        this.swapHands(stack, entity, hand);
                    } else {
                        this.handleHeld(stack, entity);
                    }
                }
            }

            if (!held) {
                NBTHelper.setIntegerTag(stack, LibNames.tag_held, 0);
            }
        }
    }

    private void swapHands(ItemStack stack, EntityLivingBase entity, EnumHand newHand) {
        NBTHelper.setIntegerTag(stack, LibNames.tag_hand, newHand.ordinal());
        NBTHelper.setIntegerTag(stack, LibNames.tag_held, 0);
        entity.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, ConfigHandlerPR.hotPotato));
    }

    private void handleHeld(ItemStack stack, EntityLivingBase entityLiving) {
        int heldDuration = NBTHelper.getIntegerTag(stack, LibNames.tag_held);
        if(heldDuration > ConfigHandlerPR.hotPotato * 2) {
            entityLiving.setFire(10);
        } else {
            NBTHelper.setIntegerTag(stack, LibNames.tag_held, ++heldDuration);
        }
    }
}
