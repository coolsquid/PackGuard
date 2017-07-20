package coolsquid.packguard.util;

import net.minecraftforge.fml.common.ICrashCallable;

public class WarningCrashCallable implements ICrashCallable {

	@Override
	public String call() throws Exception {
		return Util.getWarningSummary(true, ", ");
	}

	@Override
	public String getLabel() {
		return "PackGuard";
	}
}