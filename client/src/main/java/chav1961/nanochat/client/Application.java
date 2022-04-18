package chav1961.nanochat.client;


import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import chav1961.nanochat.client.anarchy.SingleWizardStep;
import chav1961.nanochat.client.anarchy.TheSameFirstForm;
import chav1961.nanochat.client.anarchy.TheSameFirstTab;
import chav1961.nanochat.client.db.DbManagement;
import chav1961.nanochat.client.db.DbManager;
import chav1961.nanochat.client.settings.SettingsWindow;
import chav1961.nanochat.client.ui.UIPainter;
import chav1961.nanochat.common.Constants;
import chav1961.nanochat.common.NanoChatUtils;
import chav1961.nanochat.common.discovery.BroadcastInfo;
import chav1961.nanochat.common.discovery.Citizen;
import chav1961.nanochat.common.discovery.NanoChatDiscovery;
import chav1961.purelib.basic.PureLibSettings;
import chav1961.purelib.basic.SimpleTimerTask;
import chav1961.purelib.basic.SubstitutableProperties;
import chav1961.purelib.basic.exceptions.ContentException;
import chav1961.purelib.basic.exceptions.EnvironmentException;
import chav1961.purelib.basic.exceptions.LocalizationException;
import chav1961.purelib.basic.exceptions.PreparationException;
import chav1961.purelib.basic.exceptions.SyntaxException;
import chav1961.purelib.basic.interfaces.LoggerFacade;
import chav1961.purelib.basic.interfaces.LoggerFacade.Severity;
import chav1961.purelib.basic.interfaces.LoggerFacadeOwner;
import chav1961.purelib.fsys.interfaces.FileSystemInterface;
import chav1961.purelib.i18n.interfaces.Localizer;
import chav1961.purelib.i18n.interfaces.Localizer.LocaleChangeListener;
import chav1961.purelib.i18n.interfaces.SupportedLanguages;
import chav1961.purelib.model.ContentModelFactory;
import chav1961.purelib.model.interfaces.ContentMetadataInterface;
import chav1961.purelib.model.interfaces.ContentMetadataInterface.ContentNodeMetadata;
import chav1961.purelib.model.interfaces.NodeMetadataOwner;
import chav1961.purelib.nanoservice.NanoServiceFactory;
import chav1961.purelib.net.DiscoveryEvent;
import chav1961.purelib.net.LightWeightNetworkDiscovery.MediaItemDescriptorImpl;
import chav1961.purelib.net.interfaces.DiscoveryEventType;
import chav1961.purelib.net.interfaces.DiscoveryListener;
import chav1961.purelib.sql.model.SimpleDatabaseManager;
import chav1961.purelib.sql.model.SimpleDatabaseModelManagement;
import chav1961.purelib.sql.model.SimpleDottedVersion;
import chav1961.purelib.sql.model.interfaces.DatabaseManagement;
import chav1961.purelib.sql.model.interfaces.DatabaseModelManagement;
import chav1961.purelib.ui.interfaces.ErrorProcessing;
import chav1961.purelib.ui.swing.SwingUtils;
import chav1961.purelib.ui.swing.interfaces.OnAction;
import chav1961.purelib.ui.swing.useful.JDialogContainer;
import chav1961.purelib.ui.swing.useful.JLocalizedOptionPane;
import chav1961.purelib.ui.swing.useful.JSystemTray;

//https://www.shubhamdipt.com/blog/how-to-create-a-systemd-service-in-linux/

public class Application implements AutoCloseable, LocaleChangeListener, NodeMetadataOwner, LoggerFacadeOwner {
	public static final String	KEY_APPLICATION_NAME = "application.name";
	public static final String	KEY_APPLICATION_TOOLTIP = "application.name.tt";
	public static final String	KEY_APPLICATION_STARTED = "application.started";
	public static final String	KEY_APPLICATION_CONFIRM_EXIT = "application.confirm.exit";
	public static final String	KEY_APPLICATION_CONFIRM_EXIT_TITLE = "application.confirm.exit.title";

	public static final String	PATH_UI_PAINTER = "/gui";
	
	private static final SubstitutableProperties	DEFAULT_PROPS = new SubstitutableProperties();
	
	static {
		try(final InputStream	is = Application.class.getResourceAsStream("/nanochat.client.default.properties")) {
			
			DEFAULT_PROPS.load(is);
		} catch (IOException e) {
			throw new PreparationException(e.getLocalizedMessage(), e);
		}
	}
	
