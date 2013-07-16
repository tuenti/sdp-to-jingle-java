/*
 * jSDP: A Java implementation of SDP protocol Copyright (C) 2007 Claudio Di
 * Vita
 */
package net.sourceforge.jsdp;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A <tt>Bandwith</tt> represents an <b>b=<i>&lt;field value&gt;</i></b>
 * field contained in a SDP message.
 * <p>
 * A bandwith field specifies the proposed bandwidth to be used by the session
 * or media, and is optional. Multiple bandwidth specifiers of different types
 * may be associated with the same {@link SessionDescription}. Each has the
 * form <b>b<i>=&lt;modifier&gt;</i>:<i>&lt;value&gt;</i></b>. There are
 * four bandwith modifers (or types):
 * </p>
 * <ul>
 * <li>AS: Application Specific maximum. The bandwidth is interpreted to be
 * application-specific, and will be treated as application's concept of maximum
 * bandwidth</li>
 * <br/>
 * <li>CT: Conference Total. An implicit maximum bandwidth is associated with
 * each TTL on the Mbone or within a particular multicast administrative scope
 * region (the Mbone bandwidth vs. TTL limits are given in the MBone FAQ). If
 * the bandwidth of a session or media in a session is different from the
 * bandwidth implicit from the scope, a <b>b=CT:<i>&lt;bandwith&gt;</i></b>
 * line should be supplied for the session giving the proposed upper limit to
 * the bandwidth used.<br/>The primary purpose of this is to give an
 * approximate idea as to whether two or more conferences can co-exist
 * simultaneously</li>
 * <br/>
 * <li>RS: RCTP bandwith for data senders</li>
 * <br/>
 * <li>RR: RCTP bandwith for other participants in the RTP session</li>
 * </ul>
 * 
 * @since 0.1.0
 * 
 * @version 1.0
 * 
 * @author <a href="mailto:cdivita@users.sourceforge.net">Claudio Di Vita</a>
 */
public class Bandwith implements Field {

    /** The class Stream Unique Identifier, SUID */
    private static final long serialVersionUID = 8829981734747249624L;

    /** The AS modifier */
    public static final String AS = "AS";

    /** The CT modifier */
    public static final String CT = "CT";

    /** The RS modifier */
    public static final String RS = "RS";

    /** The RR modifier */
    public static final String RR = "RR";

    /** The pattern used to parse the field */
    private static final Pattern fieldPattern = Pattern.compile("(.+):((\\d+))");

    /** The bandwith modifier */
    protected String modifier;

    /** The bandwith value, in kbits/second */
    protected int value;

    /**
     * Creates a new <tt>Bandwith</tt>.
     */
    protected Bandwith() {

        super();
    }

    /**
     * Creates a new <tt>Bandwith</tt>.
     * 
     * @param modifier the bandwith modifier
     * @param value the bandwith value in kbits/second
     * 
     * @throws SDPException if the modifier is unknown, or the value is negative
     */
    public Bandwith(final String modifier, final int value) throws SDPException {

        this();

        setModifier(modifier);
        setValue(value);
    }

    /**
     * Parse an input string and constructs the equivalent bandwith field.
     * 
     * @param field the string to parse
     * 
     * @return a new <tt>Bandwith</tt> instance
     * 
     * @throws SDPParseException if an error occurs while parsing
     */
    public static Bandwith parse(final String field) throws SDPParseException {

        if (!field.startsWith("b=")) {
            throw new SDPParseException("The string \"" + field + "\" isn't a bandwith field");
        }

        Bandwith b = null;

        /* Create the matcher */
        Matcher matcher = fieldPattern.matcher(field.substring(2));

        /* Test */
        if (matcher.matches()) {
            try {
                b = new Bandwith(matcher.group(1), Integer.parseInt(matcher.group(2)));
            }
            catch (SDPException parseException) {
                throw new SDPParseException("The string \"" + field + "\" isn't a valid bandwith field", parseException);
            }
        }
        else {
            throw new SDPParseException("The string \"" + field + "\" isn't a valid bandwith field");
        }

        return b;
    }

    /**
     * Returns a clone of this field.
     * 
     * @return a clone of this field
     */
    public Object clone() {

        Bandwith field = new Bandwith();
        field.modifier = new String(modifier);
        field.value = this.value;

        return field;
    }

    /**
     * Returns the bandwith modifier.
     * 
     * @return The bandwith modifier.
     *         <p>
     *         The allowed modifiers are defined by <a
     *         href="http://www.ietf.org/rfc/rfc2327.txt">RFC 2327</a> and <a
     *         href="http://www.ietf.org/rfc/rfc3556.txt">RFC 3556</a>:
     *         </p>
     *         <ul>
     *         <li>AS: Application Specific</li>
     *         <li>CT: Conference Total</li>
     *         <li>RS: RCTP bandwith for data senders</li>
     *         <li>RR: RCTP bandwith for data receivers</li>
     *         </ul>
     */
    public String getModifier() {

        return modifier;
    }

    /**
     * Returns the type character for the field.
     * 
     * @return the field type character: <b>b</b>
     */
    public char getType() {

        return Field.BANDWITH_FIELD;
    }

    /**
     * Returns the bandwith value.
     * 
     * @return the bandwith value in kbits/second
     */
    public int getValue() {

        return value;
    }

    /**
     * Sets the bandwith modifier.
     * 
     * @param modifier the modifier to set
     * 
     * @throws SDPException if the modifier is unknown
     */
    public void setModifier(final String modifier) throws SDPException {

        if (modifier.equals(AS) || modifier.equals(CT) || modifier.equals(RR) || modifier.equals(RS)) {
            this.modifier = modifier;
        }
        else {
            throw new SDPException("Unknown modifier: " + modifier);
        }
    }

    /**
     * Sets the bandwith value.
     * 
     * @param value the value to set
     * 
     * @throws SDPException if value is negative
     */
    public void setValue(final int value) throws SDPException {

        if (value < 0) {
            throw new SDPException("Invalid bandwith value: " + value);
        }

        this.value = value;
    }

    /**
     * Returns a string representation of the field. The representation has the
     * form: <b>b=<i>&lt;modifier&gt;</i>:<i>&lt;value&gt;</i></b>
     * 
     * @return the string representation of the field
     */
    public String toString() {

        return getType() + "=" + modifier + ":" + value;
    }
}