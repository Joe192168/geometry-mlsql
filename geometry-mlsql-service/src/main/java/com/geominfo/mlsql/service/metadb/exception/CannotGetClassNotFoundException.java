package com.geominfo.mlsql.service.metadb.exception;

@SuppressWarnings("serial")
public class CannotGetClassNotFoundException extends NestedRuntimeException{

    public CannotGetClassNotFoundException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
