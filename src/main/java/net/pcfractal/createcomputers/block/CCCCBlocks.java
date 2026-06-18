package net.pcfractal.createcomputers.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.pcfractal.createcomputers.CCCC;

public final class CCCCBlocks {
    static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(CCCC.MODID);
    public static final DeferredBlock<Block> REDSTONE_LINK_BRIDGE;

    private CCCCBlocks() {
    }

    public static void register(IEventBus bus) {
        BLOCKS.register(bus);
    }

    static {
        REDSTONE_LINK_BRIDGE = BLOCKS.register("redstone_link_bridge", () -> new RedstoneLinkBridgeBlock(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(2.0F, 6.0F).noOcclusion()));
    }
}
