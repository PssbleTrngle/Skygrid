package possible_triangle.skygrid.mixin;

import net.minecraft.core.RegistryAccess;
import net.minecraft.world.level.levelgen.WorldGenSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import possible_triangle.skygrid.world.SkygridGenerator;

import java.util.Properties;

@Mixin(WorldGenSettings.class)
public class WorldGenSettingsMixin {

    @Inject(at = @At("RETURN"), method = "create(Lnet/minecraft/core/RegistryAccess;Ljava/util/Properties;)Lnet/minecraft/world/level/levelgen/WorldGenSettings;", cancellable = true)
    private static void createSettings(RegistryAccess registries, Properties properties, CallbackInfoReturnable<WorldGenSettings> callback) {
        var settings = SkygridGenerator.INSTANCE.fromServerProperties(registries, properties);
        if (settings != null) callback.setReturnValue(settings);
    }

}
