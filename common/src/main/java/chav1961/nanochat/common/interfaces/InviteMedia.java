package chav1961.nanochat.common.interfaces;

import java.io.Closeable;
import java.io.IOException;
import java.util.UUID;

public interface InviteMedia<Oper extends Enum<?>> extends Closeable {
	@FunctionalInterface
	public static interface ReceiveListener<Oper extends Enum<?>, T> {
		void process(UUID sender, int channel, Oper oper, T content) throws IOException;
	}
	
	<T> void send(UUID member, int channel, Oper oper, T content) throws IOException;
	<T> void addReceiveListener(ReceiveListener<Enum<?>, T> listener);
	<T> void removeReceiveListener(ReceiveListener<Enum<?>, T> listener);
}
