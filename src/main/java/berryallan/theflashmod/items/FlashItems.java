// Autogenerated, do not edit. All changes will be undone.

package berryallan.theflashmod.items;

import berryallan.theflashmod.handlers.CreativeTabHandler;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class FlashItems
{
	public static int flashDurability = 1000000000;
	
	public static ItemArmor.ArmorMaterial enumArmorMaterialFlash = EnumHelper
			.addArmorMaterial("FLASH", "theflashmod", flashDurability, new int[] { 0, 0, 0, 0 }, 0, null);

	@Mod.Metadata public static ModMetadata meta;

	public static Item helmetFlash;
	public static Item chestPlateFlash;
	public static Item legsFlash;
	public static Item bootsFlash;
	public static Item helmetFutureFlash;
	public static Item chestPlateFutureFlash;
	public static Item legsFutureFlash;
	public static Item bootsFutureFlash;

	public static Item flashRing;
	public static Item futureFlashRing;

	public static Item barryAllenLabSpawner;
	public static Item cosmicTreadmillSpawner;

	public static Item flashHand;
	public static Item futureFlashHand;

	public static void mainRegistry()
	{
		initItems();
		registerItems();
	}

	public static void initItems()
	{
		//RenderingRegistry.addNewArmourRendererPrefix("5");

		flashRing = new ItemFlashRing("flashRing").setUnlocalizedName("flashRing");
		futureFlashRing = new ItemFutureFlashRing("futureFlashRing").setUnlocalizedName("futureFlashRing");

		barryAllenLabSpawner = new ItemBarryAllenLabSpawner("barryAllenLabSpawner")
				.setUnlocalizedName("barryAllenLabSpawner");
		cosmicTreadmillSpawner = new ItemCosmicTreadmillSpawner("cosmicTreadmillSpawner")
				.setUnlocalizedName("cosmicTreadmillSpawner");

		flashHand = new FlashHand("flashHand").setUnlocalizedName("flashHand");
		futureFlashHand = new FutureFlashHand("futureFlashHand").setUnlocalizedName("futureFlashHand");

		helmetFlash = new FlashArmor(enumArmorMaterialFlash, 5, EntityEquipmentSlot.HEAD)
				.setUnlocalizedName("helmetFlash").setCreativeTab(CreativeTabHandler.tabFlash);
		chestPlateFlash = new FlashArmor(enumArmorMaterialFlash, 5, EntityEquipmentSlot.CHEST)
				.setUnlocalizedName("chestFlash").setCreativeTab(CreativeTabHandler.tabFlash);
		legsFlash = new FlashArmor(enumArmorMaterialFlash, 5, EntityEquipmentSlot.LEGS).setUnlocalizedName("legsFlash")
				.setCreativeTab(CreativeTabHandler.tabFlash);
		bootsFlash = new FlashArmor(enumArmorMaterialFlash, 5, EntityEquipmentSlot.FEET)
				.setUnlocalizedName("bootsFlash").setCreativeTab(CreativeTabHandler.tabFlash);

		helmetFutureFlash = new FutureFlashArmor(enumArmorMaterialFlash, 0, EntityEquipmentSlot.HEAD)
				.setUnlocalizedName("helmetFutureFlash").setCreativeTab(CreativeTabHandler.tabFlash);
		chestPlateFutureFlash = new FutureFlashArmor(enumArmorMaterialFlash, 0, EntityEquipmentSlot.CHEST)
				.setUnlocalizedName("chestFutureFlash").setCreativeTab(CreativeTabHandler.tabFlash);
		legsFutureFlash = new FutureFlashArmor(enumArmorMaterialFlash, 0, EntityEquipmentSlot.LEGS)
				.setUnlocalizedName("legsFutureFlash").setCreativeTab(CreativeTabHandler.tabFlash);
		bootsFutureFlash = new FutureFlashArmor(enumArmorMaterialFlash, 0, EntityEquipmentSlot.FEET)
				.setUnlocalizedName("bootsFutureFlash").setCreativeTab(CreativeTabHandler.tabFlash);
	}
	
	public static void registerItems()
	{
		GameRegistry.registerItem(flashRing, flashRing.getUnlocalizedName().substring(5));
		GameRegistry.registerItem(futureFlashRing, futureFlashRing.getUnlocalizedName().substring(5));

		GameRegistry.registerItem(flashHand, flashHand.getUnlocalizedName().substring(5));
		GameRegistry.registerItem(futureFlashHand, futureFlashHand.getUnlocalizedName().substring(5));

		GameRegistry.registerItem(barryAllenLabSpawner, barryAllenLabSpawner.getUnlocalizedName().substring(5));
		GameRegistry.registerItem(cosmicTreadmillSpawner, cosmicTreadmillSpawner.getUnlocalizedName().substring(5));

		GameRegistry.registerItem(helmetFlash, helmetFlash.getUnlocalizedName().substring(5));
		GameRegistry.registerItem(chestPlateFlash, chestPlateFlash.getUnlocalizedName().substring(5));
		GameRegistry.registerItem(legsFlash, legsFlash.getUnlocalizedName().substring(5));
		GameRegistry.registerItem(bootsFlash, bootsFlash.getUnlocalizedName().substring(5));

		GameRegistry.registerItem(helmetFutureFlash, helmetFutureFlash.getUnlocalizedName().substring(5));
		GameRegistry.registerItem(chestPlateFutureFlash, chestPlateFutureFlash.getUnlocalizedName().substring(5));
		GameRegistry.registerItem(legsFutureFlash, legsFutureFlash.getUnlocalizedName().substring(5));
		GameRegistry.registerItem(bootsFutureFlash, bootsFutureFlash.getUnlocalizedName().substring(5));
	}
}
