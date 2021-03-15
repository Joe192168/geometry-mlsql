package com.geominfo.mlsql.service.metadb.meta.retriever;

import com.geominfo.mlsql.service.metadb.meta.core.SchemaInfoLevel;
import com.geominfo.mlsql.service.metadb.meta.schema.*;
import java.util.Map;
import java.util.Set;

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
