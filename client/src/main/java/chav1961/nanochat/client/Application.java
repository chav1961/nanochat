package chav1961.nanochat.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import chav1961.purelib.basic.PureLibSettings;
import chav1961.purelib.basic.SubstitutableProperties;
import chav1961.purelib.i18n.interfaces.Localizer;
import chav1961.purelib.ui.swing.useful.JDialogContainer;

public class Application {

	public static void main(String[] args) {
		
		try(final Localizer	localizer = Localizer.Factory.newInstance(URI.create(Localizer.LOCALIZER_SCHEME+"root://"+Application.class.getName()+"/chav1961/nanochat/client/i18n/i18n.xml"))) {
			final File		configFile = new File(System.getProperty("user.home"),".nanochat");

			PureLibSettings.PURELIB_LOCALIZER.push(localizer);
			
			if (!configFile.exists()) {
				firstRun();
			}
			else {
				final SubstitutableProperties	props = new SubstitutableProperties();
				
				try(final InputStream	is = new FileInputStream(configFile)) {
					props.load(is);
					ordinalRun(props);
				} catch (IOException e) {
					firstRun();
				}
			}
		}
		
		
	}

	public static void firstRun() {
		// TODO Auto-generated method stub
		new JDialogContainer<Common, Enum<?>, Content>(null, null, null, null)
		System.err.println("First");
	}

	public static void ordinalRun(final SubstitutableProperties props) {
		// TODO Auto-generated method stub
		System.err.println("Ordinal");
	}
}
