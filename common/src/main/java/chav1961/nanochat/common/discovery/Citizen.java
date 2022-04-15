package chav1961.nanochat.common.discovery;

import java.util.UUID;

import chav1961.purelib.net.interfaces.MediaItemDescriptor;

public class Citizen {
	private final MediaItemDescriptor	mid;
	private final UUID 		citizenId;
	private final String	citizenName;
	private final String	citizenDistrict;
	private final int		discoveryPort;
	private final boolean	isMyself;
	private boolean			isSuspended;

	public Citizen(final MediaItemDescriptor mid, final UUID citizenId, final String citizenName, final String citizenDistrict, final int discoveryPort, final boolean isSuspended) {
		this(mid, citizenId, citizenName, citizenDistrict, discoveryPort, false, isSuspended);
	}
	
	public Citizen(final MediaItemDescriptor mid, final UUID citizenId, final String citizenName, final String citizenDistrict, final int discoveryPort, final boolean isMyself, final boolean isSuspended) {
		this.mid = mid;
		this.citizenId = citizenId;
		this.citizenName = citizenName;
		this.citizenDistrict = citizenDistrict;
		this.discoveryPort = discoveryPort;
		this.isMyself = isMyself;
		this.isSuspended = isSuspended;
	}

	public boolean isSuspended() {
		return isSuspended;
	}

	public void setSuspended(boolean isSuspended) {
		this.isSuspended = isSuspended;
	}

	public MediaItemDescriptor getMid() {
		return mid;
	}

	public UUID getCitizenId() {
		return citizenId;
	}

	public String getCitizenDistrict() {
		return citizenDistrict;
	}

	public boolean isMyself() {
		return isMyself;
	}

	public String getCitizenName() {
		return citizenName;
	}

	public int getDiscoveryPort() {
		return discoveryPort;
	}

	@Override
	public String toString() {
		return "Citizen [citizenId=" + citizenId + ", citizenName=" + citizenName + ", citizenDistrict=" + citizenDistrict + ", discoveryPort=" + discoveryPort + ", isMyself=" + isMyself + ", isSuspended=" + isSuspended + "]";
	}
}