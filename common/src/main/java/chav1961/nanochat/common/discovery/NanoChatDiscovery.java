package chav1961.nanochat.common.discovery;

import java.io.IOException;
import java.io.Serializable;
import java.net.ServerSocket;
import java.util.UUID;

import chav1961.nanochat.common.Constants;
import chav1961.nanochat.common.interfaces.InviteInfo;
import chav1961.nanochat.common.interfaces.InviteMedia;
import chav1961.nanochat.common.interfaces.InviteMediaFactory;
import chav1961.purelib.basic.SubstitutableProperties;
import chav1961.purelib.net.LightWeightNetworkDiscovery;

public class NanoChatDiscovery<Op extends Enum<?>> extends LightWeightNetworkDiscovery<BroadcastInfo, QueryInfo> implements InviteMediaFactory<Op> {
	private final ServerSocket				ss = new ServerSocket();
	private final SubstitutableProperties	props;

	public NanoChatDiscovery(final SubstitutableProperties props) throws IOException {
		super(props.getProperty(Constants.PROP_GENERAL_DISCOVERY_PORT, int.class), props.getProperty(Constants.PROP_GENERAL_DISCOVERY_RECORD_SIZE, int.class),  new BroadcastGeneratorImpl(props));
		this.props = props;
	}
	
	@Override
	public synchronized void close() throws IOException {
		ss.close();
		super.close();
	}
	
	@Override
	public void maintenance(final Object content) {
		// TODO Auto-generated method stub
		System.err.println("Maintenance...");
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
	protected BroadcastInfo getBroadcastInfo() {
		return new BroadcastInfo(props.getProperty(Constants.PROP_GENERAL_ID, UUID.class)
								, props.getProperty(Constants.PROP_GENERAL_NAME, String.class)
								, props.getProperty(Constants.PROP_ANARCH_DISTRICT, String.class)
								, props.getProperty(Constants.PROP_GENERAL_DISCOVERY_PORT, int.class));
	}

	@Override
	protected QueryInfo getQueryInfo() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public int getMaintenancePeriod() {
		return 1000 * props.getProperty(Constants.PROP_GENERAL_DISCOVERY_MAINTENANCE_TIME, int.class);
	}
}
