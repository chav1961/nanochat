package chav1961.nanochat.common.net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.UUID;

import chav1961.purelib.basic.ReusableInstances;
import chav1961.purelib.basic.interfaces.LoggerFacade;
import chav1961.purelib.net.LightWeightNetworkDiscovery.MediaItemDescriptorImpl;

public class UDPBasedDispatcher extends AbstractUUIDControlledDispatcher<MediaItemDescriptorImpl> {
	private final DatagramChannel	channel; 
	private final int				bufferSize;
	private final ReusableInstances<ByteBuffer>	inst;
	
	public UDPBasedDispatcher(final LoggerFacade logger, final int portNumber, final int bufferSize) throws IOException {
		super(logger, true);
		this.channel = DatagramChannel.open();
		this.channel.bind(new InetSocketAddress(portNumber));
		this.bufferSize = bufferSize;
		this.inst = new ReusableInstances<ByteBuffer>(()->ByteBuffer.allocateDirect(bufferSize), (b)->cleanBuffer(b));
	}

	public UDPBasedDispatcher(final LoggerFacade logger, final InetSocketAddress address, final int bufferSize) throws IOException {
		super(logger, false);
		this.channel = DatagramChannel.open(); 
		this.channel.bind(address);
		this.bufferSize = bufferSize;
		this.inst = new ReusableInstances<ByteBuffer>(()->ByteBuffer.allocateDirect(bufferSize), (b)->cleanBuffer(b));
	}
	
	@Override
	public synchronized void close() throws IOException {
		channel.close();
		super.close();
	}
	
	@Override
	public synchronized void transmit(UUID to, byte[] content) throws InterruptedException, IOException {
		super.transmit(to, content);
		if (getTransmitQueue().size() == 1) {
			write(getSelector(), null);
		}
	}
	
	@Override
	protected void beforeStart(final Selector sel) throws IOException {
		channel.register(sel, SelectionKey.OP_READ);
		channel.register(sel, SelectionKey.OP_WRITE);
	}

	@Override
	protected void afterStop(final Selector sel) throws IOException {
		// TODO Auto-generated method stub
	}

	@Override
	protected void registerConnect(final Selector sel, final SelectionKey key) throws IOException {
		throw new UnsupportedOperationException("Not supported for UDP"); 
	}

	@Override
	protected void registerAccept(final Selector sel, final SelectionKey key) throws IOException {
		throw new UnsupportedOperationException("Not supported for UDP"); 
	}

	@Override
	protected void read(final Selector sel, final SelectionKey key) throws IOException {
		final ByteBuffer	buffer= inst.allocate();
		
		try{final SocketAddress	addr = channel.receive(buffer);
		
			dispatch(addr, buffer);
		} finally {
			inst.free(buffer);
		}
	}

	@Override
	protected void write(final Selector sel, final SelectionKey key) throws IOException {
		final TransmitDescriptor 	item = getTransmitQueue().poll();
		
		if (item != null) {
			final ByteBuffer	buffer = inst.allocate();
			
			try{buffer.putLong(item.id.getMostSignificantBits());
				buffer.putLong(item.id.getLeastSignificantBits());
				buffer.put(item.content);
				buffer.rewind();

				if (BROADCAST_UUID.equals(item.id)) {
					forAll((id, desc, asServer, isOwner) -> {
						if (!isOwner) {
							channel.send(buffer, null);
						}
					});
				}
				else {
					channel.send(buffer, null);
				}
			} finally {
				inst.free(buffer);
			}
		}
	}

	@Override
	protected void dispatch(SocketAddress source, ByteBuffer content) throws IOException {
		// TODO Auto-generated method stub
		
	}
	
	private ByteBuffer cleanBuffer(final ByteBuffer buffer) {
		// TODO Auto-generated method stub
		return buffer;
	}

}
