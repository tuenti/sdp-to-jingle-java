/*
 * jSDP: A Java implementation of SDP protocol Copyright (C) 2007 Claudio Di
 * Vita
 */
package net.sourceforge.jsdp;

import net.sourceforge.jsdp.util.Contact;

/**
 * An <tt>Email</tt> represents an <b>e=<i>&lt;field value&gt;</i></b>
 * field contained in a SDP message. An email field specify contact information
 * for the person responsible for the conference. This is not necessarily the
 * same person that created the conference announcement.
 * 
 * @since 0.1.0
 * 
 * @version 1.0
 * 
 * @author <a href="mailto:cdivita@users.sourceforge.net">Claudio Di Vita</a>
 */
public class Email implements Field {

    /** The class Stream Unique Identifier, SUID */
    private static final long serialVersionUID = 3055937612221619818L;

    /** The field contact information */
    protected Contact contact;

    /**
     * Creates a new <tt>Email</tt>.
     */
    protected Email() {

        super();
    }

    /**
     * Creates a new <tt>Email</tt>.
     * 
     * @param info the field contact information
     * 
     * @throws SDPException if the contact information are not valid
     */
    public Email(final String info) throws SDPException {

        super();

        this.contact = new Contact(info);
    }

    /**
     * Parse an input string and constructs the equivalent email field.
     * 
     * @param field the string to parse
     * 
     * @return a new <tt>Email</tt> instance
     * 
     * @throws SDPParseException if an error occurs while parsing
     */
    public static Email parse(final String field) throws SDPParseException {

        if (!field.startsWith("e=")) {
            throw new SDPParseException("The string \"" + field + "\" isn't an email field");
        }

        Email e = null;

        try {
            e = new Email(field.substring(2));
        }
        catch (SDPException parseException) {
            throw new SDPParseException("The string \"" + field + "\" isn't a valid email field", parseException);
        }

        return e;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#clone()
     */
    public Object clone() {

        Email field = new Email();
        field.contact = (Contact) this.contact.clone();

        return field;
    }

    /**
     * Returns the e-mail address of the contact.
     * 
     * @return a string that represensts an e-mail address
     */
    public String getEmail() {

        return contact.getAddress();
    }

    /**
     * Returns the personal information of the contact.
     * 
     * @return the personal information of the contact
     */
    public String getPersonal() {

        return contact.getPersonal();
    }

    /**
     * Returns the type character for the field.
     * 
     * @return the field type character: <b>e</b>
     */
    public char getType() {

        return Field.EMAIL_FIELD;
    }

    /**
     * Sets the e-mail address of the contact.
     * 
     * @param email the e-mail address to set
     * 
     * @throws SDPException if the em-mail address is not valid
     */
    public void setEmail(final String email) throws SDPException {

        contact.setAddress(email);
    }

    /**
     * Sets the personal information of the contact
     * 
     * @param personal the personal information to set
     * 
     * @throws SDPException if the personal information are not valid
     */
    public void setPersonal(final String personal) throws SDPException {

        contact.setPersonal(personal);
    }

    /**
     * Returns a string representation of the field. The representation has the
     * form: <b>e=<i>&lt;e-mail&gt;</i> <i>&lt;info&gt;</i></b>
     * 
     * @return The string representation of the field
     */
    public String toString() {

        return getType() + "=" + contact.toString();
    }
}