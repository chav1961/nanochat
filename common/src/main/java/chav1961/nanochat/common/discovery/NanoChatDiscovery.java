package chav1961.nanochat.common.discovery;

import java.io.IOException;
import java.io.Serializable;
import java.net.ServerSocket;
import java.util.UUID;

import chav1961.nanochat.common.interfaces.InviteInfo;
import chav1961.nanochat.common.interfaces.InviteMedia;
import chav1961.nanochat.common.interfaces.InviteMediaFactory;
import chav1961.purelib.net.LightWeightNetworkDiscovery;

public class NanoChatDiscovery<Op extends Enum<?>> extends LightWeightNetworkDiscovery<Serializable, Serializable> implements InviteMediaFactory<Op> {
	private final ServerSocket	ss = new ServerSocket();

	public NanoChatDiscovery(final int discoveryPortNumber, final int maxRecordSize, final PortBroadcastGenerator generator) throws IOException {
		super(discoveryPortNumber, maxRecordSize, generator);
		// TODO Auto-generated constructor stub
	}

	@Override
	public synchronized void close() throws IOException {
		ss.close();
		super.close();
	}
	
	@Override
	public void maintenance(final Object content) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public InviteMedia<Op> createServerInviteMedia(final UUID owner, final InviteInfo info, final Class<Op> clazz) throws IOException {
		return null;
	}
	
	@Override
	public InviteMedia<Op> createClientInviteMedia(final UUID owner, final UUID serverOwner, final UUID serverInviteId, final Class<Op> clazz) throws IOException {
		return null;
	}
	
	@Override
	protected Serializable getBroadcastInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Serializable getQueryInfo() {
		// TODO Auto-generated method stub
		return null;
	}
}
