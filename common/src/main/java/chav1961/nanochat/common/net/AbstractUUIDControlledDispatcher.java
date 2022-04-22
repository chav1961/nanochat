package chav1961.nanochat.common.net;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import chav1961.purelib.basic.interfaces.LoggerFacade;
import chav1961.purelib.concurrent.LightWeightRWLockerWrapper;
import chav1961.purelib.concurrent.LightWeightRWLockerWrapper.Locker;
import chav1961.purelib.net.AbstractSelectorBasedDispatcher;
import chav1961.purelib.net.interfaces.MediaItemDescriptor;

abstract class AbstractUUIDControlledDispatcher<Desc extends MediaItemDescriptor> extends AbstractSelectorBasedDispatcher {
	public static final UUID	BROADCAST_UUID = new UUID(0,0);
	
	private final Map<UUID, MemberDescriptor<Desc>>	repo = new HashMap<>();
	private final LightWeightRWLockerWrapper		locker = new LightWeightRWLockerWrapper();
	private final BlockingQueue<TransmitDescriptor>	queue = new ArrayBlockingQueue<>(100);

	@FunctionalInterface
	public interface MemberWalkerCallback<Desc extends MediaItemDescriptor> {
		void process(UUID id, Desc desc, boolean asServer, boolean isOwner) throws IOException, MemberIsMissing;
	}
	
	AbstractUUIDControlledDispatcher(final LoggerFacade logger, final boolean asServer) throws IOException {
		super(logger, asServer);
	}

	protected abstract void dispatch(final SocketAddress source, final ByteBuffer content) throws IOException;
	
	public void registerMember(final UUID member, final Desc desc, final boolean asServer, final boolean isOwner) {
		try(final Locker	lock = locker.lock(false)) {
			if (repo.containsKey(member)) {
				throw new IllegalArgumentException("Memeber UUID ["+member+"] is already registered");
			}
			else {
				repo.put(member, new MemberDescriptor<>(member, desc, asServer, isOwner));
			}
		}
	}

	public void unregisterMember(final UUID member) {
		try(final Locker	lock = locker.lock(false)) {
			if (!repo.containsKey(member)) {
				throw new IllegalArgumentException("Memeber UUID ["+member+"] is missing or was not registered");
			}
			else {
				repo.remove(member);
			}
		}
	}
	
	public boolean contains(final UUID member) {
		try(final Locker	lock = locker.lock()) {
			return repo.containsKey(member);
		}
	}
	
	public boolean isOwner(final UUID member) throws MemberIsMissing {
		try(final Locker	lock = locker.lock()) {
			if (repo.containsKey(member)) {
				return repo.get(member).isOwner;
			}
			else {
				throw new MemberIsMissing("Memeber UUID ["+member+"] is missing or was not registered");
			}
		}
	}
	
	public Desc getMediaDescriptor(final UUID member) throws MemberIsMissing {
		try(final Locker	lock = locker.lock()) {
			if (repo.containsKey(member)) {
				return repo.get(member).desc;
			}
			else {
				throw new MemberIsMissing("Memeber UUID ["+member+"] is missing or was not registered");
			}
		}
	}
	
	public UUID getUUID(final Desc descriptor) throws MemberIsMissing {
		try(final Locker	lock = locker.lock()) {
			for (Entry<UUID, MemberDescriptor<Desc>> item : repo.entrySet()) {
				if (item.getValue().desc.equals(descriptor)) {
					return item.getKey();
				}
			}
			throw new MemberIsMissing("Memeber descriptor ["+descriptor+"] is missing or was not registered");
		}
	}
	
	public UUID[] getMembers() {
		try(final Locker	lock = locker.lock()) {
			return repo.keySet().toArray(new UUID[repo.size()]);
		}
	}
	
	public void forAll(final MemberWalkerCallback<Desc> callback) throws IOException {
		try(final Locker	lock = locker.lock()) {
			for (Entry<UUID, MemberDescriptor<Desc>> item : repo.entrySet()) {
				try{callback.process(item.getValue().id, item.getValue().desc, item.getValue().asServer, item.getValue().isOwner);
				} catch (MemberIsMissing e) {
					throw new IOException(e.getMessage(),e);
				}
			}
		}
	}
	
	public void transmit(final UUID to, final byte[] content) throws InterruptedException, IOException {
		queue.put(new TransmitDescriptor(to, content));
	}

	protected BlockingQueue<TransmitDescriptor>	getTransmitQueue() {
		return queue;
	}
	
	protected static class TransmitDescriptor {
		final UUID		id;
		final byte[]	content;
		
		public TransmitDescriptor(final UUID id, final byte[] content) {
			this.id = id;
			this.content = content;
		}
	}
	
	private static class MemberDescriptor<Desc extends MediaItemDescriptor> {
		final UUID		id;
		final Desc		desc;
		final boolean	asServer;
		final boolean	isOwner;
		
		public MemberDescriptor(final UUID id, final Desc desc, final boolean asServer, final boolean isOwner) {
			this.id = id;
			this.desc = desc;
			this.asServer = asServer;
			this.isOwner = isOwner;
		}
	}
}
