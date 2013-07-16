/*
 * jSDP: A Java implementation of SDP protocol Copyright (C) 2007 Claudio Di
 * Vita
 */
package net.sourceforge.jsdp.util;

import net.sourceforge.jsdp.SDPException;

/**
 * A network resource specified in a SDP message.
 * 
 * @since 0.1.0
 * 
 * @version 1.0
 * 
 * @author <a href="mailto:cdivita@users.sourceforge.net">Claudio Di Vita</a>
 */
public class Resource extends Address {

    /** The class Stream Unique Identifier, SUID */
    private static final long serialVersionUID = 1512624495750991087L;

    /** The resource TTL */
    protected int ttl;

    /** The number of addresses available for the resource */
    protected int addresses;

    /**
     * Creates a new <tt>Resource</tt>.
     */
    protected Resource() {

        super();
    }

    /**
     * Creates a new <tt>Resource</tt>.
     * 
     * @param address the resource address. The address may contains ttl and the
     *        number of addresses too
     * 
     * @throws SDPException if the resource informations are not valid
     */
    public Resource(final String address) throws SDPException {

        if (address == null) {
            throw new SDPException("Invalid network resource");
        }

        String[] values = address.split("/");
        switch (values.length) {
            case 1:
                setAddress(values[0]);
                this.ttl = 0;
                this.addresses = 1;
                break;
            case 2:
                setAddress(values[0]);
                setTTL(Integer.parseInt(values[1]));
                this.addresses = 1;
                break;
            case 3:
                setAddress(values[0]);
                setTTL(Integer.parseInt(values[1]));
                setAddresses(Integer.parseInt(values[2]));
                break;
            default:
                throw new SDPException("Invalid network resource");
        }
    }

    /**
     * Creates a new <tt>Resource</tt>.
     * 
     * @param address the resource address. The address cannot contains ttl and
     *        the number of addresses
     * 
     * @param ttl The resource ttl
     * 
     * @throws SDPException if the resource informations are not valid
     */
    public Resource(final String address, final int ttl) throws SDPException {

        super(address);
        setTTL(ttl);
        this.addresses = 1;
    }

    /**
     * Creates a new <tt>Resource</tt>.
     * 
     * @param address the resource address. The address cannot contains ttl and
     *        the number of addresses
     * 
     * @param ttl the resource ttl
     * 
     * @param addresses the number of addresses available for the resource
     * 
     * @throws SDPException if the resource informations are not valid
     */
    public Resource(final String address, final int ttl, final int addresses) throws SDPException {

        super(address);
        setTTL(ttl);
        setAddresses(addresses);
    }

    /**
     * Returns a clone of this resource.
     * 
     * @return a clone of this resource
     */
    public Object clone() {

        Resource host = new Resource();

        host.addressType = this.addressType;
        host.address = new String(this.address);
        host.ttl = this.ttl;
        host.addresses = this.addresses;

        return host;
    }

    /**
     * Returns the TTL.
     * 
     * @return the TTL
     */
    public int getTTL() {

        return ttl;
    }

    /**
     * Indicates if the resource has a TTL.
     * 
     * @return <tt>true</tt> if the resource has a TTL, <tt>false</tt>
     *         otherwise
     */
    public boolean hasTTL() {

        return ttl > 0;
    }

    /**
     * Clears the resource's TTL.
     */
    public void removeTTL() {

        this.ttl = 0;
        this.addresses = 1;
    }

    /**
     * Sets the number of addresses of the resource.
     * 
     * @param addresses the number of addresses
     * 
     * @throws SDPException if the number of addesses is negative
     */
    public void setAddresses(final int addresses) throws SDPException {

        if (addresses < 1) {
            throw new SDPException();
        }

        this.addresses = addresses;
    }

    /**
     * Sets the resource TTL.
     * 
     * @param ttl the TTL
     * 
     * @throws SDPException if the TTL is minor than 1 or greater than 255
     */
    public void setTTL(final int ttl) throws SDPException {

        if (ttl < 1 || ttl > 255) {
            throw new SDPException("Invalid TTL");
        }

        this.ttl = ttl;
    }

    /**
     * Returns a string representation of the resource.
     */
    public String toString() {

        StringBuffer result = new StringBuffer(super.toString());

        if (hasTTL()) {
            result.append("/" + ttl);
            if (this.addresses > 1) {
                result.append("/" + addresses);
            }
        }

        return result.toString();
    }
}