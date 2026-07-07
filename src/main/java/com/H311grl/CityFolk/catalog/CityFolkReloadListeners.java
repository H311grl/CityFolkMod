package com.H311grl.CityFolk.catalog;

import com.H311grl.CityFolk.CityFolkMod;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CityFolkMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CityFolkReloadListeners {

    @SubscribeEvent
    public static void onAddReloadListeners(AddReloadListenerEvent event) {
        event.addListener(new BuildingCatalogLoader());
    }
}