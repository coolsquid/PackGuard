package coolsquid.packguard;

import coolsquid.packguard.client.gui.GuiTampering;
import coolsquid.packguard.config.ConfigManager;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModEventHandler {

	private static boolean shownGui;

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onGuiOpen(GuiOpenEvent event) {
		if (ConfigManager.guiWarning && (!ConfigManager.showGuiOnce || !ConfigManager.hasShownGui) && !shownGui
				&& event.getGui() instanceof GuiMainMenu) {
			event.setGui(new GuiTampering());
			shownGui = true;
			if (ConfigManager.showGuiOnce) {
				ConfigManager.CONFIG.getCategory("general").get("hasShownGui").set(true);
				ConfigManager.CONFIG.save();
				ConfigManager.hasShownGui = true;
			}
		}
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onPlayerLogin(PlayerLoggedInEvent event) {
		if (ConfigManager.chatWarning && !PackGuard.WARNINGS.isEmpty()) {
			event.player.sendMessage(
					new TextComponentString("<PackGuard> The pack has been tampered with. Do not report any errors.")
							.setStyle(new Style().setColor(TextFormatting.BLUE)));
			if (ConfigManager.sendChatOnce) {
				ConfigManager.CONFIG.getCategory("general").get("chatWarning").set(false);
				ConfigManager.CONFIG.save();
				ConfigManager.chatWarning = false;
			}
		}
	}
}