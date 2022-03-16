package chav1961.nanochat.client.anarchy;

import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JLabel;

import chav1961.purelib.basic.exceptions.FlowException;
import chav1961.purelib.basic.exceptions.LocalizationException;
import chav1961.purelib.i18n.interfaces.Localizer;
import chav1961.purelib.ui.interfaces.ErrorProcessing;
import chav1961.purelib.ui.interfaces.WizardStep;

public class TheSameFirstTab implements WizardStep<TheSameFirstTab, SingleWizardStep, JComponent> {
	public static final String	KEY_CAPTION = TheSameFirstTab.class.getSimpleName()+".caption";
	public static final String	KEY_DESCRIPTION = TheSameFirstTab.class.getSimpleName()+".description";
	public static final String	KEY_HELP = TheSameFirstTab.class.getSimpleName()+".help";
	
	private final Localizer		localizer;
	
	public TheSameFirstTab(final Localizer localizer) {
		if (localizer == null) {
			throw new NullPointerException("Localizer can't be null");
		}
		else {
			this.localizer = localizer;
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
		// TODO Auto-generated method stub
		return new JLabel("sdsds");
	}

	@Override
	public void beforeShow(TheSameFirstTab content, Map<String, Object> temporary, ErrorProcessing<TheSameFirstTab, SingleWizardStep> err) throws FlowException, LocalizationException, NullPointerException {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean validate(TheSameFirstTab content, Map<String, Object> temporary, ErrorProcessing<TheSameFirstTab, SingleWizardStep> err) throws FlowException, LocalizationException, NullPointerException {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void afterShow(TheSameFirstTab content, Map<String, Object> temporary, ErrorProcessing<TheSameFirstTab, SingleWizardStep> err) throws FlowException, LocalizationException, NullPointerException {
		// TODO Auto-generated method stub
	}
}
