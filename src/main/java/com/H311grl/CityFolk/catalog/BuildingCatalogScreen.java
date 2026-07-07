package com.H311grl.CityFolk.catalog;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

public class BuildingCatalogScreen extends Screen {

    private static final int LIST_WIDTH = 140;

    private EntryList entryList;
    private BuildingCatalogEntry selected;

    public BuildingCatalogScreen() {
        super(Component.literal("Building Catalog"));
    }

    @Override
    protected void init() {
        super.init();

        List<BuildingCatalogEntry> entries = new ArrayList<>(BuildingCatalogLoader.getEntries().values());
        entries.sort((a, b) -> a.displayName().compareTo(b.displayName()));

        entryList = new EntryList(this, this.minecraft, LIST_WIDTH, this.height, 32, this.height - 8, 20);
        for (BuildingCatalogEntry entry : entries) {
            entryList.addEntryToList(entry);
        }
        this.addWidget(entryList);

        if (selected == null && !entries.isEmpty()) {
            selected = entries.get(0);
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics);
        entryList.render(guiGraphics, mouseX, mouseY, partialTick);

        guiGraphics.drawCenteredString(this.font, this.title, this.width / 2, 10, 0xFFFFFF);

        if (selected != null) {
            renderDetails(guiGraphics);
        }

        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    private void renderDetails(GuiGraphics guiGraphics) {
        int x = LIST_WIDTH + 20;
        int y = 32;

        guiGraphics.drawString(this.font, selected.displayName(), x, y, 0xFFFFFF);
        y += 12;
        guiGraphics.drawString(this.font, "Category: " + selected.category(), x, y, 0xAAAAAA);
        y += 14;

        y = wrapAndDraw(guiGraphics, selected.description(), x, y, 220, 0xCCCCCC);
        y += 8;

        guiGraphics.drawString(this.font, "Required:", x, y, 0xFFAA00);
        y += 12;
        for (BuildingCatalogEntry.BlockRequirement req : selected.requiredBlocks()) {
            String line = "- " + req.tag().getPath() + (req.note().isEmpty() ? "" : " (" + req.note() + ")");
            y = wrapAndDraw(guiGraphics, line, x, y, 220, 0xFFFFFF);
        }

        if (!selected.optionalBlocks().isEmpty()) {
            y += 8;
            guiGraphics.drawString(this.font, "Optional:", x, y, 0x55AAFF);
            y += 12;
            for (BuildingCatalogEntry.BlockRequirement req : selected.optionalBlocks()) {
                String line = "- " + req.tag().getPath() + (req.note().isEmpty() ? "" : " (" + req.note() + ")");
                y = wrapAndDraw(guiGraphics, line, x, y, 220, 0xCCCCCC);
            }
        }

        if (selected.minFootprint() != null) {
            y += 8;
            guiGraphics.drawString(this.font, "Footprint: " + selected.minFootprint().width()
                    + "x" + selected.minFootprint().length(), x, y, 0xAAAAAA);
        }
    }

    private int wrapAndDraw(GuiGraphics guiGraphics, String text, int x, int y, int maxWidth, int color) {
        List<net.minecraft.util.FormattedCharSequence> lines =
                this.font.split(Component.literal(text), maxWidth);
        for (net.minecraft.util.FormattedCharSequence line : lines) {
            guiGraphics.drawString(this.font, line, x, y, color);
            y += 10;
        }
        return y;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    void setSelected(BuildingCatalogEntry entry) {
        this.selected = entry;
    }

    // --- Inner list class ---

    private static class EntryList extends ObjectSelectionList<EntryList.ListEntry> {

        private final BuildingCatalogScreen parentScreen;

        public EntryList(BuildingCatalogScreen parent, net.minecraft.client.Minecraft mc,
                         int width, int height, int top, int bottom, int itemHeight) {
            super(mc, width, height, top, bottom, itemHeight);
            this.parentScreen = parent;
        }

        public void addEntryToList(BuildingCatalogEntry data) {
            this.addEntry(new ListEntry(data, parentScreen));
        }

        @Override
        public int getRowWidth() {
            return this.width - 10;
        }

        @Override
        protected int getScrollbarPosition() {
            return this.getRowRight();
        }

        private class ListEntry extends ObjectSelectionList.Entry<ListEntry> {
            private final BuildingCatalogEntry data;
            private final BuildingCatalogScreen screen;

            ListEntry(BuildingCatalogEntry data, BuildingCatalogScreen screen) {
                this.data = data;
                this.screen = screen;
            }

            @Override
            public void render(GuiGraphics guiGraphics, int index, int top, int left, int width, int height,
                               int mouseX, int mouseY, boolean hovering, float partialTick) {
                guiGraphics.drawString(screen.font, data.displayName(), left + 4, top + 5, 0xFFFFFF);
            }

            @Override
            public boolean mouseClicked(double mouseX, double mouseY, int button) {
                screen.setSelected(data);
                return true;
            }

            @Override
            public Component getNarration() {
                return Component.literal(data.displayName());
            }
        }
    }
}