package com.H311grl.CityFolk.catalog;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.api.distmarker.Dist;

public class BuildingCatalogItem extends Item {

    public BuildingCatalogItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (level.isClientSide()) {
            // Keep client-only classes (Screen, Minecraft) out of this class entirely —
            // route through a helper so a dedicated server never touches them.
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> ClientCatalogHelper::openCatalogScreen);
        }

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }
}