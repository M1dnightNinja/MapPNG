package org.wallentines.mappng.mixin;

import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.wallentines.mappng.PNGMapItemData;
import org.wallentines.midnightcore.api.MidnightCoreAPI;

import java.io.IOException;

@Mixin(targets="net.minecraft.client.gui.MapRenderer$MapInstance")
public class MixinMapInstance {

    @Shadow private MapItemSavedData data;
    @Shadow @Final private DynamicTexture texture;


    @Inject(method= "updateTexture()V", at=@At("HEAD"), cancellable = true)
    private void onUpdateTexture(CallbackInfo ci) {

        if(data instanceof PNGMapItemData png && png.getCustomData() != null) {

            try {

                NativeImage image = NativeImage.fromBase64(((PNGMapItemData) data).getCustomData());

                texture.setPixels(image);
                texture.upload();

            } catch (IOException ex) {
                // Ignore
            }

            ci.cancel();
        }
    }

}
