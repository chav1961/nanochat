package chav1961.nanochat.client.settings;

import java.util.Locale;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTabbedPane;

import chav1961.nanochat.client.interfaces.Committer;
import chav1961.nanochat.client.interfaces.TabContent;
import chav1961.nanochat.common.Constants;
import chav1961.purelib.basic.SubstitutableProperties;
import chav1961.purelib.basic.exceptions.ContentException;
import chav1961.purelib.basic.exceptions.LocalizationException;
import chav1961.purelib.basic.interfaces.LoggerFacade;
import chav1961.purelib.basic.interfaces.LoggerFacadeOwner;
import chav1961.purelib.i18n.interfaces.Localizer;
import chav1961.purelib.i18n.interfaces.Localizer.LocaleChangeListener;
import chav1961.purelib.model.interfaces.ContentMetadataInterface;
import chav1961.purelib.model.interfaces.ContentMetadataInterface.ContentNodeMetadata;
import chav1961.purelib.model.interfaces.NodeMetadataOwner;
import chav1961.purelib.ui.swing.SwingUtils;
import chav1961.purelib.ui.swing.useful.JCloseableTab;

public class SettingsWindow extends JTabbedPane implements NodeMetadataOwner, LoggerFacadeOwner, LocaleChangeListener, Committer {
	private static final long serialVersionUID = 3020464704686221571L;

	private final ContentMetadataInterface	mdi;
	private final Localizer 				localizer;
	private final LoggerFacade 				logger;
	private final SubstitutableProperties 	props;
	private final GeneralTab				generalTab;
	private BandTab							bandTab = null;
	private MafiaTab						mafiaTab = null;
	
	public SettingsWindow(final ContentMetadataInterface mdi, final Localizer localizer, final LoggerFacade logger, final SubstitutableProperties props) throws NullPointerException, ContentException {
		if (mdi == null) {
			throw new NullPointerException("Metadata can't be null"); 
		}
		else if (localizer == null) {
			throw new NullPointerException("Localizer can't be null"); 
		}
		else if (logger == null) {
			throw new NullPointerException("Logger can't be null"); 
		}
		else if (props == null) {
			throw new NullPointerException("Properties can't be null"); 
		}
		else {
			this.mdi = mdi;
			this.localizer = localizer;
			this.logger = logger;
			this.props = props;
			this.generalTab = new GeneralTab(this, localizer, props);

			placeTab(this.generalTab);
			
			if (props.getProperty(Constants.PROP_GENERAL_USE_BAND_MODE, boolean.class)) {
				this.bandTab = new BandTab(this, localizer, props);

				placeTab(this.bandTab);
			}
			if (props.getProperty(Constants.PROP_GENERAL_USE_MAFIA_MODE, boolean.class)) {
				this.mafiaTab = new MafiaTab(this, localizer, props);

				placeTab(this.mafiaTab);
			}
			setSelectedIndex(0);
		}
	}

	@Override
	public ContentNodeMetadata getNodeMetadata() {
		return mdi.getRoot();
	}

	@Override
	public LoggerFacade getLogger() {
		return logger;
	}

	@Override
	public void localeChanged(final Locale oldLocale, final Locale newLocale) throws LocalizationException {
		generalTab.localeChanged(oldLocale, newLocale);
		if (bandTab != null) {
			bandTab.localeChanged(oldLocale, newLocale);
		}
		if (mafiaTab != null) {
			mafiaTab.localeChanged(oldLocale, newLocale);
		}
	}

	@Override
	public void commit() throws ContentException {
		generalTab.commit();
		if (bandTab != null) {
			bandTab.commit();
			props.setProperty(Constants.PROP_GENERAL_USE_BAND_MODE, "true");
		}
		else {
			props.setProperty(Constants.PROP_GENERAL_USE_BAND_MODE, "false");
		}
		if (mafiaTab != null) {
			mafiaTab.commit();
			props.setProperty(Constants.PROP_GENERAL_USE_MAFIA_MODE, "true");
		}
		else {
			props.setProperty(Constants.PROP_GENERAL_USE_MAFIA_MODE, "false");
		}
	}
	
	void bandModeOn() throws NullPointerException, ContentException {
		if (bandTab == null) {
			bandTab = new BandTab(this, localizer, props);
			placeTab(bandTab);
		}
	}
	
	void bandModeOff() {
		removeTabAt(tabContent2TabIndex(bandTab));
		bandTab = null;
	}

	void mafiaModeOn() throws NullPointerException, ContentException {
		if (mafiaTab == null) {
			mafiaTab = new MafiaTab(this, localizer, props);
			placeTab(mafiaTab);
		}
	}
	
	void mafiaModeOff() {
		removeTabAt(tabContent2TabIndex(mafiaTab));
		mafiaTab = null;
	}
	
	private void placeTab(final JPanel tab) {
		if (tab instanceof TabContent) {
			final JCloseableTab	label = ((TabContent)tab).getTab();
			final JPopupMenu	menu = ((TabContent)tab).getPopupMenu();
			
			if (menu != null) {
				label.associate(this, tab, menu);
				SwingUtils.assignActionListeners(menu, tab);
			}
			else {
				label.associate(this, tab);
			}
			
			label.setCloseEnable(true);
			addTab("",tab);
			setTabComponentAt(getTabCount()-1,label);
			setSelectedIndex(getTabCount()-1);
		}
		else {
			throw new UnsupportedOperationException(); 
		}
	}

	private int tabContent2TabIndex(final JComponent content) {
	    for (int index = 0; index < getTabCount(); index++) {
	    	if (getComponentAt(index).equals(content)) {
	    		return index;
	    	}
	    }
	    return -1;
	}
}
