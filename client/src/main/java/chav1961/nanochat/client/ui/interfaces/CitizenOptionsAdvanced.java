package chav1961.nanochat.client.ui.interfaces;

import java.io.IOException;
import java.util.Map.Entry;

import chav1961.nanochat.client.ui.UIUtils;
import chav1961.nanochat.common.NanoChatUtils;
import chav1961.purelib.basic.exceptions.ContentException;
import chav1961.purelib.basic.exceptions.EnvironmentException;
import chav1961.purelib.basic.exceptions.PreparationException;
import chav1961.purelib.json.JsonSerializer;
import chav1961.purelib.model.interfaces.ContentMetadataInterface.ContentNodeMetadata;
import chav1961.purelib.streams.JsonStaxPrinter;

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
	public void getJavaScript(final Appendable app) throws ContentException {
		try{app.append('\"').append(name()).append('\"');
		} catch (IOException e) {
			throw new ContentException(e.getLocalizedMessage(), e); 
		}
	}
}
