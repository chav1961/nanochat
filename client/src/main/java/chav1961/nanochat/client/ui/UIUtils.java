package chav1961.nanochat.client.ui;

import java.net.URI;

import chav1961.nanochat.client.Application;
import chav1961.purelib.basic.exceptions.EnvironmentException;
import chav1961.purelib.basic.exceptions.PreparationException;
import chav1961.purelib.model.ContentModelFactory;
import chav1961.purelib.model.interfaces.ContentMetadataInterface;
import chav1961.purelib.model.interfaces.ContentMetadataInterface.ContentNodeMetadata;

public class UIUtils {
	public static final ContentMetadataInterface	UI_METADATA;

	static {
		try{UI_METADATA = ContentModelFactory.forXmlDescription(Application.class.getResourceAsStream("ui.xml"));
		} catch (EnvironmentException e) {
			throw new PreparationException(e.getLocalizedMessage(), e); 
		}
	}
	
	public static ContentNodeMetadata getMeta(final String uri) {
		return UI_METADATA.byUIPath(URI.create(uri));
	}
}
