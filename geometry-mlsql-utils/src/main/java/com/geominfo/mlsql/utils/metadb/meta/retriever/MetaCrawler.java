package com.geominfo.mlsql.utils.metadb.meta.retriever;

import java.util.Map;
import java.util.Set;

import com.geominfo.mlsql.utils.metadb.exception.DataAccessException;
import com.geominfo.mlsql.utils.metadb.meta.core.SchemaInfoLevel;
import com.geominfo.mlsql.utils.metadb.meta.schema.Column;
import com.geominfo.mlsql.utils.metadb.meta.schema.Database;
import com.geominfo.mlsql.utils.metadb.meta.schema.DatabaseInfo;
import com.geominfo.mlsql.utils.metadb.meta.schema.ForeignKey;
import com.geominfo.mlsql.utils.metadb.meta.schema.Function;
import com.geominfo.mlsql.utils.metadb.meta.schema.PrimaryKey;
import com.geominfo.mlsql.utils.metadb.meta.schema.Procedure;
import com.geominfo.mlsql.utils.metadb.meta.schema.Schema;
import com.geominfo.mlsql.utils.metadb.meta.schema.SchemaInfo;
import com.geominfo.mlsql.utils.metadb.meta.schema.Table;
import com.geominfo.mlsql.utils.metadb.meta.schema.Trigger;

public interface MetaCrawler {
	Set<String> getTableNames();
	
//	Table getTable(String tableName);
	
	Table getTable(String tableName, SchemaInfoLevel schemaInfoLevel);
	
	Table getTable(String tableName, SchemaInfoLevel level, SchemaInfo schemaInfo);
	
//	Map<String, Column> crawlColumnInfo(String tableName);
//	
//	PrimaryKey crawlPrimaryKey(String tableName);
//	
//	Map<String,ForeignKey> crawlForeignKey(String tableName);
	
	Set<SchemaInfo> getSchemaInfos();
	
	Schema getSchema(SchemaInfoLevel level);
	
	Schema getSchema(SchemaInfo schemaInfo, SchemaInfoLevel level);
	
	DatabaseInfo getDatabaseInfo();
	
	Database getDatabase(SchemaInfoLevel level);
	
	Set<String> getProcedureNames(SchemaInfo schemaInfo);
	
	Procedure getProcedure(String procedureName);
	
	Map<String,Procedure> getProcedures();
	
	Set<String> getTriggerNames();
	
	Trigger getTrigger(String triggerName);
	
	Map<String, Trigger> getTriggers();
	
	Set<String> getFunctionNames();
	
	Function getFunction(String name);
	
	Map<String, Function> getFunctions();
}
