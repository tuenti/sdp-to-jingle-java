/*
 * jSDP: A Java implementation of SDP protocol Copyright (C) 2007 Claudio Di
 * Vita
 */
package net.sourceforge.jsdp;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * An <tt>Uri</tt> represents a <b>u=<i>&lt;field value&gt;</i></b> field
 * contained in a SDP message. The uri field should be a pointer to additional
 * information about the conference.
 * 
 * @since 0.1.0
 * 
 * @version 1.0
 * 
 * @author <a href="mailto:cdivita@users.sourceforge.net">Claudio Di Vita</a>
 */
public class Uri implements Field {

    /** The class Stream Unique Identifier, SUID */
    private static final long serialVersionUID = 5886238353833030564L;

    /** The uri value */
    protected URL url;

    /**
     * Creates a new <tt>Uri</tt>.
     */
    protected Uri() {

        super();
    }

    /**
     * Creates a new <tt>Uri</tt>.
     * 
     * @param url the uri value
     * 
     * @throws SDPException if the value specified is a not valid uri
     */
    public Uri(final String url) throws SDPException {

        super();
        setURL(url);
    }

    /**
     * Creates a new <tt>Uri</tt>.
     * 
     * @param url the resource url
     */
    public Uri(final URL url) {

        super();
        this.url = url;
    }

    /**
     * Parse an input string and constructs the equivalent uri field.
     * 
     * @param field the string to parse
     * 
     * @return a new <tt>Uri</tt> instance
     * 
     * @throws SDPParseException if an error occurs while parsing
     */
    public static Uri parse(final String field) throws SDPParseException {

        if (!field.startsWith("u=")) {
            throw new SDPParseException("The string \"" + field + "\" isn't an uri field");
        }

        /* The parsed field */
        Uri u = null;

        try {
            /* Create th field */
            u = new Uri(field.substring(2));
        }
        catch (SDPException parseException) {
            throw new SDPParseException("The string \"" + field + "\" isn't a valid uri field", parseException);
        }

        return u;
    }

    /**
     * Returns a clone of this field.
     * 
     * @return a clone of this field
     */
    public Object clone() {

        Uri field = new Uri();

        try {
            field.url = new URL(this.url.toExternalForm());
        }
        /* Never thrown */
        catch (MalformedURLException malformedURL) {
            malformedURL.printStackTrace();
        }

        return field;
    }

    /**
     * Returns the type character for the field.
     * 
     * @return the field type character: <tt>u</tt>
     */
    public char getType() {

        return Field.URI_FIELD;
    }

    /**
     * Returns the resource url.
     * 
     * @return an url
     */
    public String getURL() {

        return url.toExternalForm();
    }

    /**
     * Sets the uri value.
     * 
     * @param url the resource url
     * 
     * @throws IllegalArgumentException if the url is <tt>null</tt>
     * 
     * @throws SDPException if the value specified is a not valid uri
     */
    public void setURL(final String url) throws IllegalArgumentException, SDPException {

        if (url == null) {
            throw new IllegalArgumentException("The resource url cannot be null");
        }

        try {
            this.url = new URL(url);
        }
        catch (MalformedURLException malformedURL) {
            throw new SDPException("Invalid URL: " + url);
        }
    }

    /**
     * Sets the uri value.
     * 
     * @param url the resource url
     * 
     * @throws IllegalArgumentException if the url is <tt>null</tt>
     */
    public void setURL(final URL url) throws IllegalArgumentException {

        if (url == null) {
            throw new IllegalArgumentException("The resource url cannot be null");
        }

        this.url = url;
    }

    /**
     * Returns a string representation of the field. The representation has the
     * form: <b>u=<i>&lt;uri&gt;</i></b>.
     * 
     * @return the string representation of the field
     */
    public String toString() {

        return getType() + "=" + url.toExternalForm();
    }
}