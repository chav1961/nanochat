package chav1961.nanochat.client.db;

import java.sql.Connection;
import java.sql.SQLException;

import chav1961.purelib.basic.interfaces.LoggerFacade;
import chav1961.purelib.basic.interfaces.LoggerFacadeOwner;
import chav1961.purelib.model.interfaces.ContentMetadataInterface.ContentNodeMetadata;
import chav1961.purelib.sql.model.SQLModelUtils;
import chav1961.purelib.sql.model.SimpleDottedVersion;
import chav1961.purelib.sql.model.interfaces.DatabaseManagement;

public class DbManagement implements DatabaseManagement<SimpleDottedVersion>, LoggerFacadeOwner {
	private static final SimpleDottedVersion	INITIAL_VERSION = new SimpleDottedVersion("0.0"); 
	
	private final LoggerFacade	logger;
	
	public DbManagement(final LoggerFacade logger) {
		if (logger == null) {
			throw new NullPointerException("Logger can't be null"); 
		}
		else {
			this.logger = logger;
		}
	}

	@Override
	public LoggerFacade getLogger() {
		return logger;
	}
	
	@Override
	public SimpleDottedVersion getInitialVersion() throws SQLException {
		return INITIAL_VERSION;
	}

	@Override
	public SimpleDottedVersion getVersion(final ContentNodeMetadata model) throws SQLException {
		return INITIAL_VERSION;
	}

	@Override
	public SimpleDottedVersion getDatabaseVersion(final Connection conn) throws SQLException {
		return INITIAL_VERSION;
	}

	@Override
	public ContentNodeMetadata getDatabaseModel(final Connection conn) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate(final Connection conn, final ContentNodeMetadata model) throws SQLException {
		SQLModelUtils.createDatabaseByModel(conn, model);
	}

	@Override
	public void onUpgrade(final Connection conn, final SimpleDottedVersion version, final ContentNodeMetadata model, final SimpleDottedVersion oldVersion, final ContentNodeMetadata oldModel) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDowngrade(final Connection conn, final SimpleDottedVersion version, final ContentNodeMetadata model, final SimpleDottedVersion oldVersion, final ContentNodeMetadata oldModel) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onOpen(final Connection conn, final ContentNodeMetadata model) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onClose(final Connection conn, final ContentNodeMetadata model) throws SQLException {
		// TODO Auto-generated method stub
		
	}
}
