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
@LocaleResource(value="BandForm.caption",tooltip="BandForm.caption.tt",help="BandForm.help")
public class BandForm implements FormManager<Object,BandForm>, ModuleAccessor, Committer {
	private final LoggerFacade	logger;
	private final SubstitutableProperties props;

	@LocaleResource(value="GeneralForm.name",tooltip="GeneralForm.name.tt")
	@Format("30mo")
	public String				name;
	
	@LocaleResource(value="GeneralForm.district",tooltip="GeneralForm.district.tt")
	@Format("30mo")
	public String				district;
	
	@LocaleResource(value="GeneralForm.lang",tooltip="GeneralForm.lang.tt")
	@Format("30m")
	public SupportedLanguages	defaultLang;
	
	@LocaleResource(value="GeneralForm.useEn",tooltip="GeneralForm.useEn.tt")
	@Format("30m")
	public boolean				useEnInTray;
	
	@LocaleResource(value="GeneralForm.addr",tooltip="GeneralForm.addr.tt")
	@Format("30*5m")
	public ItemAndSelection<InetAddress>[]	addr;
	
	BandForm(final LoggerFacade logger, final SubstitutableProperties props) {
		if (logger == null) {
			throw new NullPointerException("Logger can't be null");
		}
		else {
			this.logger = logger;
			this.props = props;
			
			this.name = props.getProperty(Constants.PROP_GENERAL_NAME);
			this.district = props.getProperty(Constants.PROP_ANARCH_DISTRICT);
			this.defaultLang = props.getProperty(Constants.PROP_GENERAL_DEFAULT_LANG, SupportedLanguages.class);
			this.useEnInTray = props.getProperty(Constants.PROP_GENERAL_TRAY_LANG_EN, boolean.class);
			this.addr = NanoChatUtils.string2ItemAndSelection(props.getProperty(Constants.PROP_ANARCH_SUBNETS));
		}
	}
	
	@Override
	public RefreshMode onField(final BandForm inst, final Object id, final String fieldName, final Object oldValue, final boolean beforeCommit) throws FlowException, LocalizationException {
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
		props.setProperty(Constants.PROP_GENERAL_DEFAULT_LANG, defaultLang.name());
		props.setProperty(Constants.PROP_GENERAL_TRAY_LANG_EN, String.valueOf(useEnInTray));
		props.setProperty(Constants.PROP_ANARCH_SUBNETS, NanoChatUtils.itemAndSelection2String(addr));
	}
}
