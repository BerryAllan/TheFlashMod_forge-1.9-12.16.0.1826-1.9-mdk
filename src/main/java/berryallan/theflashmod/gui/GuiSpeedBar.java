package berryallan.theflashmod.gui;

import berryallan.theflashmod.handlers.SpeedForceHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT) public class GuiSpeedBar extends Gui
{
	private Minecraft mc = Minecraft.getMinecraft();
	
	public GuiSpeedBar()
	{
		super();
	}

	@SubscribeEvent(priority = EventPriority.NORMAL) public void onRenderExperienceBar(
			RenderGameOverlayEvent.Post event)
	{
		if ((SpeedForceHandler.isFlash || SpeedForceHandler.isFutureFlash) && SpeedForceHandler.onCosmicTreadmill
				&& SpeedForceHandler.cosmicTreadmillRunning)
		{
			ScaledResolution res = new ScaledResolution(mc);
			int x = res.getScaledWidth() / 2 - 91;

			this.mc.mcProfiler.startSection("jumpBar");
			this.mc.getTextureManager().bindTexture(Gui.icons);

			int i = 182;
			int j = (int) (SpeedForceHandler.cosmicTreadmillFloat * (float) (i + 1));
			int k = res.getScaledHeight() - 32 + 3;
			this.drawTexturedModalRect(x, k, 0, 84, i, 5);

			if (j > 0)
			{
				this.drawTexturedModalRect(x, k, 0, 89, j, 5);
			}

			this.mc.mcProfiler.endSection();

			ScaledResolution res2 = new ScaledResolution(mc);
			int x2 = res2.getScaledWidth() / 2 - 91;

			this.mc.mcProfiler.startSection("jumpBar");
			this.mc.getTextureManager().bindTexture(new ResourceLocation("textures/gui/bars.png"));

			int i2 = 182;
			int j2 = (int) ((SpeedForceHandler.cosmicTreadmillUpgradeSpeed - 0.01) * (float) (i2 + 1));
			int k2 = res2.getScaledHeight() - 32 + 3;
			this.drawTexturedModalRect(x2, k2 - 6, 0, 40, i2, 5);

			if (j2 > 0)
			{
				this.drawTexturedModalRect(x2, k2 - 6, 0, 45, j2, 5);
			}

			this.mc.mcProfiler.endSection();

			FontRenderer fontRenderer = mc.fontRendererObj;

			int width = res.getScaledWidth();
			int height = res.getScaledHeight();

			int x3 = width / 2;
			int y3 = height - 32 + 3;
			int color = 0xffffff;
			fontRenderer.drawString(SpeedForceHandler.keyToHit, x3, y3 - 15, color);
		}
	}
}
