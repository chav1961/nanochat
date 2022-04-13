package chav1961.nanochat.common.intern;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import chav1961.nanochat.common.interfaces.InviteInfo;
import chav1961.nanochat.common.interfaces.InviteMedia;
import chav1961.nanochat.common.interfaces.SharedContent;
import chav1961.purelib.basic.SubstitutableProperties;

public class AbstractServerInviteImpl extends AbstractPluggableInviteImpl {
	private final InviteInfo						info;
	private final InviteMedia<? extends Enum<?>>	media;
	private final List<SharedContent<?>>			sharedContent = new ArrayList<>();
	private final List<UUID>						members = new ArrayList<>();
	
	protected AbstractServerInviteImpl(final UUID ownerId, final SubstitutableProperties props, final InviteInfo info, final InviteMedia<? extends Enum<?>> media) {
		super(ownerId, props);
		if (info == null) {
			throw new NullPointerException("Invite information can't be null"); 
		}
		else if (media == null) {
			throw new NullPointerException("Invite media can't be null"); 
		}
		else {
			this.info = info;
			this.media = media;
		}
	}

	@Override
	public InviteInfo getInviteInfo() {
		return info;
	}

	@Override
	public UUID[] getMembers() {
		synchronized(members) {
			return members.toArray(new UUID[members.size()]);
		}
	}

	@Override
	public <T> boolean shareContent(final SharedContent<T> content) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public <T> boolean unshareContent(final SharedContent<T> content) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean unshareAll() {
		// TODO Auto-generated method stub
		return false;
	}
	
	protected void addMember(final UUID member) {
		
	}

	protected void removeMember(final UUID member) {
		
	}
}
