package chav1961.nanochat.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Arrays;

import javax.swing.JComponent;
import javax.swing.JFrame;

import chav1961.nanochat.client.anarchy.SingleWizardStep;
import chav1961.nanochat.client.anarchy.TheSameFirstTab;
import chav1961.purelib.basic.PureLibSettings;
import chav1961.purelib.basic.SubstitutableProperties;
import chav1961.purelib.basic.exceptions.LocalizationException;
import chav1961.purelib.i18n.interfaces.Localizer;
import chav1961.purelib.ui.interfaces.ErrorProcessing;
import chav1961.purelib.ui.swing.useful.JDialogContainer;

public class Application {

	public static void main(String[] args) {
		
		try(final Localizer	localizer = Localizer.Factory.newInstance(URI.create(Localizer.LOCALIZER_SCHEME+":xml:root://"+Application.class.getName()+"/chav1961/nanochat/client/i18n.xml"))) {
			final File		configFile = new File(System.getProperty("user.home"),".nanochat");

			PureLibSettings.PURELIB_LOCALIZER.push(localizer);
			
			if (!configFile.exists()) {
				firstRun(localizer, configFile);
			}
			else {
				final SubstitutableProperties	props = new SubstitutableProperties();
				
				try(final InputStream	is = new FileInputStream(configFile)) {
					props.load(is);
					ordinalRun(props);
				} catch (IOException e) {
					firstRun(localizer, configFile);
				}
			}
		}
		
		
	}

	public static void firstRun(final Localizer localizer, final File configFile) {
		// TODO Auto-generated method stub
		System.err.println("First");
		
		final TheSameFirstTab			tab = new TheSameFirstTab(localizer);
		final SubstitutableProperties	props = new SubstitutableProperties();
		final ErrorProcessing<TheSameFirstTab, SingleWizardStep> ep = new ErrorProcessing<TheSameFirstTab, SingleWizardStep>() {
											@Override
											public void processWarning(TheSameFirstTab content, SingleWizardStep err, Object... parameters) throws LocalizationException {
												// TODO Auto-generated method stub
												System.err.println("WARN: "+Arrays.toString(parameters));
											}
										};
		
		if (new JDialogContainer<TheSameFirstTab, SingleWizardStep, JComponent>(localizer, (JFrame)null, tab, ep, tab).showDialog()) {
			ordinalRun(props);
		}
		else {
			System.err.println("Cancel");
		}
	}

	public static void ordinalRun(final SubstitutableProperties props) {
		// TODO Auto-generated method stub
		System.err.println("Ordinal");
	}
}
