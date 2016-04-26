package berryallan.theflashmod.handlers;

import berryallan.theflashmod.items.FlashItems;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class CraftingHandler
{
	
	public static void mainRegistry()
	{
		addCraftingRecipies();
	}
	
	public static void addCraftingRecipies()
	{
		// shapeless
		GameRegistry.addShapelessRecipe(new ItemStack(FlashItems.barryAllenLabSpawner), Blocks.diamond_block,
				Blocks.light_weighted_pressure_plate, Blocks.brick_block, Blocks.glass, Blocks.planks, Blocks.bookshelf,
				Items.brewing_stand, Items.glowstone_dust, Items.bed);
	}
}
