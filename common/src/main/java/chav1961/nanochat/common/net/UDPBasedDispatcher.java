package chav1961.nanochat.common.net;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;

import chav1961.purelib.basic.interfaces.LoggerFacade;
import chav1961.purelib.net.AbstractSelectorBasedDispatcher;
import chav1961.purelib.net.interfaces.MediaItemDescriptor;

public class UDPBasedDispatcher extends AbstractUUIDControlledDispatcher<MediaItemDescriptor> {
	public UDPBasedDispatcher(final LoggerFacade logger, final boolean asServer) throws IOException {
		super(logger, asServer);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void beforeStart(final Selector sel) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void afterStop(final Selector sel) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void registerConnect(final Selector sel, final SelectionKey key) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void registerAccept(final Selector sel, final SelectionKey key) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void read(final Selector sel, final SelectionKey key) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void write(final Selector sel, final SelectionKey key) throws IOException {
		// TODO Auto-generated method stub
		
	}

}
