package com.geominfo.mlsql.utils.metadb.util;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.geominfo.mlsql.utils.metadb.exception.DataAccessException;

public interface ResultSetExtractor<T> {
	
	T extractData(ResultSet rs) throws SQLException;
}
