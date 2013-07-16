/*
 * jSDP: A Java implementation of SDP protocol Copyright (C) 2007 Claudio Di
 * Vita
 */
package net.sourceforge.jsdp;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A Key represents an <<b>k=<i>&lt;field value&gt;</i></b> field contained
 * in a SDP message.
 * <p>
 * A SDP message may be used to convey encryption keys, if transported over a
 * secure and trusted channel. Key fields has the form:
 * <b>&lt;method&gt;:&lt;key&gt;</b>, but the key value is optional. The
 * following methods are defined:
 * </p>
 * <ul>
 * <li><b>k=clear:<i>&lt;encryption key&gt;</i></b>, the encryption key is
 * included untransformed in the key field</li>
 * <li><b>k=base64:<i>&lt;encoded encryption key&gt;</i></b>, the encryption
 * key is included in the key field but has been base64 encoded because it
 * includes characters that are prohibited in SDP</li>
 * <li><b>k=uri:<i>&lt;URI to obtain key&gt;</i></b>, a Universal Resource
 * Identifier as used by WWW clients is included in the key field. The URI
 * refers to the data containing the key, and may require additional
 * authentication before the key can be returned. When a request is made to the
 * given URI, the MIME content-type of the reply specifies the encoding for the
 * key in the reply</li>
 * <li><b>k=prompt</b>, no key is included in this SDP description, but the
 * session or media stream referred to by the key field is encrypted. The user
 * should be prompted for the key when attempting to join the session, and this
 * user-supplied key should then be used to decrypt the media streams</li>
 * </ul>
 * 
 * @since 0.1.2
 * 
 * @version 1.0
 * 
 * @author <a href="mailto:cdivita@users.sourceforge.net">Claudio Di Vita</a>
 */
public class Key implements Field {

    /** The class Stream Unique Identifier, SUID */
    private static final long serialVersionUID = 1767674946938133759L;

    /** The base64 encryption method modifier */
    public static final String BASE64 = "base64";

    /** The clear encryption method modifier */
    public static final String CLEAR = "clear";

    /** The prompt encryption method modifier */
    public static final String PROMPT = "prompt";

    /** The uri encryption method modifier */
    public static final String URI = "uri";

    /** The pattern used to parse the field */
    private static final Pattern fieldPattern = Pattern.compile("([^:]+)(:((.+)))?");

    /** The pattern used to validate the base64 encryption method key values */
    private static final Pattern methodBase64Pattern = Pattern.compile("([\\w\\+/]{4})*([\\w\\+/]{2}==|[\\w\\+/]{3}=)*");

    /** The pattern used to validate the clear encryption method key values */
    private static final Pattern methodClearPattern = Pattern.compile("[\\w'-\\./:?#\\$&\\*;=@\\[\\]\\^_`\\{\\}\\|\\+\\~ \\t]+");

    /** The encryption method */
    protected String method;

    /** The encription key */
    protected String key;

    /**
     * Creates a new <tt>Key</tt>.
     */
    protected Key() {

        super();
    }

    /**
     * Creates a new <tt>Key</tt>.
     * 
     * @param method the encryption method
     * 
     * @param key the encryption key
     * 
     * @throws SDPException if the encryption method is not allowed, or the
     *         encryption key is not valid for the specified method
     */
    public Key(final String method, final String key) throws SDPException {

        super();

        setMethod(method);
        setKey(key);
    }

    /**
     * Parse an input string and constructs the equivalent key field.
     * 
     * @param field the string to parse
     * 
     * @return a new <tt>Key</tt> instance
     * 
     * @throws SDPParseException if an error occurs while parsing
     */
    public static Key parse(final String field) throws SDPParseException {

        if (!field.startsWith("i=")) {
            throw new SDPParseException("The string \"" + field + "\" isn't an key field");
        }

        Key k = null;
        Matcher matcher = fieldPattern.matcher(field.substring(2));

        /* Test */
        if (matcher.matches()) {
            try {
                k = new Key(matcher.group(1), matcher.group(3));
            }
            catch (SDPException parseException) {
                throw new SDPParseException("The string \"" + field + "\" isn't a valid key field", parseException);
            }
        }
        else {
            throw new SDPParseException("The string \"" + field + "\" isn't a valid key field");
        }

        return k;
    }

    /**
     * Returns a clone of this field.
     * 
     * @return a clone of this field
     */
    public Object clone() {

        Key field = new Key();

        field.method = new String(method);
        field.key = new String(key);

        return field;
    }

    /**
     * Returns the encryption key.
     * 
     * @return the encryption key.
     */
    public String getKey() {

        return key;
    }

    /**
     * Returns the encryption method.
     * 
     * @return the encryption method
     */
    public String getMethod() {

        return method;
    }

    /**
     * Returns the type character for the field.
     * 
     * @return the field type character: <b>k</b>
     */
    public char getType() {

        return Field.KEY_FIELD;
    }

    /**
     * Determines if the method has an associated key.
     * 
     * @return <tt>true</tt> if the method has an associated key,
     *         <tt>false</tt> otherwise
     */
    public boolean hasKey() {

        return key != null;
    }

    /**
     * Sets the encryption key.
     * 
     * @param key the encryption key to set
     * 
     * @throws SDPException if the key is not valid
     */
    public void setKey(String key) throws SDPException {

        boolean result = false;

        if (method.equals(Key.BASE64)) {
            result = methodBase64Pattern.matcher(key).matches();
        }
        else if (method.equals(Key.CLEAR)) {
            result = methodClearPattern.matcher(key).matches();
        }
        else if (method.equals(Key.PROMPT)) {
            result = (key == null) || (key.length() == 0);

            /*
             * By default, the key value for the prompt encryption method is
             * null
             */
            key = null;
        }
        else if (method.equals(Key.URI)) {
            try {
                /* Verifica della validità dell'URL */
                new URL(key);
            }
            catch (MalformedURLException invalidUrl) {
                /* The specified URL is not valid */
                result = false;
            }
        }

        /*
         * The specified key doesn't match the parameters required by the
         * encryption method
         */
        if (!result) {
            throw new SDPException("Invalid key for method " + method);
        }

        this.key = key;
    }

    /**
     * Sets the encryption method.
     * 
     * @param method the encryption method to set
     * 
     * @throws SDPException if the method is not allowed or <tt>null</tt>
     */
    public void setMethod(final String method) throws SDPException {

        if (method == null) {
            throw new SDPException("The encryption method cannot be null");
        }
        else if (!method.equals(Key.BASE64) && !method.equals(Key.CLEAR) && !method.equals(Key.PROMPT) && !method.equals(Key.URI)) {
            throw new SDPException("The method " + method + " is not supported by SDP");
        }

        this.method = method;
    }

    /**
     * Returns a string representation of the field. The representation has one
     * the following forms:
     * <ul>
     * <li><b>k=<i>&lt;method&gt;</i></b></li>
     * <li><b>k=<i>&lt;method&gt;</i>:<i>&lt;key&gt;</i></b></li>
     * </ul>
     * 
     * @return the string representation of the field
     */
    public String toString() {

        StringBuffer result = new StringBuffer(getType() + "=");
        result.append(method);

        if (key != null) {
            result.append(":" + key);
        }

        return result.toString();
    }
}