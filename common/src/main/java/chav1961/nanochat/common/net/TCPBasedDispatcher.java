package chav1961.nanochat.common.net;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;

import chav1961.purelib.basic.interfaces.LoggerFacade;
import chav1961.purelib.net.interfaces.MediaItemDescriptor;

public class TCPBasedDispatcher extends AbstractUUIDControlledDispatcher<MediaItemDescriptor> {

	TCPBasedDispatcher(LoggerFacade logger, boolean asServer) throws IOException {
		super(logger, asServer);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void beforeStart(Selector sel) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void afterStop(Selector sel) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void registerConnect(Selector sel, SelectionKey key) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void registerAccept(Selector sel, SelectionKey key) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void read(Selector sel, SelectionKey key) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void write(Selector sel, SelectionKey key) throws IOException {
		// TODO Auto-generated method stub
		
	}

}
