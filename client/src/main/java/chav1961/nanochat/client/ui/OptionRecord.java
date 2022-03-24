package chav1961.nanochat.client.ui;

public class OptionRecord<Option extends Enum<?>, OptionValue extends Enum<?>> {
	public final Option	option;
	public OptionValue	value;

	public OptionRecord(final Option option, final OptionValue value) {
		if (option == null) {
			throw new NullPointerException("Option can't be null");
		}
		else if (value == null) {
			throw new NullPointerException("Option value can't be null");
		}
		else {
			this.option = option;
			this.value = value;
		}
	}

	@Override
	public String toString() {
		return "OptionRecord [option=" + option + ", value=" + value + "]";
	}
}
