package com.invadermonky.petrock.items;

import com.google.common.collect.Multimap;
import com.invadermonky.petrock.PetRock;
import com.invadermonky.petrock.handlers.ConfigHandlerPR;
import com.invadermonky.petrock.handlers.RockBreedingHandler;
import com.invadermonky.petrock.util.StringHelper;
import com.invadermonky.petrock.util.libs.LibNames;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.IRarity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class ItemPetRock extends Item {
    public ItemPetRock() {
        this.setRegistryName(PetRock.MOD_ID, LibNames.pet_rock);
        this.setTranslationKey(this.getRegistryName().toString());
        this.setCreativeTab(CreativeTabs.MISC);
        this.setMaxStackSize(1);
        this.addPropertyOverride(new ResourceLocation(PetRock.MOD_ID, LibNames.tag_blocking), (stack, worldIn, entityIn) ->
                EnumPetRockMode.GUARD.isUpgradeActive(stack) && entityIn != null && entityIn.isHandActive() && entityIn.getActiveItemStack() == stack ? 1 : 0);
        this.addPropertyOverride(new ResourceLocation(PetRock.MOD_ID, LibNames.tag_mode), (stack, worldIn, entityIn) ->
                EnumPetRockMode.getActiveRockMode(stack).ordinal());
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if(this.isInCreativeTab(tab)) {
            ItemStack stack = new ItemStack(this);
            EnumPetRockMode.NONE.setAsActiveMode(stack);
            items.add(stack);

            ItemStack upgradedStack = stack.copy();
            for(EnumPetRockMode mode : EnumPetRockMode.values()) {
                mode.applyUpgrade(stack);
            }
            items.add(upgradedStack);
        }
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        EnumPetRockMode mode = EnumPetRockMode.getActiveRockMode(stack);
        return net.minecraft.util.text.translation.I18n.translateToLocal(StringHelper.getTranslationKey(mode.getTagKey(), "item", "name"));
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return EnumPetRockMode.getActiveRockMode(oldStack) != EnumPetRockMode.getActiveRockMode(newStack) || slotChanged;
    }

    @Override
    public void onUpdate(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
        if(entity instanceof EntityLivingBase) {
            EnumPetRockMode activeMode = EnumPetRockMode.getActiveRockMode(stack);
            activeMode.getPetRockMode().onUpdate(stack, world, (EntityLivingBase) entity, itemSlot, isSelected);
        }
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        EnumPetRockMode activeMode = EnumPetRockMode.getActiveRockMode(stack);
        if(player.isSneaking()) {
            if(!world.isRemote)
                activeMode.next(stack).setAsActiveMode(stack);
            return new ActionResult<>(EnumActionResult.SUCCESS, stack);
        } else {
            return activeMode.getPetRockMode().onRightClick(stack, world, player, hand);
        }
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft) {
        EnumPetRockMode activeMode = EnumPetRockMode.getActiveRockMode(stack);
        activeMode.getPetRockMode().onPlayerStoppedUsing(stack, worldIn, entityLiving, timeLeft);
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving) {
        EnumPetRockMode activeMode = EnumPetRockMode.getActiveRockMode(stack);
        return activeMode.getPetRockMode().onUseFinish(stack, worldIn, entityLiving);
    }

    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack) {
        EnumPetRockMode petRockMode = EnumPetRockMode.getActiveRockMode(stack);
        return petRockMode.getPetRockMode().getAttributeModifiers(slot, stack);
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumPetRockMode.getActiveRockMode(stack).getItemUseAction();
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        EnumPetRockMode mode = EnumPetRockMode.getActiveRockMode(stack);
        switch (mode.getItemUseAction()) {
            case NONE:
                break;
            case EAT:
            case DRINK:
                return 32;
            case BLOCK:
            case BOW:
                return 72000;
        }
        return super.getMaxItemUseDuration(stack);
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        EnumPetRockMode petRockMode = EnumPetRockMode.getActiveRockMode(stack);
        return petRockMode.getPetRockMode().canApplyAtEnchantingTable(stack, enchantment) || super.canApplyAtEnchantingTable(stack, enchantment);
    }

    @Override
    public IRarity getForgeRarity(ItemStack stack) {
        return EnumPetRockMode.getActiveRockMode(stack).getPetRockMode().getRarity(stack);
    }

    @Override
    public boolean onEntityItemUpdate(EntityItem entityItem) {
        RockBreedingHandler.onEntityItemUpdate(entityItem);
        //Method must always return false;
        return false;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        boolean noFunMode = ConfigHandlerPR.youAreGonnaMakeMeExplainEverythingArntYou;
        for(EnumPetRockMode mode : EnumPetRockMode.values()) {
            if(mode.hasUpgrade(stack)) {
                if(mode.isUpgradeActive(stack)) {
                    tooltip.add(TextFormatting.AQUA + " - " + I18n.format(StringHelper.getTranslationKey(mode.getTagKey(), "tooltip")));
                } else {
                    tooltip.add(TextFormatting.DARK_GRAY + " - " + I18n.format(StringHelper.getTranslationKey(mode.getTagKey(), "tooltip")));
                }
                if(GuiScreen.isShiftKeyDown() && noFunMode) {
                    tooltip.add(TextFormatting.DARK_GRAY + "" + TextFormatting.ITALIC + "  " + I18n.format(StringHelper.getTranslationKey(mode.getTagKey(), "tooltip", "desc")));
                }
            }
        }
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

}
