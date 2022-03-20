package chav1961.nanochat.client.settings;

import java.net.InetAddress;

import chav1961.nanochat.client.interfaces.Committer;
import chav1961.nanochat.common.Constants;
import chav1961.nanochat.common.NanoChatUtils;
import chav1961.purelib.basic.SubstitutableProperties;
import chav1961.purelib.basic.exceptions.ContentException;
import chav1961.purelib.basic.exceptions.FlowException;
import chav1961.purelib.basic.exceptions.LocalizationException;
import chav1961.purelib.basic.interfaces.LoggerFacade;
import chav1961.purelib.basic.interfaces.ModuleAccessor;
import chav1961.purelib.i18n.interfaces.LocaleResource;
import chav1961.purelib.i18n.interfaces.LocaleResourceLocation;
import chav1961.purelib.i18n.interfaces.SupportedLanguages;
import chav1961.purelib.ui.interfaces.FormManager;
import chav1961.purelib.ui.interfaces.Format;
import chav1961.purelib.ui.interfaces.ItemAndSelection;
import chav1961.purelib.ui.interfaces.RefreshMode;

@LocaleResourceLocation("i18n:xml:root://chav1961.nanochat.client.anarchy.TheSameFirstForm/chav1961/nanochat/client/i18n.xml")
@LocaleResource(value="NetworkForm.caption",tooltip="NetworkForm.caption.tt",help="NetworkForm.help")
public class NetworkForm implements FormManager<Object,NetworkForm>, ModuleAccessor, Committer {
	private final LoggerFacade	logger;
	private final SubstitutableProperties props;

	
	public static final String	PROP_GENERAL_DISCOVERY_PORT = "discoveryPort";
	public static final String	PROP_GENERAL_DISCOVERY_MAINTENANCE_TIME = "discoveryMaintenanceTime";
	public static final String	PROP_GENERAL_TCP_PORT = "tcpPort";
	public static final String	PROP_GENERAL_HTTP_PORT = "httpPort";
	
	
	@LocaleResource(value="NetworkForm.discoveryPort",tooltip="NetworkForm.discoveryPort.tt")
	@Format("30ms")
	public int					discoveryPort;

	@LocaleResource(value="NetworkForm.tcpPort",tooltip="NetworkForm.tcpPort.tt")
	@Format("30ms")
	public int					tcpPort;
	
	@LocaleResource(value="NetworkForm.httpPort",tooltip="NetworkForm.httpPort.tt")
	@Format("30ms")
	public int					httpPort;

	@LocaleResource(value="NetworkForm.discoveryMaintenanceTime",tooltip="NetworkForm.discoveryMaintenanceTime.tt")
	@Format("30ms")
	public int					discoveryMaintenanceTime;
	
	NetworkForm(final LoggerFacade logger, final SubstitutableProperties props) {
		if (logger == null) {
			throw new NullPointerException("Logger can't be null");
		}
		else {
			this.logger = logger;
			this.props = props;
			
			this.discoveryPort = props.getProperty(Constants.PROP_GENERAL_DISCOVERY_PORT, int.class);
			this.tcpPort = props.getProperty(Constants.PROP_GENERAL_TCP_PORT, int.class);
			this.httpPort = props.getProperty(Constants.PROP_GENERAL_HTTP_PORT, int.class);
			this.discoveryMaintenanceTime = props.getProperty(Constants.PROP_GENERAL_DISCOVERY_MAINTENANCE_TIME, int.class);
		}
	}
	
	@Override
	public RefreshMode onField(final NetworkForm inst, final Object id, final String fieldName, final Object oldValue, final boolean beforeCommit) throws FlowException, LocalizationException {
		return RefreshMode.DEFAULT;
	}

	@Override
	public LoggerFacade getLogger() {
		return logger;
	}

	@Override
	public void allowUnnamedModuleAccess(final Module... unnamedModules) {
		for (Module item : unnamedModules) {
			this.getClass().getModule().addExports(this.getClass().getPackageName(), item);
		}
	}

	@Override
	public void commit() throws ContentException {
		props.setProperty(Constants.PROP_GENERAL_DISCOVERY_PORT, String.valueOf(this.discoveryPort));
		props.setProperty(Constants.PROP_GENERAL_TCP_PORT, String.valueOf(this.tcpPort));
		props.setProperty(Constants.PROP_GENERAL_HTTP_PORT, String.valueOf(this.httpPort));
		props.setProperty(Constants.PROP_GENERAL_DISCOVERY_MAINTENANCE_TIME, String.valueOf(this.discoveryMaintenanceTime));
	}
}
