package net.pcfractal.createcomputers;

import dan200.computercraft.api.peripheral.PeripheralCapability;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.pcfractal.createcomputers.block.CCCCBlocks;
import net.pcfractal.createcomputers.blockentity.CCCCBlockEntities;
import net.pcfractal.createcomputers.item.CCCCItems;
import net.pcfractal.createcomputers.peripheral.RedstoneLinkBridgePeripheral;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(CCCC.MODID)
public class CCCC {

    public static final String MODID = "cccc";
    public static final Logger LOGGER = LogUtils.getLogger();

    public CCCC(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);

        CCCCBlocks.register(modEventBus);
        CCCCBlockEntities.register(modEventBus);
        CCCCItems.register(modEventBus);
        NeoForge.EVENT_BUS.register(this);
        modEventBus.addListener(this::addCreative);
        modEventBus.addListener(this::registerCapabilities);
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        LOGGER.info("HELLO FROM COMMON SETUP");
        if (Config.LOG_DIRT_BLOCK.getAsBoolean()) {
            LOGGER.info("DIRT BLOCK >> {}", BuiltInRegistries.BLOCK.getKey(Blocks.DIRT));
        }
        LOGGER.info("{}{}", Config.MAGIC_NUMBER_INTRODUCTION.get(), Config.MAGIC_NUMBER.getAsInt());
        Config.ITEM_STRINGS.get().forEach((item) -> LOGGER.info("ITEM >> {}", item));
    }

    private static final ResourceKey<CreativeModeTab> COMPUTERCRAFT_TAB = ResourceKey.create(
            Registries.CREATIVE_MODE_TAB,
            ResourceLocation.fromNamespaceAndPath("computercraft", "tab")
    );

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey().equals(COMPUTERCRAFT_TAB)) {
            ItemStack anchorItem = new ItemStack(BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("computercraft", "disk_drive")));
            if (!anchorItem.isEmpty()) {
                event.insertAfter(anchorItem,
                        new ItemStack(CCCCItems.REDSTONE_LINK_BRIDGE.get()),
                        CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            } else {
                event.accept(CCCCItems.REDSTONE_LINK_BRIDGE.get());
            }
        }
    }

    private void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(PeripheralCapability.get(),CCCCBlockEntities.REDSTONE_LINK_BRIDGE.get(),
                (blockEntity, side) -> new RedstoneLinkBridgePeripheral(blockEntity));
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }
}
