package com.H311grl.CityFolk.citymap;

import com.H311grl.CityFolk.CityFolkMod;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CityFolkMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CityFolkCommandRegistrar {

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        CityFolkTestCommand.register(event.getDispatcher());
    }
}