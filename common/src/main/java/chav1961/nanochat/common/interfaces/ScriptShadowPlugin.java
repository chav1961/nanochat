package chav1961.nanochat.common.interfaces;

import java.net.URI;

import chav1961.purelib.model.interfaces.ContentMetadataInterface.ContentNodeMetadata;

public interface ScriptShadowPlugin {
	String getScriptContent();
	String getStyleContent();
	String[] getI18nKeysContent();
	URI getLocalizerURI();
	ContentNodeMetadata getTemplateMetadata();
}
