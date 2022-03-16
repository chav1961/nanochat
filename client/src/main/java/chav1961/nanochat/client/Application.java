package chav1961.nanochat.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import chav1961.purelib.basic.SubstitutableProperties;

public class Application {

	public static void main(String[] args) {
		final File	configFile = new File(System.getProperty("user.home"),".nanochat");
		
		if (!configFile.exists()) {
			firstRun();
		}
		else {
			final SubstitutableProperties	props = new SubstitutableProperties();
			
			try(final InputStream	is = new FileInputStream(configFile)) {
				props.load(is);
				ordinalRun(props);
			} catch (IOException e) {
				firstRun();
			}
		}
	}

	public static void firstRun() {
		// TODO Auto-generated method stub
		System.err.println("First");
	}

	public static void ordinalRun(final SubstitutableProperties props) {
		// TODO Auto-generated method stub
		System.err.println("Ordinal");
	}
}
