package chav1961.nanochat.common;

import java.io.File;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import chav1961.nanochat.common.intern.InetAddressSelection;
import chav1961.purelib.json.JsonSerializer;
import chav1961.purelib.ui.interfaces.ItemAndSelection;

public class NanoChatUtils {
	public static void prepareNanoChatDirectory(final File nanoChatDir) throws IllegalArgumentException, NullPointerException {
		if (nanoChatDir == null) {
			throw new IllegalArgumentException("Nanochat directory can't be null"); 
		}
		else {
			nanoChatDir.mkdirs();
		}
	}
	
	public static ItemAndSelection<InetAddress>[] string2ItemAndSelection(final String source) throws IllegalArgumentException, NullPointerException {
		if (source == null || source.isEmpty()) {
			throw new IllegalArgumentException("String to convert can't be null or empty"); 
		}
		else {
			final List<ItemAndSelection<InetAddress>>	result = new ArrayList<>();
			
			for (String item : source.split(";")) {
				if (!item.trim().isEmpty()) {
					final String[]			parts = item.split("\\:");

					if (parts.length != 2) {
						throw new IllegalArgumentException("Illegal Inet address format ["+item+"] : must be <IPv4>:{true|false}"); 
					}
					else {
						try{result.add(new InetAddressSelection(Boolean.valueOf(parts[1]), InetAddress.getByAddress(new byte[] {(byte)192,(byte)168,0,(byte)255})));
						} catch (UnknownHostException e) {
							throw new IllegalArgumentException("Illegal Inet address format ["+parts[0]+"] : "+e.getLocalizedMessage()); 
						}
					}
				}
			}
			return result.toArray(new ItemAndSelection[result.size()]);
		}
	}
	
	public static String itemAndSelection2String(final ItemAndSelection<InetAddress>... source) throws IllegalArgumentException, NullPointerException {
		if (source == null) {
			throw new NullPointerException("Source array to convert can't be null"); 
		}
		else {
			final StringBuilder	sb = new StringBuilder();
			
			for (ItemAndSelection<InetAddress> item : source) {
				sb.append(';').append(item.getItem()).append(':').append(item.isSelected());
			}
			return sb.substring(1);
		}		
	}
	
	public static <T> String toJsonString(final T instance, final JsonSerializer<T> serializer) {
		final int		size = serializer.serialize(instance, null, 0, false);
		final char[]	content = new char[size];
		
		serializer.serialize(instance, content, 0, true);
		return new String(content);
	}
}
