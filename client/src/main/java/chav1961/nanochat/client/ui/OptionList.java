package chav1961.nanochat.client.ui;

import java.io.IOException;
import java.util.EnumMap;

import chav1961.nanochat.client.ui.interfaces.JavaScriptKeeper;
import chav1961.purelib.basic.exceptions.ContentException;

public class OptionList<T extends Enum<T>, V> extends EnumMap<T, V> implements JavaScriptKeeper {
	private static final long serialVersionUID = 4720790274922683682L; 
	
	public OptionList(final Class<T> keyType) {
		super(keyType);
	}

	@Override
	public void getJavaScript(final Appendable app) throws ContentException {
		try{if (isEmpty()) {
				app.append("{}");
			}
			else {
				char	prefix = '{';
				
				for (Entry<T, V> item : entrySet()) {
					app.append(prefix).append('\"').append(item.getKey().toString()).append("\":");
					if (item.getValue() instanceof JavaScriptKeeper) {
						((JavaScriptKeeper)item.getValue()).getJavaScript(app);
					}
					else {
						app.append('\"').append(item.getValue().toString()).append('\"');
					}
					prefix = ',';
				}
				app.append('}');
			}
		} catch (IOException e) {
			throw new ContentException(e.getLocalizedMessage(), e); 
		}
	}
}
