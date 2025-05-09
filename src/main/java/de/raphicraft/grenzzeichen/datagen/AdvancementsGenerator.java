package de.raphicraft.grenzzeichen.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementFrame;
import net.minecraft.advancement.criterion.InventoryChangedCriterion;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

/** Advancement data gen */
public class AdvancementsGenerator extends FabricAdvancementProvider {
    /** Setup generator */
    public AdvancementsGenerator(FabricDataOutput output) {
        super(output);
    }

    /** Generate advancements */
    @Override
    public void generateAdvancement(Consumer<Advancement> consumer) {
        Advancement rootAdvancement = Advancement.Builder.create()
                .display(
                        Items.DIRT,
                        Text.literal("Your First Dirt Block"),
                        Text.literal("Now make a three by three"),
                        new Identifier("textures/gui/advancements/backgrounds/adventure.png"),
                        AdvancementFrame.TASK,
                        true,
                        true,
                        false
                )
                .criterion("got_dirt", InventoryChangedCriterion.Conditions.items(Items.DIRT))
                .build(consumer, "grenzzeichzen" + "/root");
    }
}