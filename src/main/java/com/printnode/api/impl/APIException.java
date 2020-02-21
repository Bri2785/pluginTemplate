package com.printnode.api.impl;

/**
 * Just a small Exception class for HTTP request based exceptions.
 * */
public class APIException extends RuntimeException {

    /**
     * Calls an APIException.
     * @param message message to be sent out with the exception
     * */
    public APIException(final String message) {
        super(message);
    }

}
