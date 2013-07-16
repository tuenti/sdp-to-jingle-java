/*
 * jSDP: A Java implementation of SDP protocol Copyright (C) 2007 Claudio Di
 * Vita
 */
package net.sourceforge.jsdp;

import java.util.regex.Pattern;

/**
 * An <tt>Information</tt> represents an <b>i=<i>&lt;field value&gt;</i></b>
 * field contained in a SDP message. This field is information about the session
 * and may be omitted, although this is discouraged for session announcements,
 * and user interfaces for composing sessions should require text to be entered.
 * 
 * @since 0.1.0
 * 
 * @version 1.0
 * 
 * @author <a href="mailto:cdivita@users.sourceforge.net">Claudio Di Vita</a>
 */
public class Information implements Field {

    /** The class Stream Unique Identifier, SUID */
    private static final long serialVersionUID = 7479814033467823932L;

    /** The pattern used to validated the session information value */
    private static final Pattern valuePattern = Pattern.compile("[^\\r\\n\0]+");

    /** The information about the session */
    private String value;

    /**
     * Creates a new <tt>Information</tt>.
     */
    protected Information() {

        super();
    }

    /**
     * Creates a new <tt>Information</tt>.
     * 
     * @param value the information about the session
     * 
     * @throws SDPException if the characters of the information are not allowed
     */
    public Information(final String value) throws SDPException {

        super();
        setValue(value);
    }

    /**
     * Parse an input string and constructs the equivalent information field.
     * 
     * @param field the string to parse
     * 
     * @return a new <tt>Information</tt> instance
     * 
     * @throws SDPParseException if an error occurs while parsing
     */
    public static Information parse(final String field) throws SDPParseException {

        if (!field.startsWith("i=")) {
            throw new SDPParseException("The string \"" + field + "\" isn't an information field");
        }

        Information i = null;

        try {
            i = new Information(field.substring(2));
        }
        catch (SDPException parseException) {
            throw new SDPParseException("The string \"" + field + "\" isn't a valid information field", parseException);
        }

        return i;
    }

    /**
     * Returns a clone of this field.
     * 
     * @return a clone of this field
     */
    public Object clone() {

        Information info = new Information();
        info.value = new String(this.value);

        return info;
    }

    /**
     * Returns the type character for the field.
     * 
     * @return the field type character: <b>i</b>
     */
    public char getType() {

        return Field.INFORMATION_FIELD;
    }

    /**
     * Returns the information about the session.
     * 
     * @return the session information
     */
    public String getValue() {

        return value;
    }

    /**
     * Sets the session information.
     * 
     * @param value the information about the session
     * 
     * @throws SDPException if the characters of the information are not allowed
     */
    public void setValue(final String value) throws SDPException {

        if (!valuePattern.matcher(value).matches()) {
            throw new SDPException("Invalid session information");
        }

        this.value = value;
    }

    /**
     * Returns a string representation of the field. The representation has the
     * form: <b>i=&lt;value&gt;</b>
     * 
     * @return the string representation of the field
     */
    public String toString() {

        return getType() + "=" + getValue();
    }
}