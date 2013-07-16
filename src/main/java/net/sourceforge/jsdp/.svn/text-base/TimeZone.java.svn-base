/*
 * jSDP: A Java implementation of SDP protocol Copyright (C) 2007 Claudio Di
 * Vita
 */
package net.sourceforge.jsdp;

import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;

import net.sourceforge.jsdp.util.TypedTime;
import net.sourceforge.jsdp.util.ZoneAdjustment;

/**
 * A <tt>TimeZone</tt> represents a <b>z=<i>&lt;field value&gt;</i></b>
 * field contained in a SDP message. The timezone field allows the sender to
 * specify a list of adjustment times and offsets from the base time.
 * 
 * @since 0.1.0
 * 
 * @version 1.0
 * 
 * @author <a href="mailto:cdivita@users.sourceforge.net">Claudio Di Vita</a>
 */
public class TimeZone implements Field {

    /** The class Stream Unique Identifier, SUID */
    private static final long serialVersionUID = -87926730273093503L;

    /** Indicates if the field will be output as a typed time or a number */
    protected boolean isTyped;

    /** The map of timezone adjustments */
    protected Hashtable adjustments;

    /**
     * Creates a new <tt>TimeZone</tt>.
     */
    protected TimeZone() {

        super();

        adjustments = new Hashtable(3);
    }

    /**
     * Creates a new <tt>TimeZone</tt>.
     * 
     * @param time the adjustment time
     * 
     * @param offset the offset
     * 
     * @throws SDPException if the NTP representation of the adjustment time is
     *         negative
     */
    public TimeZone(final Date time, final long offset) throws SDPException {

        this(new ZoneAdjustment(time, offset));
    }

    /**
     * Creates a new <tt>TimeZone</tt>.
     * 
     * @param adjustment the time zone adjustment
     */
    public TimeZone(final ZoneAdjustment adjustment) {

        this();

        addZoneAdjustment(adjustment);
        this.isTyped = true;
    }

    /**
     * Parse an input string and constructs the equivalent timezone field.
     * 
     * @param field the string to parse
     * 
     * @return a new <tt>TimeZone</tt> instance
     * 
     * @throws SDPParseException if an error occurs while parsing
     */
    public static TimeZone parse(final String field) throws SDPParseException {

        if (!field.startsWith("z=")) {
            throw new SDPParseException("The string \"" + field + "\" isn't a timezone field");
        }

        String[] values = field.substring(2).split(" ");

        if (values.length < 2 || (values.length % 2 != 0)) {
            throw new SDPParseException("The string \"" + field + "\" isn't a valid timezone field");
        }

        TimeZone z = new TimeZone();

        for (int i = 0; i < values.length; i += 2) {
            try {
                z.addZoneAdjustment(new ZoneAdjustment(Long.parseLong(values[i]), TypedTime.getTime(values[i + 1])));
            }
            catch (Exception parseException) {
                throw new SDPParseException("The string \"" + field + "\" isn't a valid timezone field", parseException);
            }
        }

        return z;
    }

    /**
     * Add a zone adjustment to the field.
     * 
     * @param time the adjustment time
     * @param offset the offset
     * 
     * @throws SDPException if the NTP representation of the adjustment time is
     *         negative
     */
    public void addZoneAdjustment(final Date time, final long offset) throws SDPException {

        adjustments.put(time, new ZoneAdjustment(time, offset));
    }

    /**
     * Add a zone adjustment to the field.
     * 
     * @param adjustment the time zone adjustment
     */
    public void addZoneAdjustment(final ZoneAdjustment adjustment) {

        adjustments.put(new Date(adjustment.getTime()), adjustment);
    }

    /**
     * Returns a clone of this field.
     * 
     * @return a clone of this field
     */
    public Object clone() {

        TimeZone field = new TimeZone();

        field.isTyped = this.isTyped;
        field.adjustments = (Hashtable) this.adjustments.clone();

        return field;
    }

    /**
     * Returns the type character for the field.
     * 
     * @return the field type character: <tt>z</t>
     */
    public char getType() {

        return Field.TIMEZONE_FIELD;
    }

    /*
     * <p>Returns a Map of adjustment times, where :</p>
     * 
     * <ul> <li>key = {@link java.util.Date}</li> <li>value =
     * {@link ZoneAdjustment}</li> </ul>
     * 
     * @return The time zone adjustments
     */
    /*
     * public Map getZoneAdjustments() {
     * 
     * return adjustments; }
     */

    /**
     * Returns whether the field will be output as a typed time or a number.
     * 
     * <p>
     * Typed time is formatted as an number followed by a unit character: the
     * unit indicates an appropriate multiplier for the number. The following
     * unit types are allowed:
     * </p>
     * <ul>
     * <li><tt><b>d</b></tt> - days (86400 seconds)</li>
     * <li><tt><b>h</b></tt> - hours (3600 seconds)</li>
     * <li><tt><b>m</b></tt> - minutes (60 seconds)</li>
     * <li><tt><b>s</b></tt> - seconds (1 second)</li>
     * </ul>
     * 
     * @return <tt>true</tt> if the field will be output as a typed time,
     *         <tt>false</tt> if as a number
     */
    public boolean isTypedTime() {

        return isTyped;
    }

    /**
     * Sets whether the field will be output as a typed time or a number.
     * 
     * <p>
     * Typed time is formatted as an number followed by a unit character: the
     * unit indicates an appropriate multiplier for the number. The following
     * unit types are allowed:
     * </p>
     * <ul>
     * <li><tt><b>d</b></tt> - days (86400 seconds)</li>
     * <li><tt><b>h</b></tt> - hours (3600 seconds)</li>
     * <li><tt><b>m</b></tt> - minutes (60 seconds)</li>
     * <li><tt><b>s</b></tt> - seconds (1 second)</li>
     * </ul>
     */
    public void setTypedTime(final boolean typedTime) {

        this.isTyped = typedTime;
    }

    /**
     * Returns a string representation of the field. The representation has the
     * form: <b>z=<i>&lt;time&gt;</i> <i>&lt;offset&gt;</i>+
     * 
     * @return The string representation of the field
     */
    public String toString() {

        StringBuffer result = new StringBuffer(getType() + "=");
        ZoneAdjustment adjustment;
        Enumeration values = adjustments.elements();
        while (values.hasMoreElements()) {

            adjustment = (ZoneAdjustment) values.nextElement();

            result.append(adjustment.getTime() + " ");
            if (this.isTyped) {
                result.append(TypedTime.toString(adjustment.getOffset()) + " ");
            }
            else {
                result.append(adjustment.getOffset() + " ");
            }
        }
        result.deleteCharAt(result.length() - 1);

        return result.toString();
    }
}