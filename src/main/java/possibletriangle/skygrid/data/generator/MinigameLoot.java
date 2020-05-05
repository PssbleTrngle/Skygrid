package possibletriangle.skygrid.data.generator;

import net.minecraft.data.DataGenerator;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.*;
import net.minecraft.world.storage.loot.conditions.TableBonus;
import net.minecraft.world.storage.loot.functions.ApplyBonus;
import net.minecraft.world.storage.loot.functions.EnchantRandomly;
import net.minecraft.world.storage.loot.functions.SetCount;
import net.minecraft.world.storage.loot.functions.SetNBT;
import net.minecraftforge.common.Tags;
import possibletriangle.skygrid.Skygrid;

public class MinigameLoot extends LootTables {

    public MinigameLoot(DataGenerator generator) {
        super(generator);
    }

    private SetNBT.Builder<?> SetPotion(Potion potion) {
        ItemStack stack = new ItemStack(Items.POTION);
        PotionUtils.addPotionToItemStack(stack, potion);
        CompoundNBT nbt = stack.getOrCreateTag();
        return SetNBT.func_215952_a(nbt);
    }

    @Override
    protected void addTables() {

        addTable(new ResourceLocation("skygridgame", "chests/basic"), LootTable.builder()
                        .addLootPool(pool("tools")
                                .rolls(ConstantRange.of(1))
                                .addEntry(ItemLootEntry.builder(Items.WOODEN_AXE).weight(20))
                                .addEntry(ItemLootEntry.builder(Items.WOODEN_PICKAXE).weight(20))
                                .addEntry(ItemLootEntry.builder(Items.WOODEN_SHOVEL).weight(20))
                                .addEntry(ItemLootEntry.builder(Items.DIAMOND_HOE).weight(2))
                                .addEntry(ItemLootEntry.builder(Items.FISHING_ROD).weight(1))
                                .addEntry(ItemLootEntry.builder(Items.LEATHER_HELMET).weight(2))
                        ).addLootPool(pool("gadgets")
                                .rolls(RandomValueRange.of(0, 1))
                                .addEntry(ItemLootEntry.builder(Items.FIRE_CHARGE).weight(1))
                                .addEntry(ItemLootEntry.builder(Items.ENDER_PEARL).weight(1))
                                .addEntry(ItemLootEntry.builder(Items.EXPERIENCE_BOTTLE).weight(2).acceptFunction(SetCount.builder(RandomValueRange.of(3, 6))))
                        ).addLootPool(pool("weapons")
                                .rolls(ConstantRange.of(1))
                                .addEntry(ItemLootEntry.builder(Items.LEATHER_CHESTPLATE).weight(2))
                                .addEntry(ItemLootEntry.builder(Items.LEATHER_BOOTS).weight(2))
                                .addEntry(ItemLootEntry.builder(Items.LEATHER_LEGGINGS).weight(2))
                                .addEntry(ItemLootEntry.builder(Items.ARROW).weight(10).acceptFunction(SetCount.builder(RandomValueRange.of(1, 3))))
                                .addEntry(ItemLootEntry.builder(Items.BOW).weight(1))
                                .addEntry(ItemLootEntry.builder(Items.CROSSBOW).weight(1))
                                .addEntry(ItemLootEntry.builder(Items.FLINT_AND_STEEL).weight(4))
                        ).addLootPool(pool("resources")
                                .rolls(ConstantRange.of(4))
                                .addEntry(ItemLootEntry.builder(Items.STICK).weight(20).acceptFunction(SetCount.builder(RandomValueRange.of(4, 6))))
                                .addEntry(ItemLootEntry.builder(Items.STRING).weight(20).acceptFunction(SetCount.builder(RandomValueRange.of(1, 3))))
                                .addEntry(ItemLootEntry.builder(Items.BONE_MEAL).weight(20).acceptFunction(SetCount.builder(RandomValueRange.of(7, 14))))
                                .addEntry(ItemLootEntry.builder(Items.FLINT).weight(20).acceptFunction(SetCount.builder(RandomValueRange.of(1, 2))))
                                .addEntry(ItemLootEntry.builder(Items.FEATHER).weight(20).acceptFunction(SetCount.builder(RandomValueRange.of(1, 3))))
                                .addEntry(ItemLootEntry.builder(Items.BIRCH_PLANKS).weight(10).acceptFunction(SetCount.builder(RandomValueRange.of(3, 6))))
                                .addEntry(ItemLootEntry.builder(Items.OAK_PLANKS).weight(10).acceptFunction(SetCount.builder(RandomValueRange.of(3, 6))))
                                .addEntry(ItemLootEntry.builder(Items.SPRUCE_PLANKS).weight(10).acceptFunction(SetCount.builder(RandomValueRange.of(3, 6))))
                                .addEntry(ItemLootEntry.builder(Items.DARK_OAK_PLANKS).weight(10).acceptFunction(SetCount.builder(RandomValueRange.of(3, 6))))
                                .addEntry(ItemLootEntry.builder(Items.COBBLESTONE).weight(20).acceptFunction(SetCount.builder(RandomValueRange.of(2, 4))))
                        ).addLootPool(pool("shiny")
                                .rolls(RandomValueRange.of(0, 2))
                                .addEntry(ItemLootEntry.builder(Items.IRON_INGOT).weight(20).acceptFunction(SetCount.builder(RandomValueRange.of(1, 2))))
                                .addEntry(ItemLootEntry.builder(Items.GOLD_INGOT).weight(20).acceptFunction(SetCount.builder(RandomValueRange.of(1, 4))))
                                .addEntry(ItemLootEntry.builder(Items.COAL).weight(10).acceptFunction(SetCount.builder(RandomValueRange.of(1, 2))))
                                .addEntry(ItemLootEntry.builder(Items.CHARCOAL).weight(10).acceptFunction(SetCount.builder(RandomValueRange.of(1, 2))))
                                .addEntry(ItemLootEntry.builder(Items.LEATHER).weight(15).acceptFunction(SetCount.builder(RandomValueRange.of(1, 2))))
                                .addEntry(ItemLootEntry.builder(Items.DIAMOND).weight(1))
                        ).addLootPool(pool("books")
                                .rolls(ConstantRange.of(1))
                                .addEntry(EmptyLootEntry.func_216167_a().weight(10))
                                .addEntry(ItemLootEntry.builder(Items.BOOK).acceptFunction(EnchantRandomly.func_215900_c()).weight(1))
                        ).addLootPool(pool("potions")
                                .rolls(RandomValueRange.of(1, 2))
                                .addEntry(EmptyLootEntry.func_216167_a().weight(80))
                                .addEntry(ItemLootEntry.builder(Items.POTION).acceptFunction(SetPotion(Potions.INVISIBILITY)).weight(1))
                                .addEntry(ItemLootEntry.builder(Items.POTION).acceptFunction(SetPotion(Potions.SWIFTNESS)).weight(1))
                                .addEntry(ItemLootEntry.builder(Items.POTION).acceptFunction(SetPotion(Potions.SLOW_FALLING)).weight(2))
                                .addEntry(ItemLootEntry.builder(Items.POTION).acceptFunction(SetPotion(Potions.FIRE_RESISTANCE)).weight(1))
                                .addEntry(ItemLootEntry.builder(Items.POTION).acceptFunction(SetPotion(Potions.LUCK)).weight(1))
                                .addEntry(ItemLootEntry.builder(Items.POTION).acceptFunction(SetPotion(Potions.HEALING)).weight(1))
                                .addEntry(ItemLootEntry.builder(Items.POTION).acceptFunction(SetPotion(Potions.LONG_LEAPING)).weight(2))

                                .addEntry(ItemLootEntry.builder(Items.SPLASH_POTION).acceptFunction(SetPotion(Potions.POISON)).weight(1))
                                .addEntry(ItemLootEntry.builder(Items.SPLASH_POTION).acceptFunction(SetPotion(Potions.SLOWNESS)).weight(4))
                                .addEntry(ItemLootEntry.builder(Items.SPLASH_POTION).acceptFunction(SetPotion(Potions.WEAKNESS)).weight(4))
                                .addEntry(ItemLootEntry.builder(Items.SPLASH_POTION).acceptFunction(SetPotion(Potions.HARMING)).weight(2))
                                .addEntry(ItemLootEntry.builder(Items.SPLASH_POTION).acceptFunction(SetPotion(Potions.SLOW_FALLING)).weight(4))

                                .addEntry(ItemLootEntry.builder(Items.LINGERING_POTION).acceptFunction(SetPotion(Potions.POISON)).weight(1))
                                .addEntry(ItemLootEntry.builder(Items.LINGERING_POTION).acceptFunction(SetPotion(Potions.SLOWNESS)).weight(4))
                                .addEntry(ItemLootEntry.builder(Items.LINGERING_POTION).acceptFunction(SetPotion(Potions.WEAKNESS)).weight(4))
                                .addEntry(ItemLootEntry.builder(Items.LINGERING_POTION).acceptFunction(SetPotion(Potions.HARMING)).weight(2))
                                .addEntry(ItemLootEntry.builder(Items.LINGERING_POTION).acceptFunction(SetPotion(Potions.SLOW_FALLING)).weight(4))

                                .addEntry(ItemLootEntry.builder(Items.TIPPED_ARROW).acceptFunction(SetPotion(Potions.POISON)).weight(1))
                                .addEntry(ItemLootEntry.builder(Items.TIPPED_ARROW).acceptFunction(SetPotion(Potions.SLOWNESS)).weight(4))
                                .addEntry(ItemLootEntry.builder(Items.TIPPED_ARROW).acceptFunction(SetPotion(Potions.WEAKNESS)).weight(4))
                                .addEntry(ItemLootEntry.builder(Items.TIPPED_ARROW).acceptFunction(SetPotion(Potions.HARMING)).weight(2))
                                .addEntry(ItemLootEntry.builder(Items.TIPPED_ARROW).acceptFunction(SetPotion(Potions.SLOW_FALLING)).weight(4))
                        ),
                LootParameterSets.CHEST
        );

    }
}
