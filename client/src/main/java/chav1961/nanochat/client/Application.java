package chav1961.nanochat.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import chav1961.nanochat.client.anarchy.SingleWizardStep;
import chav1961.nanochat.client.anarchy.TheSameFirstForm;
import chav1961.nanochat.client.anarchy.TheSameFirstTab;
import chav1961.nanochat.common.Constants;
import chav1961.nanochat.common.NanoChatUtils;
import chav1961.purelib.basic.PureLibSettings;
import chav1961.purelib.basic.SubstitutableProperties;
import chav1961.purelib.basic.exceptions.ContentException;
import chav1961.purelib.basic.exceptions.EnvironmentException;
import chav1961.purelib.basic.exceptions.LocalizationException;
import chav1961.purelib.basic.interfaces.LoggerFacade.Severity;
import chav1961.purelib.i18n.interfaces.Localizer;
import chav1961.purelib.model.ContentModelFactory;
import chav1961.purelib.model.interfaces.ContentMetadataInterface;
import chav1961.purelib.ui.interfaces.ErrorProcessing;
import chav1961.purelib.ui.interfaces.ItemAndSelection;
import chav1961.purelib.ui.swing.SwingUtils;
import chav1961.purelib.ui.swing.interfaces.OnAction;
import chav1961.purelib.ui.swing.useful.JDialogContainer;
import chav1961.purelib.ui.swing.useful.JLocalizedOptionPane;
import chav1961.purelib.ui.swing.useful.JSystemTray;

public class Application {
	public static final String	KEY_APPLICATION_NAME = "application.name";
	public static final String	KEY_APPLICATION_TOOLTIP = "application.name.tt";
	public static final String	KEY_APPLICATION_STARTED = "application.started";
	public static final String	KEY_APPLICATION_CONFIRM_EXIT = "application.confirm.exit";
	public static final String	KEY_APPLICATION_CONFIRM_EXIT_TITLE = "application.confirm.exit.title";

	private final ContentMetadataInterface	mdi;
	private final Localizer					localizer;
	private final SubstitutableProperties	props;
	private final CountDownLatch			latch = new CountDownLatch(1);

	private Application(final ContentMetadataInterface mdi, final Localizer localizer, final SubstitutableProperties props) {
		this.mdi = mdi;
		this.localizer = localizer;
		this.props = props;
	}
	
	
	@OnAction("action:/settings")
	private void settings() {
		System.err.println("SDLKLasdkaskdl;kasdl;k");
	}

	@OnAction("action:/exit")
	private void exit() {
		if (new JLocalizedOptionPane(localizer).confirm(null, KEY_APPLICATION_CONFIRM_EXIT, KEY_APPLICATION_CONFIRM_EXIT_TITLE, JOptionPane.QUESTION_MESSAGE, JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
			saveSettings();
			latch.countDown();
		}
	}
	
	private void saveSettings() {
		try(final OutputStream	fos = new FileOutputStream(Constants.NANOCHAT_CONFIG)) {
			
			props.store(fos, "");
		} catch (IOException e) {
		}
	}

	private void await() {
		try{
			latch.await();
		} catch (InterruptedException e) {
			saveSettings();
		}
	}
	
	public static void main(String[] args) {
		try(final Localizer	localizer = Localizer.Factory.newInstance(URI.create(Localizer.LOCALIZER_SCHEME+":xml:root://"+Application.class.getName()+"/chav1961/nanochat/client/i18n.xml"))) {
			final ContentMetadataInterface	mdi = ContentModelFactory.forXmlDescription(Application.class.getResourceAsStream("application.xml"));

			PureLibSettings.PURELIB_LOCALIZER.push(localizer);
			
			if (!Constants.NANOCHAT_CONFIG.exists()) {
				firstRun(mdi, localizer, Constants.NANOCHAT_CONFIG);
			}
			else {
				final SubstitutableProperties	props = new SubstitutableProperties();
				
				try(final InputStream	is = new FileInputStream(Constants.NANOCHAT_CONFIG)) {
					props.load(is);
					ordinalRun(mdi, localizer, props);
				} catch (IOException e) {
					firstRun(mdi, localizer, Constants.NANOCHAT_CONFIG);
				}
			}
		} catch (ContentException | EnvironmentException e) {
			e.printStackTrace();
		}
	}

	private static void firstRun(final ContentMetadataInterface mdi, final Localizer localizer, final File configFile) throws ContentException {
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
			
			NanoChatUtils.prepareNanoChatDirectory(Constants.NANOCHAT_DIRECTORY);
			try(final OutputStream	fos = new FileOutputStream(Constants.NANOCHAT_CONFIG)) {
				
				props.store(fos, "");
				ordinalRun(mdi, localizer, props);
			} catch (IOException e) {
				throw new ContentException(e.getLocalizedMessage(), e); 
			}
		}
		else {
			System.err.println("Cancel");
		}
	}

	private static void ordinalRun(final ContentMetadataInterface mdi, final Localizer localizer, final SubstitutableProperties props) throws IOException {
		final Application				app = new Application(mdi, localizer, props);
		final JPopupMenu				popup = SwingUtils.toJComponent(mdi.byUIPath(URI.create("ui:/model/navigation.top.traymenu")),JPopupMenu.class); 

		SwingUtils.assignActionListeners(popup,app);
				
		try(final JSystemTray			tray = new JSystemTray(localizer, KEY_APPLICATION_NAME, URI.create("root://"+Application.class.getName()+"/images/trayicon.png"), KEY_APPLICATION_TOOLTIP, popup, props.getProperty(Constants.PROP_GENERAL_TRAY_LANG_EN, boolean.class))) {
//			final LightWeightDiscovery	discovery = new LightWeightDiscovery(null, null, 0, 0, null, 0)) {
			
			tray.addActionListener((e)->app.settings());
			tray.message(Severity.info, localizer.getValue(KEY_APPLICATION_STARTED));
			app.await();
		} catch (EnvironmentException e) {
			throw new IOException(e.getLocalizedMessage(), e);
		}
	}
}
