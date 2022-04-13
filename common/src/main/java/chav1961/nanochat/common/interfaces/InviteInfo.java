package chav1961.nanochat.common.interfaces;

import java.io.Serializable;
import java.util.Date;

public interface InviteInfo extends Serializable {
	String getTheme();
	Date getDateCreated();
	String getOwnerName();
}
