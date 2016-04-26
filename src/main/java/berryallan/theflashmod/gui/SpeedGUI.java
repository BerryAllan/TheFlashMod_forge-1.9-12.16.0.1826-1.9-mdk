package berryallan.theflashmod.gui;

import berryallan.theflashmod.handlers.SpeedForceHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;

public class SpeedGUI
{
	private static Minecraft mc = Minecraft.getMinecraft();
	
	public static void renderToHud()
	{
		if (mc.inGameHasFocus)
		{
			ScaledResolution res = new ScaledResolution(mc);
			FontRenderer fontRenderer = mc.fontRendererObj;
			
			int width = res.getScaledWidth();
			int height = res.getScaledHeight();
			
			if (SpeedForceHandler.isFlash || SpeedForceHandler.isFutureFlash)
			{
				String FlashFactor = "Flash Factor: ";
				String test = Integer.toString(SpeedForceHandler.flashFactor);
				int x = width / 2 + 175;
				int y = height / 2 + 75;
				int color = 0xffffff;
				fontRenderer.drawStringWithShadow(test, x, y, color);
				fontRenderer.drawStringWithShadow(FlashFactor, x - 75, y, color);

				String SloMoFactor = "Slo-Mo Factor: ";
				String test2 = Float.toString(1.0F - SpeedForceHandler.slowMoFactor);
				fontRenderer.drawStringWithShadow(test2, x, y + 15, color);
				fontRenderer.drawStringWithShadow(SloMoFactor, x - 75, y + 15, color);

				String Speed = "Speed: ";
				String test3 = SpeedForceHandler.speedkmh;
				fontRenderer.drawStringWithShadow(test3, x, y - 15, color);
				fontRenderer.drawStringWithShadow(Speed, x - 75, y - 15, color);
			}

		}
	}
}
