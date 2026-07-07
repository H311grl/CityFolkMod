package com.H311grl.CityFolk.citymap;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;

import java.util.UUID;

public class ZoneEntry {

    public enum ZoneType { BEACH, PARK, POOL, PLAYGROUND, DOG_PARK, GARDEN, OTHER }

    public UUID id;
    public ZoneType zoneType;
    public String name;
    public BlockPos min;
    public BlockPos max;

    public ZoneEntry(UUID id, ZoneType zoneType, String name, BlockPos min, BlockPos max) {
        this.id = id;
        this.zoneType = zoneType;
        this.name = name;
        this.min = min;
        this.max = max;
    }

    public CompoundTag toNbt() {
        CompoundTag tag = new CompoundTag();
        tag.putUUID("id", id);
        tag.putString("zone_type", zoneType.name());
        tag.putString("name", name);
        tag.putInt("min_x", min.getX());
        tag.putInt("min_y", min.getY());
        tag.putInt("min_z", min.getZ());
        tag.putInt("max_x", max.getX());
        tag.putInt("max_y", max.getY());
        tag.putInt("max_z", max.getZ());
        return tag;
    }

    public static ZoneEntry fromNbt(CompoundTag tag) {
        UUID id = tag.getUUID("id");
        ZoneType zoneType = ZoneType.valueOf(tag.getString("zone_type"));
        String name = tag.getString("name");
        BlockPos min = new BlockPos(tag.getInt("min_x"), tag.getInt("min_y"), tag.getInt("min_z"));
        BlockPos max = new BlockPos(tag.getInt("max_x"), tag.getInt("max_y"), tag.getInt("max_z"));
        return new ZoneEntry(id, zoneType, name, min, max);
    }
}