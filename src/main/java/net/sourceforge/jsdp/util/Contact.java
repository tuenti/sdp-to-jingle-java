/*
 * jSDP: A Java implementation of SDP protocol Copyright (C) 2007 Claudio Di
 * Vita
 */
package net.sourceforge.jsdp.util;

import java.io.Serializable;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.jsdp.SDPException;

/**
 * A representation of a contact information.
 * 
 * @since 1.0
 * 
 * @version 1.0
 * 
 * @author <a href="mailto:cdivita@users.sourceforge.net">Claudio Di Vita</a>
 */
public class Contact implements Cloneable, Serializable {

    /** The class Stream Unique Identifier, SUID */
    private static final long serialVersionUID = -1082887678662975345L;

    /** Contact e-mail address regular expression */
    private static final String EMAIL_REGEX = "([\\w'-/:?#\\$&\\*;=\\[\\]\\^_`\\{\\}\\+~]+(\\.[\\w'-/:?#\\$&\\*;=\\[\\]\\^_`\\{\\}\\+~]+)*)@([\\w'-/:?#\\$&\\*;=\\[\\]\\^_`\\{\\}\\+~]+(\\.[\\w'-/:?#\\$&\\*;=\\[\\]\\^_`\\{\\}\\+~]+)*)";

    /** Contact personal information regular expression */
    private static final String PERSONAL_REGEX = "[^\\(\\)<>\\r\\n]+";

    /** The pattern used to validate an e-mail address */
    private static final Pattern emailPattern = Pattern.compile(EMAIL_REGEX);

    /** The pattern used to validate a contact personal information */
    private static final Pattern personalPattern = Pattern.compile(PERSONAL_REGEX);

    /** The e-mail address of the contact */
    protected String address;

    /** The personal information of the contact */
    protected String personal;

    /**
     * Creates a new <tt>Contact</tt>.
     */
    protected Contact() {

        super();
    }

    /**
     * Creates a new <tt>Contact</tt>.
     * 
     * @param info the contact information
     * 
     * @throws SDPException if the contact information aren't valid
     */
    public Contact(final String info) throws SDPException {

        super();

        Matcher emailMatcher = emailPattern.matcher(info);
        if (emailMatcher.lookingAt()) {

            int position = emailMatcher.end();
            if (position == info.length()) {
                this.address = info;
            }
            else {
                this.address = info.substring(0, position);

                StringTokenizer tokenizer = new StringTokenizer(info.substring(position), "()");
                StringBuffer temp = new StringBuffer();
                while (tokenizer.hasMoreTokens()) {
                    temp.append(tokenizer.nextToken());
                }

                String name = temp.toString().trim();
                if (name.length() > 0) {
                    setPersonal(name);
                }
                else {
                    throw new SDPException("Invalid e-mail address");
                }
            }
        }
        else {
            StringTokenizer tokenizer = new StringTokenizer(info, "<>");
            setPersonal(tokenizer.nextToken().trim());
            setAddress(tokenizer.nextToken());
        }
    }

    /**
     * Creates a new <tt>Contact</tt>.
     * 
     * @param address the e-mail address of the contact
     * 
     * @param personal the personal information of the contact
     * 
     * @throws SDPException if the e-mail address or the personal information
     *         aren't valid
     */
    public Contact(final String address, final String personal) throws SDPException {

        super();
        setAddress(address);
        setPersonal(personal);
    }

    /**
     * Returns a clone of this contact.
     * 
     * @return a clone of this object
     */
    public Object clone() {

        Contact email = new Contact();
        email.address = new String(this.address);

        if (this.personal != null) {
            email.personal = new String(personal);
        }
        else {
            email.personal = null;
        }

        return email;
    }

    /**
     * Returns the e-mail address.
     * 
     * @return the e-mail address of the contact
     */
    public String getAddress() {

        return address;
    }

    /**
     * Returns the personal information.
     * 
     * @return the personal information of the contact
     */
    public String getPersonal() {

        return personal;
    }

    /**
     * Sets the e-mail address of the contact.
     * 
     * @param address the e-mail address to set
     * 
     * @throws SDPException if the e-mail address is not valid
     */
    public void setAddress(final String address) throws SDPException {

        if (!emailPattern.matcher(address).matches()) {
            throw new SDPException("Invalid e-mail address:" + address);
        }

        this.address = address;
    }

    /**
     * Sets the personal information of the contact.
     * 
     * @param personal the personal information to set
     * 
     * @throws SDPException if the personal information are not valid
     */
    public void setPersonal(final String personal) throws SDPException {

        if (!personalPattern.matcher(personal).matches()) {
            throw new SDPException("Invalid personal information:" + personal);
        }

        this.personal = personal.trim();
    }

    /**
     * Returns a string representation of the contact. The representation has
     * the form: <b><i>personal</i> <i>&lt;e-mail&gt;</i></b>
     * 
     * @return the string representation of the contact
     */
    public String toString() {

        StringBuffer result;

        result = new StringBuffer();

        if (personal != null) {
            result.append(personal);
            result.append(" <" + getAddress() + ">");
        }
        else {
            result.append(getAddress());
        }

        return result.toString();
    }
}