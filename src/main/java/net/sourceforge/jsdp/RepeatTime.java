/*
 * jSDP: A Java implementation of SDP protocol Copyright (C) 2007 Claudio Di
 * Vita
 */
package net.sourceforge.jsdp;

import java.util.Vector;

import net.sourceforge.jsdp.util.TypedTime;

/**
 * A <tt>RepeatTime</tt> represents a <b>r=<i>&lt;field value&gt;</i></b>
 * field contained in a SDP message. A repeat time field specify repeat times
 * for a session: his consists of a <i>repeat interval</i>, an <i>active
 * duration</i> and a list of offsets relative to the
 * {@link Time#getStartTime()} start time.
 * 
 * @see Time
 * 
 * @since 0.1.0
 * 
 * @version 1.0
 * 
 * @author <a href="mailto:cdivita@users.sourceforge.net">Claudio Di Vita</a>
 */
public class RepeatTime implements Field {

    /** The class Stream Unique Identifier, SUID */
    private static final long serialVersionUID = -7772961172651467572L;

    /** The repeat interval */
    protected long repeatInterval;

    /** The active duraction */
    protected long activeDuration;

    /** The list of offset relative to the <b>t</b> field start time */
    protected Vector offsets;

    /** Indicates if the field will be output as a typed time or a number */
    protected boolean isTyped;

    /**
     * Creates a new <tt>RepeatTime</tt>.
     */
    protected RepeatTime() {

        super();
    }

    /**
     * Creates a new <tt>RepeatTime</tt>.
     * 
     * @param repeatInterval the repeat interval
     * 
     * @param activeDuration the active duration
     * 
     * @param offset the offset
     * 
     * @throws SDPException if the parameters are not valid
     */
    public RepeatTime(final long repeatInterval, final long activeDuration, final long offset) throws SDPException {

        super();

        this.isTyped = true;

        setActiveDuration(activeDuration);
        setRepeatInterval(repeatInterval);

        this.offsets = new Vector(3);
        addOffset(offset);
    }

    /**
     * Creates a new <tt>RepeatTime</tt>.
     * 
     * @param repeatInterval the repeat interval
     * 
     * @param activeDuration the active duration
     * 
     * @param offsets the offsets
     * 
     * @throws SDPException if the parameters are not valid
     */
    public RepeatTime(final long repeatInterval, final long activeDuration, final long[] offsets) throws SDPException {

        super();

        this.isTyped = true;

        setRepeatInterval(repeatInterval);
        setActiveDuration(activeDuration);

        this.offsets = new Vector(offsets.length);
        for (int i = 0; i < offsets.length; i++) {
            addOffset(offsets[i]);
        }
    }

    /**
     * Parse an input string and constructs the equivalent repeat time field.
     * 
     * @param field the string to parse
     * 
     * @return a new <tt>RepeatTime</tt> instance
     * 
     * @throws SDPParseException if an error occurs while parsing
     */
    public static RepeatTime parse(final String field) throws SDPParseException {

        if (!field.startsWith("r=")) {
            throw new SDPParseException("The string \"" + field + "\" isn't a repeat time field");
        }

        RepeatTime r = null;

        long repeatInterval;
        long activeDuration;
        long[] offsets;

        String[] values = field.substring(2).split(" ");

        try {
            switch (values.length) {
                case 0:
                case 1:
                case 2:
                    throw new SDPParseException("The string \"" + field + "\" isn't a valid repeat time field");
                default:
                    /* Get the repeat interval */
                    repeatInterval = TypedTime.getTime(values[0]);

                    /* Get the active duraction */
                    activeDuration = TypedTime.getTime(values[1]);

                    /* Get the offsets */
                    offsets = new long[values.length - 2];
                    for (int i = 0; i < offsets.length; i++) {
                        offsets[i] = TypedTime.getTime(values[i + 2]);
                    }

                    /* Create the field */
                    r = new RepeatTime(repeatInterval, activeDuration, offsets);
            }
        }
        catch (SDPException parseException) {
            throw new SDPParseException("The string \"" + field + "\" isn't a valid repeat time field", parseException);
        }

        return r;
    }

