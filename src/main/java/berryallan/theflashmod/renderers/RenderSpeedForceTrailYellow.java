package berryallan.theflashmod.renderers;

import berryallan.theflashmod.entities.EntitySpeedForceTrailBase;
import berryallan.theflashmod.lib.RefStrings;
import berryallan.theflashmod.models.ModelSpeedForceTrail;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderSpeedForceTrailYellow extends RenderLiving<EntitySpeedForceTrailBase>
{
	protected ResourceLocation golemTexture = new ResourceLocation(
			RefStrings.MODID + ":textures/entity/SpeedForceTrailYellow.png");

	public RenderSpeedForceTrailYellow(RenderManager renderManagerIn)
	{
		super(renderManagerIn, new ModelSpeedForceTrail(), 0.0f);
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

