package chav1961.nanochat.common.discovery;

import chav1961.nanochat.common.interfaces.InviteInfo;
import chav1961.nanochat.common.interfaces.InviteMedia;
import chav1961.purelib.net.AbstractDiscovery;
import chav1961.purelib.net.LightWeightNetworkDiscovery;
import chav1961.purelib.net.interfaces.MediaAdapter;
import chav1961.purelib.net.interfaces.MediaDescriptor;

import java.io.IOException;
import java.io.Serializable;
import java.util.UUID;

public class NanoChatDiscovery extends LightWeightNetworkDiscovery<Serializable, Serializable> {

	public NanoChatDiscovery(final int discoveryPortNumber, final int maxRecordSize, final PortBroadcastGenerator generator) throws IOException {
		super(discoveryPortNumber, maxRecordSize, generator);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void maintenance(final Object content) {
		// TODO Auto-generated method stub
		
	}

	public <Op extends Enum<?>> InviteMedia<Op> createServerInviteMedia(final UUID owner, final InviteInfo info, final Class<Op> clazz) throws IOException {
		return null;
	}
	
	public <Op extends Enum<?>> InviteMedia<Op> createClientInviteMedia(final UUID owner, final UUID serverOwner, final UUID serverInviteId, final Class<Op> clazz) throws IOException {
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
