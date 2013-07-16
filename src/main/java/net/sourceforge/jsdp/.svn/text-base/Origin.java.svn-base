/*
 * jSDP: A Java implementation of SDP protocol Copyright (C) 2007 Claudio Di
 * Vita
 */
package net.sourceforge.jsdp;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.jsdp.util.Address;

/**
 * An <tt>Origin</tt> represents an <b>o=<i>&lt;field value&gt;</i></b>
 * field contained in a SDP message. The origin field identifies the originator
 * of the session, that is not necessarily the same entity who is involved in
 * the session. This field contains:
 * <ul>
 * <li>The name of the user originating the session</li>
 * <li>An unique session identifier</li>
 * <li>An unique version for the session</li>
 * <li>The network type</li>
 * <li>The ddress type</li>
 * <li>The address of the originator</li>
 * </ul>
 * <p>
 * These fields should uniquely identify the session.
 * </p>
 * 
 * @since 0.1.0
 * 
 * @version 1.0
 * 
 * @author <a href="mailto:cdivita@users.sourceforge.net">Claudio Di Vita</a>
 */
public class Origin implements Field {

    /** The class Stream Unique Identifier, SUID */
    private static final long serialVersionUID = 8892230839201408790L;

    /** The pattern used to parse the field */
    private static final Pattern fieldPattern = Pattern.compile("([^ ]+) (\\d+) (\\d+) IN (IP4|IP6) (([^ ]+))");

    /** The pattern used to validate the user name */
    private static final Pattern userPattern = Pattern.compile("[\\w'-\\./:?#\\$&\\*;=@\\[\\]\\^_`\\{\\}\\|\\+\\~]+");

    /** The name of the session originator */
    private String user;

    /** The session identifier */
    private long sessionID;

    /** The session version */
    private long sessionVersion;

    /** The originator's host information */
    private Address address;

    /**
     * Creates a new <tt>Origin</tt>. The values are sets as follows:
     * <p>
     * <code>
     * o=<i>&lt;user&gt; &lt;current time as NTP&gt; &lt;current time as NTP&gt; &lt;localhost name&gt;</i><br/>
     * </code>
     * </p>
     * The <i>&lt;user&gt;</i> value is the name of the OS user that invokes
     * this method.
     * 
     * @throws SDPException if no IP address for the local host could be found,
     *         or the current user name contains characters that are not allowed
     */
    public Origin() throws SDPException {

        super();

        setUser(System.getProperty("user.name"));

        this.sessionID = Time.getNTP(new Date());
        this.sessionVersion = Time.getNTP(new Date());

        try {
            this.address = new Address(InetAddress.getLocalHost().getHostName());
        }
        catch (UnknownHostException noLocalHost) {
            throw new SDPException("Cannot find the localhost name");
        }
    }

    /**
     * Creates a new <tt>Origin</tt>. The session id was generated
     * automatically using NTP and the user value is set with the name of the OS
     * user that invokes this constructor.
     * 
     * @param sessionVersion the session version
     * 
     * @param address the session originator host address
     * 
     * @throws SDPException if one or more parameter provided are not valid
     */
    public Origin(final long sessionVersion, final String address) throws SDPException {

        super();

        setUser(System.getProperty("user.name"));
        this.sessionID = Time.getNTP(new Date());
        setSessionVersion(sessionVersion);

        this.address = new Address(address);
    }

    /**
     * Creates a new <tt>Origin</tt>. This constructor is used to implement
     * the {@link #clone()} method, because this class has a default constructor
     * that performs some operations that aren't required while cloning a field.
     * 
     * @param origin the <tt>Origin</tt> to clone
     */
    protected Origin(final Origin origin) {

        super();

        this.user = new String(origin.user);
        this.sessionID = origin.sessionID;
        this.sessionVersion = origin.sessionVersion;
        this.address = (Address) origin.address.clone();
    }

    /**
     * Creates a new <tt>Origin</tt>. Both session identifier and session
     * version were generated automatically using NTP and the user value is set
     * with the name of the OS user that invokes this constructor.
     * 
     * @param address the session originator host address
     * 
     * @throws SDPException if the address are not valid
     */
    public Origin(final String address) throws SDPException {

        super();

        setUser(System.getProperty("user.name"));
        this.sessionID = Time.getNTP(new Date());
        this.sessionVersion = Time.getNTP(new Date());

        this.address = new Address(address);
    }

    /**
     * Creates a new <tt>Origin</tt>.
     * 
     * @param user the name of the session originator
     * 
     * @param sessionID the session identifier
     * 
     * @param sessionVersion the session version
     * 
     * @param address the session originator host address
     * 
     * @throws SDPException if one or more parameter provided are not valid
     */
    public Origin(final String user, final long sessionID, final long sessionVersion, final String address) throws SDPException {

        super();

        setUser(user);
        setSessionID(sessionID);
        setSessionVersion(sessionVersion);

        this.address = new Address(address);
    }

