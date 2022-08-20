package possible_triangle.skygrid.mixin;

import net.minecraft.core.RegistryAccess;
import net.minecraft.server.dedicated.DedicatedServerProperties;
import net.minecraft.world.level.levelgen.WorldGenSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import possible_triangle.skygrid.world.ServerProvider;
import possible_triangle.skygrid.world.SkygridGenerator;

@Mixin(WorldGenSettings.class)
public class WorldGenSettingsMixin {

    @Inject(at = @At("RETURN"), method = "create(Lnet/minecraft/core/RegistryAccess;Lnet/minecraft/server/dedicated/DedicatedServerProperties$WorldGenProperties;)Lnet/minecraft/world/level/levelgen/WorldGenSettings;", cancellable = true)
    private static void createSettings(RegistryAccess registries, DedicatedServerProperties.WorldGenProperties properties, CallbackInfoReturnable<WorldGenSettings> callback) {
        var settings = ServerProvider.INSTANCE.fromServerProperties(registries, properties);
        if (settings != null) callback.setReturnValue(settings);
    }

}
