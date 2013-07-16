/*
 * jSDP: A Java implementation of SDP protocol Copyright (C) 2007 Claudio Di
 * Vita
 */
package net.sourceforge.jsdp;

import java.util.Vector;
import java.util.regex.Pattern;

/**
 * A <tt>Media</tt> represents a <b>m=<i>&lt;field value&gt;</i></b> field
 * contained in a SDP message. The media field identifies information about the
 * formats of the media associated with the session. A media field includes:
 * <ul>
 * <li>A media type (audio, video, etc.)</li>
 * <li>A port number (or set of ports)</li>
 * <li>A protocol to be used (like RTP/AVP)</li>
 * </ul>
 * 
 * @since 0.1.0
 * 
 * @version 1.0
 * 
 * @author <a href="mailto:cdivita@users.sourceforge.net">Claudio Di Vita</a>
 */
public class Media implements Field {

    /** The class Stream Unique Identifier, SUID */
    private static final long serialVersionUID = 8009552820375695269L;

    /** The pattern used to validate media type and formats */
    private static final Pattern mediaPattern = Pattern.compile("\\w+");

    /** The pattern used to validate the transport protocol */
    private static final Pattern protocolPattern = Pattern.compile("[\\w/]+");

    /** The media type */
    protected String media;

    /** The transport port to which the media stream will be sent */
    protected int port;

    /** The number of transport port where the media stream will be sent */
    protected int portsCount;

    /** The transport protocol */
    protected String protocol;

    /** The media formats */
    protected Vector formats;

    /**
     * Creates a new <tt>Media</tt>.
     */
    protected Media() {

        super();
    }

    /**
     * Creates a new <tt>Media</tt>.
     * 
     * @param media the media type
     * 
     * @param port the transport port to which the media stream will be sent
     * 
     * @param portsCount the number of transport ports where the media stream
     *        will be sent
     * 
     * @param protocol the transport protocol
     * 
     * @param format the media default format
     * 
     * @throws SDPException if the parameters are not valid
     */
    public Media(final String media, final int port, final int portsCount, final String protocol, final String format) throws SDPException {

        super();

        setMediaType(media);
        setPort(port);
        setPortsCount(portsCount);
        setProtocol(protocol);

        formats = new Vector(3);
        addMediaFormat(format);
    }

    /**
     * Creates a new <tt>Media</tt>.
     * 
     * @param media the media type
     * 
     * @param port the transport port to which the media stream will be sent
     * 
     * @param protocol the transport protocol
     * 
     * @param format the media default format
     * 
     * @throws SDPException if the parameters are not valid
     */
    public Media(final String media, final int port, final String protocol, final String format) throws SDPException {

        this(media, port, 1, protocol, format);
    }

    /**
     * Parse an input string and constructs the equivalent media field.
     * 
     * @param field the string to parse
     * 
     * @return a new <tt>Origin</tt> instance
     * 
     * @throws SDPParseException if an error occurs while parsing
     */
    public static Media parse(final String field) throws SDPParseException {

        if (!field.startsWith("m=")) {
            throw new SDPParseException("The string \"" + field + "\" isn't a media field");
        }

        Media m = null;

        /* Split the values */
        String[] values = field.substring(2).split(" ");

        try {

            /* Each media field must have at least 4 parameters */
            if (values.length < 4) {
                throw new SDPException("Some media field parameters are missing");
            }

            String portInfo = values[1];
            String[] portInfoValues = portInfo.split("/");

            if (portInfoValues.length == 1) {
                m = new Media(values[0], Integer.parseInt(values[1]), values[2], values[3]);
            }
            else if (portInfoValues.length == 2) {
                m = new Media(values[0], Integer.parseInt(portInfoValues[0]), Integer.parseInt(portInfoValues[1]), values[2], values[3]);
            }

            /* Add the media formats, if present */
            try {
                for (int i = 4; i < values.length; i++) {
                    m.addMediaFormat(values[i]);
                }
            }
            catch (SDPException invalidMediaFormat) {
                /*
                 * The media format is not valid, but because it's optional the
                 * exception is not throwed
                 */
            }
        }
        catch (SDPException parseException) {
            throw new SDPParseException("The string \"" + field + "\" isn't a valid origin field", parseException);
        }

        return m;
    }

    /**
     * Adds a media format.
     * 
     * @param format the media format to add
     * 
     * @throws SDPException if the media format is not valid
     */
    public void addMediaFormat(final String format) throws SDPException {

        if (!mediaPattern.matcher(format).matches()) {
            throw new SDPException("Invalid media format: " + format);
        }

        formats.add(format);
    }

