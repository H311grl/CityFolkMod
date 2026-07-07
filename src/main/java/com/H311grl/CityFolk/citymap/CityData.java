package com.H311grl.CityFolk.citymap;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CityData {

    public UUID cityId;
    public String name;
    public UUID owner;
    public List<BuildingEntry> buildings = new ArrayList<>();
    public List<ZoneEntry> zones = new ArrayList<>();
    public List<UUID> villagerRegistry = new ArrayList<>();

    public CityData(UUID cityId, String name, UUID owner) {
        this.cityId = cityId;
        this.name = name;
        this.owner = owner;
    }

    public CompoundTag toNbt() {
        CompoundTag tag = new CompoundTag();
        tag.putUUID("city_id", cityId);
        tag.putString("name", name);
        tag.putUUID("owner", owner);

        ListTag buildingsList = new ListTag();
        for (BuildingEntry building : buildings) {
            buildingsList.add(building.toNbt());
        }
        tag.put("buildings", buildingsList);

        ListTag zonesList = new ListTag();
        for (ZoneEntry zone : zones) {
            zonesList.add(zone.toNbt());
        }
        tag.put("zones", zonesList);

        ListTag villagersList = new ListTag();
        for (UUID villagerId : villagerRegistry) {
            CompoundTag idTag = new CompoundTag();
            idTag.putUUID("id", villagerId);
            villagersList.add(idTag);
        }
        tag.put("villager_registry", villagersList);

        return tag;
    }

    public static CityData fromNbt(CompoundTag tag) {
        UUID cityId = tag.getUUID("city_id");
        String name = tag.getString("name");
        UUID owner = tag.getUUID("owner");

        CityData data = new CityData(cityId, name, owner);

        ListTag buildingsList = tag.getList("buildings", 10); // 10 = CompoundTag type id
        for (int i = 0; i < buildingsList.size(); i++) {
            data.buildings.add(BuildingEntry.fromNbt(buildingsList.getCompound(i)));
        }

        ListTag zonesList = tag.getList("zones", 10);
        for (int i = 0; i < zonesList.size(); i++) {
            data.zones.add(ZoneEntry.fromNbt(zonesList.getCompound(i)));
        }

        ListTag villagersList = tag.getList("villager_registry", 10);
        for (int i = 0; i < villagersList.size(); i++) {
            data.villagerRegistry.add(villagersList.getCompound(i).getUUID("id"));
        }

        return data;
    }
}