/*
 * jSDP: A Java implementation of SDP protocol Copyright (C) 2007 Claudio Di
 * Vita
 */
package net.sourceforge.jsdp;

import java.util.regex.Pattern;

/**
 * A <tt>Phone</tt> represents a <b>p=<i>&lt;field value&gt;</i></b> field
 * contained in a SDP message. A phone field specify contact information for the
 * person responsible for the conference. This is not necessarily the same
 * person that created the conference announcement.
 * 
 * @since 0.1.0
 * 
 * @version 1.0
 * 
 * @author <a href="mailto:cdivita@users.sourceforge.net">Claudio Di Vita</a>
 */
public class Phone implements Field {

    /** The class Stream Unique Identifier, SUID */
    private static final long serialVersionUID = 4792028746712967047L;

    /** The pattern used to validate the phone number */
    private static final Pattern phoneMatcher = Pattern.compile("\\+([1-9])+[ -](\\d)+([ -](\\d)+)*");

    /** The phone user */
    protected String name;

    /** The phone number */
    protected String phoneNumber;

    /**
     * Creates a new <tt>Phone</tt>.
     */
    private Phone() {

        super();
    }

    /**
     * Creates a new <tt>Phone</tt>.
     * 
     * @param number the phone number
     * 
     * @throws SDPException if the phone number is not valid
     */
    public Phone(final String number) throws SDPException {

        super();
        setPhoneNumber(number);
    }

    /**
     * Parse an input string and constructs the equivalent phone field.
     * 
     * @param field the string to parse
     * 
     * @return a new <tt>Phone</tt> instance
     * 
     * @throws SDPParseException if an error occurs while parsing
     */
    public static Phone parse(final String field) throws SDPParseException {

        if (!field.startsWith("p=")) {
            throw new SDPParseException("The string \"" + field + "\" isn't a phone field");
        }

        Phone p = null;

        try {
            // TODO Add support for the contact name parsing
            p = new Phone(field.substring(2));
        }
        catch (SDPException parseException) {
            throw new SDPParseException("The string \"" + field + "\" isn't a valid phone field", parseException);
        }

        return p;
    }

    /**
     * Returns a clone of this field.
     * 
     * @return a clone of this field
     */
    public Object clone() {

        Phone field = new Phone();

        field.phoneNumber = this.phoneNumber;
        if (this.name != null) {
            field.name = new String(this.name);
        }

        return field;
    }

    /**
     * Returns the name of the phone user.
     * 
     * @return the phone user
     */
    public String getName() {

        return name;
    }

    /**
     * Returns the phone number.
     * 
     * @return the phone number
     */
    public String getPhoneNumber() {

        return phoneNumber;
    }

    /**
     * Returns the type character for the field.
     * 
     * @return the field type character: <b>p</b>
     */
    public char getType() {

        return Field.PHONE_FIELD;
    }

    /**
     * Sets phone user name.
     * 
     * @param name the phone user name
     * 
     * @throws SDPException if the phone user name is not valid
     */
    public void setName(final String name) throws SDPException {

        // TODO Validate the user name
        this.name = name;
    }

    /**
     * Sets the phone number.
     * 
     * @param number the phone number
     * 
     * @throws SDPException if the phone number is not valid
     */
    public void setPhoneNumber(final String number) throws SDPException {

        if (!phoneMatcher.matcher(number).matches()) {
            throw new SDPException("Invalid phone number");
        }

        this.phoneNumber = number;
    }

    /**
     * Returns a string representation of the field. The representation has the
     * form: <b>p=<i>&lt;value&gt;</i></b>
     * 
     * @return the string representation of the field
     */
    public String toString() {

        // TODO Check the string representation of the field
        StringBuffer result = new StringBuffer(getType() + "=");

        if (name != null) {
            result.append(name);
            result.append("<" + phoneNumber + ">");
        }
        else {
            result.append(phoneNumber);
        }

        return result.toString();
    }
}