    /**
     * Returns a clone of this field.
     * 
     * @return a clone of this field
     */
    public Object clone() {

        Media field = new Media();

        field.media = new String(media);
        field.port = port;
        field.portsCount = portsCount;
        field.protocol = new String(protocol);
        field.formats = (Vector) formats.clone();

        return field;
    }

    /**
     * Returns the media formats
     * 
     * @return the media formats
     */
    public String[] getMediaFormats() {

        return (String[]) formats.toArray(new String[formats.size()]);
    }

    /**
     * Returns the type of the media.
     * 
     * @return the media tpye
     */
    public String getMediaType() {

        return media;
    }

    /**
     * Returns the transport port to which the media stream will be sent.
     * 
     * @return the transport port
     */
    public int getPort() {

        return port;
    }

    /**
     * Returns the number of ports associated with this media.
     * 
     * @return the ports count
     */
    public int getPortsCount() {

        return portsCount;
    }

    /**
     * Returns the protocol over which this media should be transmitted.
     * 
     * @return the transport protocol
     */
    public String getProtocol() {

        return protocol;
    }

    /**
     * Returns the type character for the field.
     * 
     * @return the field type character: <tt>m</tt>
     */
    public char getType() {

        return Field.MEDIA_FIELD;
    }

    /**
     * Sets the media formats.
     * 
     * @param formats the formats to set
     * 
     * @throws SDPException if one or more media formats are not valid
     * 
     * @throws IllegalArgumentException if the media formats are <tt>null</tt>
     */
    public void setMediaFormats(final String[] formats) throws SDPException {

        if (formats == null) {
            throw new IllegalArgumentException("The media formats cannot be null");
        }

        /* Make a copoy of the current formats */
        Vector elements = (Vector) this.formats.clone();

        /* Clear the current formats */
        this.formats.clear();

        try {
            for (int i = 0; i < formats.length; i++) {
                addMediaFormat(formats[i]);
            }
        }
        catch (SDPException invalidFormat) {

            /* Restore the previous formats */
            this.formats = elements;

            /* Throw the exception */
            throw new SDPException("Invalid media formats", invalidFormat);
        }
    }

    /**
     * Sets the media type.
     * 
     * @param media the media type to set
     * 
     * @throws SDPException if the media type is not valid
     */
    public void setMediaType(final String media) throws SDPException {

        if (!mediaPattern.matcher(media).matches()) {
            throw new SDPException("Invalid media type: " + media);
        }

        this.media = media;
    }

    /**
     * Sets the media transport port.
     * 
     * @param port the transport port
     * 
     * @throws SDPException if the transport port is less than <tt>1</tt>
     */
    public void setPort(final int port) throws SDPException {

        if (port < 0) {
            throw new SDPException("The transport port must be greater than 0");
        }

        this.port = port;
    }

    /**
     * Sets the number of transport ports where the media stream will be sent.
     * 
     * @param n the number of ports
     * 
     * @throws SDPException if the number of ports is less than <tt>1</tt>
     */
    public void setPortsCount(final int n) throws SDPException {

        if (n < 1) {
            throw new SDPException("The ports count must be greater than 0");
        }

        portsCount = n;
    }

    /**
     * Sets the protocol over which this media should be transmitted.
     * 
     * @param protocol the transport protocol
     * 
     * @throws SDPException if the transport protocol is not valid
     */
    public void setProtocol(final String protocol) throws SDPException {

        if (!protocolPattern.matcher(protocol).matches()) {
            throw new SDPException("Invalid protocol: " + protocol);
        }

        this.protocol = protocol;
    }

    /**
     * Returns a string representation of the field.The representation has the
     * form: <b>m=<i>&lt;media&gt;</i> <i>&lt;port&gt;</i>/<i>&lt;portsCount&gt;</i>
     * <i>&lt;protocol&gt;</i> <i>&lt;formats&gt;</i></b>
     * 
     * @return the string representation of the field
     */
    public String toString() {

        StringBuffer result = new StringBuffer(getType() + "=");

        result.append(media);
        result.append(" " + port);

        if (portsCount > 1) {
            result.append("/" + portsCount);
        }

        result.append(" " + protocol);
        for (int i = 0; i < formats.size(); i++) {
            result.append(" " + formats.get(i));
        }

        return result.toString();
    }
}