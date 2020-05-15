package possibletriangle.skygrid;

import com.google.common.base.Predicates;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.EndPortalFrameBlock;
import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.block.pattern.BlockPatternBuilder;
import net.minecraft.block.pattern.BlockStateMatcher;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.CachedBlockInfo;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IWorld;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.SaveHandler;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.WorldPersistenceHooks;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import possibletriangle.skygrid.block.StiffAir;
import possibletriangle.skygrid.command.SkygridCommand;
import possibletriangle.skygrid.util.loading.DimensionLoader;
import possibletriangle.skygrid.world.SkygridWorldType;

import java.util.List;
import java.util.Set;

@Mod(Skygrid.MODID)
public class Skygrid implements WorldPersistenceHooks.WorldPersistenceHook {
    public static final String MODID = "skygrid";

    public Skygrid() {
        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());

        WorldPersistenceHooks.addHook(this);

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        MinecraftForge.EVENT_BUS.register(this);
    }

    public static final DeferredRegister<Block> BLOCKS = new DeferredRegister<>(ForgeRegistries.BLOCKS, Skygrid.MODID);
    public static final RegistryObject<Block> STIFF_AIR = BLOCKS.register("stiff_air", StiffAir::new);

    private void setup(final FMLCommonSetupEvent event) {
        new SkygridWorldType();
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onServerWillStart(final FMLServerAboutToStartEvent event) {
        MinecraftServer server = event.getServer();
        server.getResourceManager().addReloadListener(new DimensionLoader(server));
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onServerStarting(final FMLServerStartingEvent event) {
        SkygridCommand.register(event.getCommandDispatcher());
    }

    public static final BlockPattern PORTAL = BlockPatternBuilder.start()
            .aisle("FFF", "F?F", "FFF")
            .where('?', CachedBlockInfo.hasState(BlockStateMatcher.ANY))
            .where('F', CachedBlockInfo.hasState(BlockStateMatcher.forBlock(Blocks.END_PORTAL_FRAME)
                    .where(EndPortalFrameBlock.EYE, Predicates.equalTo(true))))
            .build();

    @SubscribeEvent
    public void onBlockUpdate(final BlockEvent event) {
        BlockState state = event.getState();
        IWorld w = event.getWorld();
        if (w instanceof ServerWorld) {
            ServerWorld world = (ServerWorld) w;

            if (state.getBlock() == Blocks.END_PORTAL_FRAME) {

                state.get(EndPortalFrameBlock.EYE);
                BlockPattern.PatternHelper match = PORTAL.match(world, event.getPos());

                if (match != null) {
                    BlockPos portal = match.getFrontTopLeft().add(-1, 0, -1);
                    world.setBlockState(portal, Blocks.END_PORTAL.getDefaultState(), 2);
                    world.playBroadcastSound(1038, portal, 0);
                }
            }
        }
    }

    private Set<ResourceLocation> getTags(Item item) {
        if (item instanceof BlockItem) return ((BlockItem) item).getBlock().getTags();
        return item.getTags();
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void onTooltip(final ItemTooltipEvent event) {
        Item item = event.getItemStack().getItem();
        List<ITextComponent> tooltip = event.getToolTip();

        if (event.getFlags().isAdvanced())
            getTags(item).stream()
                    .map(ResourceLocation::toString)
                    .map(s -> '#' + s)
                    .map(StringTextComponent::new)
                    .map(t -> t.applyTextStyle(TextFormatting.GRAY))
                    .forEachOrdered(tooltip::add);
    }

    @Override
    public String getModId() {
        return MODID;
    }

    @Override
    public CompoundNBT getDataForWriting(SaveHandler handler, WorldInfo info) {
        CompoundNBT data = new CompoundNBT();
        CompoundNBT dims = new CompoundNBT();
        DimensionManager.writeRegistry(dims);
        if (!dims.isEmpty())
            data.put("dims", dims);
        return data;
    }

    @Override
    public void readData(SaveHandler handler, WorldInfo info, CompoundNBT tag) {
        if (tag.contains("dims", 10))
            DimensionLoader.readRegistry(tag.getCompound("dims"));
    }
}
