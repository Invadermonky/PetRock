package com.invadermonky.petrock.items;

import com.invadermonky.petrock.PetRock;
import com.invadermonky.petrock.handlers.ConfigHandlerPR;
import com.invadermonky.petrock.util.NBTHelper;
import com.invadermonky.petrock.util.StringHelper;
import com.invadermonky.petrock.util.libs.LibNames;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemBrokenRock extends Item {
    public static final int maxPowerLevel = 9001;

    public ItemBrokenRock() {
        this.setRegistryName(PetRock.MOD_ID, LibNames.broken_rock);
        this.setTranslationKey(this.getRegistryName().toString());
        this.setCreativeTab(CreativeTabs.MISC);
        this.setMaxStackSize(1);
        this.setNoRepair();
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if(this.isInCreativeTab(tab)) {
            ItemStack stack = new ItemStack(this);
            NBTHelper.setIntegerTag(stack, LibNames.tag_power_level, 0);
            items.add(stack);
        }
    }

    @Override
    public void onCreated(ItemStack stack, World worldIn, EntityPlayer playerIn) {
        NBTHelper.setIntegerTag(stack, LibNames.tag_power_level, 0);
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if(!NBTHelper.verifyTag(stack, LibNames.tag_power_level)) {
            NBTHelper.setIntegerTag(stack, LibNames.tag_power_level, 0);
        }
        int powerLevel = NBTHelper.getIntegerTag(stack, LibNames.tag_power_level);
        if(powerLevel >= maxPowerLevel && entityIn instanceof EntityPlayer) {
            ((EntityPlayer) entityIn).renderBrokenItemStack(stack);
            entityIn.replaceItemInInventory(itemSlot, ItemStack.EMPTY);
        }
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return (maxPowerLevel - NBTHelper.getIntegerTag(stack, LibNames.tag_power_level)) < maxPowerLevel;
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        return (double) NBTHelper.getIntegerTag(stack, LibNames.tag_power_level) / (double) maxPowerLevel;
    }

    @Override
    public ItemStack getContainerItem(ItemStack stack) {
        ItemStack copy = stack.copy();
        int powerLevel = NBTHelper.getIntegerTag(copy, LibNames.tag_power_level);
        if(powerLevel >= maxPowerLevel)
            return ItemStack.EMPTY;
        NBTHelper.setIntegerTag(copy, LibNames.tag_power_level, powerLevel + 1);
        return copy;
    }

    @Override
    public boolean hasContainerItem(ItemStack stack) {
        return true;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        int powerLevel = NBTHelper.getIntegerTag(stack, LibNames.tag_power_level);
        tooltip.add(I18n.format(StringHelper.getTranslationKey(LibNames.broken_rock, "tooltip"), maxPowerLevel - powerLevel, maxPowerLevel));
        if(ConfigHandlerPR.youAreGonnaMakeMeExplainEverythingArntYou) {
            tooltip.add(I18n.format(StringHelper.getTranslationKey(LibNames.broken_rock, "tooltip", "desc")));
        }
    }
}
