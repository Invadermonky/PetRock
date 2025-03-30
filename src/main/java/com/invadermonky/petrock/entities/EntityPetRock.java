package com.invadermonky.petrock.entities;

import com.invadermonky.petrock.items.EnumPetRockMode;
import com.invadermonky.petrock.registry.Registrar;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;

public class EntityPetRock extends EntityThrowable {
    public ItemStack thrownStack;

    public EntityPetRock(World worldIn) {
        super(worldIn);
        this.thrownStack = new ItemStack(Registrar.pet_rock);
    }

    public EntityPetRock(World worldIn, EntityLivingBase throwerIn, ItemStack thrownStack) {
        super(worldIn, throwerIn);
        this.thrownStack = thrownStack.copy();
    }

    public ItemStack getPetRockStack() {
        return this.thrownStack;
    }

    @Override
    protected void onImpact(RayTraceResult result) {
        if(result == null || ForgeEventFactory.onProjectileImpact(this, result)) return;

        if(!this.getPetRockStack().isEmpty()) {
            EnumPetRockMode mode = EnumPetRockMode.getActiveRockMode(this.getPetRockStack());
            if(result.sideHit != null) {
                mode.getPetRockMode().onProjectileImpact(this.world, this, result, result.sideHit, null);
            } else if(result.entityHit instanceof EntityLivingBase) {
                if (this.getThrower() == null || !this.getThrower().getUniqueID().equals(result.entityHit.getUniqueID())) {
                    mode.getPetRockMode().onProjectileImpact(this.world, this, result, null, (EntityLivingBase) result.entityHit);
                }
            }
        }
    }

    @Override
    protected float getGravityVelocity() {
        return 0.04f;
    }
}
