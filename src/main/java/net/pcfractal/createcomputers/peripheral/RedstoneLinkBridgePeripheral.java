package net.pcfractal.createcomputers.peripheral;

import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.world.item.ItemStack;
import net.pcfractal.createcomputers.blockentity.RedstoneLinkBridgeBlockEntity;
import org.jspecify.annotations.NonNull;

import java.util.Optional;

public class RedstoneLinkBridgePeripheral implements IPeripheral {
    private final RedstoneLinkBridgeBlockEntity blockEntity;

    public RedstoneLinkBridgePeripheral(RedstoneLinkBridgeBlockEntity blockEntity) {
        this.blockEntity = blockEntity;
    }

    public @NonNull String getType() {
        return "redstone_link_bridge";
    }

    @LuaFunction(
            mainThread = true
    )
    public final int getLinkSignal(String frequency1, String frequency2, Optional<Integer> color1, Optional<Integer> color2) {
        ItemStack first = RedstoneLinkBridgeBlockEntity.fromFrequencySpec(frequency1, (Integer)color1.orElse(null));
        ItemStack last = RedstoneLinkBridgeBlockEntity.fromFrequencySpec(frequency2, (Integer)color2.orElse(null));
        return this.blockEntity.getLinkSignal(first, last);
    }

    @LuaFunction(
            mainThread = true
    )
    public final void sendLinkSignal(String frequency1, String frequency2, int strength, Optional<Integer> color1, Optional<Integer> color2) {
        ItemStack first = RedstoneLinkBridgeBlockEntity.fromFrequencySpec(frequency1, (Integer)color1.orElse(null));
        ItemStack last = RedstoneLinkBridgeBlockEntity.fromFrequencySpec(frequency2, (Integer)color2.orElse(null));
        this.blockEntity.sendLinkSignal(first, last, strength);
    }

    public boolean equals(IPeripheral other) {
        if (this == other) {
            return true;
        } else if (other instanceof RedstoneLinkBridgePeripheral) {
            RedstoneLinkBridgePeripheral that = (RedstoneLinkBridgePeripheral)other;
            return this.blockEntity == that.blockEntity;
        } else {
            return false;
        }
    }

    public Object getTarget() {
        return this.blockEntity;
    }
}