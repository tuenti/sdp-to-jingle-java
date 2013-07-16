/*
 * jSDP: A Java implementation of SDP protocol Copyright (C) 2007 Claudio Di
 * Vita
 */
package net.sourceforge.jsdp;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.Date;
import java.util.Vector;

/**
 * The <tt>SDPFactory</tt> enables to encode and decode SDP messages.
 * 
 * @author <a hfref="mailto:cdivita@users.sourceforge.net">Claudio Di Vita</a>
 * 
 * @since 1.0
 */
public class SDPFactory {

    /** The default charset used for session description encoding: <tt>UTF-8</tt> */
    public static final String DEFAULT_CHARSET = "UTF-8";

    /**
     * Returns a new <tt>Attribute</tt>
     * 
     * @param name the attribute name
     * 
     * @return a new <tt>Attribute</tt> instance
     * 
     * @throws SDPException if the name specified is not valid
     */
    public static Attribute createAttribute(final String name) throws SDPException {

        return new Attribute(name);
    }

    /**
     * Returns a new <tt>Attribute</tt>
     * 
     * @param name the attribute name
     * 
     * @param value the attribute value
     * 
     * @return a new <tt>Attribute</tt> instance
     * 
     * @throws SDPException if the name or the value specified are not valid
     */
    public static Attribute createAttribute(final String name, final String value) throws SDPException {

        return new Attribute(name, value);
    }

    /**
     * Returns a new <tt>Bandwith</tt>.
     * 
     * @param modifier the bandwith modifier
     * 
     * @param value the bandwith value in kbits/second
     * 
     * @return a new <tt>Bandwith</tt> instance
     * 
     * @throws SDPException if the modifier is unknown, or the value is negative
     */
    public static Bandwith createBandwith(final String modifier, final int value) throws SDPException {

        return new Bandwith(modifier, value);
    }

    /**
     * Returns a new <tt>Connection</tt>.
     * 
     * @param resource the network address on which media can be received. The
     *        address type associated with this connection depends by address
     *        value
     * 
     * @return a new <tt>Connection</tt> instance
     * 
     * @throws SDPException if the network address is not valid
     */
    public static Connection createConnection(final String resource) throws SDPException {

        return new Connection(resource);
    }

    /**
     * Returns a new <tt>Email</tt>.
     * 
     * @param info the contact information
     * 
     * @return a new <tt>Email</tt> instance
     * 
     * @throws SDPException if the contact information are not valid
     */
    public static Email createEmail(final String info) throws SDPException {

        return new Email(info);
    }

    /**
     * Returns a new <tt>Information</tt>.
     * 
     * @param value the information about the session
     * 
     * @return a new <tt>Information</tt> instance
     * 
     * @throws SDPException if the characters of the information are not allowed
     */
    public static Information createInformation(final String value) throws SDPException {

        return new Information(value);
    }

    /**
     * Returns a new <tt>Key</tt>.
     * 
     * @param method the encryption method
     * 
     * @param key the encryption key
     * 
     * @return a new <tt>Key</tt> instance
     * 
     * @throws SDPException if the encryption method is not allowed, or the
     *         encryption key is not valid for the specified method
     */
    public static Key createKey(final String method, final String key) throws SDPException {

        return new Key(method, key);
    }

    /**
     * Returns a new <tt>Media</tt>.
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
     * @return a new <tt>Media</tt> instance
     * 
     * @throws SDPException if the parameters are not valid
     */
    public static Media createMedia(final String media, final int port, final int portsCount, final String protocol, final String format) throws SDPException {

        return new Media(media, port, portsCount, protocol, format);
    }

    /**
     * Returns a new <tt>Media</tt>.
     * 
     * @param media the media type
     * 
     * @param port the transport port to which the media stream will be sent
     * 
     * @param protocol the transport protocol
     * 
     * @param format the media default format
     * 
     * @return a new <tt>Media</tt> instance
     * 
     * @throws SDPException if the parameters are not valid
     */
    public static Media createMedia(final String media, final int port, final String protocol, final String format) throws SDPException {

        return new Media(media, port, protocol, format);
    }

