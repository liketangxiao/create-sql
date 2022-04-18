package com.idea.plugin.sql.support.exception;

public class SqlException extends Exception {

    private static final long serialVersionUID = 1L;

    public SqlException(String message) {
        super(message);
    }
    public SqlException(String message, Exception e) {
        super(message, e);
    }
}
