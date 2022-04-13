package chav1961.nanochat.common.intern;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import chav1961.nanochat.common.interfaces.Invite;
import chav1961.nanochat.common.interfaces.InviteInfo;
import chav1961.nanochat.common.interfaces.MessageManager;
import chav1961.nanochat.common.interfaces.SharedContent;
import chav1961.nanochat.common.interfaces.StreamManager;
import chav1961.purelib.basic.exceptions.EnvironmentException;

abstract class AbstractInviteImpl implements Invite {
	private final UUID			inviteId = UUID.randomUUID();
	private final UUID			ownerId;
	private final Map<URI,MessageManager>	messageManagers = new HashMap<>();
	private final Map<URI,StreamManager>	streamManagers = new HashMap<>();
	private volatile boolean	started = false, suspended = false;
	
	protected AbstractInviteImpl(final UUID ownerId) {
		if (ownerId == null) {
			throw new NullPointerException("Owner Id can't be null");
		}
		else {
			this.ownerId = ownerId;
		}
	}
	
	@Override public abstract InviteInfo getInviteInfo();
	@Override public abstract UUID[] getMembers();
	@Override public abstract <T> boolean shareContent(SharedContent<T> content);
	@Override public abstract <T> boolean unshareContent(SharedContent<T> content);
	@Override public abstract boolean unshareAll();	
	protected abstract <T> MessageManager<T> createMessageManager(final URI managerURI) throws EnvironmentException;
	protected abstract <T> StreamManager<T> createStreamManager(final URI managerURI) throws EnvironmentException;
	
	@Override
	public synchronized void start() throws Exception {
		if (isStarted()) {
			throw new IllegalStateException("Entity already started");
		}
		else {
			started = true;
		}
	}

	@Override
	public synchronized void suspend() throws Exception {
		if (!isStarted()) {
			throw new IllegalStateException("Entity is not started or was stopped");
		}
		else if (isSuspended()) {
			throw new IllegalStateException("Entity is already suspended");
		}
		else {
			suspended = true;
		}
	}

	@Override
	public synchronized void resume() throws Exception {
		if (!isStarted()) {
			throw new IllegalStateException("Entity is not started or was stopped");
		}
		else if (!isSuspended()) {
			throw new IllegalStateException("Entity is not suspended");
		}
		else {
			suspended = false;
		}
	}

	@Override
	public synchronized void stop() throws Exception {
		if (!isStarted()) {
			throw new IllegalStateException("Entity is not started or was stopped");
		}
		else {
			started = false;
		}
	}

	@Override
	public boolean isStarted() {
		return started;
	}

	@Override
	public boolean isSuspended() {
		return suspended;
	}

	@Override
	public UUID getInviteId() {
		return inviteId;
	}

	@Override
	public UUID getOwnerId() {
		return ownerId;
	}

	@Override
	public <T> MessageManager<T> getMessageManager(final URI managerURI) throws EnvironmentException {
		if (managerURI == null) {
			throw new NullPointerException("Manager URI can't be null"); 
		}
		else if (!managerURI.isAbsolute()) {
			throw new IllegalArgumentException("Manager URI ["+managerURI+"] must be absolute"); 
		}
		else {
			synchronized(messageManagers) {
				if (!messageManagers.containsKey(managerURI)) {
					messageManagers.put(managerURI, createMessageManager(managerURI));
				}
				return messageManagers.get(managerURI);
			}
		}
	}


	@Override
	public <T> StreamManager<T> getStreamManager(final URI managerURI) throws EnvironmentException {
		if (managerURI == null) {
			throw new NullPointerException("Manager URI can't be null"); 
		}
		else if (!managerURI.isAbsolute()) {
			throw new IllegalArgumentException("Manager URI ["+managerURI+"] must be absolute"); 
		}
		else {
			synchronized(streamManagers) {
				if (!streamManagers.containsKey(managerURI)) {
					streamManagers.put(managerURI, createStreamManager(managerURI));
				}
				return streamManagers.get(managerURI);
			}
		}
	}

}
