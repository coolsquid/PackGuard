package coolsquid.packguard;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import coolsquid.packguard.config.ConfigManager;
import coolsquid.packguard.util.CommandPackGuard;
import coolsquid.packguard.util.IntactNoticeCrashCallable;
import coolsquid.packguard.util.ModData;
import coolsquid.packguard.util.Util;
import coolsquid.packguard.util.WarningCrashCallable;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = PackGuard.MODID, name = PackGuard.NAME, version = PackGuard.VERSION, updateJSON = PackGuard.UPDATE_JSON)
public class PackGuard {

	public static final String MODID = "packguard";
	public static final String NAME = "PackGuard";
	public static final String VERSION = "2.3.3";
	public static final String UPDATE_JSON = "https://coolsquid.me/api/version/packguard.json";

	public static final Logger LOGGER = LogManager.getFormatterLogger("PackGuard");

	public static final List<String> WARNINGS = new ArrayList<>();
	public static String checksumWarning = null;

	@Mod.EventHandler
	public void onInit(FMLInitializationEvent event) throws Throwable {
		ConfigManager.loadConfig();
		if (ConfigManager.validateChecksum) {
			String checksum = Util.calcChecksum();
			if (!checksum.equals(ConfigManager.expectedChecksum)) {
				checksumWarning = "Checksum mismatch: expected " + ConfigManager.expectedChecksum + ", got " + checksum;
			}
		}
		for (ModContainer loadedMod : Loader.instance().getModList()) {
			ModData mod = ConfigManager.expectedMods.get(loadedMod.getModId());
			if (mod == null) {
				WARNINGS.add("Added mod: " + loadedMod.getModId() + " version " + loadedMod.getVersion());
			}
		}
		for (ModData mod : ConfigManager.expectedMods.values()) {
			ModContainer loadedMod = Loader.instance().getIndexedModList().get(mod.id);
			if (loadedMod == null) {
				if (!mod.optional) {
					WARNINGS.add("Missing mod: " + mod.id + " version " + mod.textVersion);
				}
			} else if (ConfigManager.detectVersionChanges && !mod.version.containsVersion(loadedMod.getProcessedVersion())) {
				WARNINGS.add("Version mismatch: expected " + mod.id + " version " + mod.textVersion + ", but version "
						+ loadedMod.getVersion() + " was loaded");
			}
		}
		WARNINGS.sort((s1, s2) -> s1.compareTo(s2));
		if (!WARNINGS.isEmpty() || checksumWarning != null) {
			if (ConfigManager.crashReportWarning) {
				FMLCommonHandler.instance().registerCrashCallable(new WarningCrashCallable());
			}
			if (ConfigManager.logWarning) {
				LOGGER.warn("%s\n%s", "Tampering discovered: ", Util.getWarningSummary(true, System.lineSeparator()));
			}
			if (!WARNINGS.isEmpty() && ConfigManager.guiWarning || ConfigManager.chatWarning) {
				MinecraftForge.EVENT_BUS.register(new ModEventHandler());
			}
		} else {
			if (ConfigManager.crashReportIntactNotice) {
				FMLCommonHandler.instance().registerCrashCallable(new IntactNoticeCrashCallable());
			}
			if (ConfigManager.logIntactNotice) {
				LOGGER.info("No tampering discovered.");
			}
		}
		if (FMLCommonHandler.instance().getSide() == Side.CLIENT && ConfigManager.developmentMode) {
			ClientCommandHandler.instance.registerCommand(new CommandPackGuard());
		}
	}

	@EventHandler
	public static void onServerStarting(FMLServerStartingEvent event) {
		if (ConfigManager.developmentMode && FMLCommonHandler.instance().getSide() == Side.SERVER) {
			event.registerServerCommand(new CommandPackGuard());
		}
	}
}