	private final ContentMetadataInterface	mdi;
	private final Connection				dbConn;
	private final Localizer					localizer;
	private final SubstitutableProperties	props;
	private final JPopupMenu				popup;	
	private final JSystemTray				tray;
	private final UIPainter					painter;
	private final SimpleDatabaseManager<SimpleDottedVersion>	mgr;
	private final DatabaseModelManagement<SimpleDottedVersion>	modelMgmt;
	private final CountDownLatch			latch = new CountDownLatch(1);
	private final NanoServiceFactory		nanoService;
	private final DbManagement				dbm;
	private final NanoChatDiscovery<?>		discovery;
	private final DiscoveryListener			dl = this::processDiscoveryEvent;
	private final Map<UUID,Citizen>			citizenList = new HashMap<>();

	private Application(final ContentMetadataInterface mdi, final Localizer localizer, final SubstitutableProperties props) throws EnvironmentException, SyntaxException, NullPointerException, ContentException, IOException {
		this.mdi = mdi;
		this.localizer = localizer;
		this.props = props;
		
		this.popup = SwingUtils.toJComponent(mdi.byUIPath(URI.create("ui:/model/navigation.top.traymenu")),JPopupMenu.class);
		SwingUtils.assignActionListeners(popup,this);
		
		this.tray = new JSystemTray(localizer, KEY_APPLICATION_NAME, URI.create("root://"+Application.class.getName()+"/images/trayicon.png"), KEY_APPLICATION_TOOLTIP, popup, props.getProperty(Constants.PROP_GENERAL_TRAY_LANG_EN, boolean.class));
		this.tray.addActionListener((e)->browseScreen());
		this.dbm = new DbManagement(tray);
		this.discovery = new NanoChatDiscovery<>(props);
		
		localizer.addLocaleChangeListener(this);
		
		try(final InputStream	is = this.getClass().getResourceAsStream("./db/model.json");
			final Reader		rdr = new InputStreamReader(is, PureLibSettings.DEFAULT_CONTENT_ENCODING)) {
			
			this.dbConn = DriverManager.getConnection("jdbc:sqlite:"+Constants.NANOCHAT_DATABASE.getAbsolutePath().replace(File.separatorChar, '/'));
			this.modelMgmt = new SimpleDatabaseModelManagement(DbManager.class.getResource("model.json").toURI());
			this.mgr = new DbManager(tray, this.modelMgmt.getModel(this.modelMgmt.size()-1), this::getConnection, this::getDbManagement);
		} catch (SQLException | URISyntaxException e) {
			throw new EnvironmentException(e); 
		}
		
		this.nanoService = new NanoServiceFactory(PureLibSettings.CURRENT_LOGGER, props);
		this.painter = new UIPainter(this, FileSystemInterface.Factory.newInstance(URI.create(FileSystemInterface.FILESYSTEM_URI_SCHEME+":file:./")), localizer, tray);
		this.nanoService.deploy(PATH_UI_PAINTER, painter);
		
		this.discovery.addDiscoveryListener(dl);
		this.discovery.start();
	}
	
	@Override
	public void close() throws EnvironmentException {
		try{nanoService.undeploy(PATH_UI_PAINTER);
			painter.close();
			nanoService.close();
			mgr.close();
			discovery.stop();
			discovery.removeDiscoveryListener(dl);
			discovery.close();
		} catch (IOException | SQLException e) {
			tray.message(Severity.severe, e.getLocalizedMessage());
		}
		localizer.removeLocaleChangeListener(this);
		tray.close();
	}

	@Override
	public void localeChanged(final Locale oldLocale, final Locale newLocale) throws LocalizationException {
		tray.localeChanged(oldLocale, newLocale);
	}

	@Override
	public ContentNodeMetadata getNodeMetadata() {
		return mdi.getRoot();
	}
	
	@Override
	public LoggerFacade getLogger() {
		return tray;
	}
	
	@FunctionalInterface
	public interface CitizenCallback {
		void process(Citizen c) throws IOException;
	}
	
	public void forAllCitizens(final CitizenCallback callback) throws IOException {
		if (callback == null) {
			throw new NullPointerException("Callback can't be null");
		}
		else {
			synchronized (citizenList) {
				for (Entry<UUID, Citizen> item : citizenList.entrySet()) {
					callback.process(item.getValue());
				}
			}
		}
	}
	
