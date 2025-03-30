package com.invadermonky.petrock.items;

import com.invadermonky.petrock.registry.Registrar;
import com.invadermonky.petrock.util.NBTHelper;
import com.invadermonky.petrock.util.libs.LibNames;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.List;

public enum EnumPetRockMode {
    /** Basic Rock, does nothing */
    NONE(Registrar.mode_basic_rock),
    /** Rock + Wool, rock spawns heart particles when held or used */
    AFFECTIONATE(Registrar.mode_affectionate_rock),
    /** Rock + Dragon's Breath, makes ender dragon roar on use */
    FEROCIOUS(Registrar.mode_ferocious_rock),
    /** Rock + Sword, gives attack damage */
    ATTACK(Registrar.mode_attack_rock),
    /** Rock + Shield, negates all damage when blocking */
    GUARD(Registrar.mode_guard_rock),
    /** Rock + SOMETHING, throws the player in the direction they are looking */
    SOVIET(Registrar.mode_soviet_rock),
    /** Rock + Sugar, eat to grant speed 6 */
    ADDICTIVE(Registrar.mode_addictive_rock),
    /** Rock + 5 bread + 2 fish, can be eaten to restore hunger */
    JESUS(Registrar.mode_jesus_rock),
    /** Rock + Blaze Powder, grants fire resist when swapping hands */
    HOT_POTATO(Registrar.mode_hot_potato),
    /** Rock + TNT, rock can be thrown to cause a massive explosion */
    RADIOACTIVE(Registrar.mode_radioactive_rock),
    /** Rock + Nether Star, does nothing */
    FANCY(Registrar.mode_fancy_rock);

    private final IPetRockMode petRockMode;

    EnumPetRockMode(IPetRockMode petRockMode) {
        this.petRockMode = petRockMode;
    }

    public IPetRockMode getPetRockMode() {
        return this.petRockMode;
    }

    public String getTagKey() {
        return this.petRockMode.getName();
    }

    public boolean hasCustomRecipe() {
        return this.petRockMode.getUpgradeIngredient() == null;
    }

    public EnumAction getItemUseAction() {
        return this.petRockMode.getItemUseAction();
    }

    public Ingredient getUpgradeIngredient() {
        if(!this.hasCustomRecipe()) {
            return this.petRockMode.getUpgradeIngredient();
        }
        return Ingredient.EMPTY;
    }

    private EnumPetRockMode next() {
        return EnumPetRockMode.values()[(this.ordinal() + 1) % EnumPetRockMode.values().length];
    }

    private EnumPetRockMode previous() {
        int length = EnumPetRockMode.values().length;
        return EnumPetRockMode.values()[(this.ordinal() + length - 1) % length];
    }

    /**
     * Gets the next Pet Rock mode that has been enabled on the passed pet rock stack.
     */
    public EnumPetRockMode next(ItemStack stack) {
        EnumPetRockMode nextMode = this.next();
        while(nextMode != this) {
            if(nextMode.hasUpgrade(stack)) {
                break;
            } else {
                nextMode = nextMode.next();
            }
        }
        return nextMode;
    }

    /**
     * Gets the previous Pet Rock mode that has been enabled on the passed pet rock stack.
     */
    public EnumPetRockMode previous(ItemStack stack) {
        EnumPetRockMode prevMode = this.previous();
        while(prevMode != this) {
            if(prevMode.hasUpgrade(stack)) {
                break;
            } else {
                prevMode = prevMode.previous();
            }
        }
        return prevMode;
    }

    public void setAsActiveMode(ItemStack stack) {
        this.applyUpgrade(stack);
        NBTHelper.setIntegerTag(stack, LibNames.tag_mode, this.ordinal());
    }

    public void applyUpgrade(ItemStack stack) {
        int[] upgrades = NBTHelper.getIntArrayTag(stack, LibNames.tag_upgrades);
        if(upgrades.length == 0 || upgrades.length != EnumPetRockMode.values().length) {
            upgrades = new int[EnumPetRockMode.values().length];
        }
        upgrades[this.ordinal()] = 1;
        NBTHelper.setIntArrayTag(stack, LibNames.tag_upgrades, upgrades);
    }

    public boolean hasUpgrade(ItemStack stack) {
        int[] upgrades = NBTHelper.getIntArrayTag(stack, LibNames.tag_upgrades);
        return upgrades.length == EnumPetRockMode.values().length && upgrades[this.ordinal()] == 1;
    }

    public boolean isUpgradeActive(ItemStack stack) {
        return this.hasUpgrade(stack) && getActiveRockMode(stack) == this;
    }

    /*
        Static Methods
     */
    public static EnumPetRockMode getActiveRockMode(ItemStack stack) {
        return EnumPetRockMode.values()[NBTHelper.getIntegerTag(stack, LibNames.tag_mode)];
    }

    public static List<EnumPetRockMode> getRockUpgrades(ItemStack stack) {
        List<EnumPetRockMode> appliedUpgrades = new ArrayList<>();
        for(EnumPetRockMode mode : EnumPetRockMode.values()) {
            if(mode.hasUpgrade(stack)) appliedUpgrades.add(mode);
        }
        return appliedUpgrades;
    }
}

