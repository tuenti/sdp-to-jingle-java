/*
 * jSDP: A Java implementation of SDP protocol Copyright (C) 2007 Claudio Di
 * Vita
 */
package net.sourceforge.jsdp.util;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.jsdp.SDPException;

/**
 * Handles typed time representation used in SDP.
 * 
 * @since 0.1.0
 * 
 * @version 1.0
 * 
 * @author <a href="mailto:cdivita@users.sourceforge.net">Claudio Di Vita</a>
 */
public class TypedTime implements Serializable {

    /** The class Stream Unique Identifier, SUID */
    private static final long serialVersionUID = 7152238446184328783L;

    /** The days multiplier */
    public static final String DAYS = "d";

    /** The hours multiplier */
    public static final String HOURS = "h";

    /** The minutes multiplier */
    public static final String MINUTES = "m";

    /** The seconds multiplier */
    public static final String SECONDS = "s";

    /** The pattern used to parse a typed time */
    private static final Pattern typedPattern = Pattern.compile("((-){0,1}[1-9](\\d)*)|0");

    /** The map tha contains the multiplier unit value */
    private static final Hashtable units;

    static {
        units = new Hashtable(4);
        units.put(DAYS, new Long(86400));
        units.put(HOURS, new Long(3600));
        units.put(MINUTES, new Long(60));
        units.put(SECONDS, new Long(1));
    }

    /**
     * Returns the <tt>long</tt> value of a typed time.
     * 
     * @param input the typed time
     * 
     * @return a <tt>long</tt> value
     * 
     * @throws SDPException if an unknown multiplier is contained in the input
     *         string
     */
    public static long getTime(final String input) throws SDPException {

        long parsedTime = 0;
        Matcher matcher = typedPattern.matcher(input);

        if (matcher.lookingAt()) {
            String t = matcher.group();
            String modifier = input.substring(t.length());

            if (modifier.length() > 0) {
                Long multiplier = (Long) units.get(modifier);
                if (multiplier == null) {
                    throw new SDPException("Unknown multiplier");
                }

                parsedTime = multiplier.longValue() * Long.parseLong(t);
            }
            else {
                parsedTime = Long.parseLong(t);
            }
        }

        return parsedTime;
    }

    /**
     * Returns a typed representation of a time value.
     * 
     * @param time the value to represent
     * 
     * @return a <tt>String</tt>
     */
    public static String toString(final long time) {

        StringBuffer result;

        result = new StringBuffer();
        if (time == 0) {
            result.append("0");
        }
        else if ((time % 86400) == 0) {
            result.append(time / 86400);
            result.append(DAYS);
        }
        else if ((time % 3600) == 0) {
            result.append(time / 3600);
            result.append(HOURS);
        }
        else if ((time % 60) == 0) {
            result.append(time / 60);
            result.append(MINUTES);
        }
        else {
            result.append(time);
        }

        return result.toString();
    }
}