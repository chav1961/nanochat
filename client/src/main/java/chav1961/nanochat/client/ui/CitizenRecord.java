package chav1961.nanochat.client.ui;

import java.net.URI;
import java.util.Map;
import java.util.UUID;

import chav1961.nanochat.client.ui.interfaces.CitizenOptions;
import chav1961.nanochat.client.ui.interfaces.JavaScriptKeeper;
import chav1961.nanochat.client.ui.interfaces.NavigationTreeRoleKeeper;
import chav1961.nanochat.client.ui.interfaces.NavigatorTreeRole;
import chav1961.purelib.basic.exceptions.ContentException;

public class CitizenRecord implements NavigationTreeRoleKeeper, JavaScriptKeeper {
	private final UUID				id;
	private final URI				icon;
	private final String			name;
	private final String			district;
	private final NavigatorTreeRole	role;
	private final Map<CitizenOptions, Enum>	options;

	public CitizenRecord(UUID id, URI icon, String name, String district, Map<CitizenOptions, Enum> options) {
		this(false, id, icon, name, district, options);
	}
	
	public CitizenRecord(boolean isMyself, UUID id, URI icon, String name, String district, Map<CitizenOptions, Enum> options) {
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
	public String getJavaScript() throws ContentException {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String toString() {
		return "CitizenRecord [id=" + id + ", name=" + name + ", district=" + district + ", options=" + options + "]";
	}
}	
