package chav1961.nanochat.common.intern;

import java.net.URI;
import java.util.ServiceLoader;
import java.util.UUID;
import java.util.regex.Pattern;

import chav1961.nanochat.common.interfaces.MessageManager;
import chav1961.nanochat.common.interfaces.StreamManager;
import chav1961.purelib.basic.SubstitutableProperties;
import chav1961.purelib.basic.exceptions.EnvironmentException;

abstract class AbstractPluggableInviteImpl extends AbstractInviteImpl {
	private static final String				BLACKLIST_MESSAGE_MANAGER = MessageManager.class.getCanonicalName()+".blacklist"; 
	private static final String				BLACKLIST_STREAM_MANAGER = StreamManager.class.getCanonicalName()+".blacklist"; 
	
	private final SubstitutableProperties	props;
	private final Pattern					blackListMessage;
	private final Pattern					blackListStream;
	
	protected AbstractPluggableInviteImpl(final UUID ownerId, final SubstitutableProperties props) {
		super(ownerId);
		if (props == null) {
			throw new NullPointerException("Properties can't be null"); 
		}
		else {
			this.props = props;
			
			if (props.containsKey(BLACKLIST_MESSAGE_MANAGER)) {
				blackListMessage = Pattern.compile(props.getProperty(BLACKLIST_MESSAGE_MANAGER));
			}
			else {
				blackListMessage = null;
			}
			if (props.containsKey(BLACKLIST_STREAM_MANAGER)) {
				blackListStream = Pattern.compile(props.getProperty(BLACKLIST_STREAM_MANAGER));
			}
			else {
				blackListStream = null;
			}
		}
	}

	@Override
	protected <T> MessageManager<T> createMessageManager(final URI managerURI) throws EnvironmentException {
		for (MessageManager<T> item : ServiceLoader.load(MessageManager.class)) {
			if (item.canServe(managerURI) && (blackListMessage == null || !blackListMessage.matcher(item.getClass().getCanonicalName()).matches())) {
				return (MessageManager<T>) item.newInstance(managerURI);
			}
		}
		throw new IllegalArgumentException("No any ["+MessageManager.class.getCanonicalName()+"] found for URI ["+managerURI+"]");
	}

	@Override
	protected <T> StreamManager<T> createStreamManager(final URI managerURI) throws EnvironmentException {
		for (StreamManager<T> item : ServiceLoader.load(StreamManager.class)) {
			if (item.canServe(managerURI) && (blackListStream == null || !blackListStream.matcher(item.getClass().getCanonicalName()).matches())) {
				return (StreamManager<T>) item.newInstance(managerURI);
			}
		}
		throw new IllegalArgumentException("No any ["+StreamManager.class.getCanonicalName()+"] found for URI ["+managerURI+"]");
	}


	protected SubstitutableProperties getProperties() {
		return props;
	}
}
