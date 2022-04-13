package chav1961.nanochat.common.interfaces;

import java.net.URI;
import java.util.UUID;

import chav1961.purelib.basic.exceptions.EnvironmentException;
import chav1961.purelib.concurrent.interfaces.ExecutionControl;

public interface Invite extends ExecutionControl {
	UUID getInviteId();
	InviteInfo getInviteInfo();
	UUID getOwnerId();
	UUID[] getMembers();
	<T> boolean shareContent(SharedContent<T> content);
	<T> boolean unshareContent(SharedContent<T> content);
	boolean unshareAll();
	<T> MessageManager<T> getMessageManager(URI managerURI) throws EnvironmentException;
	<T> StreamManager<T> getStreamManager(URI managerURI) throws EnvironmentException;
}
