package com.invadermonky.petrock.items.modes;

import com.invadermonky.petrock.entities.EntityPetRock;
import com.invadermonky.petrock.handlers.ConfigHandlerPR;
import com.invadermonky.petrock.items.IPetRockMode;
import com.invadermonky.petrock.registry.Registrar;
import com.invadermonky.petrock.util.libs.LibNames;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.potion.PotionEffect;
import net.minecraft.stats.StatList;
import net.minecraft.util.*;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.IRarity;

import javax.annotation.Nullable;

public class ModeRadioactiveRock implements IPetRockMode {
    @Override
    public String getName() {
        return LibNames.radioactive_rock;
    }

    @Nullable
    @Override
    public Ingredient getUpgradeIngredient() {
        return Ingredient.fromStacks(new ItemStack(Blocks.TNT));
    }

    @Override
    public void onUpdate(ItemStack stack, World world, EntityLivingBase entityLiving, int itemSlot, boolean isSelected) {
        if(!world.isRemote && world.getTotalWorldTime() % 60L == 0 && world.rand.nextInt(20) == 0) {
            entityLiving.addPotionEffect(new PotionEffect(MobEffects.POISON, 200, 0, false, true));
        }
    }

    @Override
    public ActionResult<ItemStack> onRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand) {
        player.setActiveHand(hand);
        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World world, EntityLivingBase entityLiving, int timeLeft) {
        if(entityLiving instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entityLiving;
            Item item = stack.getItem();
            int useDuration = item.getMaxItemUseDuration(stack);
            if(useDuration < 0) return;

            if(!stack.isEmpty()) {
                float velocity = this.getProjectileVelocity(useDuration);
                if((double)velocity >= 0.1d) {
                    if(!world.isRemote) {
                        EntityPetRock entityPetRock = new EntityPetRock(world, player, stack);
                        entityPetRock.shoot(player, player.rotationPitch, player.rotationYaw, 0.0f, velocity, 0.98f);
                        world.spawnEntity(entityPetRock);
                    }

                    world.playSound(null, player.posX, player.posY, player.posZ,
                            Registrar.sound_radioactive_rock, SoundCategory.PLAYERS,
                            1.0f, 1.0f / (world.rand.nextFloat() * 0.4F + 1.2F) + velocity * 0.5F
                    );

                    stack.shrink(1);
                    if(stack.isEmpty()) {
                        player.inventory.deleteStack(stack);
                    }
                    player.addStat(StatList.getObjectUseStats(item));
                }
            }
        }
    }

    @Override
    public EnumAction getItemUseAction() {
        return EnumAction.BOW;
    }

    @Override
    public IRarity getRarity(ItemStack stack) {
        return EnumRarity.UNCOMMON;
    }

    @Override
    public void onProjectileImpact(World world, EntityPetRock entityPetRock, RayTraceResult result, @Nullable EnumFacing sideHit, @Nullable EntityLivingBase entityLiving) {
        if(!world.isRemote) {
            world.createExplosion(entityPetRock.getThrower(), entityPetRock.posX, entityPetRock.posY, entityPetRock.posZ, ConfigHandlerPR.somewhatRadioactive, true);

            EntityItem entityItem = new EntityItem(world, entityPetRock.posX, entityPetRock.posY, entityPetRock.posZ, entityPetRock.getPetRockStack().copy());
            entityItem.motionX = (entityPetRock.motionX * -0.05f);
            entityItem.motionY = 0.05;
            entityItem.motionZ = (entityPetRock.motionZ * -0.05f);
            entityItem.setEntityInvulnerable(true);
            entityItem.setDefaultPickupDelay();
            world.spawnEntity(entityItem);
            entityPetRock.setDead();
        }
    }
}