    /**
     * Returnss a new <tt>Origin</tt>. The values are sets as follows:
     * <p>
     * <code>
     * o=<i>&lt;user&gt; &lt;current time as NTP&gt; &lt;current time as NTP&gt; &lt;localhost name&gt;</i><br/>
     * </code>
     * </p>
     * The <i>&lt;user&gt;</i> value is the name of the OS user that invokes
     * this method.
     * 
     * @return a new <tt>Origin</tt> instance
     * 
     * @throws SDPException if no IP address for the local host could be found,
     *         or the current user name contains characters that are not allowed
     */
    public static Origin createOrigin() throws SDPException {

        return new Origin();
    }

    /**
     * Returns a new <tt>Origin</tt>. The session id was generated
     * automatically using NTP and the user value is set with the name of the OS
     * user that invokes this method.
     * 
     * @param sessionVersion the session version
     * 
     * @param address the session originator host address
     * 
     * @return a new <tt>Origin</tt> instance
     * 
     * @throws SDPException if one or more parameter provided are not valid
     */
    public static Origin createOrigin(final long sessionVersion, final String address) throws SDPException {

        return new Origin(sessionVersion, address);
    }

    /**
     * Returns a new <tt>Origin</tt>. Both session identifier and session
     * version were generated automatically using NTP and the user value is set
     * with the name of the OS user that invokes this method.
     * 
     * @param address the session originator host address
     * 
     * @return a new <tt>Origin</tt> instance
     * 
     * @throws SDPException if the address is not valid
     */
    public static Origin createOrigin(final String address) throws SDPException {

        return new Origin(address);
    }

    /**
     * Returns a new <tt>Origin</tt>.
     * 
     * @param user the name of the session originator
     * 
     * @param sessionID the session identifier
     * 
     * @param sessionVersion the session version
     * 
     * @param address the session originator host address
     * 
     * @return a new <tt>Origin</tt> instance
     * 
     * @throws SDPException if one or more parameter provided are not valid
     */
    public static Origin createOrigin(final String user, final long sessionID, final long sessionVersion, final String address) throws SDPException {

        return new Origin(user, sessionID, sessionVersion, address);
    }

    /**
     * Returns a new <tt>Origin</tt>.
     * 
     * @param user the name of the session originator
     * 
     * @param sessionVersion the session version
     * 
     * @param address the session originator host address
     * 
     * @return a new <tt>Origin</tt> instance
     * 
     * @throws SDPException if one or more parameter provided are not valid
     */
    public static Origin createOrigin(final String user, final long sessionVersion, final String address) throws SDPException {

        return new Origin(user, sessionVersion, address);
    }

    /**
     * Returns a new <tt>Phone</tt>.
     * 
     * @param number the phone number
     * 
     * @return a new <tt>Phone</tt> instance
     * 
     * @throws SDPException if the phone number is not valid
     */
    public static Phone createPhone(final String number) throws SDPException {

        return new Phone(number);
    }

    /**
     * Returns a new <tt>RepeatTime</tt>.
     * 
     * @param repeatInterval the repeat interval
     * 
     * @param activeDuration the active duration
     * 
     * @param offset the offset
     * 
     * @return a new <tt>RepeatTime</tt> instance
     * 
     * @throws SDPException if the parameters are not valid
     */
    public static RepeatTime createRepeatTime(final long repeatInterval, final long activeDuration, final long offset) throws SDPException {

        return new RepeatTime(repeatInterval, activeDuration, offset);
    }

    /**
     * Returns a new <tt>RepeatTime</tt>.
     * 
     * @param repeatInterval the repeat interval
     * 
     * @param activeDuration the active duration
     * 
     * @param offsets the offsets
     * 
     * @return a new <tt>RepeatTime</tt> instance
     * 
     * @throws SDPException if the parameters are not valid
     */
    public static RepeatTime createRepeatTime(final long repeatInterval, final long activeDuration, final long[] offsets) throws SDPException {

        return new RepeatTime(repeatInterval, activeDuration, offsets);
    }

