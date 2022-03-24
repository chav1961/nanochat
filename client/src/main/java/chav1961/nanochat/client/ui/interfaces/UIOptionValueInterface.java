package chav1961.nanochat.client.ui.interfaces;

import chav1961.purelib.model.interfaces.NodeMetadataOwner;

public interface UIOptionValueInterface<Option extends Enum<?>> extends NodeMetadataOwner {
	Option getOwner();
	boolean isDefault();
}
