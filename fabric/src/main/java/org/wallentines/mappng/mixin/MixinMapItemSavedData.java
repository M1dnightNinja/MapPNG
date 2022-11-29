package org.wallentines.mappng.mixin;

import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundCustomPayloadPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.wallentines.mappng.MapPNGExtension;
import org.wallentines.mappng.PNGMapItemData;
import org.wallentines.midnightcore.api.MidnightCoreAPI;
import org.wallentines.midnightcore.api.module.extension.ExtensionModule;
import org.wallentines.midnightcore.api.module.extension.ServerExtensionModule;
import org.wallentines.midnightcore.fabric.player.FabricPlayer;
import org.wallentines.midnightcore.fabric.util.ConversionUtil;

@Mixin(MapItemSavedData.class)
public abstract class MixinMapItemSavedData implements PNGMapItemData {
    @Shadow protected abstract void setDecorationsDirty();

    @Shadow @Final public ResourceKey<Level> dimension;
    @Unique
    private String pngData = null;

    @Redirect(method="getUpdatePacket", at=@At(value="INVOKE", target="Lnet/minecraft/world/level/saveddata/maps/MapItemSavedData$HoldingPlayer;nextUpdatePacket(I)Lnet/minecraft/network/protocol/Packet;"))
    private Packet<?> onUpdatePacket(MapItemSavedData.HoldingPlayer instance, int index) {

        AccessorHoldingPlayer hp = (AccessorHoldingPlayer) instance;
        Packet<?> out = makeCustomPacket(instance, index);

        return out == null ? hp.callNextUpdatePacket(index) : out;
    }

    @Unique
    private Packet<?> makeCustomPacket(MapItemSavedData.HoldingPlayer player, int index) {

        AccessorHoldingPlayer hp = (AccessorHoldingPlayer) player;
        if(pngData == null || (!hp.isDirtyData() && !hp.isDirtyDecorations())) return null;

        ExtensionModule mod = MidnightCoreAPI.getModule(ExtensionModule.class);
        if(!(mod instanceof ServerExtensionModule se) || !se.playerHasExtension(FabricPlayer.wrap((ServerPlayer) player.player), MapPNGExtension.ID)) return null;

        hp.setDirtyData(false);
        hp.setDirtyDecorations(false);

        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeUtf(dimension.location().toString());
        buf.writeVarInt(index);
        buf.writeUtf(pngData, 900000);

        return new ClientboundCustomPayloadPacket(ConversionUtil.toResourceLocation(MapPNGExtension.MAP_DATA_PACKET), buf);
    }


    @Override
    public String getCustomData() {
        return pngData;
    }

    @Override
    public void setCustomData(String buffer) {
        pngData = buffer;
        setDecorationsDirty();
    }
}
