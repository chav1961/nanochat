package chav1961.nanochat.common.interfaces;

import java.net.URI;

public interface SharedContent<T> {
	URI getContentType();
	T getContent();
}
