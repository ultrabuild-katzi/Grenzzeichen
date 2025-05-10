package de.raphicraft.grenzzeichen.block;

import de.raphicraft.grenzzeichen.Grenzzeichen;
import de.raphicraft.grenzzeichen.block.custom.Orby;
import de.raphicraft.grenzzeichen.block.settings.*;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlocks {
    public static final Block GRENZZEICHEN = registerWithItem("grenzzeichen", new GrenzzeichenBlock(
            AbstractBlock.Settings.create().strength(1.5F, 6.0F)
                    .requiresTool()));

public static final Block WEICHENSIGNAL = registerWithItem("weichensignal", new WeichensignalBlock(
            AbstractBlock.Settings.create().strength(1.5F, 6.0F)
                    .requiresTool()));

    public static final Block HAUPTSIGNAL = registerWithItem("hauptsignal", new HauptsignalBlock(
            AbstractBlock.Settings.create().strength(1.5F, 6.0F)
                    .requiresTool()));

    public static final Block V100DB = registerWithItem("v100db", new V100db(
            AbstractBlock.Settings.create().strength(1.5F, 6.0F)
                    .requiresTool()));

    public static final Block V100DBROT = registerWithItem("v100dbrot", new V100dbRot(
            AbstractBlock.Settings.create().strength(1.5F, 6.0F)
                    .requiresTool()));

    public static final Block V200DB = registerWithItem("v200db", new V200db(
            AbstractBlock.Settings.create().strength(1.5F, 6.0F)
                    .requiresTool()));

    public static final Block UICYWAGENGRUEN = registerWithItem("uicywagengruen", new UICYWagengruen(
            AbstractBlock.Settings.create().strength(1.5F, 6.0F)
                    .requiresTool()));

    public static final Block UICYWAGENGRUEN2 = registerWithItem("uicywagengruen2", new UICYWagengruen2(
            AbstractBlock.Settings.create().strength(1.5F, 6.0F)
                    .requiresTool()));

    public static final Block UICYWAGENROT = registerWithItem("uicywagenrot", new UICYWagenrot(
            AbstractBlock.Settings.create().strength(1.5F, 6.0F)
                    .requiresTool()));

    public static final Block UICYWAGENADVANCEMENTS = registerWithItem("uicywagenadvancements", new uicywagenadvancements(
            AbstractBlock.Settings.create().strength(1.5F, 6.0F)
                    .requiresTool()));

    public static final Block DBEAOS = registerWithItem("dbeaos", new DBEAOS(
            AbstractBlock.Settings.create().strength(1.5F, 6.0F)
                    .requiresTool()));

    public static final Block DBBRE94 = registerWithItem("dbbre94", new DBBRE94(
            AbstractBlock.Settings.create().strength(1.5F, 6.0F)
                    .requiresTool()));

    public static final Block TRAINSEAT = registerWithItem("trainseat1", new trainseat(
            AbstractBlock.Settings.create().strength(1.5F, 6.0F)
                    .requiresTool()));

    public static final Block RAILSLOP121 = registerWithItem("railslop121", new railslop121(
            AbstractBlock.Settings.create().strength(1.5F, 6.0F)
                    .requiresTool()));

    public static final Block RAILSLOP122 = registerWithItem("railslop122", new railslop122(
            AbstractBlock.Settings.create().strength(1.5F, 6.0F)
                    .requiresTool()));

    public static final Block RAILSLOP123 = registerWithItem("railslop123", new railslop123(
            AbstractBlock.Settings.create().strength(1.5F, 6.0F)
                    .requiresTool()));

    public static final Block RAILSLOP124 = registerWithItem("railslop124", new railslop124(
            AbstractBlock.Settings.create().strength(1.5F, 6.0F)
                    .requiresTool()));

    public static final Block DB_SIGN = registerWithItem("db_sign", new db_sign(
            AbstractBlock.Settings.create().strength(1.5F, 6.0F)
                    .requiresTool()));

    public static final Block OBERLEITUNGSENDESCHILD = registerWithItem("oberleitungsendeschild", new OberleitungsEndeSchild(
            AbstractBlock.Settings.create().strength(1.5F, 6.0F)
                    .requiresTool()));

    public static final Block STROMWECHSELSTART = registerWithItem("stromwechselstart", new StromwechselStart(
            AbstractBlock.Settings.create().strength(1.5F, 6.0F)
                    .requiresTool()));

    public static final Block STROMWECHSELENDE = registerWithItem("stromwechselende", new StromWechselEnde(
            AbstractBlock.Settings.create().strength(1.5F, 6.0F)
                    .requiresTool()));

    public static final Block PFEIFTAFEL = registerWithItem("pfeiftafel", new PFEIFTAFEL(
            AbstractBlock.Settings.create().strength(1.5F, 6.0F)
                    .requiresTool()));


    public static final Block DOPPELTEPFEIFTAFEL = registerWithItem("doppeltepfeiftafel", new DOPPELTEPFEIFTAFEL(
            AbstractBlock.Settings.create().strength(1.5F, 6.0F)
                    .requiresTool()));

    public static final Block FUEHRERSTAND = registerWithItem("fuehrerstand", new FUEHRERSTAND(
            AbstractBlock.Settings.create().strength(1.5F, 6.0F)
                    .requiresTool()));

    public static final Block WHITESTONE = registerWithItem("whitestone", new Block(
            AbstractBlock.Settings.create().strength(1.5F, 6.0F)
                    .requiresTool()));

    public static final Block BLACKSTONE = registerWithItem("blackstone", new Block(
            AbstractBlock.Settings.create().strength(1.5F, 6.0F)
                    .requiresTool()));

    public static final Block REDSTONE = registerWithItem("redstone", new Block(
            AbstractBlock.Settings.create().strength(1.5F, 6.0F)
                    .requiresTool()));

    public static final Block DVB = registerWithItem("dvb", new dvb(
            AbstractBlock.Settings.create().strength(1.5F, 6.0F)
                    .requiresTool()));

    public static final Block ICE1LOKF = registerWithItem("ice1lokf", new ice1lokf(
            AbstractBlock.Settings.create().strength(1.5F, 6.0F)
                    .requiresTool()),new Item.Settings().maxCount(1));

    public static final Block ICE1LOKB = registerWithItem("ice1lokb", new ice1lokb(
            AbstractBlock.Settings.create().strength(1.5F, 6.0F)
                    .requiresTool()),new Item.Settings().maxCount(1));

    public static final Block ICE1WAGEN = registerWithItem("ice1wagen", new ice1wagen(
            AbstractBlock.Settings.create().strength(1.5F, 6.0F)
                    .requiresTool()),new Item.Settings().maxCount(1));

    public static final Block ICE1BISTRO = registerWithItem("ice1bistro", new ice1bistro(
            AbstractBlock.Settings.create().strength(1.5F, 6.0F)
                    .requiresTool()),new Item.Settings().maxCount(1));

    public static final Block ZS3 = registerWithItem("zs3", new zs3(
            AbstractBlock.Settings.create().strength(1.5F, 6.0F)
                    .requiresTool()),new Item.Settings().maxCount(1));

    public static final Block ZS3FUSS = registerWithItem("zs3fuss", new zs3fuss(
            AbstractBlock.Settings.create().strength(1.5F, 6.0F)
                    .requiresTool()),new Item.Settings().maxCount(1));

    public static final Block ZS3HALTER = registerWithItem("zs3halter", new zs3halter(
            AbstractBlock.Settings.create().strength(1.5F, 6.0F)
                    .requiresTool()),new Item.Settings().maxCount(1));

    public static final Block ZS3VERLAENGERUNG = registerWithItem("zs3verlaengerung", new zs3verlaengerung(
            AbstractBlock.Settings.create().strength(1.5F, 6.0F)
                    .requiresTool()),new Item.Settings().maxCount(1));

    public static final Block ZS3WANDH = registerWithItem("zs3wandh", new zs3wandh(
            AbstractBlock.Settings.create().strength(1.5F, 6.0F)
                    .requiresTool()),new Item.Settings().maxCount(1));

    public static final Block ZS3V = registerWithItem("zs3v", new zs3v(
            AbstractBlock.Settings.create().strength(1.5F, 6.0F)
                    .requiresTool()),new Item.Settings().maxCount(1));

    public static final Block GLEIS5ZOLL = registerWithItem("gleis5zoll", new gleis5zoll(
            AbstractBlock.Settings.create().strength(1.5F, 6.0F)
                    .requiresTool().nonOpaque()),new Item.Settings().maxCount(1));

    public static final Block APPROACHSIGNAL = registerWithItem("approachsignal", new approachsignal(
            AbstractBlock.Settings.create().strength(1.5F, 6.0F)
                    .requiresTool()),new Item.Settings().maxCount(1));

    public static final Block BLACK_IRON_ORE = registerWithItem("black_iron_ore",
            new Block(FabricBlockSettings.copyOf(Blocks.STONE).strength(2f)));

    public static final Block DEEPSLATE_BLACK_IRON_ORE = registerWithItem("deepslate_black_iron_ore",
            new Block(FabricBlockSettings.copyOf(Blocks.STONE).strength(2f)));

    public static final Block GRAY_IRON_BLOCK = registerWithItem("gray_iron_block", new Block(
            AbstractBlock.Settings.create().strength(4F, 6.0F)
                    .requiresTool()));



                                   // Animated this part is only Animated

    public static final Block ORBY = Registry.register(Registries.BLOCK, new Identifier(Grenzzeichen.MOD_ID, "orby"),
            new Orby(FabricBlockSettings.copyOf(Blocks.STONE).strength(4.0f).requiresTool().nonOpaque()));


                                       // The end of Animated






    public static <T extends Block> T register(String name, T block) {
        return Registry.register(Registries.BLOCK, Grenzzeichen.MOD_ID(name), block);
    }

    public static <T extends Block> T registerWithItem(String name, T block, Item.Settings settings) {
        T registered = register(name, block);
        Registry.register(Registries.ITEM, Grenzzeichen.MOD_ID(name), new BlockItem(registered, settings));
        return registered;
    }

    public static <T extends Block> T registerWithItem(String name, T block) {
        return registerWithItem(name, block, new Item.Settings());
    }

    public static void registerModBlocks() {
    }



}