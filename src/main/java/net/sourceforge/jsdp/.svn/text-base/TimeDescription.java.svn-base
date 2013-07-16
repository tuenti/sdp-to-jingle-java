/*
 * jSDP: A Java implementation of SDP protocol Copyright (C) 2007 Claudio Di
 * Vita
 */
package net.sourceforge.jsdp;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * A <tt>TimeDescription</tt> represents the fields present within a SDP time
 * description.
 * 
 * @since 0.1.0
 * 
 * @version 1.1
 * 
 * @author <a href="mailto:cdivita@users.sourceforge.net">Claudio Di Vita</a>
 */
public class TimeDescription implements Description {

    /** The class Stream Unique Identifier, SUID */
    private static final long serialVersionUID = 6860935545790980038L;

    /** The time field */
    protected Time t;

    /** The repeat time fields */
    protected ArrayList repeatTimes;

    /**
     * Creates a new <tt>TimeDescription</tt>.
     * 
     */
    public TimeDescription() {

        super();

        setTime(new Time());
        repeatTimes = new ArrayList();
    }

    /**
     * Creates a new <tt>TimeDescription</tt>.
     * 
     * @param t the time field
     * 
     * @throws IllegalArgumentException if the time field is <tt>null</tt>
     */
    public TimeDescription(final Time t) throws IllegalArgumentException {

        this();
        setTime(t);
    }

    /**
     * Creates a new <tt>TimeDescription</tt>. This constructor is used to
     * implement the {@link #clone()} method, because this class has a default
     * constructor that performs some operations that aren't required while
     * cloning a description.
     * 
     * @param td the <tt>TimeDescription</tt> to clone
     */
    protected TimeDescription(final TimeDescription td) {

        super();

        t = (Time) td.t.clone();
        repeatTimes = (ArrayList) td.repeatTimes.clone();
    }

    /**
     * Add a repeat time field.
     * 
     * @param field the field to add
     * 
     * @throws IllegalArgumentException if the field is <tt>null</tt>
     */
    public void addRepeatTime(final RepeatTime field) throws IllegalArgumentException {

        if (field == null) {
            throw new IllegalArgumentException("A repeat time field cannot be null");
        }

        repeatTimes.add(field);
    }

    /**
     * Remove all repeat time fields contained in the time description.
     */
    public void clearRepeatTimes() {

        repeatTimes.clear();
    }

    /**
     * Returns a clone of this description.
     * 
     * @return a clone of this description
     */
    public Object clone() {

        return new TimeDescription(this);
    }

    /**
     * Returns the repeat time fields.
     * 
     * @return an array that contains the repeat time fields contained in the
     *         time description
     */
    public RepeatTime[] getRepeatTimes() {

        return (RepeatTime[]) repeatTimes.toArray(new RepeatTime[repeatTimes.size()]);
    }

    /**
     * Returns the time field.
     * 
     * @return the time field
     */
    public Time getTime() {

        return t;
    }

    /**
     * Sets the repeat time fields.
     * 
     * @param fields the repeat time fields to set
     * 
     * @throws IllegalArgumentException if one or more repeat time field is
     *         <tt>null</tt>
     */
    public void setRepeatTimes(final RepeatTime[] fields) throws IllegalArgumentException {

        if (fields == null) {
            throw new IllegalArgumentException("Repeat time fields cannot be null");
        }

        int length = fields.length;
        ArrayList backup = (ArrayList) repeatTimes.clone();

        try {

            /* Remove current repeat times */
            repeatTimes.clear();

            /* Add repeat times */
            for (int i = 0; i < length; i++) {
                addRepeatTime(fields[i]);
            }
        }
        catch (IllegalArgumentException exception) {

            /* An error is occured, so we "rollback" */
            repeatTimes = backup;

            /* Rethrow the exception */
            throw exception;
        }
    }

    /**
     * Sets the time field.
     * 
     * @param t the time field
     * 
     * @throws IllegalArgumentException if the time field is <tt>null</tt>
     */
    public void setTime(final Time t) throws IllegalArgumentException {

        if (t == null) {
            throw new IllegalArgumentException("The time field cannot be null");
        }

        this.t = t;
    }

    /**
     * Returns a string representation of this description
     * 
     * @return a string representation of the description
     */
    public String toString() {

        StringBuffer result = new StringBuffer(t.toString());
        result.append(Field.END_OF_FIELD);

        for (Iterator i = repeatTimes.iterator(); i.hasNext();) {

            result.append( i.next());
            result.append(Field.END_OF_FIELD);
        }

        return result.toString();
    }
}