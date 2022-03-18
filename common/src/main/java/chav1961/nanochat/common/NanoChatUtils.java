package chav1961.nanochat.common;

import java.io.File;

public class NanoChatUtils {
	public static void prepareNanoChatDirectory(final File nanoChatDir) {
		if (nanoChatDir == null) {
			throw new IllegalArgumentException("Nanochat directory can't be null"); 
		}
		else {
			nanoChatDir.mkdirs();
		}
	}
}
