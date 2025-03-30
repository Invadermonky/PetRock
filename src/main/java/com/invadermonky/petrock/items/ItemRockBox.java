package com.invadermonky.petrock.items;

import com.invadermonky.petrock.PetRock;
import com.invadermonky.petrock.registry.Registrar;
import com.invadermonky.petrock.util.StringHelper;
import com.invadermonky.petrock.util.libs.LibNames;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import vazkii.patchouli.common.item.PatchouliItems;

import javax.annotation.Nullable;
import java.util.List;

public class ItemRockBox extends Item {
    private final ItemStack guidebook;

    public ItemRockBox() {
        this.setRegistryName(PetRock.MOD_ID, LibNames.rock_box);
        this.setTranslationKey(this.getRegistryName().toString());
        this.setCreativeTab(CreativeTabs.MISC);
        this.setMaxStackSize(1);

        NBTTagCompound bookTag = new NBTTagCompound();
        bookTag.setString("patchouli:book", "petrock:pet_rock");
        this.guidebook = new ItemStack(PatchouliItems.book);
        this.guidebook.setTagCompound(bookTag);

    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        if(!worldIn.isRemote) {
            ItemStack stack = playerIn.getHeldItem(handIn);
            if (stack.getItem() == Registrar.rock_box) {
                ItemStack guideCopy = this.guidebook.copy();
                if (!playerIn.addItemStackToInventory(guideCopy)) {
                    playerIn.dropItem(guideCopy, true);
                }
                ItemStack rockStack = new ItemStack(Registrar.pet_rock);
                EnumPetRockMode.NONE.setAsActiveMode(rockStack);
                stack.shrink(1);
                return ActionResult.newResult(EnumActionResult.SUCCESS, rockStack);
            }
        }

        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(I18n.format(StringHelper.getTranslationKey("rock_box", "tooltip", "info")));
        tooltip.add(I18n.format(StringHelper.getTranslationKey("rock_box", "tooltip", "desc")));
        tooltip.add("  - " + I18n.format(StringHelper.getTranslationKey("rock_box", "tooltip", "contents"), 1, Registrar.pet_rock.getDefaultInstance().getDisplayName()));
        tooltip.add("  - " + I18n.format(StringHelper.getTranslationKey("rock_box", "tooltip", "contents"), 1, this.guidebook.getDisplayName()));
    }
}
