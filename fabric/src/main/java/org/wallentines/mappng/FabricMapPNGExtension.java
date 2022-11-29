package org.wallentines.mappng;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.wallentines.midnightcore.api.module.extension.Extension;
import org.wallentines.midnightcore.api.module.extension.ExtensionModule;
import org.wallentines.midnightcore.fabric.MidnightCore;
import org.wallentines.midnightcore.fabric.util.ConversionUtil;
import org.wallentines.midnightlib.Version;
import org.wallentines.midnightlib.config.ConfigSection;
import org.wallentines.midnightlib.module.ModuleInfo;
import org.wallentines.midnightlib.registry.Identifier;

import java.io.File;

public class FabricMapPNGExtension implements MapPNGExtension {

    private ExtensionModule module;

    @Override
    public Version getVersion() {
        return new Version(1,0,0);
    }

    @Override
    public boolean initialize(ConfigSection section, ExtensionModule module) {

        this.module = module;

        if(module.isClient()) {
            org.wallentines.mappng.client.ClientInit.init();
        }

        return true;
    }

    @Override
    public void loadImage(Identifier world, int id, File image) {

        if(module.isClient()) return;

        ResourceKey<Level> levelKey = ResourceKey.create(Registry.DIMENSION_REGISTRY, ConversionUtil.toResourceLocation(world));
        Level lvl = MidnightCore.getInstance().getServer().getLevel(levelKey);
        if(lvl == null) {
            MapPNG.LOGGER.warn("Unable to find requested world " + world + "!");
            return;
        }

        String key = MapItem.makeKey(id);

        MapItemSavedData data = lvl.getMapData(key);
        if(data == null) {
            data = MapItemSavedData.createFresh(0, 0, (byte) 0, false, false, levelKey).locked();
        }

        ((PNGMapItemData) data).loadFromPNG(image);
        lvl.setMapData(key, data);
    }

    public static final ModuleInfo<ExtensionModule, Extension> MODULE_INFO = new ModuleInfo<>(FabricMapPNGExtension::new, ID, new ConfigSection());

}
