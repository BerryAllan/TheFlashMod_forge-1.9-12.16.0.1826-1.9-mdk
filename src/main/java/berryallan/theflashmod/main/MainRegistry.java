/*
If this comment is removed, the program will blow up 
       ,~~.
      (  6 )-_,
 (\___ )=='-'
  \ .   ) )
   \ `-' /
~'`~'`~'`~'`~
*/

package berryallan.theflashmod.main;

import berryallan.theflashmod.blocks.FlashBlocks;
import berryallan.theflashmod.dimension.BiomeGenSpeedForce;
import berryallan.theflashmod.dimension.WorldProviderSpeedForce;
import berryallan.theflashmod.entities.*;
import berryallan.theflashmod.gui.GuiSpeedBar;
import berryallan.theflashmod.gui.SpeedGUIHandler;
import berryallan.theflashmod.handlers.CraftingHandler;
import berryallan.theflashmod.handlers.KeysHandler;
import berryallan.theflashmod.handlers.SpeedForceHandler;
import berryallan.theflashmod.items.FlashItems;
import berryallan.theflashmod.lib.Abilities;
import berryallan.theflashmod.lib.RefStrings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraft.world.DimensionType;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static berryallan.theflashmod.handlers.SpeedForceHandler.mc;

@Mod(modid = RefStrings.MODID, version = RefStrings.VERSION, name = RefStrings.NAME)
public class MainRegistry {
    @SidedProxy(clientSide = "berryallan.theflashmod.main.ClientProxy", serverSide = "berryallan.theflashmod.main.ServerProxy")
    public static ServerProxy proxy;

    @Mod.Metadata
    public static ModMetadata meta;

    @Mod.Instance(RefStrings.MODID)
    public static MainRegistry modInstance;

    public static Potion speedForce;

    public static int dimId = DimensionManager.getNextFreeDimId();
    public static String fileName = "temp.txt";

    public static boolean usable() {
        return Minecraft.getMinecraft().isSingleplayer();
    }

    @Mod.EventHandler
    public void PreLoad(FMLPreInitializationEvent preEvent) {
        proxy.registerRenderThings();

        try {
            // Assume default encoding.
            //File file = new File(fileName);
            //BufferedWriter output = new BufferedWriter(new FileWriter(file));

            FileWriter fileWriter = new FileWriter("saves\\" + MainRegistry.fileName);

            // Always wrap FileWriter in BufferedWriter.
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            // Note that write() does not automatically
            // append a newline character.
            //output.write();

            // Always close files.
            bufferedWriter.close();
        } catch (IOException ex) {
            System.out.println("Error writing to file '" + fileName + "'");
            // Or we could just do this:
            ex.printStackTrace();
        }

        FlashEntities.registerEntity(EntitySpeedForceTrailBlue.class, "speed_force_trail_blue");
        FlashEntities.registerEntity(EntitySpeedForceTrailYellow.class, "speed_force_trail_yellow");
        FlashEntities.registerEntity(EntityFutureFlashLightning.class, "future_flash_lightning");
        FlashEntities.registerEntity(EntitySpeedForceLightningThrowBlue.class, "lightning_throw_blue");
        FlashEntities.registerEntity(EntitySpeedForceLightningThrowYellow.class, "lightning_throw_yellow");

        FlashBlocks.mainRegistry();
        FlashItems.mainRegistry();
        CraftingHandler.mainRegistry();
        KeysHandler.mainRegistry();

        MinecraftForge.EVENT_BUS.register(new SpeedForceHandler());
        MinecraftForge.EVENT_BUS.register(new SpeedGUIHandler());
        MinecraftForge.EVENT_BUS.register(new GuiSpeedBar());
    }

    @Mod.EventHandler
    public void load(FMLInitializationEvent event) {
        BiomeGenSpeedForce.instance = new BiomeGenSpeedForce(
                (new BiomeGenBase.BiomeProperties("The Speed Force")).setTemperature(2.0F).setRainfall(0.0F)
                        .setRainDisabled());
        //BiomeManager
        //		.addBiome(BiomeManager.BiomeType.WARM, new BiomeManager.BiomeEntry(BiomeGenSpeedForce.instance, 10));
        DimensionType.register("The Speed Force", "_the_speed_force", dimId, WorldProviderSpeedForce.class, false);
        DimensionManager.registerDimension(dimId, DimensionType.getById(dimId));

        speedForce = (new Abilities(false, 0).setIconIndex(0, 0)).setPotionName("potion.speedForce");

        proxy.init(event);

        //register renders
        if (event.getSide() == Side.CLIENT) {
            registerBlockRenderers();
            registerItemRenderer(FlashItems.barryAllenLabSpawner);
            registerItemRenderer(FlashItems.cosmicTreadmillSpawner);
            registerItemRenderer(FlashItems.helmetFlash);
            registerItemRenderer(FlashItems.chestPlateFlash);
            registerItemRenderer(FlashItems.legsFlash);
            registerItemRenderer(FlashItems.bootsFlash);
            registerItemRenderer(FlashItems.helmetFutureFlash);
            registerItemRenderer(FlashItems.chestPlateFutureFlash);
            registerItemRenderer(FlashItems.legsFutureFlash);
            registerItemRenderer(FlashItems.bootsFutureFlash);
            registerItemRenderer(FlashItems.flashRing);
            registerItemRenderer(FlashItems.futureFlashRing);
            registerItemRenderer(FlashItems.flashHand);
            registerItemRenderer(FlashItems.futureFlashHand);
        }
    }

    @Mod.EventHandler
    public void PostLoad(FMLPostInitializationEvent postEvent) {

    }

    public void registerItemRenderer(Item parItem) {
        RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();

        renderItem.getItemModelMesher().register(parItem, 0,
                new ModelResourceLocation(RefStrings.MODID + ":" + parItem.getUnlocalizedName().substring(5),
                        "inventory"));
    }

    public void registerBlockRenderers() {
        RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();

        renderItem.getItemModelMesher()
                .register(Item.getItemFromBlock(FlashBlocks.speedForceOre), 0, new ModelResourceLocation("torch"));

    }
}
