package com.possibletriangle.skygrid.travel;

import com.possibletriangle.skygrid.ConfigSkygrid;
import com.possibletriangle.skygrid.Skygrid;
import com.possibletriangle.skygrid.generation.DimensionHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.util.ITeleporter;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

@Mod.EventBusSubscriber
public class TravelManager {

    private static final HashMap<ResourceLocation, ArrayList<ResourceLocation>> FROM_TO = new HashMap<>();

    public static boolean registerFall(ResourceLocation from, ResourceLocation to, boolean overwrite) {
        if(!FROM_TO.containsKey(from))
            FROM_TO.put(from, new ArrayList<>());

        Skygrid.LOGGER.info("Registering fall from {} to {}", from, to);

        boolean exists = !FROM_TO.get(from).isEmpty();
        if(exists && overwrite) FROM_TO.get(from).clear();
        FROM_TO.get(from).add(to);
        return exists;
    }

    public static ResourceLocation fallDimension(ResourceLocation from, Random random) {
        if(from.getResourcePath().toLowerCase().equals("nether"))
            from = new ResourceLocation(from.getResourceDomain(), "the_nether");

        return (!FROM_TO.containsKey(from) || FROM_TO.get(from).isEmpty()) ? null : FROM_TO.get(from).get(random.nextInt(FROM_TO.get(from).size()));
    }

    public static ResourceLocation climbDimension(ResourceLocation from) {
        if(from.getResourcePath().toLowerCase().equals("nether"))
            from = new ResourceLocation(from.getResourceDomain(), "the_nether");

        for(ResourceLocation r : FROM_TO.keySet())
            if(FROM_TO.get(r).contains(from))
                return r;
        return null;
    }

    public static final DataParameter<Boolean> FALL_TO_DEATH = EntityDataManager.createKey(EntityPlayer.class, DataSerializers.BOOLEAN);
    public static final DataParameter<Integer> LIMBO_COUNT = EntityDataManager.createKey(EntityPlayer.class, DataSerializers.VARINT);
    public static final DataParameter<Integer> LIMBO_COUNTDOWN = EntityDataManager.createKey(EntityPlayer.class, DataSerializers.VARINT);

    @SubscribeEvent
    public static void playerConstruction(EntityEvent.EntityConstructing event) {

        if(event.getEntity() instanceof EntityPlayer) {
            event.getEntity().getDataManager().register(FALL_TO_DEATH, false);
            event.getEntity().getDataManager().register(LIMBO_COUNT, 0);
            event.getEntity().getDataManager().register(LIMBO_COUNTDOWN, 0);
        }

    }

    @SubscribeEvent
    public static void crossDim(PlayerEvent.PlayerChangedDimensionEvent event) {

        String from = DimensionManager.getProviderType(event.fromDim).getName();
        String to = DimensionManager.getProviderType(event.toDim).getName();

        if(from.equals(to))
            return;

        if("limbo".equals(from)) {

            Potion weakness = Potion.getPotionFromResourceLocation("minecraft:weakness");
            if(weakness != null)
                event.player.addPotionEffect(new PotionEffect(weakness, 20*60*5, 2, true, false));

        }

        else if("limbo".equals(to)) {

            event.player.getDataManager().set(LIMBO_COUNT, event.player.getDataManager().get(LIMBO_COUNT)+1);
            event.player.sendMessage(new TextComponentString("Limbo Count: " + event.player.getDataManager().get(LIMBO_COUNT)));

        }
    }

