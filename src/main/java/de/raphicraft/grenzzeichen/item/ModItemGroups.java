package de.raphicraft.grenzzeichen.item;

import de.raphicraft.grenzzeichen.Grenzzeichen;
import de.raphicraft.grenzzeichen.block.ModBlocks;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItemGroups {

    public static final ItemGroup HEST = Registry.register(Registries.ITEM_GROUP,
            Identifier.of(Grenzzeichen.MOD_ID,"luly"),
            FabricItemGroup.builder()
                    .displayName(Text.translatable("itemgroup.luly"))
                    .icon(() -> new ItemStack(ModBlocks.WHITESTONE))
                    .entries((displayContext, entries) -> {
                        entries.add(ModBlocks.WHITESTONE);
                        entries.add(ModBlocks.BLACKSTONE);
                        entries.add(ModBlocks.REDSTONE);
                        entries.add(ModBlocks.BLACK_IRON_ORE);
                        entries.add(ModBlocks.DEEPSLATE_BLACK_IRON_ORE);
                        entries.add(ModBlocks.GRAY_IRON_BLOCK);
                        entries.add(ModBlocks.FAHRDIENSTLEITERKASTEN);
                        entries.add(ModBlocks.FUEHRERSTAND);
                        entries.add(ModBlocks.DB_SIGN);
                        entries.add(ModBlocks.DVB);
                        entries.add(ModBlocks.HAZARDBLOCK);
                        entries.add(ModItems.TRACK_PLOUGHITEM);
                        //Items
                        entries.add(ModItems.LEDLAMPE);
                        entries.add(ModItems.LEDLAMPEROT);
                        entries.add(ModItems.LEDLAMPEWEISS);
                        entries.add(ModItems.LEDLAMPEGRUEN);
                        entries.add(ModItems.FUEHRERSTANDSKELETT);
                        entries.add(ModItems.DECALPLATE);
                        entries.add(ModItems.DECAL_P);
                        entries.add(ModItems.DECAL_DB);
                        entries.add(ModItems.SBRUECKE_KOPF);
                        entries.add(ModItems.SBRUECKE_CATWALK);
                        entries.add(ModItems.SBRUECKE_STAND);
                        //Tools
                        entries.add(ModItems.BLACK_IRON_PICKAXE);
                        entries.add(ModItems.BLACK_IRON_AXE);
                        entries.add(ModItems.BLACK_IRON_SHOVEL);
                        entries.add(ModItems.BLACK_IRON_HOE);
                        entries.add(ModItems.BLACK_IRON_SWORD);

                    }).build());

    public static final ItemGroup KEST = Registry.register(Registries.ITEM_GROUP,
            Identifier.of(Grenzzeichen.MOD_ID,"dudy"),
            FabricItemGroup.builder()
                    .displayName(Text.translatable("itemgroup.dudy"))
                    .icon(() -> new ItemStack(ModBlocks.V100DB))
                    .entries((displayContext, entries) -> {
                       //Loks
                        entries.add(ModBlocks.V100DB);
                        entries.add(ModBlocks.V100DBROT);
                        entries.add(ModBlocks.V200DB);
                        entries.add(ModBlocks.DBBRE94);
                        entries.add(ModBlocks.UICYWAGENGRUEN);
                        entries.add(ModBlocks.UICYWAGENGRUEN2);
                        entries.add(ModBlocks.UICYWAGENROT);
                        entries.add(ModBlocks.DBEAOS);
                        entries.add(ModBlocks.ICE1LOKF);
                        entries.add(ModBlocks.ICE1LOKB);
                        entries.add(ModBlocks.ICE1WAGEN);
                        entries.add(ModBlocks.ICE1BISTRO);
                        //Gleise
                        entries.add(ModBlocks.RAILSLOP121);
                        entries.add(ModBlocks.RAILSLOP122);
                        entries.add(ModBlocks.RAILSLOP123);
                        entries.add(ModBlocks.RAILSLOP124);
                        entries.add(ModBlocks.GLEIS5ZOLL);
                        //Items
                        entries.add(ModItems.ACHSE);
                        entries.add(ModItems.EMOTOR);
                        entries.add(ModItems.DMOTOR);
                        entries.add(ModItems.DREHGESTELL);
                        entries.add(ModItems.TRAINWHEEL);
                        entries.add(ModItems.GRAYIRON);
                        entries.add(ModItems.MICROCONTROLER);
                        entries.add(ModItems.CPU);
                        entries.add(ModItems.CHIPPLATTE);
                        entries.add(ModItems.CAPACITOR);
                        entries.add(ModItems.RITZEL);
                        entries.add(ModItems.GRUNDPLATTE);
                        entries.add(ModItems.EMPFAENGER);
                        entries.add(ModItems.DIESELTANK);
                        entries.add(ModItems.KOLBEN);
                        entries.add(ModItems.PANTOGRAPH);
                        entries.add(ModItems.INTERIOR);
                        entries.add(ModItems.SCHIENE);
                        entries.add(ModItems.SCHWELLE);
                        entries.add(ModItems.EISENSTANGE);

                    }).build());

    public static final ItemGroup UEST = Registry.register(Registries.ITEM_GROUP,
            Identifier.of(Grenzzeichen.MOD_ID,"uuuy"),
            FabricItemGroup.builder()
                    .displayName(Text.translatable("itemgroup.uuuy"))
                    .icon(() -> new ItemStack(ModBlocks.WEICHENSIGNAL))
                    .entries((displayContext, entries) -> {
                        entries.add(ModBlocks.HAUPTSIGNALBRUECKE);
                        entries.add(ModBlocks.APPROACHSIGNAL);
                        entries.add(ModBlocks.WEICHENSIGNAL);
                        entries.add(ModBlocks.ZS3FUSS);
                        entries.add(ModBlocks.ZS3HALTER);
                        entries.add(ModBlocks.ZS3WANDH);
                        entries.add(ModBlocks.ZS3VERLAENGERUNG);
                        entries.add(ModBlocks.RA10);
                        entries.add(ModBlocks.RA10LEER);
                        entries.add(ModBlocks.GRENZZEICHEN);
                        entries.add(ModBlocks.OBERLEITUNGSENDESCHILD);
                        entries.add(ModBlocks.STROMWECHSELSTART);
                        entries.add(ModBlocks.STROMWECHSELENDE);
                        entries.add(ModBlocks.PFEIFTAFEL);
                        entries.add(ModBlocks.DOPPELTEPFEIFTAFEL);


                        //ZS3 Signale
                        entries.add(ModBlocks.ZS3_10);
                        entries.add(ModBlocks.ZS3_20);
                        entries.add(ModBlocks.ZS3_30);
                        entries.add(ModBlocks.ZS3_40);
                        entries.add(ModBlocks.ZS3_50);
                        entries.add(ModBlocks.ZS3_60);
                        entries.add(ModBlocks.ZS3_70);
                        entries.add(ModBlocks.ZS3_80);
                        entries.add(ModBlocks.ZS3_90);
                        entries.add(ModBlocks.ZS3_100);
                        entries.add(ModBlocks.ZS3_110);
                        entries.add(ModBlocks.ZS3_120);
                        entries.add(ModBlocks.ZS3_130);
                        entries.add(ModBlocks.ZS3_140);
                        entries.add(ModBlocks.ZS3_150);
                        entries.add(ModBlocks.ZS3_160);

                        //ZS3 Vorsignale
                        entries.add(ModBlocks.ZS3V);

                    }).build());



    public static void registerItemGroups() {
        Grenzzeichen.LOGGER.info("Registering Item Groups for " + Grenzzeichen.MOD_ID);
    }
}

