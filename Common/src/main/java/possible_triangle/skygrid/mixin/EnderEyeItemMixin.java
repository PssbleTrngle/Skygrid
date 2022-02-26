package possible_triangle.skygrid.mixin;

import com.google.common.base.Predicates;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.EnderEyeItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EndPortalFrameBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.level.block.state.pattern.BlockPattern;
import net.minecraft.world.level.block.state.pattern.BlockPatternBuilder;
import net.minecraft.world.level.block.state.predicate.BlockStatePredicate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Predicate;

@Mixin(EnderEyeItem.class)
public class EnderEyeItemMixin {

    private static Predicate<BlockInWorld> frameBlock(Direction facing) {
        return BlockInWorld.hasState(BlockStatePredicate.forBlock(Blocks.END_PORTAL_FRAME)
                .where(EndPortalFrameBlock.HAS_EYE, Predicates.equalTo(true))
                .where(EndPortalFrameBlock.FACING, Predicates.equalTo(facing))
        );
    }

    private static final BlockPattern portalShape = BlockPatternBuilder.start()
            .aisle("^^^", "<?>", "vvv")
            .where('?', BlockInWorld.hasState(BlockStatePredicate.ANY))
            .where('^', frameBlock(Direction.EAST))
            .where('v', frameBlock(Direction.WEST))
            .where('<', frameBlock(Direction.NORTH))
            .where('>', frameBlock(Direction.SOUTH))
            .build();

    @Inject(at = @At("RETURN"), method = "useOn(Lnet/minecraft/world/item/context/UseOnContext;)Lnet/minecraft/world/InteractionResult;", cancellable = true)
    public void useOn(UseOnContext ctx, CallbackInfoReturnable<InteractionResult> callback) {
        Level level = ctx.getLevel();
        BlockPos pos = ctx.getClickedPos();
        BlockState state = level.getBlockState(pos);
        if (!level.isClientSide && state.is(Blocks.END_PORTAL_FRAME) && callback.getReturnValue() == InteractionResult.CONSUME) {
            BlockPattern.BlockPatternMatch match = portalShape.find(level, pos);
            if (match != null) {
                BlockPos portal = match.getFrontTopLeft().offset(-1, 0, -1);
                level.setBlock(portal, Blocks.END_PORTAL.defaultBlockState(), 2);
                level.globalLevelEvent(1038, portal.offset(1, 0, 1), 0);
            }
        }
    }

}
