package org.wallentines.mappng.mixin;

import net.minecraft.network.protocol.Packet;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(MapItemSavedData.HoldingPlayer.class)
public interface AccessorHoldingPlayer {
    @Invoker
    Packet<?> callNextUpdatePacket(int i);

    @Accessor
    boolean isDirtyData();

    @Accessor
    void setDirtyData(boolean dirtyData);

    @Accessor
    boolean isDirtyDecorations();

    @Accessor
    void setDirtyDecorations(boolean dirtyDecorations);
}
