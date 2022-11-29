package org.wallentines.mappng;

import net.minecraft.commands.Commands;
import org.wallentines.midnightcore.api.module.extension.ExtensionModule;
import org.wallentines.midnightcore.fabric.event.server.CommandLoadEvent;
import org.wallentines.midnightcore.fabric.util.CommandUtil;
import org.wallentines.midnightcore.fabric.util.ConversionUtil;
import org.wallentines.midnightlib.event.Event;

import java.io.File;

public class TestStuff {

    public static void init() {

        Event.register(CommandLoadEvent.class, TestStuff.class, ev -> {

            ev.getDispatcher().register(Commands.literal("testmap")
                .executes(ctx -> {

                    try {

                        MapPNGExtension ext = CommandUtil.getModule(ExtensionModule.class).getExtension(MapPNGExtension.class);

                        ext.loadImage(
                                ConversionUtil.toIdentifier(ctx.getSource().getServer().overworld().dimension().location()),
                            0,
                            new File("map.png")
                        );

                    } catch (Throwable th) {
                        th.printStackTrace();
                        throw th;
                    }

                    return 1;
                })
            );
        });

    }

}
