package chav1961.nanochat.client.ui.interfaces;

import chav1961.nanochat.client.ui.UIUtils;
import chav1961.purelib.model.interfaces.ContentMetadataInterface.ContentNodeMetadata;

public enum CitizenOptionsFileStatus implements UIOptionValueInterface<CitizenOptions> {
	DISABLED(UIUtils.getMeta("ui:/model/navigation.top.traymenu"), true),
	PARTIALLY_READ(UIUtils.getMeta("ui:/model/navigation.top.traymenu")),
	READ(UIUtils.getMeta("ui:/model/navigation.top.traymenu")),
	PARTIALLY_WRITE(UIUtils.getMeta("ui:/model/navigation.top.traymenu")),
	WRITE(UIUtils.getMeta("ui:/model/navigation.top.traymenu"));

	private final ContentNodeMetadata	meta;
	private final boolean				isDefault; 			 
	
	private CitizenOptionsFileStatus(final ContentNodeMetadata meta) {
		this(meta, false);
	}

	private CitizenOptionsFileStatus(final ContentNodeMetadata meta, final boolean isDefault) {
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
		return CitizenOptions.FILESTATUS;
	}
}
