package com.yaps.petstore.exception;

/**
 * This exception is thrown when an object cannot be created.
 */
@SuppressWarnings("serial")
public class CreateException extends ApplicationException {

    public CreateException() {
    }

    public CreateException(final String message) {
        super(message);
    }
}
