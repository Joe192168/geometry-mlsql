package com.geominfo.mlsql.utils.metadb.meta.core;

import java.sql.Connection;

import com.geominfo.mlsql.utils.metadb.meta.retriever.MetaCrawler;

public interface MetaCrawlerFactory {
	/**
	 * according the database information create a MetaCrawler instance.
	 * You can implements an other MetaCrawlerFactory using you's MetaCrawler instance to
	 * crawle database meta;  
	 * 
	 * @param con
	 * @return
	 */
	MetaCrawler newInstance(Connection con);
}
