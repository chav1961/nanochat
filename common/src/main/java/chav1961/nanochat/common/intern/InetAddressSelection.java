package chav1961.nanochat.common.intern;

import java.net.InetAddress;

import chav1961.purelib.ui.interfaces.ItemAndSelection;

public class InetAddressSelection implements ItemAndSelection<InetAddress> {
	private final InetAddress	address;
	private boolean				state;
	
	public InetAddressSelection(final boolean state, final InetAddress address) {
		this.address = address;
		this.state = state;
	}

	@Override
	public boolean isSelected() {
		return state;
	}

	@Override
	public void setSelected(final boolean selected) {
		this.state = selected;
	}

	@Override
	public InetAddress getItem() {
		return address;
	}
}