    /**
     * Returns a new, empty, <tt>SessionDescrition</tt>. The session values
     * are set as follows:
     * <p>
     * <code>
     * v=0<br/>
     * o=<i>&lt;user&gt; &lt;current time as NTP&gt; &lt;current time as NTP&gt; &lt;localhost name&gt;</i><br/>
     * s=-<br/>
     * t=0 0
     * </code>
     * </p>
     * The <i>&lt;user&gt;</i> value is the name of the OS user that invokes
     * this method.
     * 
     * @return a new <tt>SessionDescription</tt>
     * 
     * @throws SDPException if there is a problem while constructing the
     *         <tt>SessionDescription</tt>
     */
    public static SessionDescription createSessionDescription() throws SDPException {

        return new SessionDescription(Version.DEFAULT_VERSION, new Origin(), new SessionName(), new TimeDescription());
    }

    /**
     * Returns a new <tt>SessionName</tt>. The name value is set to
     * <tt>-</tt>.
     * 
     * @return a new <tt>SessionName</tt> instance
     */
    public static SessionName createSessionName() {

        return new SessionName();
    }

    /**
     * Returns a new <tt>SessionName</tt>.
     * 
     * @param value the name of the session
     * 
     * @return a new <tt>SessionName</tt> instance
     * 
     * @throws SDPException if the characters of the session name are not
     *         allowed
     */
    public static SessionName createSessionName(final String value) throws SDPException {

        return new SessionName(value);
    }

    /**
     * Returns a new <tt>Time</tt>. The session start and stop time are set
     * to zero.
     * 
     * @return a new <tt>Time</tt> instance
     */
    public static Time createTime() {

        return new Time();
    }

    /**
     * Returns a new <tt>Time</tt>.
     * 
     * @param start the session start time
     * 
     * @param stop the session stop time
     * 
     * @return a new <tt>Time</tt> instance
     * 
     * @throws SDPException if the NTP representation of the session start
     *         and/or stop time are negative
     */
    public static Time createTime(final Date start, final Date stop) throws SDPException {

        return new Time(start, stop);
    }

    /**
     * Returns a new <tt>Time</tt>.
     * 
     * @param start the session start time
     * 
     * @param stop the session stop time
     * 
     * @return a new <tt>Time</tt> instance
     * 
     * @throws SDPException if the session start or stop time are negative
     */
    public static Time createTime(final long start, final long stop) throws SDPException {

        return new Time(start, stop);
    }

    /**
     * Returns a new <tt>TimeZone</tt>.
     * 
     * @param time the adjustment time
     * 
     * @param offset the offset
     * 
     * @return a new <tt>TimeZone</tt> instance
     * 
     * @throws SDPException if the NTP representation of the adjustment time is
     *         negative
     */
    public static TimeZone createTimeZone(final Date time, final long offset) throws SDPException {

        return new TimeZone(time, offset);
    }

    /**
     * Returns a new <tt>Uri</tt>.
     * 
     * @param url the uri value
     * 
     * @return a new <tt>Uri</tt> instance
     * 
     * @throws SDPException if the value specified is a not valid uri
     */
    public static Uri createUri(final String url) throws SDPException {

        return new Uri(url);
    }

    /**
     * Creates a new <tt>Uri</tt>.
     * 
     * @param url the resource url
     * 
     * @return a new <tt>Uri</tt> instance
     */
    public static Uri createUri(final URL url) {

        return new Uri(url);
    }

    /**
     * Returns a <tt>Version</tt>.
     * 
     * @return a <tt>Version</tt> instance
     */
    public static Version createVersion() {

        return Version.DEFAULT_VERSION;
    }

    /**
     * Write a <tt>SessionDescription</tt> to an output stream, using the
     * default enconding.
     * 
     * @param sd the session description to write
     * 
     * @param stream the stream where write to
     * 
     * @throws IOException if an I/O error occurs
     */
    public static void encode(final SessionDescription sd, final OutputStream stream) throws IOException {

        encode(sd, DEFAULT_CHARSET, stream);
    }

    /**
     * Write a <tt>SessionDescription</tt> to an output stream, using a
     * specified encoding.
     * 
     * @param sd the session description to write
     * 
     * @param encoding the charset encoding to use
     * 
     * @param stream the stream where write to
     * 
     * @throws IOException if an I/O error occurs
     */
    public static void encode(final SessionDescription sd, final String encoding, final OutputStream stream) throws IOException {

        new OutputStreamWriter(new BufferedOutputStream(stream), encoding).write(sd.toString());
    }

