package chav1961.nanochat.common.interfaces;

import java.io.IOException;
import java.util.UUID;

public interface InviteMediaFactory<Op extends Enum<?>> {
	InviteMedia<Op> createServerInviteMedia(final UUID owner, final InviteInfo info, final Class<Op> clazz) throws IOException;
	InviteMedia<Op> createClientInviteMedia(final UUID owner, final UUID serverOwner, final UUID serverInviteId, final Class<Op> clazz) throws IOException;
}
