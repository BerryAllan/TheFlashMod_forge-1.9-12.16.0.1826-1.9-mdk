package berryallan.theflashmod.renderers;

import berryallan.theflashmod.entities.EntitySpeedForceTrailBase;
import berryallan.theflashmod.lib.RefStrings;
import berryallan.theflashmod.models.ModelFutureFlashLightning;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderFutureFlashLightning extends RenderLiving<EntitySpeedForceTrailBase>
{
	protected ResourceLocation golemTexture = new ResourceLocation(
			RefStrings.MODID + ":textures/entity/FutureFlashLightning.png");
	
	public RenderFutureFlashLightning(RenderManager renderManagerIn)
	{
		super(renderManagerIn, new ModelFutureFlashLightning(), 0.0f);
	}
	
	public void doRender(EntitySpeedForceTrailBase entitySpeedForceTrailBase, double posX, double posY, double posZ,
			float rotationYaw, float rotationPitch)
	{
		super.doRender(entitySpeedForceTrailBase, posX, posY, posZ, rotationYaw, rotationPitch);
	}
	
	protected ResourceLocation getEntityTexture(EntitySpeedForceTrailBase golem)
	{
		return golemTexture;
	}
}
