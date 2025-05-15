package de.raphicraft.grenzzeichen.screen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.item.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

@Environment(EnvType.CLIENT)
public class TestScreen extends Screen {
    public TestScreen() {
        super(Text.literal("My Test screen"));
    }

    public ButtonWidget button1;
    public ButtonWidget button2;

    @Override
    protected void init() {
        button1 = ButtonWidget.builder(Text.literal("Add Diamond"), button -> {
                    if (client != null && client.player != null) {
                        client.player.getInventory().insertStack(new ItemStack(Items.DIAMOND, 1));
                        client.setScreen(null);
                    }
                })
                .dimensions(width / 2 - 205, 20, 200, 20)
                .tooltip(Tooltip.of(Text.literal("Get 1 Diamond")))
                .build();

        button2 = ButtonWidget.builder(Text.literal("Add Emerald"), button -> {
                    if (client != null && client.player != null) {
                        client.player.getInventory().insertStack(new ItemStack(Items.EMERALD, 1));
                        client.setScreen(null);
                    }
                })
                .dimensions(width / 2 + 5, 20, 200, 20)
                .tooltip(Tooltip.of(Text.literal("Get 1 Emerald")))
                .build();

        addDrawableChild(button1);
        addDrawableChild(button2);
    }
}