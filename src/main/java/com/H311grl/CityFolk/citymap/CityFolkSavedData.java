package com.H311grl.CityFolk.citymap;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CityFolkSavedData extends SavedData {

    private static final String DATA_NAME = "cityfolk_cities";

    private final Map<UUID, CityData> cities = new HashMap<>();

    public static CityFolkSavedData get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(
                CityFolkSavedData::load,
                CityFolkSavedData::new,
                DATA_NAME
        );
    }

    // --- Query / mutation API ---

    public CityData createCity(String name, UUID owner) {
        UUID id = UUID.randomUUID();
        CityData data = new CityData(id, name, owner);
        cities.put(id, data);
        setDirty();
        return data;
    }

    public CityData getCity(UUID cityId) {
        return cities.get(cityId);
    }

    public Map<UUID, CityData> getAllCities() {
        return cities;
    }

    public void addBuilding(UUID cityId, BuildingEntry building) {
        CityData city = cities.get(cityId);
        if (city != null) {
            city.buildings.add(building);
            setDirty();
        }
    }

    public void addZone(UUID cityId, ZoneEntry zone) {
        CityData city = cities.get(cityId);
        if (city != null) {
            city.zones.add(zone);
            setDirty();
        }
    }

    public void registerVillager(UUID cityId, UUID villagerId) {
        CityData city = cities.get(cityId);
        if (city != null && !city.villagerRegistry.contains(villagerId)) {
            city.villagerRegistry.add(villagerId);
            setDirty();
        }
    }

    // --- Serialization ---

    @Override
    public CompoundTag save(CompoundTag tag) {
        ListTag citiesList = new ListTag();
        for (CityData city : cities.values()) {
            citiesList.add(city.toNbt());
        }
        tag.put("cities", citiesList);
        return tag;
    }

    public static CityFolkSavedData load(CompoundTag tag) {
        CityFolkSavedData data = new CityFolkSavedData();
        ListTag citiesList = tag.getList("cities", 10); // 10 = CompoundTag type id
        for (int i = 0; i < citiesList.size(); i++) {
            CityData city = CityData.fromNbt(citiesList.getCompound(i));
            data.cities.put(city.cityId, city);
        }
        return data;
    }
}