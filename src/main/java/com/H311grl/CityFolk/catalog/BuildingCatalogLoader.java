package com.H311grl.CityFolk.catalog;

import com.google.gson.*;
import com.H311grl.CityFolk.CityFolkMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.*;

public class BuildingCatalogLoader extends SimpleJsonResourceReloadListener {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static Map<ResourceLocation, BuildingCatalogEntry> ENTRIES = Collections.emptyMap();

    public BuildingCatalogLoader() {
        // "building_catalog" -> matches data/cityfolk/building_catalog/*.json
        super(GSON, "building_catalog");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> jsonMap, ResourceManager resourceManager, ProfilerFiller profiler) {
        Map<ResourceLocation, BuildingCatalogEntry> built = new HashMap<>();

        for (Map.Entry<ResourceLocation, JsonElement> entry : jsonMap.entrySet()) {
            try {
                built.put(entry.getKey(), parseEntry(entry.getKey(), entry.getValue().getAsJsonObject()));
            } catch (Exception e) {
                CityFolkMod.LOGGER.error("Failed to parse building catalog entry {}: {}", entry.getKey(), e.getMessage());
            }
        }

        ENTRIES = Collections.unmodifiableMap(built);
        CityFolkMod.LOGGER.info("Loaded {} building catalog entries", ENTRIES.size());
    }

    private BuildingCatalogEntry parseEntry(ResourceLocation id, JsonObject obj) {
        String displayName = obj.get("display_name").getAsString();
        String category = obj.get("category").getAsString();
        String description = obj.has("description") ? obj.get("description").getAsString() : "";
        String compatSource = obj.has("compat_source") ? obj.get("compat_source").getAsString() : "vanilla";

        List<ResourceLocation> associatedJobs = new ArrayList<>();
        if (obj.has("associated_jobs")) {
            for (JsonElement el : obj.getAsJsonArray("associated_jobs")) {
                ResourceLocation jobId = ResourceLocation.tryParse(el.getAsString());
                if (jobId != null) {
                    associatedJobs.add(jobId);
                } else {
                    CityFolkMod.LOGGER.warn("Invalid job id '{}' in building catalog entry {}", el.getAsString(), id);
                }
            }
        }

        List<BuildingCatalogEntry.BlockRequirement> required = parseBlockList(obj, "required_blocks");
        List<BuildingCatalogEntry.BlockRequirement> optional = parseBlockList(obj, "optional_blocks");

        BuildingCatalogEntry.Footprint footprint = null;
        if (obj.has("min_footprint")) {
            JsonObject fp = obj.getAsJsonObject("min_footprint");
            footprint = new BuildingCatalogEntry.Footprint(fp.get("width").getAsInt(), fp.get("length").getAsInt());
        }

        return new BuildingCatalogEntry(id, displayName, category, description,
                associatedJobs, required, optional, footprint, compatSource);
    }

    private List<BuildingCatalogEntry.BlockRequirement> parseBlockList(JsonObject obj, String key) {
        List<BuildingCatalogEntry.BlockRequirement> list = new ArrayList<>();
        if (obj.has(key)) {
            for (JsonElement el : obj.getAsJsonArray(key)) {
                JsonObject blockObj = el.getAsJsonObject();
                ResourceLocation tag = ResourceLocation.tryParse(blockObj.get("tag").getAsString());
                if (tag == null) {
                    CityFolkMod.LOGGER.warn("Invalid block tag '{}' in building catalog", blockObj.get("tag").getAsString());
                    continue;
                }
                String note = blockObj.has("note") ? blockObj.get("note").getAsString() : "";
                list.add(new BuildingCatalogEntry.BlockRequirement(tag, note));
            }
        }
        return list;
    }

    public static Map<ResourceLocation, BuildingCatalogEntry> getEntries() {
        return ENTRIES;
    }

    public static Optional<BuildingCatalogEntry> get(ResourceLocation id) {
        return Optional.ofNullable(ENTRIES.get(id));
    }
}