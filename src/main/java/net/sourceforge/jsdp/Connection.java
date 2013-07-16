/*
 * jSDP: A Java implementation of SDP protocol Copyright (C) 2007 Claudio Di
 * Vita
 */
package net.sourceforge.jsdp;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.jsdp.util.Address;
import net.sourceforge.jsdp.util.Resource;

/**
 * A <tt>Connection</tt> represents an <b>c=<i>&lt;field value&gt;</i></b>
 * field contained in a SDP message.
 * <p>
 * A connection field is used to identify a network address on which media can
 * be received. For this purpose it also identifies the network type, the
 * address type, the start of an address range, the time to live of the session
 * and the number of addresses in the range, but the time to live and number of
 * addresses are optional.
 * </p>
 * 
 * @since 0.1.0
 * 
 * @version 1.0
 * 
 * @author <a href="mailto:cdivita@users.sourceforge.net">Claudio Di Vita</a>
 */
public class Connection implements Field {

    /** The class Stream Unique Identifier, SUID */
    private static final long serialVersionUID = 1555839237745744131L;

    /** The IP4 address type */
    public static final String IP4 = Address.IP4;

    /** The IP6 address type */
    public static final String IP6 = Address.IP6;

    /** The pattern used to parse the field */
    private static final Pattern fieldPattern = Pattern.compile("IN (IP4|IP6) ((.+))");

    /** The network resource specified by the field */
    protected Resource resource;

    /**
     * Creates a new <tt>Connection</tt>.
     */
    protected Connection() {

        super();
    }

    /**
     * Creates a new <tt>Connection</tt>.
     * 
     * @param resource the network address on which media can be received. The
     *        address type associated with this connection depends by address
     *        value
     * 
     * @throws SDPException if the network address is not valid
     */
    public Connection(final String resource) throws SDPException {

        super();
        this.resource = new Resource(resource);
    }

    /**
     * Parse an input string and constructs the equivalent connection field.
     * 
     * @param field the string to parse
     * 
     * @return a new <tt>Connection</tt> instance
     * 
     * @throws SDPParseException if an error occurs while parsing
     */
    public static Connection parse(final String field) throws SDPParseException {

        if (!field.startsWith("c=")) {
            throw new SDPParseException("The string \"" + field + "\" isn't a connection field");
        }

        Connection c = null;

        /* Create the matcher */
        Matcher matcher = fieldPattern.matcher(field.substring(2));

        /* Test */
        if (matcher.matches()) {
            try {

                /* Get the address type */
                String type = matcher.group(1);

                c = new Connection(matcher.group(2));

                if (!c.getAddressType().equals(type)) {
                    throw new SDPParseException("The address " + c.getAddress() + " isn't an " + type + " address");
                }
            }
            catch (SDPException parseException) {
                throw new SDPParseException("The string \"" + field + "\" isn't a valid connection field", parseException);
            }
        }
        else {
            throw new SDPParseException("The string \"" + field + "\" isn't a valid connection field");
        }

        return c;
    }

    /**
     * Returns a clone of this field.
     * 
     * @return a clone of this field
     */
    public Object clone() {

        Connection field = new Connection();
        field.resource = (Resource) resource.clone();

        return field;
    }

    /**
     * Returns the network address.
     * 
     * @return the network address
     */
    public String getAddress() {

        return resource.getAddress();
    }

    /**
     * Returns the type of the address.
     * 
     * @return the type of the address: <b>IP4</b> or <b>IP6</b>
     */
    public String getAddressType() {

        return resource.getAddressType();
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
     * Returns the type character for the field.
     * 
     * @return the field type character: <b>c</b>
     */
    public char getType() {

        return Field.CONNECTION_FIELD;
    }

    /**
     * Sets the network address.
     * 
     * @param address the network address
     * 
     * @throws SDPException if the address isn't a valid IP4, IP6 or FQDN
     *         address
     */
    public void setAddress(final String address) throws SDPException {

        resource.setAddress(address);
    }

    /**
     * Sets the network address.
     * 
     * @param address the network address
     * 
     * @param ttl the time to live
     * 
     * @throws SDPException if the address isn't a valid IP4, IP6 or FQDN
     *         address, or TTL is minor than 1 or greater than 255
     */
    public void setAddress(final String address, final int ttl) throws SDPException {

        resource.setAddress(address);
        resource.setTTL(ttl);
    }

    /**
     * Sets the network address.
     * 
     * @param address the network address
     * 
     * @param ttl the time to live
     * 
     * @param addresses the number of the addresses
     * 
     * @throws SDPException if the address isn't a valid IP4, IP6 or FQDN
     *         address, or TTL is minor than 1 or greater than 255, or the
     *         number of addresses is negative
     */
    public void setAddress(final String address, final int ttl, final int addresses) throws SDPException {

        resource.setAddress(address);
        resource.setTTL(ttl);
        resource.setAddresses(addresses);
    }

    /**
     * Returns a string representation of the field. The representation has the
     * form: <b>c=IN <i>&lt;type&gt;</i> <i>&lt;address&gt;</i></b>
     * 
     * @return The string representation of the field
     */
    public String toString() {

        return getType() + "=" + Address.IN + " " + resource.toString();
    }
}