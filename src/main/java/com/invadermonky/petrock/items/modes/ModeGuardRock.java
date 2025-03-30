package com.invadermonky.petrock.items.modes;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.invadermonky.petrock.handlers.ConfigHandlerPR;
import com.invadermonky.petrock.items.EnumPetRockMode;
import com.invadermonky.petrock.items.IPetRockMode;
import com.invadermonky.petrock.util.libs.LibNames;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import javax.annotation.Nullable;
import java.util.UUID;

public class ModeGuardRock implements IPetRockMode {
    @Override
    public String getName() {
        return LibNames.guard_rock;
    }

    @Nullable
    @Override
    public Ingredient getUpgradeIngredient() {
        return Ingredient.fromStacks(new ItemStack(Items.SHIELD));
    }

    @Override
    public ActionResult<ItemStack> onRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand) {
        player.setActiveHand(hand);
        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }

    @Override
    public EnumAction getItemUseAction() {
        return EnumAction.BLOCK;
    }

    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack) {
        Multimap<String, AttributeModifier> multimap = HashMultimap.create();
        if(slot == EntityEquipmentSlot.OFFHAND && EnumPetRockMode.GUARD.isUpgradeActive(stack)) {
            multimap.put(SharedMonsterAttributes.ARMOR.getName(), new AttributeModifier(UUID.fromString("f9c58d75-afcf-43ce-956e-4fb794356ec2"),"Armor modifier", ConfigHandlerPR.guardRock, 0));
            multimap.put(SharedMonsterAttributes.ARMOR_TOUGHNESS.getName(), new AttributeModifier(UUID.fromString("2ea81026-04ac-4258-885f-b0e6a2cdc3b8"), "Armor modifier", 1.5f, 0));
        }
        return multimap;
    }

    @Override
    public void onLivingHurt(LivingHurtEvent event) {
        if(event.getEntityLiving() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.getEntityLiving();
            if(player.isHandActive()) {
                event.setCanceled(true);
            }
        }
    }
}
