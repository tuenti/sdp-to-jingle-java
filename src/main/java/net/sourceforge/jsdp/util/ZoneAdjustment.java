/*
 * jSDP: A Java implementation of SDP protocol Copyright (C) 2007 Claudio Di
 * Vita
 */
package net.sourceforge.jsdp.util;

import java.io.Serializable;
import java.util.Date;

import net.sourceforge.jsdp.SDPException;
import net.sourceforge.jsdp.Time;

/**
 * Represents an adjustment time and his offset from the base time, for a
 * repeated session scheduling.
 * 
 * @since 0.1.0
 * 
 * @version 1.0
 * 
 * @author <a href="mailto:cdivita@users.sourceforge.net">Claudio Di Vita</a>
 * 
 * @see net.sourceforge.jsdp.TimeZone
 */
public class ZoneAdjustment implements Cloneable, Serializable {

    /** The class Stream Unique Identifier, SUID */
    private static final long serialVersionUID = -5613785303341511624L;

    /** The adjustment offset */
    protected long offset;

    /** The NTP representation of adjustment time */
    protected long time;

    /**
     * Creates a new <tt>ZoneAdjustment</tt>
     */
    protected ZoneAdjustment() {

        super();
    }

    /**
     * Creates a new <tt>ZoneAdjustment</tt>.
     * 
     * @param time the adjustment time
     * 
     * @param offset the adjustment offset
     * 
     * @throws SDPException if the NTP representation of the adjustment time is
     *         negative
     */
    public ZoneAdjustment(final Date time, final long offset) throws SDPException {

        super();

        setTime(Time.getNTP(time));
        this.offset = offset;
    }

    /**
     * Creates a new <tt>ZoneAdjustment</tt>.
     * 
     * @param time the adjustment time
     * 
     * @param offset the adjustment offset
     * 
     * @throws SDPException if the adjustment time is negative
     */
    public ZoneAdjustment(final long time, final long offset) throws SDPException {

        super();

        setTime(time);
        this.offset = offset;
    }

    /**
     * Returns a clone of this zone adjustment.
     * 
     * @return a clone of this zone adjustment
     */
    public Object clone() {

        ZoneAdjustment adjustment = new ZoneAdjustment();

        adjustment.time = this.time;
        adjustment.offset = this.offset;

        return adjustment;
    }

    /**
     * Returns the adjustment offset.
     * 
     * @return the adjustment offset
     */
    public long getOffset() {

        return offset;
    }

    /**
     * Returns the adjustment time.
     * 
     * @return the adjustment time
     */
    public long getTime() {

        return time;
    }

    /**
     * Sets the adjustment offset.
     * 
     * @param offset the adjustment offset
     */
    public void setOffset(final long offset) {

        this.offset = offset;
    }

    /**
     * Sets the adjustment time.
     * 
     * @param time the adjustment time
     * 
     * @throws SDPException if the adjustment time is negative
     */
    public void setTime(final long time) throws SDPException {

        if (time < 0) {
            throw new SDPException("Adjustment time must be > 0");
        }

        this.time = time;
    }

    /**
     * Returns a string representation of the object. The representation has the
     * form: <b>&lt;time&gt; &lt;offset&gt;</b>
     * 
     * @return the string representation of the object
     */
    public String toString() {

        return time + " " + offset;
    }
}