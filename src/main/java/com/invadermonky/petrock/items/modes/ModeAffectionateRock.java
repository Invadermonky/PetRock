package com.invadermonky.petrock.items.modes;

import com.invadermonky.petrock.handlers.ConfigHandlerPR;
import com.invadermonky.petrock.items.IPetRockMode;
import com.invadermonky.petrock.registry.Registrar;
import com.invadermonky.petrock.util.libs.LibNames;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.OreIngredient;

import javax.annotation.Nullable;

public class ModeAffectionateRock implements IPetRockMode {
    @Override
    public String getName() {
        return LibNames.affectionate_rock;
    }

    @Nullable
    @Override
    public Ingredient getUpgradeIngredient() {
        return OreIngredient.fromStacks(OreDictionary.getOres("wool").toArray(new ItemStack[0]));
    }

    @Override
    public void onUpdate(ItemStack stack, World world, EntityLivingBase entityLiving, int itemSlot, boolean isSelected) {
        if(world.getTotalWorldTime() % 20L == 0 && world.rand.nextInt(20) == 0) {
            this.spawnAffectionateParticles(world, entityLiving);
        }
    }

    @Override
    public ActionResult<ItemStack> onRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand) {
        this.spawnAffectionateParticles(world, player);
        player.getCooldownTracker().setCooldown(Registrar.pet_rock, 100);
        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }

    public void spawnAffectionateParticles(World world, EntityLivingBase entityLiving) {
        world.playSound(null, entityLiving.getPosition(), SoundEvents.ENTITY_CAT_PURR, SoundCategory.PLAYERS, 1.0f, 1.0f);
        if (world.isRemote) {
            for (int i = 0; i < ConfigHandlerPR.emotionalSupportRock; i++) {
                double d0 = world.rand.nextGaussian() * 0.02D;
                double d1 = world.rand.nextGaussian() * 0.02D;
                double d2 = world.rand.nextGaussian() * 0.02D;
                world.spawnParticle(
                        EnumParticleTypes.HEART,
                        entityLiving.posX + (double) (world.rand.nextFloat() * entityLiving.width * 2.0F) - (double) entityLiving.width,
                        entityLiving.posY + 0.5D + (world.rand.nextFloat() * entityLiving.height),
                        entityLiving.posZ + (double) (world.rand.nextFloat() * entityLiving.width * 2.0F) - (double) entityLiving.width,
                        d0, d1, d2
                );
            }
        }
    }
}