	private void browseScreen() {
		browseScreen(PATH_UI_PAINTER+"/index");
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
	private void paused(final Hashtable<String,String[]> langs) {
		try{if (!discovery.isSuspended()) {
				discovery.suspend();
			}
			else {
				discovery.resume();
			}
		} catch (IOException e) {
			getLogger().message(Severity.warning, e.getLocalizedMessage());
		}
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
			getLogger().message(Severity.warning, e.getLocalizedMessage());
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

	private Connection getConnection() {
		return dbConn;
	}

	private DatabaseManagement<SimpleDottedVersion> getDbManagement(final Connection conn) throws SQLException {
		return dbm;
	}

	private void processDiscoveryEvent(final DiscoveryEvent event) {
		System.err.println("Event: "+event);
		switch (event.getEventType()) {
			case INFO		:
				break;
			case START		:
				final BroadcastInfo	startBroadcast = (BroadcastInfo)event.getSource();
				
				synchronized(citizenList) {
					if (startBroadcast.citizenUUID.equals(props.getProperty(Constants.PROP_GENERAL_ID, UUID.class))) {
						citizenList.put(startBroadcast.citizenUUID, 
								new Citizen(event.getDescriptor(), startBroadcast.citizenUUID, startBroadcast.name, startBroadcast.district
											, ((MediaItemDescriptorImpl)event.getDescriptor()).getAddress(), startBroadcast.discoveryPort, true, false));
					}
					else {
						citizenList.put(startBroadcast.citizenUUID, new Citizen(event.getDescriptor(), startBroadcast.citizenUUID, startBroadcast.name, startBroadcast.district
											, ((MediaItemDescriptorImpl)event.getDescriptor()).getAddress(), startBroadcast.discoveryPort, false));
					}
				}
				break;
			case SUSPENDED	:
				final BroadcastInfo	suspendBroadcast = (BroadcastInfo)event.getSource();
				
				synchronized(citizenList) {
					if (citizenList.containsKey(suspendBroadcast.citizenUUID)) {
						citizenList.get(suspendBroadcast.citizenUUID).setSuspended(true);
					}
				}
				break;
			case RESUMED	:
				final BroadcastInfo	resumeBroadcast = (BroadcastInfo)event.getSource();
				
				synchronized(citizenList) {
					if (citizenList.containsKey(resumeBroadcast.citizenUUID)) {
						citizenList.get(resumeBroadcast.citizenUUID).setSuspended(false);
					}
				}
				break;
			case STOP		:
				final BroadcastInfo	stopBroadcast = (BroadcastInfo)event.getSource();
				
				synchronized(citizenList) {
					citizenList.remove(stopBroadcast.citizenUUID);
				}
				break;
			case PING : case PONG : case GET_STATE : case STATE : case QUERY_INFO	:
			default:
				throw new UnsupportedOperationException("Discovery event type ["+event.getEventType()+"] is not supported yet");
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
					ordinalRun(mdi, localizer, props, false, false);
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
				ordinalRun(mdi, localizer, props, true, true);
			} catch (IOException e) {
				throw new ContentException(e.getLocalizedMessage(), e); 
			}
		}
		else {
			System.err.println("Cancel");
		}
	}

	private static void ordinalRun(final ContentMetadataInterface mdi, final Localizer localizer, final SubstitutableProperties props, final boolean prepareDatabase, final boolean startHelp) throws IOException {
		final Set<String>	names = new HashSet<>();

		names.add(props.getProperty(Constants.PROP_ANARCH_DISTRICT));
		
		try(final Application			app = new Application(mdi, localizer, props)) {
			final Connection			conn = app.getConnection();

			try{
				if (prepareDatabase) {
					app.getDbManagement(conn).onCreate(conn, app.modelMgmt.getModel(app.modelMgmt.size()-1));
				}
				else {
					final int	action = app.getDbManagement(conn).getDatabaseVersion(conn).compareTo(app.getDbManagement(conn).getVersion(app.modelMgmt.getModel(app.modelMgmt.size()-1)));
					
					if (action > 0) {
						app.getDbManagement(conn).onUpgrade(conn, app.getDbManagement(conn).getDatabaseVersion(conn), app.getDbManagement(conn).getDatabaseModel(conn), app.getDbManagement(conn).getVersion(app.modelMgmt.getModel(app.modelMgmt.size()-1)), app.modelMgmt.getModel(app.modelMgmt.size()-1));
					}
					else if (action < 0) {
						app.getDbManagement(conn).onDowngrade(conn, app.getDbManagement(conn).getDatabaseVersion(conn), app.getDbManagement(conn).getDatabaseModel(conn), app.getDbManagement(conn).getVersion(app.modelMgmt.getModel(app.modelMgmt.size()-1)), app.modelMgmt.getModel(app.modelMgmt.size()-1));
					}
				}
				app.getDbManagement(app.getConnection()).onOpen(conn, app.modelMgmt.getModel(app.modelMgmt.size()-1));
			} catch (SQLException e) {
				app.getLogger().message(Severity.error, e.getLocalizedMessage());
			}
			
			if (startHelp) {
				SimpleTimerTask.start(()->app.browseScreen("/static/index.cre"), 1000);
			}
			
			app.await();
			
			try{
				app.getDbManagement(conn).onClose(conn, app.modelMgmt.getModel(app.modelMgmt.size()-1));
			} catch (SQLException e) {
				app.getLogger().message(Severity.error, e.getLocalizedMessage());
			}
		} catch (EnvironmentException | ContentException e) {
			throw new IOException(e.getLocalizedMessage(), e);
		}
	}
}
