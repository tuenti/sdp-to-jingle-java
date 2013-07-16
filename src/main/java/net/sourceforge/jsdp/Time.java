/*
 * jSDP: A Java implementation of SDP protocol Copyright (C) 2007 Claudio Di
 * Vita
 */
package net.sourceforge.jsdp;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A <tt>Time</tt> represents a <b>t=<i>&lt;field value&gt;</i></b> field
 * contained in a SDP message. A time field specifies the start and stop times
 * for a SDP announce.
 * 
 * @since 0.1.0
 * 
 * @version 1.0
 * 
 * @author <a href="mailto:cdivita@users.sourceforge.net">Claudio Di Vita</a>
 */
public class Time implements Field {

    /** The class Stream Unique Identifier, SUID */
    private static final long serialVersionUID = -4529219101991941664L;

    /**
     * Constant used to translate between NTP time used in SDP and "native" Java
     * time. NTP time is defined as the number of seconds relative to midnight,
     * January 1, 1900 and Java time is measured in number of milliseconds since
     * midnight, January 1, 1970 UTC (see {@link System#currentTimeMillis()}).
     * 
     * <p>
     * The value of this constant is 2208988800L. It can be used to convert
     * between NTP and Java time using the following formulas:
     * </p>
     * 
     * <ul>
     * <li><code>ntpTime = (javaTime/1000) + Time.NTP_CONST;</code></li>
     * <li><code>javaTime = (ntpTime - Time.NTP_CONST) * 1000;</code></li>
     * </ul>
     * 
     * <p>
     * The Network Time Protocol (NTP) is defined in <a
     * href="http://www.ietf.org/rfc/rfc1035.txt">RFC 1305</a>.
     * </p>
     */
    public static final long NTP_CONSTANT = 2208988800L;

    /** The pattern used to validate the field */
    private static final Pattern fieldPattern = Pattern.compile("(([1-9](\\d){0,9})|0) (([1-9](\\d){0,9})|0)");

    /** The pattern used to validate a NTP value */
    private static final Pattern ntpPattern = Pattern.compile("[1-9](\\d){0,9}");

    /** The session start time */
    protected long start;

    /** The session stop time */
    protected long stop;

    /**
     * Creates a new <tt>Time</tt>. The session start and stop time are set
     * to zero.
     */
    public Time() {

        super();
        this.setZero();
    }

    /**
     * Creates a new <tt>Time</tt>.
     * 
     * @param start the session start time
     * 
     * @param stop the session stop time
     * 
     * @throws SDPException if the NTP representation of the session start
     *         and/or stop time are negative
     */
    public Time(final Date start, final Date stop) throws SDPException {

        super();

        setStartTime(start);
        setStopTime(stop);
    }

    /**
     * Creates a new <tt>Time</tt>.
     * 
     * @param start the session start time
     * 
     * @param stop the session stop time
     * 
     * @throws SDPException if the session start or stop time are negative
     */
    public Time(final long start, final long stop) throws SDPException {

        super();

        setStartTime(start);
        setStopTime(stop);
    }

    /**
     * Returns a <tt>Date</tt> object for a given NTP date value.
     * 
     * @param ntpTime the NTP date value
     * 
     * @return a new <tt>Date</tt> instance that represents the given NTP date
     *         value
     */
    public static Date getDateFromNtp(final long ntpTime) {

        return new Date((ntpTime - NTP_CONSTANT) * 1000);
    }

    /**
     * Transform a <tt>Date</tt> in a long containing the corresponding NTP
     * value.
     * 
     * @param date the <tt>Date</tt> to transform
     * 
     * @return the NTP value of the given date
     */
    public static long getNTP(final Date date) {

        long result = 0;

        if (date == null) {
            result = -1;
        }
        else {
            result = (date.getTime() / 1000) + NTP_CONSTANT;
        }

        return result;
    }

    /**
     * Indicates if a string represents a valid NTP value.
     * 
     * @param input the string to test
     * 
     * @return <tt>true</tt> if the specified string represents a valid NTP
     *         value, <tt>false</tt> otherwise
     */
    public static boolean isValidNTP(final String input) {

        return ntpPattern.matcher(input).matches();
    }

