package chav1961.nanochat.common.discovery;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import chav1961.nanochat.common.Constants;
import chav1961.nanochat.common.NanoChatUtils;
import chav1961.purelib.basic.SubstitutableProperties;
import chav1961.purelib.net.LightWeightNetworkDiscovery.InetAddressDescriptor;
import chav1961.purelib.net.LightWeightNetworkDiscovery.PortBroadcastGenerator;
import chav1961.purelib.ui.interfaces.ItemAndSelection;

class BroadcastGeneratorImpl implements PortBroadcastGenerator {
	private final SubstitutableProperties	props;
	private final InetAddressDescriptor[]	desc;
	
	BroadcastGeneratorImpl(final SubstitutableProperties props) {
		final List<InetAddressDescriptor>	list= new ArrayList<>();
		
		for (ItemAndSelection<InetAddress> item : NanoChatUtils.string2ItemAndSelection(props.getProperty(Constants.PROP_ANARCH_SUBNETS))) {
			list.add(new InetAddressDescriptor(item.getItem(), props.getProperty(Constants.PROP_GENERAL_DISCOVERY_PORT, int.class), props.getProperty(Constants.PROP_GENERAL_DISCOVERY_MAINTENANCE_TIME, int.class)));
		}
		
		this.props = props;
		this.desc = list.toArray(new InetAddressDescriptor[list.size()]);
	}

	@Override
	public Iterable<InetAddressDescriptor> enumPorts() {
		return Arrays.asList(desc);
	}
}