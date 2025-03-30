package com.invadermonky.petrock.util;

import com.invadermonky.petrock.PetRock;
import net.minecraft.util.text.TextComponentTranslation;

public class StringHelper {
    public static String getTranslationKey(String unloc, String type, String... params) {
        StringBuilder str = new StringBuilder(type + "." + PetRock.MOD_ID + ":" + unloc);
        for(String param : params) {
            str.append(".").append(param);
        }
        return str.toString();
    }

    public static TextComponentTranslation getTranslationComponent(String unloc, String type, String... params) {
        return new TextComponentTranslation(getTranslationKey(unloc, type, params));
    }

}
