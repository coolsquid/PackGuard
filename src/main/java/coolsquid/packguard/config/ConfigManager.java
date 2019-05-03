package coolsquid.packguard.config;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.versioning.VersionParser;
import net.minecraftforge.fml.common.versioning.VersionRange;
import coolsquid.packguard.util.ModData;
import coolsquid.packguard.util.Util;

public class ConfigManager {

	public static final File CONFIG_FILE = new File("./config/PackGuard.cfg");
	public static final Configuration CONFIG = new Configuration(CONFIG_FILE);

	public static Map<String, ModData> expectedMods;
	public static String expectedChecksum;
	public static boolean hasShownGui;
	public static boolean hasSentChat;

	public static boolean validateChecksum;
	public static String[] checksumPaths;
	public static boolean detectVersionChanges;
	public static boolean crashReportWarning;
	public static boolean logWarning;
	public static boolean chatWarning;
	public static boolean sendChatOnce;
	public static boolean guiWarning;
	public static boolean showGuiOnce;
	public static boolean crashReportIntactNotice;
	public static boolean logIntactNotice;

	public static boolean developmentMode;

	public static void loadConfig() throws IOException {
		CONFIG.load();
		expectedMods = new HashMap<>();
		boolean hasExpectedMods = CONFIG.hasKey("mods", "expectedMods");
		String[] expectedModsStrings = CONFIG.getStringList("expectedMods", "mods", new String[0],
				"Mods that are required for the pack author to accept a bug report. Format: modid;versionrange, for example packguard[1.0.0]. Delete to automatically update to the current mod list on next launch. Can also be autogenerated with the command \"/packguard resetmods\", if development mode is enabled.");
		if (!hasExpectedMods) {
			expectedModsStrings = getMods();
			CONFIG.getCategory("mods").get("expectedMods").set(expectedModsStrings);
		}
		for (String s : expectedModsStrings) {
			String[] a = s.trim().split(";");
			if (a.length == 2) {
				expectedMods.put(a[0], new ModData(a[0], a[1], false));
			} else if (a.length == 1) {
				expectedMods.put(a[0], new ModData(a[0], "(0,)", false));
			} else {
				throw new RuntimeException();
			}
		}
		for (String s : CONFIG.getStringList("optionalMods", "mods", new String[0],
				"Mods that may be added without triggering the warnings. Format: modid;versionrange, for example packguard[1.0.0]")) {
			String[] a = s.trim().split(";");
			if (a.length == 2) {
				expectedMods.put(a[0], new ModData(a[0], a[1], true));
			} else if (a.length == 1) {
				expectedMods.put(a[0], new ModData(a[0], "(0,)", true));
			} else {
				throw new RuntimeException();
			}
		}

		validateChecksum = CONFIG.getBoolean("validateChecksum", "checksum", false,
				"Whether to validate the CRC32 checksum of the Minecraft instance as well as the mod list.");
		checksumPaths = CONFIG.getStringList("checksumPaths", "checksum",
				new String[] { "config", "scripts", "resources" },
				"The directories and files to include in the checksum. Run the command \"/packguard resetchecksum\" in development mode after changing this, or delete the \"expectedChecksum\" config entry and launch the game.");
		expectedChecksum = CONFIG.getString("expectedChecksum", "internal", "",
				"Do not edit this manually unless instructed otherwise.");
		Property expectedChecksumProp = CONFIG.getCategory("internal").get("expectedChecksum");
		if (expectedChecksumProp.getString().isEmpty()) {
			expectedChecksum = Util.calcChecksum();
			expectedChecksumProp.set(expectedChecksum);
		}

		hasShownGui = CONFIG.getBoolean("hasShownGui", "internal", false, "Do not edit this manually.");
		hasSentChat = CONFIG.getBoolean("hasSentChat", "internal", false, "Do not edit this manually.");

		detectVersionChanges = CONFIG.getBoolean("detectVersionChanges", "general", true,
				"Whether to look for version mismatches or not");
		crashReportWarning = CONFIG.getBoolean("crashReportWarning", "general", true,
				"Whether to add tampering warnings to crash reports or not.");
		logWarning = CONFIG.getBoolean("logWarning", "general", true,
				"Whether to add tampering warnings to the log or not.");
		chatWarning = CONFIG.getBoolean("chatWarning", "general", false,
				"Whether to send warnings through chat or not.");
		sendChatOnce = CONFIG.getBoolean("sendChatOnce", "general", true,
				"Whether to send the chat warning once only.");
		guiWarning = CONFIG.getBoolean("guiWarning", "general", false, "Whether to use the warning GUI or not.");
		showGuiOnce = CONFIG.getBoolean("showGuiOnce", "general", true,
				"Whether to display the warning GUI once only.");
		crashReportIntactNotice = CONFIG.getBoolean("crashReportIntactNotice", "general", true,
				"Whether to add a notice to crash reports when the pack is intact.");
		logIntactNotice = CONFIG.getBoolean("logIntactNotice", "general", true,
				"Whether to add a notice to the log when the pack is intact.");
		developmentMode = CONFIG.getBoolean("developmentMode", "general", true,
				"Allows you to use the \"/packguard\" command. Disable this before shipping the pack.");
		if (CONFIG.hasChanged()) {
			CONFIG.save();
		}
	}

	public static String[] getMods() {
		String[] mods = new String[Loader.instance().getModList().size()];
		for (int i = 0; i < Loader.instance().getModList().size(); i++) {
			ModContainer mod = Loader.instance().getModList().get(i);
			mods[i] = mod.getModId() + ";[" + mod.getVersion() + ']';
		}
		return mods;
	}
}