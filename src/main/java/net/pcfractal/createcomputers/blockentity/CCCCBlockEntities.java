package net.pcfractal.createcomputers.blockentity;

import com.mojang.datafixers.types.Type;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.pcfractal.createcomputers.CCCC;
import net.pcfractal.createcomputers.block.CCCCBlocks;

public class CCCCBlockEntities {
    static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES;
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<RedstoneLinkBridgeBlockEntity>> REDSTONE_LINK_BRIDGE;

    private CCCCBlockEntities() {
    }

    public static void register(IEventBus bus) {
        BLOCK_ENTITY_TYPES.register(bus);
    }

    static {
        BLOCK_ENTITY_TYPES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, CCCC.MODID);
        REDSTONE_LINK_BRIDGE = BLOCK_ENTITY_TYPES.register("redstone_link_bridge", () -> BlockEntityType.Builder.of(
                RedstoneLinkBridgeBlockEntity::new, new Block[]{CCCCBlocks.REDSTONE_LINK_BRIDGE.get()}).build(null));
    }
}
