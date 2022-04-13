package chav1961.nanochat.common.interfaces;

import java.io.Closeable;
import java.io.IOException;
import java.net.URI;
import java.util.UUID;

public interface InviteMedia<Oper extends Enum<?>> extends Closeable {
	@FunctionalInterface
	public static interface ReceiveListener<Oper extends Enum<?>, T> {
		void process(UUID sender, int channel, Oper oper, T content) throws IOException;
	}

	public static enum ChannelPriority {
		UNTIMATE,
		IMPORTANT,
		ORDINAL,
		BACKGROUND;
	}

	public static enum TransmitPolicy {
		PACKED,
		UNLOOSED;
	}

	UUID getInviteMediaId() throws IOException;
	int createChannel(URI channelURI, ChannelPriority prty, TransmitPolicy... policy) throws IOException;
	void closeChannel() throws IOException;
	
	<T> void send(int channel, Oper oper, T content, UUID... members) throws IOException;
	<T> void sendOwner(int channel, Oper oper, T content) throws IOException;
	<T> void sendAll(int channel, Oper oper, T content) throws IOException;
	<T> void addReceiveListener(ReceiveListener<Enum<?>, T> listener);
	<T> void removeReceiveListener(ReceiveListener<Enum<?>, T> listener);
}
