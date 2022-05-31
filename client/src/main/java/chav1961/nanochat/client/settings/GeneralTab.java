package chav1961.nanochat.client.settings;

import java.awt.BorderLayout;
import java.util.Locale;

import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import chav1961.nanochat.client.interfaces.Committer;
import chav1961.nanochat.client.interfaces.TabContent;
import chav1961.purelib.basic.PureLibSettings;
import chav1961.purelib.basic.SubstitutableProperties;
import chav1961.purelib.basic.exceptions.ContentException;
import chav1961.purelib.basic.exceptions.LocalizationException;
import chav1961.purelib.i18n.interfaces.Localizer;
import chav1961.purelib.i18n.interfaces.Localizer.LocaleChangeListener;
import chav1961.purelib.model.ContentModelFactory;
import chav1961.purelib.model.interfaces.ContentMetadataInterface;
import chav1961.purelib.ui.swing.AutoBuiltForm;
import chav1961.purelib.ui.swing.useful.JCloseableTab;

public class GeneralTab extends JPanel implements TabContent, LocaleChangeListener, Committer {
	private static final long serialVersionUID = -3700729728528011664L;
	
	private final SettingsWindow				parent;
	private final Localizer						localizer;
	private final SubstitutableProperties		props;
	private final GeneralForm					generalForm;
	private final ContentMetadataInterface		mdi;
	private final AutoBuiltForm<GeneralForm,?>	abf;
	
	GeneralTab(final SettingsWindow parent, final Localizer localizer, final SubstitutableProperties props) throws NullPointerException, ContentException {
		super(new BorderLayout(5,5));
		if (parent == null) {
			throw new NullPointerException("Parent can't be null"); 
		}
		else if (localizer == null) {
			throw new NullPointerException("Localizer can't be null"); 
		}
		else if (props == null) {
			throw new NullPointerException("Properties can't be null"); 
		}
		else {
			this.parent = parent;
			this.localizer = localizer;
			this.props = props;
			this.mdi = ContentModelFactory.forAnnotatedClass(GeneralForm.class);
			this.generalForm = new GeneralForm(parent, parent.getLogger(), props);
			
			abf = new AutoBuiltForm<GeneralForm,Object>(mdi, localizer, PureLibSettings.INTERNAL_LOADER, generalForm, generalForm);
			add(abf, BorderLayout.CENTER);
		}
	}
	
	@Override
	public JPopupMenu getPopupMenu() {
		return null;
	}

	@Override
	public JCloseableTab getTab() {
		final JCloseableTab	result = new JCloseableTab(localizer, mdi.getRoot().getLabelId(), false);
		
		if (mdi.getRoot().getTooltipId() != null) {
			result.setToolTipText(mdi.getRoot().getTooltipId());
		}
		return result; 
	}

	@Override
	public void localeChanged(final Locale oldLocale, final Locale newLocale) throws LocalizationException {
		abf.localeChanged(oldLocale, newLocale);
	}

	@Override
	public void commit() throws ContentException {
		generalForm.commit();
	}
}
