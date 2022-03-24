package chav1961.nanochat.client.ui.interfaces;

import chav1961.nanochat.client.ui.UIUtils;
import chav1961.purelib.basic.exceptions.ContentException;
import chav1961.purelib.json.JsonSerializer;
import chav1961.purelib.model.interfaces.ContentMetadataInterface.ContentNodeMetadata;

public enum CitizenOptionsAdvanced implements UIOptionValueInterface<CitizenOptions>, JavaScriptKeeper {
	DISABLED(UIUtils.getMeta("ui:/model/navigation.top.traymenu"), true),
	AVAILABLE(UIUtils.getMeta("ui:/model/navigation.top.traymenu"));

	private final ContentNodeMetadata	meta;
	private final boolean				isDefault; 			 
	
	private CitizenOptionsAdvanced(final ContentNodeMetadata meta) {
		this(meta, false);
	}

	private CitizenOptionsAdvanced(final ContentNodeMetadata meta, final boolean isDefault) {
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
		return CitizenOptions.ADVANCED;
	}

	@Override
	public String getJavaScript() throws ContentException {
		// TODO Auto-generated method stub
	//	final JsonSerializer<ContentNodeMetadata>	ser = JsonSerializer.buildSerializer(ContentNodeMetadata.class);
		return null;
	}
}
