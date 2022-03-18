package chav1961.nanochat.client.anarchy;

import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import chav1961.purelib.basic.exceptions.FlowException;
import chav1961.purelib.basic.exceptions.LocalizationException;
import chav1961.purelib.basic.exceptions.PreparationException;
import chav1961.purelib.basic.interfaces.LoggerFacade;
import chav1961.purelib.basic.interfaces.ModuleAccessor;
import chav1961.purelib.i18n.interfaces.LocaleResource;
import chav1961.purelib.i18n.interfaces.LocaleResourceLocation;
import chav1961.purelib.i18n.interfaces.SupportedLanguages;
import chav1961.purelib.ui.interfaces.FormManager;
import chav1961.purelib.ui.interfaces.Format;
import chav1961.purelib.ui.interfaces.ItemAndSelection;
import chav1961.purelib.ui.interfaces.RefreshMode;

@LocaleResourceLocation("i18n:xml:root://chav1961.nanochat.client.anarchy.TheSameFirstForm/chav1961/nanochat/client/i18n.xml")
@LocaleResource(value="TheSameFirstForm.caption",tooltip="TheSameFirstForm.caption.tt",help="TheSameFirstForm.help")
public class TheSameFirstForm implements FormManager<Object,TheSameFirstForm>, ModuleAccessor {
	public final UUID			id = UUID.randomUUID();
	@LocaleResource(value="TheSameFirstForm.name",tooltip="TheSameFirstForm.name.tt")
	@Format("30m")
	public String				name = System.getProperty("user.name");
	
	@LocaleResource(value="TheSameFirstForm.district",tooltip="TheSameFirstForm.district.tt")
	@Format("30m")
	public String				district = "Одуванчики";

	@LocaleResource(value="TheSameFirstForm.lang",tooltip="TheSameFirstForm.lang.tt")
	@Format("30m")
	public SupportedLanguages	lang = SupportedLanguages.ru; 
	
	@LocaleResource(value="TheSameFirstForm.addr",tooltip="TheSameFirstForm.addr.tt")
	@Format("30*5m")
	public ItemAndSelection<InetAddress>[]	addr = collectSubnet();

	@LocaleResource(value="TheSameFirstForm.useEn",tooltip="TheSameFirstForm.useEn.tt")
	@Format("30m")
	public boolean				useEn = !"ru".equalsIgnoreCase(System.getProperty("user.language"));

	private final LoggerFacade	logger;

	public TheSameFirstForm(final LoggerFacade logger) {
		if (logger == null) {
			throw new NullPointerException("Logger can't be null"); 
		}
		else {
			this.logger = logger;
		}
	}
	
	@Override
	public RefreshMode onField(final TheSameFirstForm inst, final Object id, final String fieldName, final Object oldValue, final boolean beforeCommit) throws FlowException, LocalizationException {
		switch (fieldName) {
			case "name"		:
				return name.trim().isEmpty() ? RefreshMode.REJECT : RefreshMode.DEFAULT; 
			case "district"	:
				return district.trim().isEmpty() ? RefreshMode.REJECT : RefreshMode.DEFAULT;
			case "addr"		:
				return ItemAndSelection.extract(true, addr).length == 0 ? RefreshMode.REJECT : RefreshMode.DEFAULT;
			default 		:
				return RefreshMode.DEFAULT;
		}
	}

	@Override
	public LoggerFacade getLogger() {
		return logger;
	}

	@Override
	public void allowUnnamedModuleAccess(final Module... unnamedModules) {
		for (Module item : unnamedModules) {
			this.getClass().getModule().addExports(this.getClass().getPackageName(), item);
		}
	}

	private static ItemAndSelection<InetAddress>[] collectSubnet() {
		final List<ItemAndSelection<InetAddress>>	result = new ArrayList<>();
		
		try{for (NetworkInterface netInt : Collections.list(NetworkInterface.getNetworkInterfaces())) {
				if (!netInt.isLoopback() && netInt.isUp()) {
					for (InterfaceAddress interfAddress : netInt.getInterfaceAddresses()) {
						final InetAddress	broadcast = interfAddress.getBroadcast();
						
						result.add(new InetAddressSelection(broadcast != null, broadcast != null ? broadcast : interfAddress.getAddress()));
			        }
				}
			}
			return result.toArray(new ItemAndSelection[result.size()]);
		} catch (SocketException e) {
			throw new PreparationException(e.getLocalizedMessage(), e);
		}
	}
	
	private static class InetAddressSelection implements ItemAndSelection<InetAddress> {
		private final InetAddress	address;
		private boolean				state;
		
		public InetAddressSelection(final boolean state, final InetAddress address) {
			this.address = address;
			this.state = state;
		}

		@Override
		public boolean isSelected() {
			return state;
		}

		@Override
		public void setSelected(final boolean selected) {
			this.state = selected;
		}

		@Override
		public InetAddress getItem() {
			return address;
		}
	}
}
