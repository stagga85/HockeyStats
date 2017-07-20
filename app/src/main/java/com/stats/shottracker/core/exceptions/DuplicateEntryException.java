package com.stats.shottracker.core.exceptions;

public class DuplicateEntryException extends Exception{

    public DuplicateEntryException () {

    }

    public DuplicateEntryException (String message) {
        super (message);
    }

    public DuplicateEntryException (Throwable cause) {
        super (cause);
    }

    public DuplicateEntryException (String message, Throwable cause) {
        super (message, cause);
    }
}
