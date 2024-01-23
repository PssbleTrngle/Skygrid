package com.possible_triangle.skygrid.mixin;

import com.possible_triangle.skygrid.SkygridMod;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FallingBlock.class)
public class FallingBlockMixin {

    @Inject(at = @At("HEAD"), method = "isFree(Lnet/minecraft/world/level/block/state/BlockState;)Z", cancellable = true)
    private static void isFree(BlockState state, CallbackInfoReturnable<Boolean> callback) {
        if (state.is(SkygridMod.INSTANCE.getSTIFF_AIR())) callback.setReturnValue(false);
    }

}
