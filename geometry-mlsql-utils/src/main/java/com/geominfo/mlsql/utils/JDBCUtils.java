package com.geominfo.mlsql.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.sql.*;


public class JDBCUtils {
	private static Logger logger=LoggerFactory.getLogger(JDBCUtils.class);
	/**
	 * Close the given JDBC Connection and ignore any thrown exception.
	 * This is useful for typical finally blocks in manual JDBC code.
	 * @param con the JDBC Connection to close (may be {@code null})
	 */
	public static void closeConnection(Connection con) {
		if (con != null) {
			try {
				logger.debug("close connection "+con+" "+con.hashCode());
				con.close();
			}
			catch (SQLException ex) {
				logger.debug("Could not close JDBC Connection", ex);
			}
			catch (Throwable ex) {
				// We don't trust the JDBC driver: It might throw RuntimeException or Error.
				logger.debug("Unexpected exception on closing JDBC Connection", ex);
			}
		}
	}
	
	
	/**
	 * Get a JDBC Connection from datasource
	 * 
	 * @return Connection
	 * @throws CannotGetJdbcConnectionException
	 */
	public static Connection getConnection(DataSource dataSource) throws CannotGetJdbcConnectionException {
		Assert.notNull(dataSource, "no datasource can be find!");
		Connection con;
		try {
			con = dataSource.getConnection();
			con.isReadOnly();//Is readOnly
			logger.debug("Get the connection "+con+" "+con.hashCode());
		} catch (SQLException e) {
			throw new CannotGetJdbcConnectionException("Could not get JDBC Connection", e);
		}
		return con;
	}
	
	
	/**
	 * Close the given JDBC ResultSet and ignore any thrown exception.
	 * This is useful for typical finally blocks in manual JDBC code.
	 * @param rs the JDBC Resultset to close (may be {@code null})
	 */
	public static void closeResultSet(ResultSet rs){
		if (rs != null) {
			try {
				logger.debug("close ResultSet "+rs+" "+rs.hashCode());
				rs.close();
			}
			catch (SQLException ex) {
				logger.debug("Could not close JDBC ResultSet", ex);
			}
			catch (Throwable ex) {
				// We don't trust the JDBC driver: It might throw RuntimeException or Error.
				logger.debug("Unexpected exception on closing JDBC ResultSet", ex);
			}
		}
	}
	
	public static void closePreparedStatement(PreparedStatement rs){
		if (rs != null) {
			try {
				logger.debug("close PreparedStatement "+rs+" "+rs.hashCode());
				rs.close();
			}
			catch (SQLException ex) {
				logger.debug("Could not close JDBC PreparedStatement", ex);
			}
			catch (Throwable ex) {
				// We don't trust the JDBC driver: It might throw RuntimeException or Error.
				logger.debug("Unexpected exception on closing JDBC PreparedStatement", ex);
			}
		}
	}
	
	public static <T> T query(DatabaseMetaData dbm, String sql, String exceptionMessage, ResultSetExtractor<T> rsExtractor, Object... args){
		ResultSet rs=null;
		PreparedStatement st=null;
		try{
			Connection con=dbm.getConnection();
			 st=con.prepareStatement(sql);
			for(int i=1;i<=args.length;++i){
				st.setObject(i, args[i-1]);
			}
			rs=st.executeQuery();
			return rsExtractor.extractData(rs);
		}catch(SQLException e){
			throw new RuntimeException(exceptionMessage, e);
		}finally{
			JDBCUtils.closePreparedStatement(st);
			JDBCUtils.closeResultSet(rs);
		}
	}


}
