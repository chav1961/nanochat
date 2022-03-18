package chav1961.nanochat.common;

import java.io.File;

public class Constants {
	public static final File	NANOCHAT_DIRECTORY = new File(System.getProperty("user.home"),"nanochat");
	public static final File	NANOCHAT_CONFIG = new File(NANOCHAT_DIRECTORY,".nanochat");
	
	public static final String	PROP_GENERAL = "general";
	public static final String	PROP_GENERAL_ID = "uuid";
	public static final String	PROP_GENERAL_NAME = "name";
	public static final String	PROP_GENERAL_DEFAULT_LANG = "defaultLang";
	public static final String	PROP_GENERAL_TRAY_LANG_EN = "trayLangEn";

	public static final String	PROP_ANARCH = "anarch";
	public static final String	PROP_ANARCH_SUBNETS = "subnets";
	public static final String	PROP_ANARCH_DISTRICT = "district";
}