package net.pcfractal.createcomputers.blockentity;

import com.simibubi.create.Create;
import com.simibubi.create.content.redstone.link.IRedstoneLinkable;
import com.simibubi.create.content.redstone.link.RedstoneLinkNetworkHandler;
import net.createmod.catnip.data.Couple;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.NonNull;

import javax.annotation.Nullable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class RedstoneLinkBridgeBlockEntity extends BlockEntity {
    private final Map<String, RedstoneLinkChannel> channels = new LinkedHashMap();

    public RedstoneLinkBridgeBlockEntity(BlockPos pos, BlockState state) {
        super(CCCCBlockEntities.REDSTONE_LINK_BRIDGE.get(), pos, state);
    }

    public void onLoad() {
        super.onLoad();
        Level currentLevel = this.level;
        if (currentLevel != null && !currentLevel.isClientSide) {
            for(RedstoneLinkChannel channel : this.channels.values()) {
                channel.register(currentLevel);
                channel.update(currentLevel);
            }
        }

    }

    public void setRemoved() {
        Level currentLevel = this.level;
        if (currentLevel != null && !currentLevel.isClientSide) {
            for(RedstoneLinkChannel channel : this.channels.values()) {
                channel.unregister(currentLevel);
            }
        }

        super.setRemoved();
    }

    public int getLinkSignal(ItemStack first, ItemStack last) {
        Level currentLevel = this.level;
        if (currentLevel != null && !currentLevel.isClientSide) {
            Couple<RedstoneLinkNetworkHandler.Frequency> key = Couple.create(RedstoneLinkNetworkHandler.Frequency.of(normalize(first)), RedstoneLinkNetworkHandler.Frequency.of(normalize(last)));
            return querySignal(currentLevel, key);
        } else {
            return 0;
        }
    }

    public void sendLinkSignal(ItemStack first, ItemStack last, int strength) {
        ItemStack normalizedFirst = normalize(first);
        ItemStack normalizedLast = normalize(last);
        String key = channelKey(normalizedFirst, normalizedLast);
        RedstoneLinkChannel channel = this.channels.get(key);
        Level currentLevel = this.level;
        if (channel == null) {
            channel = new RedstoneLinkChannel(this, normalizedFirst, normalizedLast, clampStrength(strength));
            this.channels.put(key, channel);
            this.setChanged();
            if (currentLevel != null && !currentLevel.isClientSide) {
                channel.register(currentLevel);
                channel.update(currentLevel);
            }
        } else {
            channel.setTransmitStrength(strength);
        }

    }

    public static ItemStack fromFrequencyId(String value) {
        String trimmed = value == null ? "" : value.trim();
        if (trimmed.isEmpty()) {
            return ItemStack.EMPTY;
        } else {
            ResourceLocation id = ResourceLocation.tryParse(trimmed);
            return id == null ? ItemStack.EMPTY : BuiltInRegistries.ITEM.getOptional(id).map(ItemStack::new).orElse(ItemStack.EMPTY);
        }
    }

    public static ItemStack fromFrequencySpec(String itemId, @Nullable Integer rgb) {
        ItemStack stack = fromFrequencyId(itemId);
        if (!stack.isEmpty() && rgb != null) {
            ItemStack copy = stack.copy();
            copy.set(DataComponents.DYED_COLOR, new DyedItemColor(rgb & 16777215, false));
            return copy;
        } else {
            return stack;
        }
    }

    private static int getDyeColorRgb(ItemStack stack) {
        return stack.has(DataComponents.DYED_COLOR) ? (Objects.requireNonNull(stack.get(DataComponents.DYED_COLOR))).rgb() : -1;
    }

    private static ItemStack normalize(ItemStack stack) {
        if (stack != null && !stack.isEmpty()) {
            ItemStack copy = stack.copy();
            copy.setCount(1);
            return copy;
        } else {
            return ItemStack.EMPTY;
        }
    }

    private static int clampStrength(int strength) {
        return Math.max(0, Math.min(15, strength));
    }

    private static String itemIdString(ItemStack stack) {
        return stack.isEmpty() ? "" : BuiltInRegistries.ITEM.getKey(stack.getItem()).toString();
    }

    private static String channelKey(ItemStack first, ItemStack last) {
        String var10000 = itemIdString(first);
        return var10000 + "#" + getDyeColorRgb(first) + "\u0000" + itemIdString(last) + "#" + getDyeColorRgb(last);
    }

    private static int querySignal(Level level, Couple<RedstoneLinkNetworkHandler.Frequency> frequency) {
        int power = 0;
        Set<?> network = Create.REDSTONE_LINK_NETWORK_HANDLER.networksIn(level).get(frequency);
        if (network == null) {
            return 0;
        } else {
            for(Object obj : network) {
                IRedstoneLinkable linkable = (IRedstoneLinkable)obj;
                power = Math.max(power, linkable.getTransmittedStrength());
                if (power >= 15) {
                    return 15;
                }
            }

            return power;
        }
    }

    protected void saveAdditional(@NonNull CompoundTag tag, HolderLookup.@NonNull Provider provider) {
        super.saveAdditional(tag, provider);
        ListTag channelList = new ListTag();

        for(RedstoneLinkChannel channel : this.channels.values()) {
            channelList.add(channel.save(provider));
        }

        tag.put("Channels", channelList);
    }

    protected void loadAdditional(@NonNull CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        this.channels.clear();
        if (tag.contains("Channels", 9)) {
            for(Tag entry : tag.getList("Channels", 10)) {
                if (entry instanceof CompoundTag) {
                    CompoundTag channelTag = (CompoundTag)entry;
                    RedstoneLinkChannel channel = this.loadChannel(channelTag, provider);
                    if (channel != null) {
                        this.channels.put(channel.key(), channel);
                    }
                }
            }
        } else {
            ItemStack frequencyFirst = fromFrequencyId(tag.getString("FrequencyFirst"));
            ItemStack frequencyLast = fromFrequencyId(tag.getString("FrequencyLast"));
            RedstoneLinkChannel channel = new RedstoneLinkChannel(this, frequencyFirst, frequencyLast, tag.getInt("Transmit"));
            this.channels.put(channel.key(), channel);
        }

    }

    private RedstoneLinkChannel loadChannel(CompoundTag tag, HolderLookup.Provider provider) {
        ItemStack frequencyFirst;
        ItemStack frequencyLast;
        if (tag.contains("FrequencyFirst", 10)) {
            frequencyFirst = ItemStack.parseOptional(provider, tag.getCompound("FrequencyFirst"));
            frequencyLast = ItemStack.parseOptional(provider, tag.getCompound("FrequencyLast"));
        } else {
            frequencyFirst = fromFrequencyId(tag.getString("FrequencyFirst"));
            frequencyLast = fromFrequencyId(tag.getString("FrequencyLast"));
            if (tag.contains("DyeColorFirst", 3) && !frequencyFirst.isEmpty()) {
                frequencyFirst.set(DataComponents.DYED_COLOR, new DyedItemColor(tag.getInt("DyeColorFirst"), false));
            }

            if (tag.contains("DyeColorLast", 3) && !frequencyLast.isEmpty()) {
                frequencyLast.set(DataComponents.DYED_COLOR, new DyedItemColor(tag.getInt("DyeColorLast"), false));
            }
        }

        return new RedstoneLinkChannel(this, frequencyFirst, frequencyLast, tag.getInt("Transmit"));
    }

    private final class RedstoneLinkChannel implements IRedstoneLinkable {
        private final ItemStack frequencyFirst;
        private final ItemStack frequencyLast;
        private int transmittedStrength;
        private boolean registered;

        private RedstoneLinkChannel(final RedstoneLinkBridgeBlockEntity var1, ItemStack first, ItemStack last, int strength) {
            this.frequencyFirst = RedstoneLinkBridgeBlockEntity.normalize(first);
            this.frequencyLast = RedstoneLinkBridgeBlockEntity.normalize(last);
            this.transmittedStrength = RedstoneLinkBridgeBlockEntity.clampStrength(strength);
        }

        private CompoundTag save(HolderLookup.Provider provider) {
            CompoundTag tag = new CompoundTag();
            tag.put("FrequencyFirst", this.frequencyFirst.saveOptional(provider));
            tag.put("FrequencyLast", this.frequencyLast.saveOptional(provider));
            tag.putInt("Transmit", this.transmittedStrength);
            return tag;
        }

        private String key() {
            return RedstoneLinkBridgeBlockEntity.channelKey(this.frequencyFirst, this.frequencyLast);
        }

        private void register(Level level) {
            if (!this.registered) {
                Create.REDSTONE_LINK_NETWORK_HANDLER.addToNetwork(level, this);
                this.registered = true;
            }
        }

        private void unregister(Level level) {
            if (this.registered) {
                Create.REDSTONE_LINK_NETWORK_HANDLER.removeFromNetwork(level, this);
                this.registered = false;
            }
        }

        private void update(Level level) {
            if (level != null && !level.isClientSide) {
                Create.REDSTONE_LINK_NETWORK_HANDLER.updateNetworkOf(level, this);
            }

        }

        private void setTransmitStrength(int strength) {
            this.transmittedStrength = RedstoneLinkBridgeBlockEntity.clampStrength(strength);
            RedstoneLinkBridgeBlockEntity.this.setChanged();
            Level currentLevel = RedstoneLinkBridgeBlockEntity.this.level;
            if (currentLevel != null && !currentLevel.isClientSide) {
                this.update(currentLevel);
            }

        }

        public int getTransmittedStrength() {
            return this.transmittedStrength;
        }

        public void setReceivedStrength(int power) {
        }

        public boolean isListening() {
            return false;
        }

        public boolean isAlive() {
            return RedstoneLinkBridgeBlockEntity.this.level != null && !RedstoneLinkBridgeBlockEntity.this.isRemoved();
        }

        public Couple<RedstoneLinkNetworkHandler.Frequency> getNetworkKey() {
            return Couple.create(
                    RedstoneLinkNetworkHandler.Frequency.of(frequencyFirst),
                    RedstoneLinkNetworkHandler.Frequency.of(frequencyLast));
        }

        public BlockPos getLocation() {
            return RedstoneLinkBridgeBlockEntity.this.worldPosition;
        }
    }
}
