package com.geominfo.mlsql.utils.metadb.meta.core;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import com.geominfo.mlsql.utils.metadb.exception.NonTransientDataAccessException;
import com.geominfo.mlsql.utils.metadb.meta.retriever.MetaCrawler;
import com.geominfo.mlsql.utils.metadb.meta.retriever.MySqlMetaCrawler;
import com.geominfo.mlsql.utils.metadb.meta.retriever.OracleMetaCrawler;
import com.geominfo.mlsql.utils.metadb.meta.retriever.SqlServerMetaCrawler;
import com.geominfo.mlsql.utils.metadb.util.JDBCUtils;

/**
 * can be inherit and override the newInstance function
 * 
 * @author xumh
 *
 */
public class DefaultMetaCrawlerFactory implements MetaCrawlerFactory{
	
	public static final int MYSQL = 1;
	public static final int SQL_SERVER = 2;
	public static final int ORACLE = 3;

	public MetaCrawler newInstance(Connection con) {
		String product=getProductName(con);
		
		DatabaseMetaData dbm=getDatabaseMetaData(con);
		if (product.equals("MySQL")) {
			return new MySqlMetaCrawler(dbm);
		} else if (product.equals("Oracle")) {
			return new OracleMetaCrawler(dbm);
		} else if (product.equals("Microsoft SQL Server")) {
			return new SqlServerMetaCrawler(dbm);
		} else {
			return null;
		}
	}

	protected String getProductName(Connection conn) {
		String product = null;
		try {
			DatabaseMetaData dbm = conn.getMetaData();
			product = dbm.getDatabaseProductName();
			return product;

		} catch (SQLException e) {
			throw new NonTransientDataAccessException("can not get database product information!",e);
		}
	}
	
	protected DatabaseMetaData getDatabaseMetaData(Connection connection) {
		DatabaseMetaData dbm;
		try {
			dbm = connection.getMetaData();
		} catch (SQLException e) {
			JDBCUtils.closeConnection(connection);
			throw new NonTransientDataAccessException("Could not get DatabaseMeta");
		}
		return dbm;
	}
	
	
}
