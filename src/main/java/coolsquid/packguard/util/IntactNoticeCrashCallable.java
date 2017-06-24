package coolsquid.packguard.util;

import net.minecraftforge.fml.common.ICrashCallable;

public class IntactNoticeCrashCallable implements ICrashCallable {

	@Override
	public String call() throws Exception {
		return "No tampering discovered.";
	}

	@Override
	public String getLabel() {
		return "PackGuard";
	}
}