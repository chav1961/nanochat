package chav1961.nanochat.client;

import java.awt.Component;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.Arrays;

import javax.swing.JComponent;
import javax.swing.JFrame;

import chav1961.nanochat.client.anarchy.SingleWizardStep;
import chav1961.nanochat.client.anarchy.TheSameFirstForm;
import chav1961.nanochat.client.anarchy.TheSameFirstTab;
import chav1961.nanochat.common.Constants;
import chav1961.purelib.basic.PureLibSettings;
import chav1961.purelib.basic.SubstitutableProperties;
import chav1961.purelib.basic.exceptions.ContentException;
import chav1961.purelib.basic.exceptions.LocalizationException;
import chav1961.purelib.i18n.interfaces.Localizer;
import chav1961.purelib.ui.interfaces.ErrorProcessing;
import chav1961.purelib.ui.interfaces.ItemAndSelection;
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
		} catch (ContentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void firstRun(final Localizer localizer, final File configFile) throws ContentException {
		final TheSameFirstTab			tab = new TheSameFirstTab(localizer, PureLibSettings.CURRENT_LOGGER);
		final SubstitutableProperties	props = new SubstitutableProperties();
		final ErrorProcessing<TheSameFirstTab, SingleWizardStep> ep = new ErrorProcessing<TheSameFirstTab, SingleWizardStep>() {
											@Override
											public void processWarning(final TheSameFirstTab content, final SingleWizardStep err, final Object... parameters) throws LocalizationException {
												// TODO Auto-generated method stub
												System.err.println("WARN: "+Arrays.toString(parameters));
											}
										};
		
		if (new JDialogContainer<TheSameFirstTab, SingleWizardStep, JComponent>(localizer, (JFrame)null, tab, ep, tab).showDialog()) {
			final TheSameFirstForm		form = tab.getForm();
			
			props.setProperty(Constants.PROP_GENERAL_ID, form.id.toString());
			props.setProperty(Constants.PROP_GENERAL_NAME, form.name);
			props.setProperty(Constants.PROP_GENERAL_DEFAULT_LANG, form.lang.name());
			props.setProperty(Constants.PROP_GENERAL_TRAY_LANG_EN, form.useEn ? "true" : "false");
			
			props.setProperty(Constants.PROP_ANARCH_SUBNETS, Arrays.toString(ItemAndSelection.extract(true, form.addr)));
			props.setProperty(Constants.PROP_ANARCH_DISTRICT, form.district);
			Component c;
			try(final OutputStream	fos = new FileOutputStream(configFile)) {
				
				props.store(fos, "");
			} catch (IOException e) {
				throw new ContentException(e.getLocalizedMessage(), e); 
			}
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
