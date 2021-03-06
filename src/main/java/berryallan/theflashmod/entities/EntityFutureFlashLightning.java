package berryallan.theflashmod.entities;

import berryallan.theflashmod.handlers.SpeedForceHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.world.World;

public class EntityFutureFlashLightning extends EntitySpeedForceTrailBase
{
	public EntityFutureFlashLightning(World world)
	{
		super(world);
		this.posX = Minecraft.getMinecraft().thePlayer.prevPosX;
		this.posY = Minecraft.getMinecraft().thePlayer.prevPosY;
		this.posZ = Minecraft.getMinecraft().thePlayer.prevPosZ;
		this.rotationYaw = Minecraft.getMinecraft().thePlayer.rotationYaw;
		this.rotationPitch = Minecraft.getMinecraft().thePlayer.rotationPitch;

		this.noClip = true;
	}

	@Override protected void entityInit()
	{
		super.entityInit();
	}

	@Override protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();

		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(
				Minecraft.getMinecraft().thePlayer.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED)
						.getBaseValue() * (SpeedForceHandler.flashFactor * 8));
	}

	/*
	@SideOnly(Side.CLIENT)
	public int getBrightnessForRender(float partialTicks)
	{
		return 15728880;
	}

	/**
	 * Gets how bright this entity is.

	public float getBrightness(float partialTicks)
	{
		return 1.0F;
	}
	*/
}