    /**
     * Adds an offset to the field.
     * 
     * @param offset the offset value
     * 
     * @throws SDPException if the offset value is negative
     */
    public void addOffset(final long offset) throws SDPException {

        if (offset >= 0) {
            offsets.add(new Long(offset));
        }
        else {
            throw new SDPException("Offsets must be >= 0");
        }
    }

    /**
     * Returns a clone of this field.
     * 
     * @return a clone of this field
     */
    public Object clone() {

        RepeatTime field = new RepeatTime();
        field.isTyped = this.isTyped;
        field.activeDuration = this.activeDuration;
        field.repeatInterval = this.repeatInterval;
        field.offsets = (Vector) this.offsets.clone();

        return field;
    }

    /**
     * Returns the active duration in seconds
     * 
     * @return the active duration
     */
    public long getActiveDuration() {

        return activeDuration;
    }

    /**
     * Returns the list of offsets. These are relative to the start time given
     * in the {@link Time} field with which this <tt>RepeatTime</tt> is
     * associated.
     * 
     * @return an array of repeat time offsets
     */
    public long[] getOffsets() {

        long[] values = new long[offsets.size()];
        for (int i = 0; i < values.length; i++) {
            Long offset = (Long) offsets.get(i);
            values[i] = offset.longValue();
        }

        return values;
    }

    /**
     * Returns the repeat interval.
     * 
     * @return the repeat interval in seconds
     */
    public long getRepeatInterval() {

        return repeatInterval;
    }

    /**
     * Returns the type character for the field.
     * 
     * @return the field type character: <b>r</b>
     */
    public char getType() {

        return Field.REPEAT_TIME_FIELD;
    }

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
     * Sets the active duration.
     * 
     * @param activeDuration the active duration in seconds
     * 
     * @throws SDPException if the active duration is negative
     */
    public void setActiveDuration(final long activeDuration) throws SDPException {

        if (activeDuration < 0) {
            throw new SDPException("The active duration cannot be negative");
        }

        this.activeDuration = activeDuration;

    }

    /**
     * Set the list of offsets. These are relative to the start time given in
     * the {@link Time} field with which this <tt>RepeatTime</tt> is
     * associated.
     * 
     * @param offsets an array of repeat time offsets
     * 
     * @throws SDPException if one or more offset is negative
     */
    public void setOffset(final long[] offsets) throws SDPException {

        Vector temp = new Vector(offsets.length);
        for (int i = 0; i < offsets.length; i++) {
            if (offsets[i] >= 0) {
                temp.add(new Long(offsets[i]));
            }
            else {
                throw new SDPException("Offsets must be >= 0");
            }
        }
        // this.offsets = null;
        // this.offsets = (Vector)temp.clone();
        this.offsets = temp;
    }

    /**
     * Sets the repeat interval.
     * 
     * @param repeatInterval the repeat interval in seconds
     * 
     * @throws SDPException if repeatInterval is negative
     */
    public void setRepeatInterval(final long repeatInterval) throws SDPException {

        if (repeatInterval < 0) {
            throw new SDPException("The repeat interval cannot be negative");
        }

        this.repeatInterval = repeatInterval;
    }

    /**
     * Sets whether the field will be output as a typed time or a number. Typed
     * time is formatted as an number followed by a unit character: the unit
     * indicates an appropriate multiplier for the number. The following unit
     * types are allowed:
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
     * form: <b>r=<i>&lt;value&gt;</i></b>
     * 
     * @return the string representation of the field
     */
    public String toString() {

        StringBuffer result;

        result = new StringBuffer(getType() + "=");

        result.append(repeatInterval + " ");
        if (this.isTyped) {
            result.append(TypedTime.toString(activeDuration));
            for (int i = 0; i < offsets.size(); i++) {
                Long offset = (Long) offsets.get(i);
                result.append(" " + TypedTime.toString(offset.longValue()));
            }
        }
        else {
            result.append(activeDuration);
            for (int i = 0; i < offsets.size(); i++) {
                result.append(" " + offsets.get(i));
            }
        }

        return result.toString();
    }
}