package coolsquid.packguard.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.util.ArrayList;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

import com.google.common.collect.Iterators;

import coolsquid.packguard.PackGuard;
import coolsquid.packguard.config.ConfigManager;

public class Util {

	public static String calcChecksum() {
		File[] toCalc = new File[ConfigManager.checksumPaths.length];
		for (int i = 0; i < ConfigManager.checksumPaths.length; i++) {
			toCalc[i] = new File(ConfigManager.checksumPaths[i]);
		}
		try {
			return Util.calcChecksum(toCalc);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static String calcChecksum(File[] files) throws IOException {
		ArrayList<InputStream> streams = new ArrayList<>();
		for (File file : files) {
			addInputStreams(streams, file);
		}
		InputStream in = new SequenceInputStream(Iterators.asEnumeration(streams.iterator()));
		Checksum digest = new CRC32();
		byte[] buffer = new byte[8192];
		int i = 0;
		while (i != -1) {
			i = in.read(buffer);
			digest.update(buffer, 0, buffer.length);
		}
		in.close();
		return Long.toHexString(digest.getValue());
	}

	private static void addInputStreams(ArrayList<InputStream> streams, File file) throws FileNotFoundException {
		if (file.exists()) {
			if (file.isDirectory()) {
				for (File otherFile : file.listFiles()) {
					addInputStreams(streams, otherFile);
				}
			} else {
				if (!file.getName().equals("PackGuard.cfg") && !file.getName().equals("splash.properties")) {
					streams.add(new FileInputStream(file));
				}
			}
		}
	}

	public static String getWarningSummary(boolean includeChecksum, String separator) {
		StringBuilder b = new StringBuilder();
		for (String warning : PackGuard.WARNINGS) {
			b.append(warning).append(separator);
		}
		if (includeChecksum && PackGuard.checksumWarning != null) {
			return b.append(PackGuard.checksumWarning).toString();
		}
		return PackGuard.WARNINGS.isEmpty() ? b.toString() : b.substring(0, b.length() - separator.length());
	}
}