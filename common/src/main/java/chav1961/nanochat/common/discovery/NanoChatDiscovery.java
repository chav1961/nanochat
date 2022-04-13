package chav1961.nanochat.common.discovery;

import chav1961.purelib.net.AbstractDiscovery;
import chav1961.purelib.net.interfaces.MediaAdapter;
import chav1961.purelib.net.interfaces.MediaDescriptor;

import java.io.IOException;
import java.io.Serializable;

public class NanoChatDiscovery extends AbstractDiscovery<Serializable, Serializable> {
	
	public NanoChatDiscovery(final MediaAdapter adapter, final MediaDescriptor mediaDesc, final int maxBufferSize, final int discoveryPeriod)throws IOException {
		super(adapter, mediaDesc, maxBufferSize, discoveryPeriod);
	}

	@Override
	public void maintenance(final Object content) {
		// TODO Auto-generated method stub
		
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
