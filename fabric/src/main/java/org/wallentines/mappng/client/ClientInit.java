package org.wallentines.mappng.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.MapRenderer;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.wallentines.mappng.MapPNGExtension;
import org.wallentines.mappng.PNGMapItemData;
import org.wallentines.midnightcore.api.module.messaging.ClientMessagingModule;
import org.wallentines.midnightcore.fabric.client.MidnightCoreClient;

@Environment(EnvType.CLIENT)
public class ClientInit {

    public static void init() {

        ClientMessagingModule mod = MidnightCoreClient.CLIENT_MODULES.getModule(ClientMessagingModule.class);

        mod.registerHandler(MapPNGExtension.MAP_DATA_PACKET, pck -> {

            FriendlyByteBuf buf = (FriendlyByteBuf) pck;

            Minecraft mc = Minecraft.getInstance();
            MapRenderer mapRenderer = mc.gameRenderer.getMapRenderer();

            String level = buf.readUtf();
            int mapId = buf.readVarInt();
            String mapData = buf.readUtf(900000);

            ResourceLocation loc = ResourceLocation.tryParse(level);
            if(loc == null || mc.level == null) {
                return null;
            }

            String mapKey = MapItem.makeKey(mapId);

            MapItemSavedData dt = mc.level.getMapData(mapKey);
            if(dt == null) {
                dt = MapItemSavedData.createForClient((byte) 0, true, ResourceKey.create(Registry.DIMENSION_REGISTRY, loc));
            }

            ((PNGMapItemData) dt).setCustomData(mapData);

            mc.level.setMapData(mapKey, dt);
            mapRenderer.update(mapId, dt);

            return null; // No response necessary
        });
    }
}
