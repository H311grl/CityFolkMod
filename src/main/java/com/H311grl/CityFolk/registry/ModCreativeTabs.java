package com.H311grl.CityFolk.registry;

import com.H311grl.CityFolk.CityFolkMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;


public class ModCreativeTabs {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, CityFolkMod.MODID);

    public static final RegistryObject<CreativeModeTab> CITY_FOLK_TAB = CREATIVE_TABS.register("city_folk_tab",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(ModItems.BUILDING_CATALOG.get()))
                    .title(Component.translatable("itemGroup.cityfolk"))
                    .displayItems((parameters, output) -> {
                        output.accept(ModItems.BUILDING_CATALOG.get());
                        // Future items (blocks, other registered items) get added here
                        // as you register them, e.g. output.accept(ModItems.SOME_OTHER_ITEM.get());
                    })
                    .build());

    public static void register(IEventBus modBus) {
        CREATIVE_TABS.register(modBus);
    }
}