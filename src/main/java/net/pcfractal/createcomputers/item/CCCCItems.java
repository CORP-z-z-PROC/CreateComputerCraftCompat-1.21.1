package net.pcfractal.createcomputers.item;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.pcfractal.createcomputers.CCCC;
import net.pcfractal.createcomputers.block.CCCCBlocks;

public class CCCCItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(CCCC.MODID);

    public static final DeferredItem<Item> LAPIS_SHEET = ITEMS.register("lapis_sheet",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INTEGRATED_CIRCUIT = ITEMS.register("integrated_circuit",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INCOMPLETE_INTEGRATED_CIRCUIT = ITEMS.register("incomplete_integrated_circuit",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> ADVANCED_INTEGRATED_CIRCUIT = ITEMS.register("advanced_integrated_circuit",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INCOMPLETE_ADVANCED_INTEGRATED_CIRCUIT = ITEMS.register("incomplete_advanced_integrated_circuit",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> REDSTONE_LINK_BRIDGE = ITEMS.register("redstone_link_bridge",
            () -> new BlockItem(CCCCBlocks.REDSTONE_LINK_BRIDGE.get(), new Item.Properties()));;

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