    /**
     * Reads data from a file and constructs the equivalent
     * <tt>SessionDescription</tt>.
     * 
     * @param file the source file
     * 
     * @return a <tt>SessionDescription</tt> that contains the data readed
     *         from the source file
     * 
     * @throws SDPParseException if an error occurs while parsing
     * 
     * @throws IOException if an I/O error occurs
     */
    public static SessionDescription parseSessionDescription(final File file) throws SDPParseException, IOException {

        BufferedReader reader = new BufferedReader(new FileReader(file));

        String buffer = null;
        Vector fields = new Vector();

        while ((buffer = reader.readLine()) != null) {
            fields.add(buffer);
        }

        /* Close the stream */
        reader.close();

        return parseSessionDescription((String[]) fields.toArray(new String[fields.size()]));
    }

    /**
     * Reads data from a stream and constructs the equivalent
     * <tt>SessionDescription</tt>.
     * 
     * @param stream the input stream
     * 
     * @return a <tt>SessionDescription</tt> that contains the data readed
     *         from the stream
     * 
     * @throws SDPParseException if an error occurs while parsing
     * 
     * @throws IOException if an I/O error occurs
     */
    public static SessionDescription parseSessionDescription(final InputStream stream) throws SDPParseException, IOException {

        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

        Vector fields = new Vector();
        StringBuffer buffer = new StringBuffer();

        int c;

        while ((c = reader.read()) != -1) {

            if (c != '\r') {
                buffer.append((char) c);
            }
            else {

                /* Search a \n character */
                c = reader.read();

                if (c == '\n') {

                    fields.add(buffer.toString());

                    /* Reset the buffer */
                    buffer.setLength(0);
                }
                else {
                    buffer.append((char) c);
                }
            }
            /* */
        }

        /* Close the stream */
        reader.close();

        /* */
        return parseSessionDescription((String[]) fields.toArray(new String[fields.size()]));
    }

    /**
     * Parse an input string and constructs the equivalent
     * <tt>SessionDescription</tt>.
     * 
     * @param input the string to parse
     * 
     * @return a new <tt>SessionDescription</tt>
     * 
     * @throws SDPParseException if an error occurs while parsing
     */
    public static SessionDescription parseSessionDescription(final String input) throws SDPParseException {

        return parseSessionDescription(input.split(Field.END_OF_FIELD));
    }

