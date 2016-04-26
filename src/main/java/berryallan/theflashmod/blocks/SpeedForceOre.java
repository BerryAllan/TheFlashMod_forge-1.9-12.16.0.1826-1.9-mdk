package berryallan.theflashmod.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SpeedForceOre extends Block
{

	public SpeedForceOre(Material mat)
	{
		super(mat);
		this.setHarvestLevel("pickaxe", 3);
	}

	@SideOnly(Side.CLIENT) public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.TRANSLUCENT;
	}

	public boolean getUseNeighborBrightness(IBlockState state)
	{
		return true;
	}
}
