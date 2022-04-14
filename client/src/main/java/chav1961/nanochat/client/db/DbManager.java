package chav1961.nanochat.client.db;

import java.sql.SQLException;
import java.util.Arrays;

import chav1961.purelib.basic.exceptions.ContentException;
import chav1961.purelib.basic.interfaces.LoggerFacade;
import chav1961.purelib.model.interfaces.ContentMetadataInterface.ContentNodeMetadata;
import chav1961.purelib.sql.model.SQLModelUtils.ConnectionGetter;
import chav1961.purelib.sql.model.SimpleDatabaseManager;
import chav1961.purelib.sql.model.SimpleDottedVersion;
import chav1961.purelib.sql.model.interfaces.DatabaseModelManagement;

public class DbManager extends SimpleDatabaseManager<SimpleDottedVersion> {
	public DbManager(final LoggerFacade logger, final ContentNodeMetadata model, final ConnectionGetter connGetter, final DatabaseManagementGetter<SimpleDottedVersion> mgmtGetter) throws SQLException, ContentException {
		super(logger, new DatabaseModelManagement<SimpleDottedVersion>() {
			private final DatabaseModelContent<SimpleDottedVersion>	content = new DatabaseModelContent<>() {
																		@Override public SimpleDottedVersion getVersion() {return new SimpleDottedVersion("0");}
																		@Override public ContentNodeMetadata getModel() {return model;}
																	};
			@Override public int size() {return 1;}
			@Override public SimpleDottedVersion getVersion(int versionNumber) {return content.getVersion();}
			@Override public ContentNodeMetadata getModel(int versionNumber) {return content.getModel();}
			@Override public Iterable<DatabaseModelContent<SimpleDottedVersion>> allAscending() {return Arrays.asList(content);}
			@Override public Iterable<DatabaseModelContent<SimpleDottedVersion>> allDescending() {return Arrays.asList(content);}
		}, connGetter, mgmtGetter);
	}

//	public DbManager(final LoggerFacade logger, final DatabaseModelManagement<SimpleDottedVersion> model, final ConnectionGetter connGetter, final DatabaseManagementGetter<SimpleDottedVersion> mgmtGetter) throws SQLException, ContentException {
//		super(logger, model, connGetter, mgmtGetter);
//	}
}
