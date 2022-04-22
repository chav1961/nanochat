package chav1961.nanochat.common.net;

import chav1961.purelib.basic.exceptions.ContentException;

public class MemberIsMissing extends ContentException {
	private static final long serialVersionUID = 2163942119374225879L;

	public MemberIsMissing(final String message) {
		super(message);
	}
}
