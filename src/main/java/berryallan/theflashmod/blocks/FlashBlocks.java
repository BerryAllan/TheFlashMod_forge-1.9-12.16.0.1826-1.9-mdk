package berryallan.theflashmod.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class FlashBlocks
{
	public static Block speedForceOre;

	public static void mainRegistry()
	{
		initBlocks();
		registerBlocks();
	}

	public static void initBlocks()
	{
		speedForceOre = new SpeedForceOre(Material.barrier).setHardness(1000000000.0F)
				.setUnlocalizedName("speedForceOre");
	}
	
	public static void registerBlocks()
	{
		GameRegistry.registerBlock(speedForceOre, speedForceOre.getUnlocalizedName());
	}
}
