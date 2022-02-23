package possible_triangle.skygrid;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;

public class SkygridFabric implements ModInitializer {
    
    @Override
    public void onInitialize() {
        CommonClass.INSTANCE.init();
        ItemTooltipCallback.EVENT.register(CommonClass.INSTANCE::onItemTooltip);
    }
}
