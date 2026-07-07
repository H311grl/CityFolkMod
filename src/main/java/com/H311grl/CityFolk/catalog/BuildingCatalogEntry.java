package com.H311grl.CityFolk.catalog;

import net.minecraft.resources.ResourceLocation;
import java.util.List;

public record BuildingCatalogEntry(
        ResourceLocation id,
        String displayName,
        String category,
        String description,
        List<ResourceLocation> associatedJobs,
        List<BlockRequirement> requiredBlocks,
        List<BlockRequirement> optionalBlocks,
        Footprint minFootprint,
        String compatSource
) {
    public record BlockRequirement(ResourceLocation tag, String note) {}
    public record Footprint(int width, int length) {}
}