    /**
     * Parse an input string and constructs the equivalent time field.
     * 
     * @param field the string to parse
     * 
     * @return a new <tt>Time</tt> instance
     * 
     * @throws SDPParseException if an error occurs while parsing
     */
    public static Time parse(final String field) throws SDPParseException {

        if (!field.startsWith("t=")) {
            throw new SDPParseException("The string \"" + field + "\" isn't a time field");
        }

        Time t = null;

        /* Create the matcher */
        Matcher matcher = fieldPattern.matcher(field.substring(2));

        /* Test */
        if (matcher.matches()) {

            try {
                /* Create the field */
                t = new Time(Long.parseLong(matcher.group(1)), Long.parseLong(matcher.group(4)));
            }
            catch (SDPException parseException) {
                throw new SDPParseException("The string \"" + field + "\" isn't a valid time field", parseException);
            }
        }
        else {
            throw new SDPParseException("The string \"" + field + "\" isn't a valid time field");
        }

        return t;
    }

    /**
     * Returns a clone of this field.
     * 
     * @return a clone of this field
     */
    public Object clone() {

        Time field = new Time();
        field.start = this.start;
        field.stop = this.stop;

        return field;
    }

    /**
     * Returns the start time of the session.
     * 
     * @return the start time of the session
     */
    public Date getStartTime() {

        return getDateFromNtp(start);
    }

    /**
     * Returns the stop time of the session.
     * 
     * @return the stop time of the session
     */
    public Date getStopTime() {

        return getDateFromNtp(stop);
    }

    /**
     * Returns the type character for the field.
     * 
     * @return the field type character: <b>t</b>
     */
    public char getType() {

        return Field.TIME_FIELD;
    }

    /**
     * Returns whether the start and stop times were set to zero (in NTP).
     * 
     * @return <tt>true</tt> if the start and stop times are set to zero,
     *         <tt>false</tt> otherwise
     */
    public boolean isZero() {

        return ((stop == 0) && (start == 0));
    }

    /**
     * Sets the start time of the session.
     * 
     * @param start the start time
     * 
     * @throws SDPException if the date is <tt>null</tt> or if his NTP
     *         representation is negative
     */
    public void setStartTime(final Date start) throws SDPException {

        if (start == null) {
            throw new SDPException("The start time cannot be null");
        }

        setStartTime(getNTP(start));
    }

    /**
     * Sets the start time of the session.
     * 
     * @param start The start time
     * 
     * @throws SDPException If the start time is negative
     */
    public void setStartTime(final long start) throws SDPException {

        if (start < 0) {
            throw new SDPException("The session start time cannot be negative");
        }

        this.start = start;
    }

    /**
     * Sets the stop time of the session.
     * 
     * @param stop the stop time
     * 
     * @throws SDPException if the date is <tt>null</tt> or his NTP
     *         representation is negative
     */
    public void setStopTime(final Date stop) throws SDPException {

        if (stop == null) {
            throw new SDPException("The stop time cannot be null");
        }

        setStopTime(getNTP(stop));
    }

    /**
     * Sets the stop time of the session.
     * 
     * @param stop the stop time
     * 
     * @throws SDPException if the stop time is negative
     */
    public void setStopTime(final long stop) throws SDPException {

        if (stop < 0) {
            throw new SDPException("The session stop time cannot be negative");
        }

        this.stop = stop;
    }

    /**
     * Sets the start and stop times to zero (in NTP).
     */
    public void setZero() {

        this.start = 0;
        this.stop = 0;
    }

    /**
     * Returns a string representation of the field. The representation has the
     * form: <b>t=<i>&lt;start time&gt;</i> <i>&lt;stop time&gt;</i></b>.
     * 
     * @return the string representation of the field
     */
    public String toString() {

        StringBuffer result = new StringBuffer(getType() + "=");
        result.append(start);
        result.append(" ");
        result.append(stop);

        return result.toString();
    }
}