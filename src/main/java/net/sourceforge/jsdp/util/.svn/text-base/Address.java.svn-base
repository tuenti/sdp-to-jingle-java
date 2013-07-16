/*
 * jSDP: A Java implementation of SDP protocol Copyright (C) 2007 Claudio Di
 * Vita
 */
package net.sourceforge.jsdp.util;

import java.io.Serializable;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.regex.Pattern;

import net.sourceforge.jsdp.SDPException;

/**
 * A generic SDP network address. There are three network address types:
 * <ul>
 * <li><b>IP4</b>: the standard IP address</li>
 * <li><b>IP6</b>: the newer version of IP, still not used</li>
 * <li><b>FQDN</b>: Fully Qualified Domain Name, that is a DNS host name</li>
 * </ul>
 * 
 * @since 0.1.0
 * 
 * @version 1.0
 * 
 * @author <a href="mailto:cdivita@users.sourceforge.net">Claudio Di Vita</a>
 */
public class Address implements Cloneable, Serializable {

    /** The class Stream Unique Identifier, SUID */
    private static final long serialVersionUID = -3442566371424669052L;

    /**
     * FQDN format:
     * <ul>
     * <li>fqdn: <i>&lt;label&gt;</i> ("." <i>&lt;label&gt;</i>)</li>
     * <li>label: <code>([a-zA-Z]+(([\\w-]+)*[\\w]+)*)+</code>
     * </ul>
     */
    private static final Pattern fqdnPattern = Pattern.compile("([a-zA-Z]+(([\\w-]+)*[\\w]+)*)+(\\.[a-zA-Z]+(([\\w-]+)*[\\w]+)*)*");

    /** The Internet network type */
    public static final String IN = "IN";

    /** The FDQN address type */
    private static final String FQDN = "FQDN";

    /** The IP4 address type */
    public static final String IP4 = "IP4";

    /** The IP6 address type */
    public static final String IP6 = "IP6";

    /** The network address */
    protected String address;

    /** The address type */
    protected String addressType;

    /**
     * Creates a new <tt>Address</tt>.
     * 
     */
    protected Address() {

        super();

        try {
            setAddress(InetAddress.getLocalHost().getHostAddress());
        }
        catch (Exception someException) {
            /* Do nothing */
        }
    }

    /**
     * Creates a new <tt>Address</tt>.
     * 
     * @param address the address value
     * 
     * @throws SDPException if the address is not valid
     */
    public Address(final String address) throws SDPException {

        super();

        if (address == null) {
            throw new SDPException("Invalid host name");
        }

        setAddress(address);
    }

    /**
     * Indicate if an address is a Fully Qualified Domain Name (and not an IP4
     * or IP6 address)
     * 
     * @param address the address to check
     * 
     * @return <tt>true</tt> if the address is a Fully Qualified Domain Name,
     *         <tt>false</tt> otherwise
     */
    public static boolean isFQDN(final String address) {

        return fqdnPattern.matcher(address).matches();
    }

    /**
     * Returns a clone of this address.
     * 
     * @return a clone of this address
     */
    public Object clone() {

        Address host = new Address();

        host.addressType = this.addressType;
        host.address = new String(this.address);

        return host;
    }

    /**
     * Compare for equality of hosts. Host names are compared by textual
     * equality. No dns lookup is performed.
     * 
     * @param object object to compare
     * 
     * @return <tt>true</tt> if two address are equals, <tt>false</tt>
     *         otherwise
     */
    public boolean equals(final Object object) {

        if (!(object instanceof Address)) {
            return false;
        }

        Address otherHost = (Address) object;
        return otherHost.address.equalsIgnoreCase(this.address);
    }

    /**
     * Returns the address value.
     * 
     * @return the address value
     */
    public String getAddress() {

        return address;
    }

    /**
     * Returns the address type. Possible values are:
     * <ul>
     * <li><b>IP4</b>: the standard IP address</li>
     * <li><b>IP6</b>: the newer version of IP, still not used</li>
     * <li><b>FQDN</b>: Fully Qualified Domain Name, that is a DNS host name</li>
     * </ul>
     * 
     * @return the address type
     */
    public String getAddressType() {

        return addressType;
    }

    /**
     * Indicate if the address is a Fully Qualified Domain Name (and not an IP4
     * or IP6 address).
     * 
     * @return <tt>true</tt> if the address is a Fully Qualified Domain Name,
     *         <tt>false</tt> otherwise
     */
    public boolean isFQDN() {

        return addressType.compareTo(FQDN) == 0;
    }

    /**
     * Indicates if the address is an IP address (and not a Fully Qualified
     * Domain name).
     * 
     * @return <tt>true</tt> if is an IP address, <tt>false</tt> otherwise
     */
    public boolean isIPAddress() {

        return addressType.compareTo(FQDN) != 0;
    }

    /**
     * Sets the address value
     * 
     * @param address the address value to set
     * 
     * @throws SDPException if the address is not valid
     */
    public void setAddress(final String address) throws SDPException {

        if (isFQDN(address)) {
            this.address = address;
            /*
             * Because at this time all internet host has an IP4 address, if the
             * host name is a FQDN, the address type is considered to be IP4
             */
            this.addressType = IP4;
        }
        else {
            try {
                InetAddress ip = InetAddress.getByName(address);
                this.addressType = (ip instanceof Inet4Address) ? IP4 : IP6;
                /*
                 * if (ip instanceof Inet4Address) { this.addressType = IP4; }
                 * else { this.addressType = IP6; }
                 */
                this.address = address;
            }
            catch (UnknownHostException invalidIP) {
                throw new SDPException("Not IP4 or IP6 address: " + address);
            }
        }
    }

    /**
     * Returns a string representation of the address.
     * 
     * @return The string representation of the address
     */
    public String toString() {

        return addressType + " " + address;
    }
}