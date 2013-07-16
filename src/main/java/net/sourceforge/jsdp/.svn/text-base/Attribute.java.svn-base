/*
 * jSDP: A Java implementation of SDP protocol Copyright (C) 2007 Claudio Di
 * Vita
 */
package net.sourceforge.jsdp;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * An <tt>Attribute</tt> represents an <b>a=<i>&lt;field value&gt;</i></b>
 * field contained in a SDP message. Attribute fields may be of two forms:
 * <ul>
 * <li>Property attribute, that is simply of the form <i><b>a=&lt;flag&gt;</i></b>.
 * These are binary attributes, and the presence of the attribute conveys that
 * the attribute is a property of the session. An example might be <i>a=recvonly</i></li>
 * <li>Value attribute, that is of the form <b>a=<i>&lt;attribute&gt;:&lt;value&gt;</i></b>.
 * For example, a whiteboard could have the value attribute
 * <i>a=orient:landscape</i></li>
 * </ul>
 * <p>
 * Obviously attribute field interpretation depends on the media tool being
 * invoked.
 * </p>
 * 
 * @since 0.1.0
 * 
 * @version 1.1
 * 
 * @author <a href="mailto:cdivita@users.sourceforge.net">Claudio Di Vita</a>
 */
public class Attribute implements Field {

    /** The class Stream Unique Identifier, SUID */
    private static final long serialVersionUID = -6003475409338057035L;

    /** The pattern used to parse the field */
    private static final Pattern fieldPattern = Pattern.compile("([^:]+)(:((.+)))?");

    /** The pattern used to validate the attribute name */
    private static final Pattern namePattern = Pattern.compile("(\\w)+(-\\w+)*");

    /** The pattern used to validate the attribute value */
    private static final Pattern valuePattern = Pattern.compile("[^\0\\r\\n]+");

    /** The attribute property name */
    protected String name;

    /** The attribute value */
    protected String value;

    /**
     * Creates a new <tt>Attribute</tt>.
     */
    protected Attribute() {

        super();
    }

    /**
     * Creates a new <tt>Attribute</tt>.
     * 
     * @param name the attribute name
     * 
     * @throws SDPException if the name is <tt>null</tt> or not valid
     */
    public Attribute(final String name) throws SDPException {

        super();

        setName(name);
        this.value = null;
    }

    /**
     * Creates a new <tt>Attribute</tt>.
     * 
     * @param name the attribute name
     * 
     * @param value the attribute value
     * 
     * @throws SDPException if the name is <tt>null</tt> or not valid, or the
     *         value is not valid
     */
    public Attribute(final String name, final String value) throws SDPException {

        super();

        setName(name);
        setValue(value);
    }

    /**
     * Parse an input string and constructs the equivalent attribute field.
     * 
     * @param field the string to parse
     * 
     * @return a new <tt>Attribute</tt> instance
     * 
     * @throws SDPParseException if an error occurs while parsing
     */
    public static Attribute parse(final String field) throws SDPParseException {

        if (!field.startsWith("a=")) {
            throw new SDPParseException("The string \"" + field + "\" isn't an attribute field");
        }

        Attribute a = null;
        Matcher matcher = fieldPattern.matcher(field.substring(2));

        /* Test */
        if (matcher.matches()) {

            try {
                /* The attribute has the form a=<attribute>:<value> */
                if (matcher.group(3) != null) {
                    a = new Attribute(matcher.group(1), matcher.group(3));
                }
                else {
                    /* The attribute has the form a=<flag> */
                    a = new Attribute(matcher.group(1));
                }
            }
            catch (SDPException parseException) {
                throw new SDPParseException("The string \"" + field + "\" isn't a valid attribute field", parseException);
            }
        }
        else {
            throw new SDPParseException("The string \"" + field + "\" isn't a valid attribute field");
        }

        return a;
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.sourceforge.jsdp.Field#clone()
     */
    public Object clone() {

        Attribute field = new Attribute();
        field.name = new String(this.name);

        if (this.value != null) {
            field.value = new String(this.value);
        }
        else {
            field.value = null;
        }

        return field;
    }

    /**
     * Returns the name of the attribute.
     * 
     * @return the attribute name
     */
    public String getName() {

        return name;
    }

    /**
     * Returns the type character for the field.
     * 
     * @return the field type character: <b>a</b>
     */
    public char getType() {

        return Field.ATTRIBUTE_FIELD;
    }

    /**
     * Returns the value of the attribute.
     * 
     * @return a <tt>String</tt> if the attribute has an associated value
     *         <tt>null</tt> otherwise
     */
    public String getValue() {

        return value;
    }

    /**
     * Determines if this attribute has an associated value.
     * 
     * @return <tt>true</tt> if the attribute has a value, <tt>false</tt>
     *         otherwise
     */
    public boolean hasValue() {

        return value != null;
    }

    /**
     * Resets the attribute value.
     */
    public void resetValue() {

        this.value = null;
    }

    /**
     * Sets the name of the attribute.
     * 
     * @param name the name/id of the attribute
     * 
     * @throws SDPException if the name is <tt>null</tt> or not valid
     */
    public void setName(final String name) throws SDPException {

        if (name == null) {
            throw new SDPException("The attribute name cannot be null");
        }

        if (!namePattern.matcher(name).matches()) {
            throw new SDPException("Invalid attribute name: " + name);
        }

        this.name = name;
    }

    /**
     * Sets the value of this attribute.
     * 
     * @param value the attribute value
     * 
     * @throws SDPException if the specified value is not valid
     */
    public void setValue(final String value) throws SDPException {

        if (value != null && !valuePattern.matcher(value).matches()) {
            throw new SDPException("Invalid attribute value: " + value);
        }

        this.value = value;
    }

    /**
     * Returns a string representation of the field. The representation can had
     * the following forms:
     * <ul>
     * <li><b>a=<i>&lt;flag&gt;</i></b></li>
     * <li><b>a=<i>&lt;attribute&gt;</i>:<i>&lt;value&gt;</i></b></li>
     * </ul>
     * 
     * @return the string representation of the field
     */
    public String toString() {

        StringBuffer result;

        result = new StringBuffer(getType() + "=");
        result.append(name);

        if (hasValue()) {
            result.append(":" + value);
        }

        return result.toString();
    }
}