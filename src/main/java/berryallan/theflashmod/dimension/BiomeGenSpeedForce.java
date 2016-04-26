package berryallan.theflashmod.dimension;

import net.minecraft.world.biome.BiomeGenBase;

public class BiomeGenSpeedForce extends BiomeGenBase
{
	public static BiomeGenBase instance;

	public BiomeGenSpeedForce(BiomeGenBase.BiomeProperties properties)
	{
		super(properties);
		this.spawnableCaveCreatureList.clear();
		this.spawnableCreatureList.clear();
		this.spawnableMonsterList.clear();
		this.spawnableWaterCreatureList.clear();
	}
}
