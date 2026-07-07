package com.H311grl.CityFolk.citymap;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BuildingEntry {

    public enum OwnershipStatus { UNOWNED, OWNED, RENTED }

    public UUID id;
    public ResourceLocation buildingType; // references a BuildingCatalogEntry id
    public String name;
    public BlockPos min;
    public BlockPos max;
    public OwnershipStatus ownershipStatus;
    public List<ResourceLocation> assignedJobs = new ArrayList<>();

    public BuildingEntry(UUID id, ResourceLocation buildingType, String name,
                         BlockPos min, BlockPos max, OwnershipStatus ownershipStatus) {
        this.id = id;
        this.buildingType = buildingType;
        this.name = name;
        this.min = min;
        this.max = max;
        this.ownershipStatus = ownershipStatus;
    }

    public CompoundTag toNbt() {
        CompoundTag tag = new CompoundTag();
        tag.putUUID("id", id);
        tag.putString("building_type", buildingType.toString());
        tag.putString("name", name);
        tag.putInt("min_x", min.getX());
        tag.putInt("min_y", min.getY());
        tag.putInt("min_z", min.getZ());
        tag.putInt("max_x", max.getX());
        tag.putInt("max_y", max.getY());
        tag.putInt("max_z", max.getZ());
        tag.putString("ownership_status", ownershipStatus.name());

        ListTag jobsList = new ListTag();
        for (ResourceLocation job : assignedJobs) {
            jobsList.add(StringTag.valueOf(job.toString()));
        }
        tag.put("assigned_jobs", jobsList);

        return tag;
    }

    public static BuildingEntry fromNbt(CompoundTag tag) {
        UUID id = tag.getUUID("id");
        ResourceLocation buildingType = ResourceLocation.tryParse(tag.getString("building_type"));
        String name = tag.getString("name");
        BlockPos min = new BlockPos(tag.getInt("min_x"), tag.getInt("min_y"), tag.getInt("min_z"));
        BlockPos max = new BlockPos(tag.getInt("max_x"), tag.getInt("max_y"), tag.getInt("max_z"));
        OwnershipStatus status = OwnershipStatus.valueOf(tag.getString("ownership_status"));

        BuildingEntry entry = new BuildingEntry(id, buildingType, name, min, max, status);

        ListTag jobsList = tag.getList("assigned_jobs", 8); // 8 = StringTag type id
        for (int i = 0; i < jobsList.size(); i++) {
            ResourceLocation job = ResourceLocation.tryParse(jobsList.getString(i));
            if (job != null) {
                entry.assignedJobs.add(job);
            }
        }

        return entry;
    }
}