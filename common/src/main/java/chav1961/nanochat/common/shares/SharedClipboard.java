package chav1961.nanochat.common.shares;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.FlavorEvent;
import java.awt.datatransfer.FlavorListener;
import java.awt.datatransfer.Transferable;
import java.io.Serializable;

import chav1961.nanochat.common.intern.DiscoveryRequests;
import chav1961.purelib.net.AbstractDiscovery;
import chav1961.purelib.net.DiscoveryEvent;
import chav1961.purelib.net.interfaces.DiscoveryListener;

public class SharedClipboard implements AutoCloseable, DiscoveryListener, FlavorListener, ClipboardOwner {
	private final AbstractDiscovery<Serializable, DiscoveryRequests>	discovery;
	private final Clipboard			delegate;
	
	public SharedClipboard(final Clipboard clipboard, final AbstractDiscovery<Serializable, DiscoveryRequests> discovery) {
		if (clipboard == null) {
			throw new NullPointerException("Clipboard can't be null"); 
		}
		else if (discovery == null) {
			throw new NullPointerException("Discovery can't be null"); 
		}
		else {
			this.delegate = clipboard;
			this.discovery = discovery;
			discovery.addDiscoveryListener(this);
			delegate.addFlavorListener(this);
		}
	}

	public Clipboard getClipboard() {
		return delegate;
	}
	
	@Override
	public void flavorsChanged(final FlavorEvent e) {
		// TODO Auto-generated method stub
		for (DataFlavor item : delegate.getAvailableDataFlavors()) {
		}
	}

	@Override
	public void lostOwnership(final Clipboard clipboard, final Transferable contents) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public boolean filterEvent(final DiscoveryEvent event) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void processEvent(final DiscoveryEvent event) {
		// TODO Auto-generated method stub
	}

	@Override
	public void close() throws Exception {
		// TODO Auto-generated method stub
		delegate.removeFlavorListener(this);
		discovery.removeDiscoveryListener(this);
	}
};
