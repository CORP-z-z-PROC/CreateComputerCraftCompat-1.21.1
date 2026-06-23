package net.pcfractal.createcomputers.datagen;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.pcfractal.createcomputers.block.CCCCBlocks;

import java.util.Set;

public class ModBlockLootTableProvider extends BlockLootSubProvider {
    protected ModBlockLootTableProvider(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    protected void generate() {
        dropSelf(CCCCBlocks.REDSTONE_LINK_BRIDGE.get());
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return CCCCBlocks.BLOCKS.getEntries().stream().map(Holder::value)::iterator;
    }
}
