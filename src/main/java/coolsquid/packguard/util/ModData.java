package coolsquid.packguard.util;

import net.minecraftforge.fml.common.versioning.VersionParser;
import net.minecraftforge.fml.common.versioning.VersionRange;

public class ModData {

	public final String id, textVersion;
	public final VersionRange version;
	public final boolean optional;

	public ModData(String id, String textVersion, boolean optional) {
		this.id = id;
		this.textVersion = textVersion;
		this.version = VersionParser.parseRange(textVersion);
		this.optional = optional;
	}
}