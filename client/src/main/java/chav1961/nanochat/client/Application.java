package chav1961.nanochat.client;

import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import chav1961.nanochat.client.anarchy.SingleWizardStep;
import chav1961.nanochat.client.anarchy.TheSameFirstForm;
import chav1961.nanochat.client.anarchy.TheSameFirstTab;
import chav1961.nanochat.client.net.ClientDiscovery;
import chav1961.nanochat.client.settings.SettingsWindow;
import chav1961.nanochat.client.ui.UIPainter;
import chav1961.nanochat.common.Constants;
import chav1961.nanochat.common.NanoChatUtils;
import chav1961.purelib.basic.PureLibSettings;
import chav1961.purelib.basic.SimpleTimerTask;
import chav1961.purelib.basic.SubstitutableProperties;
import chav1961.purelib.basic.exceptions.ContentException;
import chav1961.purelib.basic.exceptions.EnvironmentException;
import chav1961.purelib.basic.exceptions.LocalizationException;
import chav1961.purelib.basic.exceptions.PreparationException;
import chav1961.purelib.basic.exceptions.SyntaxException;
import chav1961.purelib.basic.interfaces.LoggerFacade.Severity;
import chav1961.purelib.fsys.interfaces.FileSystemInterface;
import chav1961.purelib.i18n.interfaces.Localizer;
import chav1961.purelib.i18n.interfaces.Localizer.LocaleChangeListener;
import chav1961.purelib.i18n.interfaces.SupportedLanguages;
import chav1961.purelib.model.ContentModelFactory;
import chav1961.purelib.model.interfaces.ContentMetadataInterface;
import chav1961.purelib.nanoservice.NanoServiceFactory;
import chav1961.purelib.ui.interfaces.ErrorProcessing;
import chav1961.purelib.ui.swing.SwingUtils;
import chav1961.purelib.ui.swing.interfaces.OnAction;
import chav1961.purelib.ui.swing.useful.JDialogContainer;
import chav1961.purelib.ui.swing.useful.JLocalizedOptionPane;
import chav1961.purelib.ui.swing.useful.JSystemTray;

public class Application implements AutoCloseable, LocaleChangeListener {
	public static final String	KEY_APPLICATION_NAME = "application.name";
	public static final String	KEY_APPLICATION_TOOLTIP = "application.name.tt";
	public static final String	KEY_APPLICATION_STARTED = "application.started";
	public static final String	KEY_APPLICATION_CONFIRM_EXIT = "application.confirm.exit";
	public static final String	KEY_APPLICATION_CONFIRM_EXIT_TITLE = "application.confirm.exit.title";

	public static final String	PATH_UI_PAINTER = "/ui/";
	
	private static final SubstitutableProperties	DEFAULT_PROPS = new SubstitutableProperties();
	
	static {
		try(final InputStream	is = Application.class.getResourceAsStream("/nanochat.client.default.properties")) {
			
			DEFAULT_PROPS.load(is);
		} catch (IOException e) {
			throw new PreparationException(e.getLocalizedMessage(), e);
		}
	}
	
	private final ContentMetadataInterface	mdi;
	private final Localizer					localizer;
	private final SubstitutableProperties	props;
	private final JPopupMenu				popup;	
	private final JSystemTray				tray;
	private final UIPainter					painter;
	private final CountDownLatch			latch = new CountDownLatch(1);
	private final NanoServiceFactory		nanoService;

	private Application(final ContentMetadataInterface mdi, final Localizer localizer, final SubstitutableProperties props) throws EnvironmentException, SyntaxException, NullPointerException, ContentException, IOException {
		this.mdi = mdi;
		this.localizer = localizer;
		this.props = props;
		
		this.popup = SwingUtils.toJComponent(mdi.byUIPath(URI.create("ui:/model/navigation.top.traymenu")),JPopupMenu.class);
		SwingUtils.assignActionListeners(popup,this);
		
		this.tray = new JSystemTray(localizer, KEY_APPLICATION_NAME, URI.create("root://"+Application.class.getName()+"/images/trayicon.png"), KEY_APPLICATION_TOOLTIP, popup, props.getProperty(Constants.PROP_GENERAL_TRAY_LANG_EN, boolean.class));
		this.tray.addActionListener((e)->browseScreen());
		localizer.addLocaleChangeListener(this);
		this.nanoService = new NanoServiceFactory(PureLibSettings.CURRENT_LOGGER, props);
		this.painter = new UIPainter(FileSystemInterface.Factory.newInstance(URI.create(FileSystemInterface.FILESYSTEM_URI_SCHEME+":file:./")), localizer, tray);
		this.nanoService.deploy(PATH_UI_PAINTER, painter);
	}
	
