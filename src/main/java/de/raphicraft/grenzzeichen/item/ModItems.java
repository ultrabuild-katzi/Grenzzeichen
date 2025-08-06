package de.raphicraft.grenzzeichen.item;

import de.raphicraft.grenzzeichen.Grenzzeichen;
import de.raphicraft.grenzzeichen.block.ModBlocks;
import de.raphicraft.grenzzeichen.item.custom.*;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {

    public static final Item RUBY = registerItem("ruby", new Item(new Item.Settings()));
    public static final Item ACHSE = registerItem("achse", new Item(new Item.Settings()));
    public static final Item EMOTOR = registerItem("emotor", new Item(new Item.Settings()));
    public static final Item DMOTOR = registerItem("dmotor", new Item(new Item.Settings()));
    public static final Item DREHGESTELL = registerItem("drehgestell", new Item(new Item.Settings()));
    public static final Item LEDLAMPE = registerItem("ledlampe", new Item(new Item.Settings()));
    public static final Item LEDLAMPEROT = registerItem("ledlamperot", new Item(new Item.Settings()));
    public static final Item LEDLAMPEWEISS = registerItem("ledlampeweiss", new Item(new Item.Settings()));
    public static final Item LEDLAMPEGRUEN = registerItem("ledlampegruen", new Item(new Item.Settings()));
    public static final Item TRAINWHEEL = registerItem("trainwheel", new Item(new Item.Settings()));
    public static final Item RAW_BLACK_IRON = registerItem("raw_black_iron", new Item(new Item.Settings()));
    public static final Item BLACK_IRON_CRYSTAL = registerItem("black_iron_crystal", new Item(new Item.Settings()));
    public static final Item GRAYIRON = registerItem("grayiron", new Item(new Item.Settings()));
    public static final Item MICROCONTROLER = registerItem("microcontroler", new Item(new Item.Settings()));
    public static final Item RITZEL = registerItem("ritzel", new Item(new Item.Settings()));
    public static final Item GRUNDPLATTE = registerItem("grundplatte", new Item(new Item.Settings()));
    public static final Item EMPFAENGER = registerItem("empfaenger", new Item(new Item.Settings()));
    public static final Item DIESELTANK = registerItem("dieseltank", new Item(new Item.Settings()));
    public static final Item KOLBEN = registerItem("kolben", new Item(new Item.Settings()));
    public static final Item CPU = registerItem("cpu", new Item(new Item.Settings()));
    public static final Item CHIPPLATTE = registerItem("chipplatte", new Item(new Item.Settings()));
    public static final Item CAPACITOR = registerItem("capacitor", new Item(new Item.Settings()));
    public static final Item FUEHRERSTANDSKELETT = registerItem("fuehrerstandskelett", new Item(new Item.Settings()));
    public static final Item PANTOGRAPH = registerItem("pantograph", new Item(new Item.Settings()));
    public static final Item INTERIOR = registerItem("interior", new Item(new Item.Settings()));
    public static final Item DECALPLATE = registerItem("decalplate", new Item(new Item.Settings()));
    public static final Item DECAL_P = registerItem("decal_p", new Item(new Item.Settings()));
    public static final Item DECAL_DB = registerItem("decal_db", new Item(new Item.Settings()));
    public static final Item SBRUECKE_KOPF = registerItem("sbruecke_kopf", new Item(new Item.Settings()));
    public static final Item SBRUECKE_CATWALK = registerItem("sbruecke_catwalk", new Item(new Item.Settings()));
    public static final Item SBRUECKE_STAND = registerItem("sbruecke_stand", new Item(new Item.Settings()));
    public static final Item SCHIENE = registerItem("schiene", new Item(new Item.Settings()));
    public static final Item SCHWELLE = registerItem("schwelle", new Item(new Item.Settings()));
    public static final Item EISENSTANGE = registerItem("eisenstange", new Item(new Item.Settings()));

    public static final Item TRAINCAP = registerItem("traincap", new TrainCapItem(new Item.Settings()));
    public static final Item TRACK_PLOUGHITEM = registerItem("track_plough", new TrackploughItem(ModBlocks.TRACK_PLOUGH, new Item.Settings()));


    public static final Item BLACK_IRON_PICKAXE = registerItem("black_iron_pickaxe", new PickaxeItem(ToolMaterials.IRON,1,-2.8f,new Item.Settings()));
    public static final Item BLACK_IRON_AXE = registerItem("black_iron_axe", new AxeItem(ToolMaterials.IRON,6,-3.1f,new Item.Settings()));
    public static final Item BLACK_IRON_SHOVEL = registerItem("black_iron_shovel", new ShovelItem(ToolMaterials.IRON,1.5f,-3.0f,new Item.Settings()));
    public static final Item BLACK_IRON_HOE = registerItem("black_iron_hoe", new HoeItem(ToolMaterials.IRON,-2,-1.0f,new Item.Settings()));
    public static final Item BLACK_IRON_SWORD = registerItem("black_iron_sword", new HoeItem(ToolMaterials.IRON,3,-2.4f,new Item.Settings()));


                                         // Animated this part is only Animated

//    public static final Item ANIMATED_BLOCK_ITEM = registerItem("orby",
//            new OrbyItem(ModBlocks.ORBY, new Item.Settings()));
    public static final Item HAUPTSIGNALBRUECKE_ANIMATED_ITEM = registerItem("hauptsignalbruecke",
            new hauptsignalbrueckeItem(ModBlocks.HAUPTSIGNALBRUECKE, new Item.Settings()));
    public static final Item HAUPTSIGNALBLOCK_ANIMATED_ITEM = registerItem("hauptsignalblock",
            new HauptsignalblockItem(ModBlocks.HAUPTSIGNALBLOCK, new Item.Settings()));
    public static final Item VORSIGNAL_ANIMATED_ITEM = registerItem("vorsignal",
            new VorsignalItem(ModBlocks.VORSIGNAL, new Item.Settings()));

                                                   // The end of Animated



    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(Grenzzeichen.MOD_ID, name), item);
    }



    public static void registerModItems() {
        Grenzzeichen.LOGGER.info("Registering Mod Items for " + Grenzzeichen.MOD_ID);

    }
}
