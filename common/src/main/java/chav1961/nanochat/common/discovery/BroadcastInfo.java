package chav1961.nanochat.common.discovery;

import java.io.Serializable;
import java.util.UUID;

public class BroadcastInfo implements Serializable {
	private static final long serialVersionUID = -2022340209073105069L;
	
	public final UUID	citizenUUID;

	public BroadcastInfo(UUID citizenUUID) {
		this.citizenUUID = citizenUUID;
	}
}