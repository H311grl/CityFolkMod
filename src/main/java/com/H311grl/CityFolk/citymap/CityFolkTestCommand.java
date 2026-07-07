package com.H311grl.CityFolk.citymap;

import com.H311grl.CityFolk.CityFolkMod;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

import java.util.UUID;

public class CityFolkTestCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("cityfolktest")
                        .requires(source -> source.hasPermission(2)) // op-only, since this mutates world save data
                        .executes(CityFolkTestCommand::run)
        );
    }

    private static int run(com.mojang.brigadier.context.CommandContext<CommandSourceStack> ctx) {
        CommandSourceStack source = ctx.getSource();
        ServerLevel level = source.getLevel();
        ServerPlayer player = source.getPlayer();
        UUID ownerId = player != null ? player.getUUID() : UUID.randomUUID();

        CityFolkSavedData data = CityFolkSavedData.get(level);

        // 1. Create a test city
        CityData city = data.createCity("Test City", ownerId);
        CityFolkMod.LOGGER.info("[CityFolkTest] Created city '{}' with id {}", city.name, city.cityId);

        // 2. Add a test building (using the gas station catalog id from earlier)
        BuildingEntry building = new BuildingEntry(
                UUID.randomUUID(),
                ResourceLocation.tryParse("cityfolk:gas_station"),
                "Test Gas Station",
                new BlockPos(0, 64, 0),
                new BlockPos(7, 70, 9),
                BuildingEntry.OwnershipStatus.UNOWNED
        );
        data.addBuilding(city.cityId, building);
        CityFolkMod.LOGGER.info("[CityFolkTest] Added building '{}' ({})", building.name, building.buildingType);

        // 3. Add a test zone
        ZoneEntry zone = new ZoneEntry(
                UUID.randomUUID(),
                ZoneEntry.ZoneType.PARK,
                "Test Park",
                new BlockPos(20, 64, 0),
                new BlockPos(40, 70, 20)
        );
        data.addZone(city.cityId, zone);
        CityFolkMod.LOGGER.info("[CityFolkTest] Added zone '{}' ({})", zone.name, zone.zoneType);

        // 4. Register a fake villager UUID
        UUID fakeVillagerId = UUID.randomUUID();
        data.registerVillager(city.cityId, fakeVillagerId);
        CityFolkMod.LOGGER.info("[CityFolkTest] Registered villager {}", fakeVillagerId);

        // 5. Read it all back to confirm the round trip
        CityData readBack = data.getCity(city.cityId);
        CityFolkMod.LOGGER.info("[CityFolkTest] Read-back check -> buildings: {}, zones: {}, villagers: {}",
                readBack.buildings.size(), readBack.zones.size(), readBack.villagerRegistry.size());

        source.sendSuccess(() -> net.minecraft.network.chat.Component.literal(
                "CityFolk test city created — check console log for details. City ID: " + city.cityId
        ), false);

        return 1;
    }
}