    @SubscribeEvent
    public static void playerTick(TickEvent.PlayerTickEvent event) {

        if(event.player.getDataManager().get(LIMBO_COUNT) > 0) {

            if(event.player.getDataManager().get(LIMBO_COUNTDOWN) > 0) {

                if(event.player.getDataManager().get(LIMBO_COUNTDOWN) == 1) {
                    /* TODO Limbo sickness effects */
                }

                event.player.getDataManager().set(LIMBO_COUNTDOWN, event.player.getDataManager().get(LIMBO_COUNTDOWN)-1);

            } else {
                int min = 10 + 40 / event.player.getDataManager().get(LIMBO_COUNT);
                int dis = 15;
                int c = (int) ((Math.random() + min) * 20 * 60 * 60 * dis);
                event.player.getDataManager().set(LIMBO_COUNTDOWN, c);
            }

        }

        if(event.player.posY <= ConfigSkygrid.LOWER && !event.player.getDataManager().get(FALL_TO_DEATH)) {
            Skygrid.LOGGER.info("{} fell", event.player.getDisplayName().getFormattedText());

            ResourceLocation from = new ResourceLocation(DimensionManager.getProviderType(event.player.dimension).getName());
            Random r = new Random();
            ResourceLocation to = new ResourceLocation(Skygrid.MODID, "limbo");
            if(r.nextFloat() >= ConfigSkygrid.LIMBO_CHANCE_FALL)
                to = fallDimension(from, r);

            boolean die = to == null || r.nextDouble() < ConfigSkygrid.VOID_CHANCE;

            if(from.getResourcePath().equals("limbo")) {
                die = false;
                to = from;
            }

            event.player.getDataManager().set(FALL_TO_DEATH, true);
            if(!die)
                event.player.changeDimension(DimensionHelper.getIDFor(to), new FallTeleporter(new BlockPos(event.player.posX, Skygrid.WORLD_HEIGHT + ConfigSkygrid.UPPER, event.player.posZ)));

        }

        if(event.player.posY >= 0 && event.player.posY <= Skygrid.WORLD_HEIGHT && event.player.getDataManager().get(FALL_TO_DEATH)) {
            Skygrid.LOGGER.info("{} returned to a safe space", event.player.getDisplayName().getFormattedText());
            event.player.getDataManager().set(FALL_TO_DEATH, false);
        }

        if(event.player.posY >= Skygrid.WORLD_HEIGHT + ConfigSkygrid.UPPER && !event.player.getDataManager().get(FALL_TO_DEATH)) {
            Skygrid.LOGGER.info("{} climbed", event.player.getDisplayName().getFormattedText());

                ResourceLocation from = new ResourceLocation(DimensionManager.getProviderType(event.player.dimension).getName());

                ResourceLocation to = new ResourceLocation(Skygrid.MODID, "limbo");
            if(new Random().nextFloat() >= ConfigSkygrid.LIMBO_CHANCE_CLIMB)
                    to = climbDimension(from);

                if(from.getResourcePath().equals("limbo")) {
                    to = from;
                }

                event.player.getDataManager().set(FALL_TO_DEATH, true);
                if(to != null) {
                    event.player.changeDimension(DimensionHelper.getIDFor(to), new FallTeleporter(new BlockPos(event.player.posX, -10, event.player.posZ)));
                    event.player.addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("minecraft:levitation"), 20 * 10, 3, true, true));
                }

            }
        }

    private static class FallTeleporter implements ITeleporter {
        private final BlockPos targetPos;

        private FallTeleporter(BlockPos targetPos)
        {
            this.targetPos = targetPos;
        }

        @Override
        public void placeEntity(World world, Entity entity, float yaw) {
            entity.moveToBlockPosAndAngles(targetPos, yaw, entity.rotationPitch);
        }
    }

    public static void registerDefaults() {

        /*
        registerFall(new ResourceLocation("overworld"), new ResourceLocation("the_nether"), false);
        registerFall(new ResourceLocation("AetherI"), new ResourceLocation("overworld"), false);
        registerFall(new ResourceLocation("twilight_forest"), new ResourceLocation("EREBUS"), false);
        registerFall(new ResourceLocation("Tropics"), new ResourceLocation("ocean"), false);
        registerFall(new ResourceLocation("teletory"), new ResourceLocation("the_end"), false);
        */

    }

    public static void validate() {

        for(ResourceLocation from : FROM_TO.keySet()) {

            ArrayList<ResourceLocation> remove = new ArrayList<>();
            for(ResourceLocation to : FROM_TO.get(from)) {
                if(DimensionHelper.getIDFor(to) == DimensionManager.getNextFreeDimId()) {
                    Skygrid.LOGGER.info("Removing fall to \"{}\", because it does not exist", to);
                    remove.add(to);
                }
            }

            FROM_TO.get(from).removeAll(remove);

        }

    }

}
