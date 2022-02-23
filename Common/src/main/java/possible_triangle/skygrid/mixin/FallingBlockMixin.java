package possible_triangle.skygrid.mixin;

import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import possible_triangle.skygrid.platform.Services;

@Mixin(FallingBlock.class)
public class FallingBlockMixin {

    @Inject(at = @At("HEAD"), method = "isFree(Lnet/minecraft/world/level/block/state/BlockState;)Z", cancellable = true)
    private static void isFree(BlockState state, CallbackInfoReturnable<Boolean> callback) {
        if(state.is(Services.PLATFORM.getBarrier())) callback.setReturnValue(false);
    }

}
