package chav1961.nanochat.client.ui;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import chav1961.nanochat.client.Application;
import chav1961.purelib.basic.PureLibSettings;
import chav1961.purelib.basic.Utils;
import chav1961.purelib.basic.interfaces.LoggerFacade;
import chav1961.purelib.basic.interfaces.LoggerFacadeOwner;
import chav1961.purelib.fsys.interfaces.FileSystemInterface;
import chav1961.purelib.i18n.interfaces.Localizer;
import chav1961.purelib.nanoservice.interfaces.FromBody;
import chav1961.purelib.nanoservice.interfaces.FromHeader;
import chav1961.purelib.nanoservice.interfaces.FromPath;
import chav1961.purelib.nanoservice.interfaces.FromQuery;
import chav1961.purelib.nanoservice.interfaces.Path;
import chav1961.purelib.nanoservice.interfaces.QueryType;
import chav1961.purelib.nanoservice.interfaces.RootPath;
import chav1961.purelib.nanoservice.interfaces.ToBody;
import chav1961.purelib.nanoservice.interfaces.ToHeader;
import chav1961.purelib.streams.char2char.SubstitutableWriter;

@RootPath("/gui")
public class UIPainter implements Closeable, LoggerFacadeOwner {
	private final Application			parent;
	private final FileSystemInterface	fsi;
	private final Localizer				localizer;
	private final LoggerFacade			logger;
	
	public UIPainter(final Application parent, final FileSystemInterface fsi, final Localizer localizer, final LoggerFacade logger) {
		if (parent == null) {
			throw new NullPointerException("Applicaiton can't be null"); 
		}
		else if (fsi == null) {
			throw new NullPointerException("File system interface can't be null"); 
		}
		else if (localizer == null) {
			throw new NullPointerException("Localizer can't be null"); 
		}
		else if (logger == null) {
			throw new NullPointerException("Logger can't be null"); 
		}
		else {
			this.parent = parent;
			this.fsi = fsi;
			this.localizer = localizer;
			this.logger = logger;
		}
	}

	@Override
	public void close() throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public LoggerFacade getLogger() {
		return logger;
	}
	
	@Path("/index")
	public int printIndex(@ToBody(mimeType="text/html") final Writer os) throws IOException {
		final SubstitutableWriter	sw = new SubstitutableWriter(os, this::indexSubstitutor);
		
		try(final InputStream	is = this.getClass().getResourceAsStream("/html/index.html");
			final Reader		rdr = new InputStreamReader(is, PureLibSettings.DEFAULT_CONTENT_ENCODING)) {

			Utils.copyStream(rdr, sw);
			os.flush();
		}
		return 200;
	}
	
	@Path(value="/get/body/OutputStream",type={QueryType.GET,QueryType.HEAD,QueryType.DELETE})
	public int call(@ToBody(mimeType="text/plain") final OutputStream os) throws IOException {
		os.write("test string".getBytes());
		os.flush();
		return 200;
	}
	
	@Path(value="/get/body/OutputStream",type={QueryType.GET,QueryType.HEAD,QueryType.DELETE})
	public int callJson(@ToBody(mimeType="application/json") final OutputStream os) throws IOException {
		os.write("json test string".getBytes());
		os.flush();
		return 200;
	}

	@Path(value="/get/body/OutputStream",type={QueryType.GET,QueryType.HEAD,QueryType.DELETE})
	public int callOctetStream(@ToBody(mimeType="application/octet-stream") final OutputStream os) throws IOException {
		os.write("octet test string".getBytes());
		os.flush();
		return 200;
	}

	@Path("/get/path/{parm1}/parm2/parm3")
	public int callPath1(@FromPath("parm1") String parm1, @ToBody(mimeType="text/plain") final Writer os) throws IOException {
		os.write(parm1 == null ? "NULL" : parm1);
		os.flush();
		return 200;
	}

	@Path("/get/query")
	public int callQuery(@FromQuery("parm1") String parm1, @FromQuery("parm2") String parm2, @FromQuery("parm3") String parm3, @ToBody(mimeType="text/plain") final Writer os) throws IOException {
		os.write(parm1 == null ? "NULL" : parm1);
		os.write(parm2 == null ? "NULL" : parm2);
		os.write(parm3 == null ? "NULL" : parm3);
		os.flush();
		return 200;
	}

	@Path("/get/header")
	public int callRequestHead(@FromHeader("parm1") String parm1, @FromHeader("parm2") String parm2, @FromHeader("parm3") String parm3, @ToBody(mimeType="text/plain") final Writer os) throws IOException {
		os.write(parm1 == null ? "NULL" : parm1);
		os.write(parm2 == null ? "NULL" : parm2);
		os.write(parm3 == null ? "NULL" : parm3);
		os.flush();
		return 200;
	}	

	@Path("/get/responseheader")
	public int callResponseHead(@ToHeader("parm1") StringBuilder sb, @ToHeader("parm2") List<String> list, @ToHeader("@ForJson") ForJson json, @ToBody(mimeType="text/plain") final Writer os) throws IOException {
		list.add("URA!");
		os.flush();
		return 200;
	}	

	@Path(value="/post/body/InputStream",type={QueryType.POST,QueryType.PUT})
	public int callJson(@FromBody(mimeType="application/json") final InputStream is, @ToBody(mimeType="application/json") final OutputStream os) throws IOException {
		Utils.copyStream(is, os);
		return 200;
	}

	@Path(value="/post/body/InputStream",type={QueryType.POST,QueryType.PUT})
	public int callOctetStream(@FromBody(mimeType="application/octet-stream") final InputStream is, @ToBody(mimeType="application/octet-stream") final OutputStream os) throws IOException {
		Utils.copyStream(is, os);
		return 200;
	}

	private String indexSubstitutor(final String key) {
		try {
			switch (key) {		// https://htmldom.dev/show-a-custom-context-menu-at-clicked-position/
				case "CitizenList"	:
					final Set<String>	districts = new HashSet<>();
					final StringBuilder	sb = new StringBuilder();
					
					parent.forAllCitizens((c)->districts.add(c.getCitizenDistrict()));
					
					sb.append("<ul>");
					for (String item : districts) {
						sb.append("<li>").append(item).append("<br/>").append("<ul>");
						parent.forAllCitizens((c)-> {
							if (c.getCitizenDistrict().equals(item)) {
								sb.append("<li class=\"tooltip\" id=\"citizen.").append(c.getCitizenName()).append("\">");							
								
								if (c.isMyself()) {
									sb.append("<b>").append(c.getCitizenName()).append("</b>");
								}
								else if (c.isSuspended()) {
									sb.append("<i>").append(c.getCitizenName()).append("</i>");
								}
								else {
									sb.append(c.getCitizenName());
								}
								sb.append("<span class=\"tooltiptext\">").append(c.getDiscoveryAddress()).append(':').append(c.getDiscoveryPort()).append("</span></li>");
								sb.append("<script>document.getElementById('citizen.").append(c.getCitizenName()).append("').addEventListener('contextmenu', function (e) {e.preventDefault();});</script>");
							}
						});
						sb.append("</ul></li>");
					}
					return sb.append("</ul>").toString();
				default :
					return key;
			}
		} catch (IOException e) {
			return e.getLocalizedMessage();
		}
	}
}
