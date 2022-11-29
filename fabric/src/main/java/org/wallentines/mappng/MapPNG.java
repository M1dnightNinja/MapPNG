package org.wallentines.mappng;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.wallentines.midnightcore.api.module.extension.ExtensionModule;

public class MapPNG implements ModInitializer {

    public static final Logger LOGGER = LogManager.getLogger("MapPNG");

    @Override
    public void onInitialize() {

        if(FabricLoader.getInstance().isDevelopmentEnvironment()) {
            TestStuff.init();
        }

        ExtensionModule.REGISTRY.register(FabricMapPNGExtension.ID, FabricMapPNGExtension.MODULE_INFO);

    }
}
