package chav1961.nanochat.client.net;

import java.io.IOException;

import chav1961.nanochat.common.Constants;
import chav1961.purelib.basic.SubstitutableProperties;
import chav1961.purelib.net.LightWeightDiscovery;

public class ClientDiscovery extends LightWeightDiscovery<BroadcastInfo, QueryInfo> {
	private final SubstitutableProperties	props;
	
	public ClientDiscovery(final SubstitutableProperties props) throws IOException {
		super(props.getProperty(Constants.PROP_GENERAL_DISCOVERY_PORT, int.class), props.getProperty(Constants.PROP_GENERAL_DISCOVERY_RECORD_SIZE, int.class), new BroadcastGeneratorImpl(props));
		this.props = props;
	}

	
	@Override
	protected BroadcastInfo getBroadcastInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected QueryInfo getQueryInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void maintenance(final Object content) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public int getMaintenancePeriod() {
		return props.getProperty(Constants.PROP_GENERAL_DISCOVERY_MAINTENANCE_TIME, int.class);
	}
}
