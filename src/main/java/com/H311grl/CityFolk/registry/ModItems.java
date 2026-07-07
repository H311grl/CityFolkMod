package com.H311grl.CityFolk.registry;

import com.H311grl.CityFolk.CityFolkMod;
import com.H311grl.CityFolk.catalog.BuildingCatalogItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, CityFolkMod.MODID);

    public static final RegistryObject<Item> BUILDING_CATALOG = ITEMS.register("building_catalog",
            () -> new BuildingCatalogItem(new Item.Properties().stacksTo(1)));

    public static void register(IEventBus modBus) {
        ITEMS.register(modBus);
    }
}