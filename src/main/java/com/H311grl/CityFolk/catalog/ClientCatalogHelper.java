package com.H311grl.CityFolk.catalog;

import net.minecraft.client.Minecraft;

public class ClientCatalogHelper {
    public static void openCatalogScreen() {
        Minecraft.getInstance().setScreen(new BuildingCatalogScreen());
    }
}