package coolsquid.packguard.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import coolsquid.packguard.PackGuard;
import coolsquid.packguard.util.Util;

@SideOnly(Side.CLIENT)
public class GuiTampering extends GuiScreen {

	@Override
	public void initGui() {
		GuiButton button = new GuiButton(1, 0, 0, "I understand, let me continue");
		button.x = this.width / 2 - button.width / 2;
		button.y = this.height / 2 + this.height / 3;
		button.packedFGColour = 10526880;
		this.buttonList.add(button);
	}

	@Override
	public void drawScreen(int mouseRelX, int mouseRelY, float tickTime) {
		this.drawDefaultBackground();
		super.drawScreen(mouseRelX, mouseRelY, tickTime);
		double multiplier = 1D / 240D * this.height;
		int baseHeight = this.height / 2 + 40 - Math.min(PackGuard.WARNINGS.size() * 5, 60);
		this.drawCenteredString(this.fontRenderer, "The modpack has been tampered with.", this.width / 2,
				(int) (baseHeight - 80 * multiplier), 16777215);
		this.drawCenteredString(this.fontRenderer, "Do not report any errors to the modpack author.", this.width / 2,
				(int) (baseHeight - 60 * multiplier), 10526880);
		int a = 40;
		String[] summary = Util.getWarningSummary(false, System.lineSeparator()).split(System.lineSeparator());
		for (int i = 0; i < Math.min(summary.length, 12); i++) {
			this.drawCenteredString(this.fontRenderer, summary[i], this.width / 2, (int) (baseHeight - a * multiplier),
					10526880);
			a -= 10;
		}
		if (summary.length > 12) {
			this.drawCenteredString(this.fontRenderer, "And " + (summary.length - 12) + " more", this.width / 2,
					(int) (baseHeight - a * multiplier), 10526880);
		}
	}

	@Override
	protected void actionPerformed(GuiButton button) {
		Minecraft.getMinecraft().displayGuiScreen(null);
	}
}