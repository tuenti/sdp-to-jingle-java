/*
 * jSDP: A Java implementation of SDP protocol Copyright (C) 2007 Claudio Di
 * Vita
 */
package net.sourceforge.jsdp;

import java.util.regex.Pattern;

/**
 * A <tt>SessionName</tt> represents a <b>s=<i>&lt;field value&gt;</i></b>
 * field contained in a SDP message. This field identifies the name of the
 * session.
 * 
 * @since 0.1.0
 * 
 * @version 1.0
 * 
 * @author <a href="mailto:cdivita@users.sourceforge.net">Claudio Di Vita</a>
 */
public class SessionName implements Field {

    /** The class Stream Unique Identifier, SUID */
    private static final long serialVersionUID = 6770714073389574787L;

    /** The pattern used to validate the session name */
    private static final Pattern namePattern = Pattern.compile("[^\\r\\n\0]+");

    /** The session name */
    private String value;

    /**
     * Creates a new <tt>SessionName</tt>. The name value is set to
     * <tt>-</tt>.
     */
    public SessionName() {

        super();
        this.value = "-";
    }

    /**
     * Creates a new <tt>SessionName</tt>.
     * 
     * @param value the name of the session
     * 
     * @throws SDPException if the characters of the session name are not
     *         allowed
     */
    public SessionName(final String value) throws SDPException {

        super();
        setValue(value);
    }

    /**
     * Parse an input string and constructs the equivalent session name field.
     * 
     * @param field the string to parse
     * 
     * @return a new <tt>SessionName</tt> instance
     * 
     * @throws SDPParseException if an error occurs while parsing
     */
    public static SessionName parse(final String field) throws SDPParseException {

        if (!field.startsWith("s=")) {
            throw new SDPParseException("The string \"" + field + "\" isn't a session name field");
        }

        SessionName s = null;

        try {
            /* Create the field */
            s = new SessionName(field.substring(2));
        }
        catch (SDPException parseException) {
            throw new SDPParseException("The string \"" + field + "\" isn't a valid session name field", parseException);
        }

        return s;
    }

    /**
     * Returns a clone of this field.
     * 
     * @return a clone of this field
     */
    public Object clone() {

        SessionName session = new SessionName();
        session.value = new String(this.value);

        return session;
    }

    /**
     * Returns the type character for the field.
     * 
     * @return the field type character: <b>s</b>
     */
    public char getType() {

        return Field.SESSION_NAME_FIELD;
    }

    /**
     * Returns the session name.
     * 
     * @return the session name
     */
    public String getValue() {

        return value;
    }

    /**
     * Sets the session name.
     * 
     * @param value the session name
     * 
     * @throws SDPException if the characters of the session name are not
     *         allowed
     */
    public void setValue(final String value) throws SDPException {

        if (!namePattern.matcher(value).matches()) {
            throw new SDPException("Invalid session name");
        }

        this.value = value;
    }

    /**
     * Returns a string representation of the field. The representation has the
     * form: <b>s=<i>&lt;name&gt;</i></b>.
     * 
     * @return the string representation of the field
     */
    public String toString() {

        return getType() + "=" + value;
    }
}