    /**
     * Parse the strings contained in an array and constructs the equivalent
     * <tt>SessionDescription</tt>. Each array element represents a SDP
     * field, so the order of the elements is important.
     * 
     * @param fields the string array that stores the SDP fields
     * 
     * @return a new <tt>SessionDescription</tt>
     * 
     * @throws SDPParseException if an error occurs while parsing
     */
    public static SessionDescription parseSessionDescription(final String[] fields) throws SDPParseException {

        int index = 0;

        TimeDescription td = null;
        MediaDescription md = null;
        SessionDescription sd = null;

        boolean wellFormed = false;
        boolean getMoreFields = true;
        boolean getMoreDescriptions = true;

        try {

            /* Create an empty session description */
            sd = new SessionDescription();

            /* Parse version field */
            sd.setVersion(Version.parse(fields[index++]));

            /* Parse origin field */
            sd.setOrigin(Origin.parse(fields[index++]));

            /* Parse session name field */
            sd.setSessionName(SessionName.parse(fields[index++]));

            /* Search an information field */
            try {
                /* Parse information field */
                sd.setInformation(Information.parse(fields[index]));

                /* Increment the index */
                index++;
            }
            catch (SDPParseException exception) {
                /*
                 * The information field is not required, so the exception will
                 * not be thrown
                 */
            }

            /* Search an uri field */
            try {
                /* Parse uri field */
                sd.setUri(Uri.parse(fields[index]));

                /* Increment the index */
                index++;
            }
            catch (SDPParseException exception) {
                /*
                 * The uri field is not required, so the exception will not be
                 * thrown
                 */
            }

            /* Search email fields */
            getMoreFields = true;
            while (getMoreFields) {
                try {
                    /* Parse email field */
                    sd.addEmail(Email.parse(fields[index]));

                    /* Increment the index */
                    index++;
                }
                catch (SDPParseException exception) {
                    /*
                     * The email field is not required, so the exception will
                     * not be thrown
                     */

                    /* Stop to get email fields */
                    getMoreFields = false;
                }
            }

            /* Search phone fields */
            getMoreFields = true;
            while (getMoreFields) {
                try {
                    /* Parse phone field */
                    sd.addPhone(Phone.parse(fields[index]));

                    /* Increment the index */
                    index++;
                }
                catch (SDPParseException exception) {
                    /*
                     * The phone field is not required, so the exception will
                     * not be thrown
                     */

                    /* Stop to get phone fields */
                    getMoreFields = false;
                }
            }

            /* Search a connection field */
            try {
                /* Parse connection field */
                sd.setConnection(Connection.parse(fields[index]));

                /* Increment the index */
                index++;
            }
            catch (SDPParseException exception) {
                /*
                 * The connection field is not required, so the exception will
                 * not be thrown
                 */
            }

            /* Search bandwith fields */
            getMoreFields = true;
            while (getMoreFields) {
                try {
                    /* Parse bandwith field */
                    sd.addBandwith(Bandwith.parse(fields[index]));

                    /* Increment the index */
                    index++;
                }
                catch (SDPParseException exception) {
                    /*
                     * The bandwith field is not required, so the exception will
                     * not be thrown
                     */

                    /* Stop to get bandwith fields */
                    getMoreFields = false;
                }
            }

            /* Create the time description */
            td = new TimeDescription(Time.parse(fields[index++]));

            /* Add the time description */
            sd.addTimeDescription(td);

            /*
             * The supplied message is valid because it contains at least the
             * required fields
             */
            wellFormed = true;

            /* Find repeat time fields */
            getMoreFields = (index < fields.length);
            while (getMoreFields) {
                try {
                    /* Parse repeat time field */
                    td.addRepeatTime(RepeatTime.parse(fields[index]));

                    /* Increment the index */
                    index++;
                }
                catch (SDPParseException exception) {
                    /*
                     * The repeat time field is not required, so the exception
                     * will not be thrown
                     */

                    /* Stop to get fields */
                    getMoreFields = false;
                }
            }

            /*
             * Checking if there are more fields to parse, then search for other
             * time descriptions
             */
            getMoreDescriptions = (index < fields.length);
            while (getMoreDescriptions) {

                try {

                    /* Create the time description */
                    td = new TimeDescription(Time.parse(fields[index]));

                    /* Increment the index */
                    index++;

                    /* Add the time description */
                    sd.addTimeDescription(td);

                    /* Find repeat time fields */
                    getMoreFields = (index < fields.length);
                    while (getMoreFields) {
                        try {
                            /* Parse repeat time field */
                            td.addRepeatTime(RepeatTime.parse(fields[index]));

                            /* Increment the index */
                            index++;
                        }
                        catch (SDPParseException exception) {
                            /*
                             * The repeat time field is not required, so the
                             * exception will not be thrown
                             */

                            /* Stop to get fields */
                            getMoreFields = false;
                        }
                    }

                }
                catch (SDPParseException noTimeDescription) {

                    /*
                     * Other time descriptions aren't required, so the exception
                     * isn't thrown
                     */
                    getMoreDescriptions = false;
                }
            }

            /* Search a timezone field */
            try {
                /* Parse timezone field */
                sd.setTimeZone(TimeZone.parse(fields[index]));

                /* Increment the index */
                index++;
            }
            catch (SDPParseException exception) {
                /*
                 * The timezone field is not required, so the exception will not
                 * be thrown
                 */
            }

            /* Search a key field */
            try {
                /* Parse key field */
                sd.setKey(Key.parse(fields[index]));

                /* Increment the index */
                index++;
            }
            catch (SDPParseException exception) {
                /*
                 * The key field is not required, so the exception will not be
                 * thrown
                 */
            }

            /* Search attribute fields */

            /* Check if there are more fields to parse */
            getMoreFields = index < fields.length;
            while (getMoreFields) {
                try {
                    /* Parse attribute field */
                    sd.addAttribute(Attribute.parse(fields[index]));

                    /* Increment the index */
                    index++;
                }
                catch (SDPParseException exception) {
                    /*
                     * The attribute field is not required, so the exception
                     * will not be thrown
                     */

                    /* Stop to get fields */
                    getMoreFields = false;
                }
            }

            /* Search media descriptions */

            /* Check if there are more fields to parse */
            getMoreDescriptions = index < fields.length;
            while (getMoreDescriptions) {

                try {

                    /* Create the media description */
                    md = new MediaDescription(Media.parse(fields[index]));

                    /* Increment the index */
                    index++;

                    /* Search an information field */
                    try {

                        /* Parse information field */
                        md.setInformation(Information.parse(fields[index]));

                        /* Increment the index */
                        index++;
                    }
                    catch (SDPParseException exception) {
                        /*
                         * The information field is not required, so the
                         * exception will not be thrown
                         */
                    }

                    /* Search a connection field */
                    try {
                        /* Parse connection field */
                        md.setConnection(Connection.parse(fields[index]));

                        /* Increment the index */
                        index++;
                    }
                    catch (SDPParseException exception) {
                        /*
                         * The connection field is not required, so the
                         * exception will not be thrown
                         */
                    }

                    /* Search bandwith fields */
                    getMoreFields = true;
                    while (getMoreFields) {
                        try {
                            /* Parse bandwith field */
                            md.addBandwith(Bandwith.parse(fields[index]));

                            /* Increment the index */
                            index++;
                        }
                        catch (SDPParseException exception) {
                            /*
                             * The bandwith field is not required, so the
                             * exception will not be thrown
                             */

                            /* Stop to get bandwith fields */
                            getMoreFields = false;
                        }
                    }

                    /* Search a key field */
                    try {
                        /* Parse key field */
                        md.setKey(Key.parse(fields[index]));

                        /* Increment the index */
                        index++;
                    }
                    catch (SDPParseException exception) {
                        /*
                         * The key field is not required, so the exception will
                         * not be thrown
                         */
                    }

                    /* Search attribute fields */
                    getMoreFields = (index < fields.length);
                    while (getMoreFields) {
                        try {
                            /* Parse attribute field */
                            md.addAttribute(Attribute.parse(fields[index]));

                            /* Increment the index */
                            index++;

                            /* Check if there are more fields to parse */
                            getMoreFields = (index < fields.length);
                            getMoreDescriptions &= getMoreFields;
                        }
                        catch (SDPParseException exception) {
                            /*
                             * The attribute field is not required, so the
                             * exception will not be thrown
                             */

                            /* Stop to get fields */
                            getMoreFields = false;
                        }
                    }

                    /* Add the media description */
                    sd.addMediaDescription(md);

                    /* Reset the media description */
                    md = null;

                    /* Check if there are more fields to parse */
                    getMoreDescriptions = (index < fields.length);
                }
                catch (IndexOutOfBoundsException noMoreFields) {

                    /* There aren't more fields to parse */
                    getMoreFields = false;
                    getMoreDescriptions = false;

                    /* */
                    if (md != null) {
                        sd.addMediaDescription(md);
                    }
                }
                catch (SDPParseException noMediaDescription) {

                    /*
                     * Other media descriptions aren't required, so the
                     * exception isn't thrown
                     */
                    getMoreDescriptions = false;

                    if (index < fields.length) {
                        throw new SDPParseException("Cannot parse a media description", noMediaDescription);
                    }
                }
            }
        }
        catch (IndexOutOfBoundsException noMoreFields) {

            /* If the required fields aren't present, throw an exception */
            if (!wellFormed) {
                throw new SDPParseException("End of message reached but some required fields are missing");
            }
        }
        catch (SDPParseException parseException) {
            throw new SDPParseException("Invalid session description", parseException);
        }
        catch (SDPException sdpException) {
            throw new SDPParseException("Invalid session description", sdpException);
        }

        return sd;
    }
}
