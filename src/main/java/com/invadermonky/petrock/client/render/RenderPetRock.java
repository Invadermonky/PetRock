package com.invadermonky.petrock.client.render;

import com.invadermonky.petrock.entities.EntityPetRock;
import com.invadermonky.petrock.registry.Registrar;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderPetRock extends RenderSnowball<EntityPetRock> {
    public RenderPetRock(RenderManager renderManagerIn) {
        super(renderManagerIn, Registrar.pet_rock, Minecraft.getMinecraft().getRenderItem());
    }

    @Override
    public ItemStack getStackToRender(EntityPetRock entityIn) {
        return entityIn.getPetRockStack();
    }
}
