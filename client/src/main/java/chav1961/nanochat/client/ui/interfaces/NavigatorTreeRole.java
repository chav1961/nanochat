package chav1961.nanochat.client.ui.interfaces;

import chav1961.purelib.model.interfaces.ContentMetadataInterface.ContentNodeMetadata;
import chav1961.nanochat.client.ui.UIUtils;
import chav1961.purelib.model.interfaces.NodeMetadataOwner;

public enum NavigatorTreeRole implements NodeMetadataOwner {
	MYSELF(UIUtils.getMeta("ui:/model/navigation.top.traymenu")),
	CITIZEN(UIUtils.getMeta("ui:/model/navigation.top.traymenu")),
	COMMUNITY(UIUtils.getMeta("ui:/model/navigation.top.traymenu")),
	BAND(UIUtils.getMeta("ui:/model/navigation.top.traymenu")),
	M(UIUtils.getMeta("ui:/model/navigation.top.traymenu"));

	private final ContentNodeMetadata	meta;
	
	private NavigatorTreeRole(final ContentNodeMetadata meta) {
		this.meta = meta;
	}
	
	@Override
	public ContentNodeMetadata getNodeMetadata() {
		return meta;
	}
}
