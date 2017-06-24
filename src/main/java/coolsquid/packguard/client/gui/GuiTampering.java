package coolsquid.packguard.client.gui;

import coolsquid.packguard.util.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiTampering extends GuiScreen {

	@Override
	public void initGui() {
		GuiButton button = new GuiButton(1, 0, 0, "I understand, let me continue");
		button.x = this.width / 2 - button.width / 2;
		button.y = this.height / 2 + this.height / 3;
		this.buttonList.add(button);
	}

	@Override
	public void drawScreen(int mouseRelX, int mouseRelY, float tickTime) {
		GlStateManager.disableTexture2D();
		GlStateManager.enableTexture2D();
		this.drawDefaultBackground();
		super.drawScreen(mouseRelX, mouseRelY, tickTime);
		this.drawCenteredString(this.fontRenderer, "The pack has been tampered with. Do not report any errors.",
				this.width / 2, this.height / 2 - 80, 0xFF0000);
		int a = 40;
		for (String line : Util.getWarningSummary(false).split(System.lineSeparator())) {
			this.drawCenteredString(this.fontRenderer, line, this.width / 2, this.height / 2 - a, 0xFFFF00);
			a -= 30;
		}
	}

	@Override
	protected void actionPerformed(GuiButton button) {
		Minecraft.getMinecraft().displayGuiScreen(null);
	}
}