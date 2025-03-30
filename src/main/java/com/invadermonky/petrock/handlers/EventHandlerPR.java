package com.invadermonky.petrock.handlers;

import com.invadermonky.petrock.PetRock;
import com.invadermonky.petrock.items.EnumPetRockMode;
import com.invadermonky.petrock.registry.Registrar;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = PetRock.MOD_ID)
public class EventHandlerPR {
    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        if(event.getEntityLiving() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.getEntityLiving();
            ItemStack stack = player.getActiveItemStack();
            if(stack.getItem() == Registrar.pet_rock) {
                EnumPetRockMode.getActiveRockMode(stack).getPetRockMode().onLivingHurt(event);
            }
        }
    }
}
