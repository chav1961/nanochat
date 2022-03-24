package chav1961.nanochat.client.ui.interfaces;

import chav1961.nanochat.client.ui.UIUtils;
import chav1961.purelib.model.interfaces.ContentMetadataInterface.ContentNodeMetadata;

public enum CitizenOptionsNotificationStatus implements UIOptionValueInterface<CitizenOptions> {
	DISABLED(UIUtils.getMeta("ui:/model/navigation.top.traymenu")),
	ULTIMATE_ONLY(UIUtils.getMeta("ui:/model/navigation.top.traymenu")),
	IMPORTANT_ONLY(UIUtils.getMeta("ui:/model/navigation.top.traymenu")),
	ANY(UIUtils.getMeta("ui:/model/navigation.top.traymenu"), true);

	private final ContentNodeMetadata	meta;
	private final boolean				isDefault; 			 
	
	private CitizenOptionsNotificationStatus(final ContentNodeMetadata meta) {
		this(meta, false);
	}

	private CitizenOptionsNotificationStatus(final ContentNodeMetadata meta, final boolean isDefault) {
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
		return CitizenOptions.NOTIFICATIONSTATUS;
	}
}
