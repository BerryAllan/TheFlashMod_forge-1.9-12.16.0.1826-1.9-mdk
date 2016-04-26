package berryallan.theflashmod.dimension;

import berryallan.theflashmod.main.MainRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.BiomeProviderSingle;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class WorldProviderSpeedForce extends WorldProvider
{
	/**
	 * creates a new world chunk manager for WorldProvider
	 */
	public void registerWorldChunkManager()
	{
		this.worldChunkMgr = new BiomeProviderSingle(BiomeGenSpeedForce.instance);
		this.isHellWorld = true;
		this.hasNoSky = false;
		this.worldObj.getWorldInfo().setRainTime(0);
		this.worldObj.getWorldInfo().setRaining(false);
		this.worldObj.getWorldInfo().setThunderTime(0);
		this.worldObj.getWorldInfo().setThundering(false);
	}

	/**
	 * Return Vec3D with biome specific fog color
	 */
	@SideOnly(Side.CLIENT) public Vec3d getFogColor(float p_76562_1_, float p_76562_2_)
	{
		return new Vec3d(0.8, 0.8, 0.0);
	}

	@SideOnly(Side.CLIENT) public Vec3d getSkyColor(Entity cameraEntity, float partialTicks)
	{
		return new Vec3d(1.0, 1.0, 0.0);
	}

	public boolean canDoRainSnowIce(net.minecraft.world.chunk.Chunk chunk)
	{
		return false;
	}

	public float getCloudHeight()
	{
		return 4098.0F;
	}

	@SideOnly(Side.CLIENT) public float getStarBrightness(float par1)
	{
		return 0;
	}

	public double getMovementFactor()
	{
		return 64.0D;
	}

	public void setWorldTime(long time)
	{
		this.worldObj.getWorldInfo().setWorldTime(1000);
	}

	@SideOnly(Side.CLIENT) public Vec3d getCloudColor(float partialTicks)
	{
		return new Vec3d(1, 1, 0.0);
	}

	public boolean canDoLightning(Chunk chunk)
	{
		return false;
	}

	public boolean isDaytime()
	{
		return true;
	}

	/**
	 * Creates the light to brightness table
	 */
	protected void generateLightBrightnessTable()
	{
		float f = 0.1F;

		for (int i = 0; i <= 15; ++i)
		{
			float f1 = 1.0F - (float) i / 15.0F;
			this.lightBrightnessTable[i] = (1.0F - f1) / (f1 * 3.0F + 1.0F) * (1.0F - f) + f;
		}
	}

	public IChunkGenerator createChunkGenerator()
	{
		return new ChunkProviderSpeedForce(this.worldObj, this.worldObj.getWorldInfo().isMapFeaturesEnabled(),
				this.worldObj.getSeed());
	}

	/**
	 * Returns 'true' if in the "main surface world", but 'false' if in the Nether or End dimensions.
	 */
	public boolean isSurfaceWorld()
	{
		return false;
	}

	/**
	 * Will check if the x, z position specified is alright to be set as the map spawn point
	 */
	public boolean canCoordinateBeSpawn(int x, int z)
	{
		return false;
	}

	/**
	 * Calculates the angle of sun and moon in the sky relative to a specified time (usually worldTime)
	 */
	public float calculateCelestialAngle(long worldTime, float partialTicks)
	{
		return 0.5F;
	}

	/**
	 * True if the player can respawn in this dimension (true = overworld, false = nether).
	 */
	public boolean canRespawnHere()
	{
		return false;
	}

	/**
	 * Returns true if the given X,Z coordinate should show environmental fog.
	 */
	@SideOnly(Side.CLIENT) public boolean doesXZShowFog(int x, int z)
	{
		return true;
	}

	public WorldBorder getWorldBorder()
	{
		return new WorldBorder()
		{
			public double getCenterX()
			{
				return super.getCenterX() / 8.0D;
			}

			public double getCenterZ()
			{
				return super.getCenterZ() / 8.0D;
			}
		};
	}

	public DimensionType getDimensionType()
	{
		return DimensionType.getById(MainRegistry.dimId);
	}

	public String getWelcomeMessage()
	{
		return "Entering The Speed Force";
	}

	public String getDepartMessage()
	{
		return "Exiting The Speed Force";
	}
}
