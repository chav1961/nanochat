package chav1961.nanochat.client.anarchy;

import java.awt.Dimension;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import chav1961.purelib.basic.PureLibSettings;
import chav1961.purelib.basic.exceptions.ContentException;
import chav1961.purelib.basic.exceptions.FlowException;
import chav1961.purelib.basic.exceptions.LocalizationException;
import chav1961.purelib.basic.interfaces.LoggerFacade;
import chav1961.purelib.i18n.interfaces.Localizer;
import chav1961.purelib.model.ContentModelFactory;
import chav1961.purelib.model.interfaces.ContentMetadataInterface;
import chav1961.purelib.ui.interfaces.ErrorProcessing;
import chav1961.purelib.ui.interfaces.WizardStep;
import chav1961.purelib.ui.swing.AutoBuiltForm;

public class TheSameFirstTab implements WizardStep<TheSameFirstTab, SingleWizardStep, JComponent> {
	public static final String	KEY_CAPTION = TheSameFirstTab.class.getSimpleName()+".caption";
	public static final String	KEY_DESCRIPTION = TheSameFirstTab.class.getSimpleName()+".description";
	public static final String	KEY_HELP = TheSameFirstTab.class.getSimpleName()+".help";
	
	private final Localizer			localizer;
	private final TheSameFirstForm	form;
	private final ContentMetadataInterface			mdi;
	private final AutoBuiltForm<TheSameFirstForm>	abf;
	
	public TheSameFirstTab(final Localizer localizer, final LoggerFacade logger) throws ContentException {
		if (localizer == null) {
			throw new NullPointerException("Localizer can't be null");
		}
		else {
			this.localizer = localizer;
			this.form = new TheSameFirstForm(logger);
			this.mdi = ContentModelFactory.forAnnotatedClass(TheSameFirstForm.class);
			this.abf = new AutoBuiltForm<>(mdi, localizer, PureLibSettings.INTERNAL_LOADER, form, form);
			this.abf.setPreferredSize(new Dimension(400,300));
		}
	}
	
	@Override
	public String getStepId() {
		return this.getClass().getSimpleName();
	}

	@Override
	public StepType getStepType() {
		return StepType.THE_ONLY;
	}

	@Override
	public String getCaption() {
		return KEY_CAPTION;
	}

	@Override
	public String getDescription() {
		return KEY_DESCRIPTION;
	}

	@Override
	public String getHelpId() {
		return KEY_HELP;
	}

	@Override
	public JComponent getContent() {
		return abf;
	}

	@Override
	public void beforeShow(final TheSameFirstTab content, final Map<String, Object> temporary, final ErrorProcessing<TheSameFirstTab, SingleWizardStep> err) throws FlowException, LocalizationException, NullPointerException {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean validate(final TheSameFirstTab content, final Map<String, Object> temporary, final ErrorProcessing<TheSameFirstTab, SingleWizardStep> err) throws FlowException, LocalizationException, NullPointerException {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void afterShow(final TheSameFirstTab content, final Map<String, Object> temporary, final ErrorProcessing<TheSameFirstTab, SingleWizardStep> err) throws FlowException, LocalizationException, NullPointerException {
		// TODO Auto-generated method stub
	}
	
	public TheSameFirstForm getForm() {
		return form;
	}
}
