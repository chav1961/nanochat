package chav1961.nanochat.client.ui;

import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.util.UUID;

import chav1961.nanochat.client.ui.interfaces.CitizenOptions;
import chav1961.nanochat.client.ui.interfaces.JavaScriptKeeper;
import chav1961.nanochat.client.ui.interfaces.NavigationTreeRoleKeeper;
import chav1961.nanochat.client.ui.interfaces.NavigatorTreeRole;
import chav1961.purelib.basic.exceptions.ContentException;

public class CitizenRecord implements NavigationTreeRoleKeeper, JavaScriptKeeper, Serializable {
	private static final long 							serialVersionUID = 6708014789073165043L;

	private final UUID				id;
	private final URI				icon;
	private final String			name;
	private final String			district;
	private final NavigatorTreeRole	role;
	private final OptionList<CitizenOptions, Enum>	options;

	public CitizenRecord(final UUID id, final URI icon, final String name, final String district, final OptionList<CitizenOptions, Enum> options) {
		this(false, id, icon, name, district, options);
	}
	
	public CitizenRecord(final boolean isMyself, final UUID id, final URI icon, final String name, final String district, final OptionList<CitizenOptions, Enum> options) {
		this.id = id;
		this.name = name;
		this.icon = icon;
		this.district = district;
		this.options = options;
		this.role = isMyself ? NavigatorTreeRole.MYSELF : NavigatorTreeRole.CITIZEN;
	}
	
	@Override
	public NavigatorTreeRole getRole() {
		return role;
	}

	public UUID getId() {
		return id;
	}
	
	public URI getIcon() {
		return icon;
	}

	public String getName() {
		return name;
	}

	public String getDistrict() {
		return district;
	}

	public <T extends Enum<?>> T getOption(final CitizenOptions option, final Class<T> awaited) {
		return awaited.cast(options.get(option));
	}

	public <T extends Enum<?>> void setOption(final CitizenOptions option, final T value) {
		options.put(option, value);
	}

	@Override
	public void getJavaScript(final Appendable app) throws ContentException {
		try{app.append("{\"type\":\"").append(this.getClass().getSimpleName()).append("\",\"id\":\"")
			   .append(id.toString()).append("\",\"icon\":\"")
			   .append(icon.toString()).append("\",\"name\":\"")
			   .append(name).append("\",\"district\":\"")
			   .append(district).append("\",\"role\":");
			role.getJavaScript(app);
		   	app.append(",\"options\":");
			options.getJavaScript(app);
			app.append("}");
		} catch (IOException e) {
			throw new ContentException(e.getLocalizedMessage(), e); 
		}
	}
	
	@Override
	public String toString() {
		return "CitizenRecord [id=" + id + ", name=" + name + ", district=" + district + ", options=" + options + "]";
	}
}	
