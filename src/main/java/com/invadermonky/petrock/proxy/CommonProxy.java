package com.invadermonky.petrock.proxy;

import com.invadermonky.petrock.handlers.ConfigHandlerPR;
import com.invadermonky.petrock.registry.Registrar;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.oredict.OreDictionary;

public class CommonProxy {
    public void preInit(FMLPreInitializationEvent event) {}

    public void init(FMLInitializationEvent event) {}

    public void postInit(FMLPostInitializationEvent event) {
        Registrar.registerEntities();
        if(ConfigHandlerPR.perfectlyBalanced) {
            for(String oreDict : OreDictionary.getOreNames()) {
                OreDictionary.registerOre(oreDict, Registrar.broken_rock);
            }
        }
    }

    public void loadComplete(FMLLoadCompleteEvent event) {}
}
