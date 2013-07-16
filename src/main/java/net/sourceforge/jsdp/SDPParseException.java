/*
 * jSDP: A Java implementation of SDP protocol Copyright (C) 2007 Claudio Di
 * Vita
 */
package net.sourceforge.jsdp;

/**
 * This exception is thrown when an error occurs while parsing an SDP message or
 * an SDP field.
 * 
 * @author <a hfref="mailto:cdivita@users.sourceforge.net">Claudio Di Vita</a>
 * 
 * @since 1.0
 * 
 * @version 1.0
 */
public class SDPParseException extends SDPException {

    /** The class Stream Unique Identifier, SUID */
    private static final long serialVersionUID = -2057138208340422310L;

    /**
     * Creates a new <tt>SDPException</tt>.
     */
    public SDPParseException() {

        super();
    }

    /**
     * Creates a new <tt>SDPException</tt>.
     * 
     * @param message the exception message
     */
    public SDPParseException(final String message) {

        super(message);
    }

    /**
     * Creates a new <tt>SDPException</tt>.
     * 
     * @param message the exception message
     * @param cause the exception root cause
     */
    public SDPParseException(final String message, final Throwable cause) {

        super(message, cause);
    }

}
