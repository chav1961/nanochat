package chav1961.nanochat.client.ui.interfaces;

import chav1961.nanochat.client.ui.UIUtils;
import chav1961.purelib.model.interfaces.ContentMetadataInterface.ContentNodeMetadata;

public enum CitizenOptions implements UIOptionInterface {
	AVAILABILITY(UIUtils.getMeta("ui:/model/navigation.top.traymenu")),
	COMMUNITYSTATUS(UIUtils.getMeta("ui:/model/navigation.top.traymenu")),
	CLIPBOARDSTATUS(UIUtils.getMeta("ui:/model/navigation.top.traymenu")),
	FILESTATUS(UIUtils.getMeta("ui:/model/navigation.top.traymenu")),
	VOICESTATUS(UIUtils.getMeta("ui:/model/navigation.top.traymenu")),
	NOTIFICATIONSTATUS(UIUtils.getMeta("ui:/model/navigation.top.traymenu")),
	ADVANCED(UIUtils.getMeta("ui:/model/navigation.top.traymenu"));

	private final ContentNodeMetadata	meta;
	
	private CitizenOptions(final ContentNodeMetadata meta) {
		this.meta = meta;
	}
	
	@Override
	public ContentNodeMetadata getNodeMetadata() {
		return meta;
	}
}
