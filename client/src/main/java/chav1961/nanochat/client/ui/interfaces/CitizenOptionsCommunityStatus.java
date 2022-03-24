package chav1961.nanochat.client.ui.interfaces;

import chav1961.nanochat.client.ui.UIUtils;
import chav1961.purelib.model.interfaces.ContentMetadataInterface.ContentNodeMetadata;

public enum CitizenOptionsCommunityStatus implements UIOptionValueInterface<CitizenOptions> {
	COMMUNITY(UIUtils.getMeta("ui:/model/navigation.top.traymenu"), true),
	BAND(UIUtils.getMeta("ui:/model/navigation.top.traymenu")),
	M(UIUtils.getMeta("ui:/model/navigation.top.traymenu"));

	private final ContentNodeMetadata	meta;
	private final boolean				isDefault; 			 
	
	private CitizenOptionsCommunityStatus(final ContentNodeMetadata meta) {
		this(meta, false);
	}

	private CitizenOptionsCommunityStatus(final ContentNodeMetadata meta, final boolean isDefault) {
		this.meta = meta;
		this.isDefault = isDefault;
	}
	
	@Override
	public ContentNodeMetadata getNodeMetadata() {
		return meta;
	}

	@Override
	public boolean isDefault() {
		return isDefault;
	}
	
	@Override
	public CitizenOptions getOwner() {
		return CitizenOptions.COMMUNITYSTATUS;
	}
}
