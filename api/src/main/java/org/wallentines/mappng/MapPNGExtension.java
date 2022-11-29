package org.wallentines.mappng;

import org.wallentines.midnightcore.api.module.extension.Extension;
import org.wallentines.midnightlib.registry.Identifier;

import java.io.File;

public interface MapPNGExtension extends Extension {

    void loadImage(Identifier world, int id, File image);

    Identifier ID = new Identifier("mappng", "mappng");

    Identifier MAP_DATA_PACKET = new Identifier("mappng", "map");

}
