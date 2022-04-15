package chav1961.nanochat.common.discovery;

import java.io.Serializable;

public class QueryInfo implements Serializable {
	private static final long serialVersionUID = 8866635564761007152L;

	private final QueryInfoType	type;
	
	public QueryInfo(final QueryInfoType type) {
		if (type == null) {
			throw new NullPointerException("Operation type can't be null");
		}
		else {
			this.type = type;
		}
	}

	public QueryInfoType getType() {
		return type;
	}
}