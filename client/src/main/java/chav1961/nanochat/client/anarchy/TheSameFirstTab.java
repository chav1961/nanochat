package chav1961.nanochat.client.anarchy;

import java.util.Map;

import chav1961.purelib.basic.exceptions.FlowException;
import chav1961.purelib.basic.exceptions.LocalizationException;
import chav1961.purelib.ui.interfaces.ErrorProcessing;
import chav1961.purelib.ui.interfaces.WizardStep;

public class TheSameFirstTab implements WizardStep<TheSameFirstTab, SingleWizardStep, TheSameFirstForm> {

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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getHelpId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TheSameFirstForm getContent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void beforeShow(TheSameFirstTab content, Map<String, Object> temporary, ErrorProcessing<TheSameFirstTab, SingleWizardStep> err) throws FlowException, LocalizationException, NullPointerException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean validate(TheSameFirstTab content, Map<String, Object> temporary, ErrorProcessing<TheSameFirstTab, SingleWizardStep> err) throws FlowException, LocalizationException, NullPointerException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void afterShow(TheSameFirstTab content, Map<String, Object> temporary, ErrorProcessing<TheSameFirstTab, SingleWizardStep> err) throws FlowException, LocalizationException, NullPointerException {
		// TODO Auto-generated method stub
		
	}

}
