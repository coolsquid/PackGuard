package coolsquid.packguard.util;

import java.util.Collections;
import java.util.List;

import com.google.common.collect.Lists;

import coolsquid.packguard.config.ConfigManager;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public class CommandPackGuard extends CommandBase {

	@Override
	public String getName() {
		return "packguard";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (args.length < 1) {
			sender.sendMessage(new TextComponentString("<PackGuard> Not enough arguments!")
					.setStyle(new Style().setColor(TextFormatting.BLUE)));
		} else if (args[0].equals("resetmods")) {
			ConfigManager.CONFIG.load();
			ConfigManager.CONFIG.getCategory("mods").get("expectedMods").set(ConfigManager.getMods());
			ConfigManager.CONFIG.save();
			sender.sendMessage(new TextComponentString("<PackGuard> Successfully reset the expected mods.")
					.setStyle(new Style().setColor(TextFormatting.BLUE)));
		} else if (args[0].equals("resetchecksum")) {
			ConfigManager.CONFIG.load();
			ConfigManager.CONFIG.getCategory("internal").get("expectedChecksum").set(Util.calcChecksum());
			ConfigManager.CONFIG.save();
			sender.sendMessage(new TextComponentString("<PackGuard> Successfully reset the expected checksum.")
					.setStyle(new Style().setColor(TextFormatting.BLUE)));
		} else if (args[0].equals("disabledev")) {
			ConfigManager.CONFIG.load();
			ConfigManager.CONFIG.getCategory("general").get("developmentMode").set(false);
			ConfigManager.CONFIG.save();
			sender.sendMessage(new TextComponentString("<PackGuard> Development mode has been disabled.")
					.setStyle(new Style().setColor(TextFormatting.BLUE)));
		} else {
			sender.sendMessage(new TextComponentString("<PackGuard> No such subcommand.")
					.setStyle(new Style().setColor(TextFormatting.BLUE)));
		}
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
		return args.length == 1 ? Lists.newArrayList("resetmods", "resetchecksum", "disabledev")
				: Collections.emptyList();
	}
}