	@Override
	public void close() throws EnvironmentException {
		try{nanoService.undeploy(PATH_UI_PAINTER);
			painter.close();
			nanoService.close();
		} catch (IOException e) {
			tray.message(Severity.severe, e.getLocalizedMessage());
		}
		localizer.removeLocaleChangeListener(this);
		tray.close();
	}

	@Override
	public void localeChanged(final Locale oldLocale, final Locale newLocale) throws LocalizationException {
		tray.localeChanged(oldLocale, newLocale);
	}

	public JSystemTray getTray() {
		return tray;
	}

	private void browseScreen() {
		browseScreen("/gui/index");
	}

	private void browseScreen(final String address) {
		final String	uri = "http://localhost:"+props.getProperty(NanoServiceFactory.NANOSERVICE_PORT)+address;
		
		if (Desktop.isDesktopSupported()) {
			try{Desktop.getDesktop().browse(URI.create(uri));
			} catch (IOException exc) {
				tray.message(Severity.error, "Errir starting browser on ["+uri+"] : "+exc.getLocalizedMessage());
			}
		}
		else {
			tray.message(Severity.error, "No desktop support on your computer. Try to type ["+uri+"] manually in your browser address string");
		}
	}

	@OnAction("action:/paused")
	private void paused(final Hashtable<String,String[]> langs) throws LocalizationException {
		System.err.println("Paused");
	}
	
	@OnAction("action:/notification")
	private void notification(final Hashtable<String,String[]> langs) throws LocalizationException {
		System.err.println("Notification");
	}
	
	@OnAction("action:/settings")
	private void settings() {
		try {
			final SettingsWindow	w = new SettingsWindow(mdi, localizer, PureLibSettings.CURRENT_LOGGER, props);
			final JDialogContainer<SettingsWindow, SingleWizardStep, JComponent>	container = new JDialogContainer<SettingsWindow, SingleWizardStep, JComponent>(localizer, (JFrame)null, KEY_APPLICATION_NAME, w); 
			
			container.setSize(800, 600);
			if (container.showDialog()) {
				w.commit();
				saveSettings();
			}			
		} catch (ContentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@OnAction("action:builtin:/builtin.languages")
	private void selectLang(final Hashtable<String,String[]> langs) throws LocalizationException {
		PureLibSettings.PURELIB_LOCALIZER.setCurrentLocale(SupportedLanguages.valueOf(langs.get("lang")[0]).getLocale());
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

	private void await() throws IOException {
		try{nanoService.start();
			tray.message(Severity.info, localizer.getValue(KEY_APPLICATION_STARTED));
			latch.await();
		} catch (InterruptedException e) {
			saveSettings();
		} finally {
			nanoService.stop();
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
				final SubstitutableProperties	props = new SubstitutableProperties(DEFAULT_PROPS);
				
				try(final InputStream	is = new FileInputStream(Constants.NANOCHAT_CONFIG)) {
					props.load(is);
					ordinalRun(mdi, localizer, props, false);
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
		final SubstitutableProperties	props = new SubstitutableProperties(DEFAULT_PROPS);
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
			
			props.setProperty(Constants.PROP_ANARCH_SUBNETS, NanoChatUtils.itemAndSelection2String(form.addr));
			props.setProperty(Constants.PROP_ANARCH_DISTRICT, form.district);
			
			NanoChatUtils.prepareNanoChatDirectory(Constants.NANOCHAT_DIRECTORY);
			try(final OutputStream	fos = new FileOutputStream(Constants.NANOCHAT_CONFIG)) {
				
				props.store(fos, "");
				ordinalRun(mdi, localizer, props, true);
			} catch (IOException e) {
				throw new ContentException(e.getLocalizedMessage(), e); 
			}
		}
		else {
			System.err.println("Cancel");
		}
	}

	private static void ordinalRun(final ContentMetadataInterface mdi, final Localizer localizer, final SubstitutableProperties props, final boolean startHelp) throws IOException {
		final Set<String>	names = new HashSet<>();
		final int			discoveryPort = props.getProperty(Constants.PROP_GENERAL_DISCOVERY_PORT, int.class);
		final int			tcpPort = props.getProperty(Constants.PROP_GENERAL_DISCOVERY_PORT, int.class);
		final int			maintenanceTime = props.getProperty(Constants.PROP_GENERAL_DISCOVERY_MAINTENANCE_TIME, int.class);

		names.add(props.getProperty(Constants.PROP_ANARCH_DISTRICT));
		
		try(final Application			app = new Application(mdi, localizer, props);
			final ClientDiscovery		discovery = new ClientDiscovery(props)) {

			if (startHelp) {
				SimpleTimerTask.start(()->app.browseScreen("/static/index.cre"), 1000);
			}
			app.await();
		} catch (EnvironmentException | ContentException e) {
			throw new IOException(e.getLocalizedMessage(), e);
		}
	}
}
