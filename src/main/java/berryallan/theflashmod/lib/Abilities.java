package berryallan.theflashmod.lib;

import net.minecraft.client.Minecraft;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class Abilities extends Potion
{
	private static final ResourceLocation texture = new ResourceLocation(
			RefStrings.MODID + ":textures/items/abilityicons.png");
	
	public Abilities(boolean par2, int par3)
	{
		super(par2, par3);
	}

	public Potion setPotionName(String name)
	{
		super.setPotionName(name);
		return this;
	}

	public boolean shouldRender(PotionEffect effect)
	{
		return true;
	}

	/**
	 * Returns true if the potion has a associated status icon to display in then inventory when active.
	 */
	@SideOnly(Side.CLIENT) public boolean hasStatusIcon()
	{
		Minecraft.getMinecraft().renderEngine.bindTexture(texture);
		return true;
	}

	/**
	 * Returns the index for the icon to display when the potion is active.
	 */
	@SideOnly(Side.CLIENT) public int getStatusIconIndex()
	{
		return 1;
	}

	public Potion setIconIndex(int p_76399_1_, int p_76399_2_)
	{
		//Minecraft.getMinecraft().renderEngine.bindTexture(texture);
		super.setIconIndex(p_76399_1_, p_76399_2_);
		return this;
	}
}
