package com.invadermonky.petrock.handlers;

import com.invadermonky.petrock.items.EnumPetRockMode;
import com.invadermonky.petrock.registry.Registrar;
import com.invadermonky.petrock.util.libs.LibNames;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class RockBreedingHandler {
    public static void onEntityItemUpdate(EntityItem rockEntity1) {
        if(!ConfigHandlerPR.rockBreedingProgram || rockEntity1.getItem().getItem() != Registrar.pet_rock)
            return;

        World world = rockEntity1.world;
        if(rockEntity1.ticksExisted % 300 == 0) {
            if(!canRockBreed(rockEntity1)) {
                setCanRockBreed(rockEntity1, world.rand.nextInt(3) == 0);
                return;
            }

            BlockPos rockPos = rockEntity1.getPosition();

            List<EntityItem> nearbyEntities = world.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(rockPos.add(-3, -1, -3), rockPos.add(3, 2, 3)));
            EntityItem rockEntity2 = null;
            List<EntityItem> flintEntities = new ArrayList<>();
            int flintTotal = 0;

            for (EntityItem checkEntity : nearbyEntities) {
                if (checkEntity == rockEntity1 || !canRockBreed(checkEntity))
                    continue;

                if (checkEntity.getItem().getItem() == Registrar.pet_rock) {
                    rockEntity2 = checkEntity;
                } else if (checkEntity.getItem().getItem() == Items.FLINT) {
                    flintEntities.add(checkEntity);
                    flintTotal += checkEntity.getItem().getCount();
                }
            }

            if (rockEntity2 != null && flintTotal >= 2) {
                setCanRockBreed(rockEntity1, false);
                setCanRockBreed(rockEntity2, false);

                spawnHeartParticles(world, rockEntity1);
                spawnHeartParticles(world, rockEntity2);

                for (EntityItem flintEntity : flintEntities) {
                    ItemStack flintStack = flintEntity.getItem();
                    flintTotal -= flintStack.getCount();
                    if (flintTotal <= 0) {
                        flintStack.shrink(2);
                    } else {
                        flintStack.shrink(flintStack.getCount());
                    }

                    if (flintStack.isEmpty()) {
                        flintEntity.setDead();
                    }
                }

                if (!world.isRemote) {
                    ItemStack babyStack = new ItemStack(Registrar.pet_rock);
                    EnumPetRockMode.NONE.setAsActiveMode(babyStack);
                    getUpgradeFromParent(rockEntity1, babyStack);
                    getUpgradeFromParent(rockEntity2, babyStack);

                    EntityXPOrb xpOrb = new EntityXPOrb(world, rockEntity1.posX, rockEntity1.posY, rockEntity1.posZ, world.rand.nextInt(5) + 1);
                    EntityItem rockEntity3 = new EntityItem(world, rockEntity1.posX, rockEntity1.posY, rockEntity1.posZ, babyStack);

                    world.spawnEntity(xpOrb);
                    world.spawnEntity(rockEntity3);
                }
            }
        }

    }

    private static boolean canRockBreed(EntityItem rockEntity) {
        return !rockEntity.getEntityData().getBoolean(LibNames.tag_has_bred);
    }

    private static void setCanRockBreed(EntityItem rockEntity, boolean canBreed) {
        rockEntity.getEntityData().setBoolean(LibNames.tag_has_bred, !canBreed);
    }

    private static void getUpgradeFromParent(EntityItem parentRock, ItemStack babyStack) {
        List<EnumPetRockMode> parentUpgrades = EnumPetRockMode.getRockUpgrades(parentRock.getItem());
        parentUpgrades.get(parentRock.world.rand.nextInt(parentUpgrades.size())).applyUpgrade(babyStack);
    }

    private static void spawnHeartParticles(World world, EntityItem entityItem) {
        BlockPos pos = entityItem.getPosition();
        for (int i = 0; i < 7; i++) {
            double d0 = world.rand.nextGaussian() * 0.02D;
            double d1 = world.rand.nextGaussian() * 0.02D;
            double d2 = world.rand.nextGaussian() * 0.02D;
            world.spawnParticle(
                    EnumParticleTypes.HEART,
                    pos.getX() + (double) (world.rand.nextFloat() * entityItem.width * 2.0F) - (double) entityItem.width,
                    pos.getY() + 0.5D + (world.rand.nextFloat() * entityItem.height),
                    pos.getZ() + (double) (world.rand.nextFloat() * entityItem.width * 2.0F) - (double) entityItem.width,
                    d0, d1, d2);
        }
    }
}
