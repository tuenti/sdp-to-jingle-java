/*
 * jSDP: A Java implementation of SDP protocol Copyright (C) 2007 Claudio Di
 * Vita
 */
package net.sourceforge.jsdp;

import java.io.Serializable;

/**
 * A field is a line of text in the SDP message. Each field contain information
 * specific some aspect of the session. There are several field types: each
 * field type is identified by a unique character and has a fixed format for its
 * content.
 * 
 * @since 0.1.0
 * 
 * @version 1.0
 * 
 * @author <a href="mailto:cdivita@users.sourceforge.net">Claudio Di Vita</a>
 */
public interface Field extends Cloneable, Serializable {

    /** The field terminator characters sequence: <b>\r\n</b> */
    public static final String END_OF_FIELD = "\r\n";

    /** The attribute field char type character: <b>a</b> */
    public static final char ATTRIBUTE_FIELD = 'a';

    /** The bandwith field char type character: <b>b</b> */
    public static final char BANDWITH_FIELD = 'b';

    /** The connection field char type character: <b>c</b> */
    public static final char CONNECTION_FIELD = 'c';

    /** The email field char type character: <b>e</b> */
    public static final char EMAIL_FIELD = 'e';

    /** The information field char type character: <b>i</b> */
    public static final char INFORMATION_FIELD = 'i';

    /** The encryption key field char type character: <b>k</b> */
    public static final char KEY_FIELD = 'k';

    /** The media field char type character: <b>m</b> */
    public static final char MEDIA_FIELD = 'm';

    /** The origin field char type character: <b>o</b> */
    public static final char ORIGIN_FIELD = 'o';

    /** The phone field char type character: <b>p</b> */
    public static final char PHONE_FIELD = 'p';

    /** The repeat time field char type character: <b>r</b> */
    public static final char REPEAT_TIME_FIELD = 'r';

    /** The session name field char type character: <b>s</b> */
    public static final char SESSION_NAME_FIELD = 's';

    /** The time field char type character: <b>t</b> */
    public static final char TIME_FIELD = 't';

    /** The timezone field char type character: <b>z</b> */
    public static final char TIMEZONE_FIELD = 'z';

    /** The uri field char type character: <b>u</b> */
    public static final char URI_FIELD = 'u';

    /** The information field char type character: <b>v</b> */
    public static final char VERSION_FIELD = 'v';

    /**
     * Returns a clone of this field.
     * 
     * @return a clone of this field
     */
    public Object clone();

    /**
     * Returns the type character for the field.
     * 
     * @return the field type character
     */
    public char getType();

    /**
     * Returns a string representation of the field. The representation has the
     * form: <b><i>&lt;type&gt;</i>=<i>&lt;value&gt;</i></b>.
     * 
     * @return the string representation of the field
     */
    public String toString();
}