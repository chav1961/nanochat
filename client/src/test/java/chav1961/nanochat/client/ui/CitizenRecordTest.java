package chav1961.nanochat.client.ui;

import java.net.URI;
import java.util.HashMap;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;

import chav1961.nanochat.client.ui.interfaces.CitizenOptions;
import chav1961.purelib.basic.exceptions.ContentException;

public class CitizenRecordTest {
	private static final UUID	testUUID = UUID.randomUUID(); 
	private static final URI	iconURI = URI.create("root://"+CitizenRecordTest.class.getName()+"/images/trayicon.png");

	@Test
	public void basicTest() throws ContentException {
		final CitizenRecord	rec = new CitizenRecord(testUUID, iconURI, "name", "district", new OptionList<CitizenOptions, Enum>(CitizenOptions.class));
		final StringBuilder	sb = new StringBuilder();
		
		rec.getJavaScript(sb);
		System.err.println(sb);
	}
}
