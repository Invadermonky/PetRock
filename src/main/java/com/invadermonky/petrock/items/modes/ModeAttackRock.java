package com.invadermonky.petrock.items.modes;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.invadermonky.petrock.entities.EntityPetRock;
import com.invadermonky.petrock.handlers.ConfigHandlerPR;
import com.invadermonky.petrock.items.EnumPetRockMode;
import com.invadermonky.petrock.items.IPetRockMode;
import com.invadermonky.petrock.util.libs.LibNames;
import net.minecraft.block.BlockBreakable;
import net.minecraft.block.BlockPane;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.stats.StatList;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.ForgeEventFactory;

import javax.annotation.Nullable;

public class ModeAttackRock implements IPetRockMode {
    @Override
    public String getName() {
        return LibNames.attack_rock;
    }

    @Nullable
    @Override
    public Ingredient getUpgradeIngredient() {
        return Ingredient.fromStacks(new ItemStack(Items.DIAMOND_SWORD));
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

                    world.playSound(null,
                            player.posX, player.posY, player.posZ,
                            SoundEvents.ENTITY_EGG_THROW, SoundCategory.PLAYERS,
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
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return enchantment == Enchantments.LOOTING || enchantment == Enchantments.KNOCKBACK;
    }

    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack) {
        Multimap<String, AttributeModifier> multimap = HashMultimap.create();
        if(slot == EntityEquipmentSlot.MAINHAND && EnumPetRockMode.ATTACK.isUpgradeActive(stack)) {
            multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(Item.ATTACK_DAMAGE_MODIFIER, "Weapon Modifier", (float) (ConfigHandlerPR.attackRock - 1.0), 0));
            multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(Item.ATTACK_SPEED_MODIFIER, "Weapon Modifier", -2.4f, 0));
        }
        return multimap;
    }

    @Override
    public void onProjectileImpact(World world, EntityPetRock entityPetRock, RayTraceResult result, @Nullable EnumFacing sideHit, @Nullable EntityLivingBase entityLiving) {
        if(!world.isRemote) {
            if(entityLiving != null) {
                if(!ForgeEventFactory.onProjectileImpact(entityPetRock, result)) {
                    entityLiving.attackEntityFrom(DamageSource.causeThrownDamage(entityPetRock, entityPetRock.getThrower()), (float) ConfigHandlerPR.attackRock);
                    double knockback = (double) EnchantmentHelper.getEnchantmentLevel(Enchantments.KNOCKBACK, entityPetRock.getPetRockStack()) / 2;
                    if (knockback > 0) {
                        float vector = MathHelper.sqrt(entityPetRock.motionX * entityPetRock.motionX + entityPetRock.motionZ * entityPetRock.motionZ);
                        double xRatio = knockback * entityPetRock.motionX * 0.6f / vector;
                        double zRatio = knockback * entityPetRock.motionZ * 0.6f / vector;
                        if (!ForgeHooks.onLivingKnockBack(entityLiving, entityPetRock, vector, xRatio, zRatio).isCanceled()) {
                            entityLiving.addVelocity(knockback * entityPetRock.motionX * 0.6f / vector, 0.1D, knockback * entityPetRock.motionZ * 0.6f / vector);
                        }
                    }
                }
            } else if(result != null && result.typeOfHit == RayTraceResult.Type.BLOCK) {
                BlockPos hitPos = result.getBlockPos();
                IBlockState state = world.getBlockState(hitPos);
                if(state.getBlock() instanceof BlockBreakable || state.getBlock() instanceof BlockPane) {
                    float hardness = state.getBlockHardness(world, hitPos);
                    if(hardness <= 0.3f) {
                        world.destroyBlock(hitPos, false);
                    }
                }
            }

            EntityItem entityItem = new EntityItem(world, entityPetRock.posX, entityPetRock.posY, entityPetRock.posZ, entityPetRock.getPetRockStack().copy());
            entityItem.motionX = (entityPetRock.motionX * -0.05f);
            entityItem.motionY = 0.05;
            entityItem.motionZ = (entityPetRock.motionZ * -0.05f);
            entityItem.setDefaultPickupDelay();
            world.spawnEntity(entityItem);
            entityPetRock.setDead();
        }
    }
}
