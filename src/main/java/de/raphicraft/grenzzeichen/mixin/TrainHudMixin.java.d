package de.raphicraft.grenzzeichen.mixin;



import com.simibubi.create.content.trains.TrainHUD;
import com.simibubi.create.content.trains.entity.Carriage;
import de.raphicraft.grenzzeichen.hud.etcs.ITrainInterface;
import net.minecraft.client.gui.DrawContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = TrainHUD.class)
public abstract class TrainHudMixin {


    @Shadow
    protected static Carriage getCarriage() {
        return null;
    }

    @Inject(method = "renderOverlay", at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/blaze3d/vertex/PoseStack;pushPose()V",
            ordinal = 0, // This targets the third translate call (0-indexed)
            shift = At.Shift.BEFORE // Important: inject AFTER the translate call
    ))
    private static void injectAfterDirectionTranslate(DrawContext graphics, float partialTicks, net.minecraft.client.util.Window window, CallbackInfo ci) {
        if(getCarriage().train instanceof ITrainInterface RTrain&& RealismConfig.CLIENT.ETCSEnable.get() && RealismConfig.COMMON.GlobalETCSEnable.get()) {
        RTrain.realism$getETCS().render(graphics);}
    }
}
