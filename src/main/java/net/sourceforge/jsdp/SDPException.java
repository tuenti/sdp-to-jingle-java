/*
 * jSDP: A Java implementation of SDP protocol Copyright (C) 2007 Claudio Di
 * Vita
 */
package net.sourceforge.jsdp;

/**
 * The SDP general exception. This defines a general exception for the SDP
 * classes to throw when they encounter a difficult.
 * 
 * @author <a href="mailto:cdivita@users.sourceforge.net">Claudio Di Vita</a>
 * 
 * @since 1.0
 * 
 * @version 1.0
 */
public class SDPException extends Exception {

    /** The class Stream Unique Identifier, SUID */
    private static final long serialVersionUID = 6147583906191614487L;

    /**
     * Creates a new <tt>SDPException</tt>.
     */
    public SDPException() {

        super();
    }

    /**
     * Creates a new <tt>SDPException</tt>.
     * 
     * @param message the exception message
     */
    public SDPException(final String message) {

        super(message);
    }

    /**
     * Creates a new <tt>SDPException</tt>.
     * 
     * @param message the exception message
     * @param cause the exception root cause
     */
    public SDPException(final String message, final Throwable cause) {

        super(message, cause);
    }
}