package chav1961.nanochat.common.discovery;

import java.io.Serializable;
import java.util.UUID;

public class BroadcastInfo implements Serializable {
	private static final long serialVersionUID = -2022340209073105069L;
	
	public final UUID	citizenUUID;
	public final String	name;
	public final String	district;
	public final int	discoveryPort;

	public BroadcastInfo(final UUID citizenUUID, final String name, final String district, final int discoveryPort) {
		if (citizenUUID == null) {
			throw new NullPointerException("Citizen ID can't be null");
		}
		else if (name == null || name.isEmpty()) {
			throw new IllegalArgumentException("Name can't be null or empty string");
		}
		else if (district == null || district.isEmpty()) {
			throw new IllegalArgumentException("District can't be null or empty string");
		}
		else {
			this.citizenUUID = citizenUUID;
			this.name = name;
			this.district = district;
			this.discoveryPort = discoveryPort;
		}
	}

	@Override
	public String toString() {
		return "BroadcastInfo [citizenUUID=" + citizenUUID + ", name=" + name + ", district=" + district + ", discoveryPort=" + discoveryPort + "]";
	}
}