    /**
     * Creates a new <tt>Origin</tt>.
     * 
     * @param user the name of the session originator
     * 
     * @param sessionVersion the session version
     * 
     * @param address the session originator host address
     * 
     * @throws SDPException if one or more parameter provided are not valid
     */
    public Origin(final String user, final long sessionVersion, final String address) throws SDPException {

        super();

        setUser(user);
        this.sessionID = Time.getNTP(new Date());
        setSessionVersion(sessionVersion);

        this.address = new Address(address);
    }

    /**
     * Parse an input string and constructs the equivalent origin field.
     * 
     * @param field the string to parse
     * 
     * @return a new <tt>Origin</tt> instance
     * 
     * @throws SDPParseException if an error occurs while parsing
     */
    public static Origin parse(final String field) throws SDPParseException {

        if (!field.startsWith("o=")) {
            throw new SDPParseException("The string \"" + field + "\" isn't an origin field");
        }

        Origin o = null;

        /* Create the matcher */
        Matcher matcher = fieldPattern.matcher(field.substring(2));

        /* Test */
        if (matcher.matches()) {
            try {

                String type = matcher.group(4);
                o = new Origin(matcher.group(1), Long.parseLong(matcher.group(2)), Long.parseLong(matcher.group(3)), matcher.group(5));

                if (!o.getAddressType().equals(type)) {
                    throw new SDPParseException("The address " + o.getAddress() + " isn't an " + type + " address");
                }
            }
            catch (SDPException parseException) {
                throw new SDPParseException("The string \"" + field + "\" isn't a valid origin field", parseException);
            }
        }
        else {
            throw new SDPParseException("The string \"" + field + "\" isn't a valid origin field");
        }

        return o;
    }

    /**
     * Returns a clone of this field.
     * 
     * @return a clone of this field
     */
    public Object clone() {

        return new Origin(this);
    }

    /**
     * Returns the session originator host address.
     * 
     * @return the session originator host address
     */
    public String getAddress() {

        return address.getAddress();
    }

    /**
     * Returns the address type.
     * 
     * @return the address type
     */
    public String getAddressType() {

        return address.getAddressType();
    }

    /**
     * Returns the network type.
     * 
     * @return the network type. Because SDP was used in Internet, this method
     *         always returns <b>IN</b>
     */
    public String getNetType() {

        return Address.IN;
    }

    /**
     * Returns the session identifier.
     * 
     * @return the session identifier
     */
    public long getSessionID() {

        return sessionID;
    }

    /**
     * Returns the session version.
     * 
     * @return the session version
     */
    public long getSessionVersion() {

        return sessionVersion;
    }

    /**
     * Returns the type character for the field
     * 
     * @return The field type character: <b>o</b>
     */
    public char getType() {

        return Field.ORIGIN_FIELD;
    }

    /**
     * Returns the name of the session originator.
     * 
     * @return the session originator's name
     */
    public String getUser() {

        return user;
    }

    /**
     * Sets the session originator host address.
     * 
     * @param address the session originator host address
     * 
     * @throws SDPException if the address is not valid
     */
    public void setAddress(final String address) throws SDPException {

        this.address.setAddress(address);
    }

    /**
     * Sets the session identifier.
     * 
     * @param sessionID the session identifier
     * 
     * @throws SDPException if the session identifier is negative
     */
    public void setSessionID(final long sessionID) throws SDPException {

        if (sessionID < 0) {
            throw new SDPException("Session id cannot be negative");
        }

        this.sessionID = sessionID;
    }

    /**
     * Sets the session version.
     * 
     * @param sessionVersion the session version
     * 
     * @throws SDPException if the session version is negative
     */
    public void setSessionVersion(final long sessionVersion) throws SDPException {

        if (sessionVersion < 0) {
            throw new SDPException("Session version cannot be negative");
        }

        this.sessionVersion = sessionVersion;
    }

    /**
     * Sets the name of the session originator.
     * 
     * @param user the name of the session creator
     * 
     * @throws SDPException if the name is not valid (for example it contains
     *         spaces)
     */
    public void setUser(final String user) throws SDPException {

        if (!userPattern.matcher(user).matches()) {
            throw new SDPException("Invalid user name: " + user);
        }

        this.user = user;
    }

    /**
     * Returns a string representation of the field. The representation has the
     * form: <b><i>&lt;type&gt;</i>=<i>&lt;value&gt;</i></b>.
     * 
     * @return the string representation of the field
     */
    public String toString() {

        StringBuffer result = new StringBuffer(getType() + "=");

        result.append(this.user + " ");
        result.append(this.sessionID + " ");
        result.append(this.sessionVersion);
        result.append(" IN ");
        result.append(address.toString());

        return result.toString();
    }
}