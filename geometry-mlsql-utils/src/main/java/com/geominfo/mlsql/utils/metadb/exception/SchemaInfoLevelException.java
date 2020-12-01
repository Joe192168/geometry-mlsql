package com.geominfo.mlsql.utils.metadb.exception;

public class SchemaInfoLevelException extends RuntimeException{
	public SchemaInfoLevelException(String msg) {
		super(msg);
	}
	
	public SchemaInfoLevelException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
