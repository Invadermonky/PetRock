package com.invadermonky.petrock.util;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class NBTHelper {
    public static void initNBT(ItemStack stack) {
        if(!stack.hasTagCompound()) {
            stack.setTagCompound(new NBTTagCompound());
        }
    }

    public static boolean verifyTag(ItemStack stack, String key) {
        return stack.hasTagCompound() && stack.getTagCompound().hasKey(key);
    }

    public static void setBooleanTag(ItemStack stack, String key, boolean bool) {
        initNBT(stack);
        stack.getTagCompound().setBoolean(key, bool);
    }

    public static boolean getBooleanTag(ItemStack stack, String key) {
        initNBT(stack);
        return stack.getTagCompound().getBoolean(key);
    }

    public static void setIntegerTag(ItemStack stack, String key, int value) {
        initNBT(stack);
        stack.getTagCompound().setInteger(key, value);
    }

    public static int getIntegerTag(ItemStack stack, String key) {
        initNBT(stack);
        return stack.getTagCompound().getInteger(key);
    }

    public static void setIntArrayTag(ItemStack stack, String key, int... intArray) {
        initNBT(stack);
        stack.getTagCompound().setIntArray(key, intArray);
    }

    public static int[] getIntArrayTag(ItemStack stack, String key) {
        initNBT(stack);
        return stack.getTagCompound().getIntArray(key);
    }
}
