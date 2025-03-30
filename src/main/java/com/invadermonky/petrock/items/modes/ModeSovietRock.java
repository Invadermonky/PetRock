package com.invadermonky.petrock.items.modes;

import com.invadermonky.petrock.handlers.ConfigHandlerPR;
import com.invadermonky.petrock.items.IPetRockMode;
import com.invadermonky.petrock.registry.Registrar;
import com.invadermonky.petrock.util.libs.LibNames;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class ModeSovietRock implements IPetRockMode {
    @Override
    public String getName() {
        return LibNames.soviet_rock;
    }

    @Nullable
    @Override
    public Ingredient getUpgradeIngredient() {
        return Ingredient.fromStacks(new ItemStack(Blocks.PISTON));
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

            if (!stack.isEmpty()) {
                if(!world.isRemote) {
                    EntityItem rockEntity = new EntityItem(world, player.posX, player.posY, player.posZ, stack.copy());
                    rockEntity.setDefaultPickupDelay();
                    world.spawnEntity(rockEntity);
                }

                float velocity = this.getProjectileVelocity(player.getItemInUseCount()) + (float) EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack) * 0.06F;
                this.launchPlayer(player, velocity, 0.95f);

                if(ConfigHandlerPR.sovietRockSoundEffect) {
                    world.playSound(null, player.getPosition(), Registrar.sound_soviet_rock, SoundCategory.PLAYERS, 1.0f, 1.0f);
                }
                stack.shrink(1);
                if (stack.isEmpty()) {
                    player.inventory.deleteStack(stack);
                }
                player.addStat(StatList.getObjectUseStats(stack.getItem()));
            }
        }
    }



    private void launchPlayer(EntityPlayer player, float velocity, float inaccuracy) {
        float f = -MathHelper.sin(player.rotationYaw * 0.017453292F) * MathHelper.cos(player.rotationPitch * 0.017453292F);
        float f1 = -MathHelper.sin(player.rotationPitch * 0.017453292F);
        float f2 = MathHelper.cos(player.rotationYaw * 0.017453292F) * MathHelper.cos(player.rotationPitch * 0.017453292F);
        this.launchPlayer(player, f, f1, f2, velocity, inaccuracy);
        player.motionX += player.motionX;
        player.motionZ += player.motionZ;

        if (!player.onGround) {
            player.motionY += player.motionY;
        }
    }

    private void launchPlayer(EntityPlayer player, double x, double y, double z, float velocity, float inaccuracy) {
        World world = player.world;
        float f = MathHelper.sqrt(x * x + y * y + z * z);
        x = x / (double) f;
        y = y / (double) f;
        z = z / (double) f;
        x = x + world.rand.nextGaussian() * 0.007499999832361937D * (double) inaccuracy;
        y = y + world.rand.nextGaussian() * 0.007499999832361937D * (double) inaccuracy;
        z = z + world.rand.nextGaussian() * 0.007499999832361937D * (double) inaccuracy;
        x = x * (double) velocity;
        y = y * (double) velocity;
        z = z * (double) velocity;
        player.motionX = x;
        player.motionY = y;
        player.motionZ = z;
    }

    @Override
    public float getProjectileVelocity(int charge) {
        return IPetRockMode.super.getProjectileVelocity(charge) * ConfigHandlerPR.whenInRussia;
    }

    @Override
    public EnumAction getItemUseAction() {
        return EnumAction.BOW;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return enchantment == Enchantments.POWER;
    }
}
