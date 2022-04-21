package chav1961.nanochat.common.net;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;

import chav1961.purelib.basic.interfaces.LoggerFacade;
import chav1961.purelib.net.AbstractSelectorBasedDispatcher;
import chav1961.purelib.net.interfaces.MediaItemDescriptor;

abstract class AbstractUUIDControlledDispatcher<Desc extends MediaItemDescriptor> extends AbstractSelectorBasedDispatcher {
	private final ConcurrentHashMap<UUID, Desc>	desc = new ConcurrentHashMap<>(); 
	
	AbstractUUIDControlledDispatcher(LoggerFacade logger, boolean asServer) throws IOException {
		super(logger, asServer);
		// TODO Auto-generated constructor stub
	}

	public void registerMember(final UUID member, final Desc desc, final boolean asServer, final boolean isOwner) {
		
	}

	public void unregisterMember(final UUID member) {
		
	}
	
	public boolean contains(final UUID member) {
		return false;
	}
	
	public boolean isOwner(final UUID member) {
		return false;
	}
	
	public Desc getMediaDescriptor(final UUID member) {
		return null;
	}
	
	public UUID getUUID(final Desc descriptor) {
		return null;
	}
	
	public UUID[] getMembers() {
		return null;
	}
	
	public void forAll(final BiConsumer<AbstractUUIDControlledDispatcher<Desc>, UUID> consumer) throws IOException {
	}
}
