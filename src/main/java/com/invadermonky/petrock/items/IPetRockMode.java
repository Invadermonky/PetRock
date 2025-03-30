package com.invadermonky.petrock.items;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.invadermonky.petrock.entities.EntityPetRock;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.IRarity;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import javax.annotation.Nullable;

public interface IPetRockMode {
    String getName();

    @Nullable
    Ingredient getUpgradeIngredient();

    default void onUpdate(ItemStack stack, World world, EntityLivingBase entity, int itemSlot, boolean isSelected) {

    }

    default ActionResult<ItemStack> onRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand) {
        return new ActionResult<>(EnumActionResult.PASS, player.getHeldItem(hand));
    }

    default void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft) {

    }

    default ItemStack onUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving) {
        return stack;
    }

    default EnumAction getItemUseAction() {
        return EnumAction.NONE;
    }

    default IRarity getRarity(ItemStack stack) {
        return stack.isItemEnchanted() ? EnumRarity.RARE : EnumRarity.COMMON;
    }

    default boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return false;
    }

    default Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack) {
        return HashMultimap.create();
    }

    default void onLivingHurt(LivingHurtEvent event) {

    }

    default void onProjectileImpact(World world, EntityPetRock entityPetRock, RayTraceResult result, @Nullable EnumFacing sideHit, @Nullable EntityLivingBase entityLiving) {

    }

    default float getProjectileVelocity(int charge) {
        float speed = (float) charge / 20.0f;
        speed = (speed * speed + speed * 2.0f) / 3.0f;
        return Math.min(speed, 1.0f);
    }
}
