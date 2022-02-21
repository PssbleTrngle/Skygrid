package possible_triangle.skygrid.mixins;

import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import possible_triangle.skygrid.SkygridMod;
import possible_triangle.skygrid.data.XMLResource;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {

    @Inject(at = @At("RETURN"), method = "reloadResources(Ljava/util/Collection;)Ljava/util/concurrent/CompletableFuture;", cancellable = true)
    public void onReload(Collection<String> packs, CallbackInfoReturnable<CompletableFuture<Void>> callback) {
        var self = (MinecraftServer) (Object) this;
        SkygridMod.INSTANCE.getLOGGER().info("Reloading");
        callback.getReturnValue().thenAccept($ -> XMLResource.Companion.reload(self));
    }

}
