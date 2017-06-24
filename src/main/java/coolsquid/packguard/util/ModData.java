package coolsquid.packguard.util;

public class ModData {

	public final String id, version;
	public final boolean optional;

	public ModData(String id, String version, boolean optional) {
		this.id = id;
		this.version = version;
		this.optional = optional;
	}
}