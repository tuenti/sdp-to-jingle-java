/*
 * jSDP: A Java implementation of SDP protocol Copyright (C) 2007 Claudio Di
 * Vita
 */
package net.sourceforge.jsdp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * A <tt>SessionDescription</tt> represents the data defined by the Session
 * Description Protocol.
 * <p>
 * The session description holds information about the originator of a session,
 * the media types that a client can support and the host and port on which the
 * client will listen for that media. It also holds timing information for the
 * session (e.g. start, end, repeat, time zone) and bandwidth supported for the
 * session.
 * </p>
 * 
 * <p>
 * For further information please refer to <a
 * href="http://www.ietf.org/rfc/rfc4566.txt">RFC 4566</a>.
 * </p>
 * 
 * @see TimeDescription
 * @see MediaDescription
 * 
 * @since 0.1.0
 * 
 * @version 1.1
 * 
 * @author <a href="mailto:cdivita@users.sourceforge.net">Claudio Di Vita</a>
 */
public class SessionDescription implements Description {

    /** The class Stream Unique Identifier, SUID */
    private static final long serialVersionUID = -2804972731894656668L;

    /** The version field */
    protected Version v;

    /** The origin field */
    protected Origin o;

    /** The session name field */
    protected SessionName s;

    /** The information field */
    protected Information i;

    /** The uri field */
    protected Uri u;

    /** The email fields */
    protected ArrayList emails;

    /** The phone fields */
    protected ArrayList phones;

    /** The connection field */
    protected Connection c;

    /** The bandwith fields */
    protected HashMap bandwiths;

    /** The time descriptions */
    protected ArrayList timeDescriptions;

    /** The time zone field */
    protected TimeZone z;

    /** The key field */
    protected Key k;

    /** The attribute fields */
    protected HashMap attributes;

    /** The media descriptions */
    protected ArrayList mediaDescriptions;

    /**
     * Creates a <tt>SessionDescription</tt>.
     */
    protected SessionDescription() {

        super();

        this.emails = new ArrayList();
        this.phones = new ArrayList();
        this.bandwiths = new HashMap();
        this.timeDescriptions = new ArrayList();
        this.attributes = new HashMap();
        this.mediaDescriptions = new ArrayList();
    }

    /**
     * Creates a <tt>SessionDescription</tt>. This constructor is used to
     * implement the @link #clone()} method.
     * 
     * @param sd the <tt>SessionDescription</tt> to clone
     */
    protected SessionDescription(final SessionDescription sd) {

        super();

        this.v = (Version) sd.v.clone();
        this.o = (Origin) sd.o.clone();
        this.s = (SessionName) sd.s.clone();
        this.i = (Information) sd.i.clone();
        this.u = (Uri) sd.u.clone();
        this.emails = (ArrayList) sd.emails.clone();
        this.phones = (ArrayList) sd.phones.clone();
        this.c = (Connection) sd.c.clone();
        this.bandwiths = (HashMap) sd.bandwiths.clone();
        this.timeDescriptions = (ArrayList) sd.timeDescriptions.clone();
        this.z = (TimeZone) sd.z.clone();
        this.attributes = (HashMap) sd.attributes.clone();
        this.mediaDescriptions = (ArrayList) sd.mediaDescriptions.clone();
    }

    /**
     * Creates a <tt>SessionDescription</tt>.
     * 
     * @param v the version field
     * 
     * @param o the origin field
     * 
     * @param s the session name field
     * 
     * @param td the time description
     * 
     * @throws IllegalArgumentException if one or more parameters are
     *         <tt>null</tt>
     */
    public SessionDescription(final Version v, final Origin o, final SessionName s, final TimeDescription td) throws IllegalArgumentException {

        this();

        setVersion(v);
        setOrigin(o);
        setSessionName(s);

        addTimeDescription(td);
    }

    /**
     * Adds an attribute field. If an <tt>Attribute</tt> with the same name is
     * already present it will be added.
     * 
     * @param field the attribute field to set
     * 
     * @throws IllegalArgumentException if the field is <tt>null</tt>
     */
    public void addAttribute(final Attribute field) throws IllegalArgumentException {

        if (field == null) {
            throw new IllegalArgumentException("An attribute field cannot be null");
        }

        /* Get attribute name */
        String name = field.getName();

        /* An object with the same name is already present */
        if (attributes.containsKey(name)) {

            List values = null;
            Object maybe = attributes.get(name);

            /* There are many attributes with that name */
            if (maybe instanceof List) {

                values = (List) maybe;
            }
            /* There is only an attribute with that name */
            else {

                /* Get previous attribute */
                Attribute previous = (Attribute) maybe;

                /*
                 * Create a list that contains all the attributes with the given
                 * name
                 */
                values = new LinkedList();

                /* Add the previous attribute to list */
                values.add(previous);

                attributes.put(field.getName(), values);
            }

            values.add(field);

        }
        /* An attribute with given name is not present */
        else {

            attributes.put(field.getName(), field);
        }
    }

    /**
     * Adds a bandwith field. If a <tt>Bandwith</tt> with the same modifier is
     * already present it will be replaced.
     * 
     * @param bandwith the bandwith field to set
     * 
     * @throws IllegalArgumentException if the bandwith field is <tt>null</tt>
     */
    public void addBandwith(final Bandwith bandwith) throws IllegalArgumentException {

        if (bandwith == null) {
            throw new IllegalArgumentException("A bandwith field cannot be null");
        }

        bandwiths.put(bandwith.getModifier(), bandwith);
    }

    /**
     * Adds an email field.
     * 
     * @param field the email field to add
     * 
     * @throws IllegalArgumentException if the field is <tt>null</tt>
     */
    public void addEmail(final Email field) throws IllegalArgumentException {

        if (field == null) {
            throw new IllegalArgumentException("An email field cannot be null");
        }

        emails.add(field);
    }

    /**
     * Adds a media description.
     * 
     * @param md the media description to add
     * 
     * @throws IllegalArgumentException if the media description is
     *         <tt>null</tt>
     * 
     * @throws SDPException if both session and media description doesn't have a
     *         connection field
     */
    public void addMediaDescription(final MediaDescription md) throws IllegalArgumentException, SDPException {

        if (md == null) {
            throw new IllegalArgumentException("A media description cannot be null");
        }

        if (!hasConnection() && !md.hasConnection()) {
            throw new SDPException("This media description must have a connection field");
        }

        /* Add the media description */
        mediaDescriptions.add(md);
    }

    /**
     * Adds a phone field.
     * 
     * @param field the phone field to add
     * 
     * @throws IllegalArgumentException tf the field is <tt>null</tt>
     */
    public void addPhone(final Phone field) throws IllegalArgumentException {

        if (field == null) {
            throw new IllegalArgumentException("A phone field cannot be null");
        }

        /* Add the phone field */
        phones.add(field);
    }

    /**
     * Adds a time description.
     * 
     * @param td the time description to set
     * 
     * @throws IllegalArgumentException if the description is <tt>null</tt>
     */
    public void addTimeDescription(final TimeDescription td) throws IllegalArgumentException {

        if (td == null) {
            throw new IllegalArgumentException("A time description cannot be null");
        }

        timeDescriptions.add(td);
    }

    /**
     * Remove all attribute fields contained in the description.
     */
    public void clearAttributes() {
    
        attributes.clear();
    }

    /**
     * Remove all bandwith fields contained in the session description.
     */
    public void clearBandwiths() {

        this.bandwiths.clear();
    }

    /**
     * Remove all email fields contained in the session description.
     */
    public void clearEmails() {

        this.emails.clear();
    }

    /**
     * Remove all media descriptions contained in the session description.
     */
    public void clearMediaDescriptions() {

        this.mediaDescriptions.clear();
    }

    /**
     * Remove all phone fields contained in the session description.
     */
    public void clearPhones() {

        this.phones.clear();
    }

    /**
     * Returns a clone of this description.
     * 
     * @return a clone of this description
     */
    public Object clone() {

        return new SessionDescription(this);
    }

    /**
     * Returns all attribute fields.
     * 
     * @return an array that contains the attribute fields contained in the
     *         description
     */
    public Attribute[] getAttributes() {

        Object maybe = null;
        List values = null;

        Collection result = new LinkedList();

        for (Iterator i = attributes.values().iterator(); i.hasNext();) {

            maybe = i.next();

            if (maybe instanceof Attribute) {

                result.add(maybe);
            }
            else {

                values = (List) maybe;

                for (Iterator j = values.iterator(); j.hasNext();) {

                    result.add(j.next());
                }
            }
        }

        return (Attribute[]) result.toArray(new Attribute[result.size()]);
    }

    /**
     * Returns the attribute with the specified name. If there are multiple
     * occurencies of attributes with that name, the first one is returned
     * 
     * @param name the name of the attribute
     * 
     * @return an attribute field
     */
    public Attribute getAttribute(final String name) {

        Attribute result = null;
        Object maybe = attributes.get(name);

        if (maybe != null) {

            if (maybe instanceof List) {

                List values = (List) maybe;

                result = (Attribute) values.get(0);
            }
            else {

                result = (Attribute) maybe;
            }
        }

        return result;
    }

    /**
     * Returns a specified occurence of an attribute with the specified name.
     * 
     * @param name the name of the attribute
     * @param index the occurence index
     * 
     * @return an attribute field
     */
    public Attribute getAttribute(final String name, final int index) {

        Attribute result = null;
        Object maybe = attributes.get(name);

        if ((maybe != null) && (maybe instanceof List)) {

            List values = (List) maybe;

            result = (Attribute) values.get(index);
        }

        return result;
    }

    /**
     * Returns the attributes with the specified name.
     * 
     * <p>
     * If there is only an occurence of that attribute the length of returned
     * array is <tt>1</tt>, instead if there aren't attributes with the given
     * name the length of returned array is <tt>0</tt>
     * </p>
     * 
     * @param name the name of the attribute
     * 
     * @return an array of attributes
     */
    public Attribute[] getAttributes(final String name) {

        Attribute[] result;
        Object maybe = attributes.get(name);

        if (maybe != null) {

            if (maybe instanceof List) {

                List values = (List) maybe;

                result = (Attribute[]) values.toArray(new Attribute[values.size()]);
            }
            else {

                result = new Attribute[1];
                result[0] = (Attribute) maybe;
            }
        }
        else {
            result = new Attribute[0];
        }

        return result;
    }

    /**
     * Returns the number of attributes with a specified name
     * 
     * @param name the name of the attribute
     * 
     * @return an integer
     */
    public int getAttributesCount(final String name) {

        int result = 0;
        Object maybe = attributes.get(name);

        if (maybe != null) {

            if (maybe instanceof List) {

                List values = (List) maybe;

                result = values.size();
            }
            else {

                result = 1;
            }
        }

        return result;
    }

    /**
     * Returns the bandwith field with a specified modifier.
     * 
     * @param modifier the bandwith modifier
     * 
     * @return the bandwith field specified by modifier if present,
     *         <tt>null</tt> otherwise
     */
    public Bandwith getBandwith(final String modifier) {

        return (Bandwith) bandwiths.get(modifier);
    }

    /**
     * Returns the bandwith fields.
     * 
     * @return an array that contains the bandwith fields contained in the
     *         session description
     */
    public Bandwith[] getBandwiths() {

        Bandwith[] values = new Bandwith[bandwiths.size()];
        Iterator iterator = bandwiths.values().iterator();

        for (int j = 0; j < values.length; j++) {
            values[j] = (Bandwith) iterator.next();
        }

        return values;
    }

    /**
     * Returns the connection field.
     * 
     * @return the connection field if present, <tt>null</tt> otherwise
     */
    public Connection getConnection() {

        return c;
    }

    /**
     * Returns the email fields.
     * 
     * @return an array that contains the email fields contained in the session
     *         description
     */
    public Email[] getEmails() {

        return (Email[]) emails.toArray(new Email[emails.size()]);
    }

    /**
     * Returns the information field.
     * 
     * @return the information field if present, <tt>null</tt> otherwise
     */
    public Information getInformation() {

        return i;
    }

    /**
     * Returns the key field.
     * 
     * @return the key field if present, <tt>null</tt> otherwise
     */
    public Key getKey() {

        return k;
    }

    /**
     * Returns the media descriptions.
     * 
     * @return an array that contains the media descriptions contained in the
     *         session description
     */
    public MediaDescription[] getMediaDescriptions() {

        return (MediaDescription[]) mediaDescriptions.toArray(new MediaDescription[mediaDescriptions.size()]);
    }

    /**
     * Returns the number of media descriptions.
     *
     * @return an integer
     */
    public int getMediaDescriptionsCount() {

        return mediaDescriptions.size();
    }

    /**
     * Returns the origin field.
     * 
     * @return the origin field
     */
    public Origin getOrigin() {

        return o;
    }

    /**
     * Returns the phone fields.
     * 
     * @return an array that contains the phone fields contained in the session
     *         description
     */
    public Phone[] getPhones() {

        return (Phone[]) phones.toArray(new Phone[phones.size()]);
    }

    /**
     * Returns the session name field.
     * 
     * @return the session name field
     */
    public SessionName getSessionName() {

        return s;
    }

    /**
     * Returns the time descriptions.
     * 
     * @return an array that contains the time descriptions contained in the
     *         session description
     */
    public TimeDescription[] getTimeDescriptions() {

        return (TimeDescription[]) timeDescriptions.toArray(new TimeDescription[timeDescriptions.size()]);
    }

    /**
     * Returns the time zone field.
     * 
     * @return the time zone field if present, <tt>null</tt> otherwise
     */
    public TimeZone getTimeZone() {

        return z;
    }

    /**
     * Returns the uri field.
     * 
     * @return the uri field if present, <tt>null</tt> otherwise
     */
    public Uri getUri() {

        return u;
    }

    /**
     * Returns the version field.
     * 
     * @return the version field
     */
    public Version getVersion() {

        return v;
    }

    /**
     * Indicates if an attribute with a specified name is present.
     * 
     * @param name the name of the attribute
     * 
     * @return <tt>true</tt> if the attribute is present,
     *         <tt>false</tt> otherwise
     */
    public boolean hasAttribute(String name) {

        boolean result = false;

        if (name != null) {
            result = attributes.containsKey(name);
        }

        return result;
    }

    /**
     * Indicates if the connection field is present.
     * 
     * @return <tt>true</tt> if the connection field is present,
     *         <tt>false</tt> otherwise
     */
    public boolean hasConnection() {

        return c != null;
    }

    /**
     * Indicates if one or more email fields are present.
     * 
     * @return <tt>true</tt> if one or more email fields are present,
     *         <tt>false</tt> otherwise
     */
    public boolean hasEmails() {

        return (emails.size() > 0);
    }

    /**
     * Indicates if the information field is present.
     * 
     * @return <tt>true</tt> if the information field is present,
     *         <tt>false</tt> otherwise
     */
    public boolean hasInformation() {

        return i != null;
    }

    /**
     * Indicates if the encryption key field is present.
     * 
     * @return <tt>true</tt> if the encryption key field is present,
     *         <tt>false</tt> otherwise
     */
    public boolean hasKey() {

        return k != null;
    }

    /**
     * Indicates if one or more phone fields are present.
     * 
     * @return <tt>true</tt> if one or more phone fields are present,
     *         <tt>false</tt> otherwise
     */
    public boolean hasPhones() {

        return (phones.size() > 0);
    }

    /**
     * Indicates if the timezone field is present.
     * 
     * @return <tt>true</tt> if the timezone field is present, <tt>false</tt>
     *         otherwise
     */
    public boolean hasTimeZone() {

        return z != null;
    }

    /**
     * Indicates if the uri field is present
     * 
     * @return <tt>true</tt> if the uri field is present, <tt>false</tt>
     *         otherwise
     */
    public boolean hasUri() {

        return u != null;
    }

    /**
     * Removes the attribute field with an associated name. If there are multiple
     * occurencies of attributes with that name, the first one is removed
     * 
     * @param name the name of the attribute field to remove
     * 
     * @return the removed attribute if present, <tt>null</tt>
     *         otherwise
     */
    public Attribute removeAttribute(final String name) {

        Attribute a = null;
        Object maybe = attributes.get(name);

        if (maybe != null) {

            if (maybe instanceof List) {

                List values = (List) maybe;

                /* Remove the first occurence of the attribute */
                a = (Attribute) values.remove(0);

                /* Remove multiple attributes values, if empty */
                if (values.isEmpty()) {
                    attributes.remove(name);
                }
            }
            else {
                a = (Attribute) attributes.remove(name);
            }
        }

        return a;
    }

    /**
     * Removes a specified occurence of an attribute with the specified name.
     * 
     * @param name the name of the attribute
     * @param index the occurence index
     * 
     * @return the removed attribute
     */
    public Attribute removeAttribute(final String name, final int index) {

        Attribute a = null;
        Object maybe = attributes.get(name);

        if ((maybe != null) && (maybe instanceof List)) {

            List values = (List) maybe;

            a = (Attribute) values.remove(index);
        }

        return a;
    }

    /**
     * Remove all occurencies of attributes with the specified name.
     * 
     * @param name the name of the attribute
     */
    public Attribute[] removeAttributes(final String name) {

        /* Get attributes with given name */
        Attribute[] result = getAttributes(name);

        /* Remove attributes */
        attributes.remove(name);

        return result;
    }

    /**
     * Remove the bandwith field with an associated modifier
     * 
     * @param modifier the modifier associated to the bandwith field to remove
     * 
     * @return the removed bandwith if present, <tt>null</tt>
     *         otherwise
     */
    public Bandwith removeBandwith(final String modifier) {

        return (Bandwith) bandwiths.remove(modifier);
    }

    /**
     * Set attribute fields. Please note that previous attributes will be
     * replaced
     * 
     * @param fields the fields to set
     * 
     * @throws IllegalArgumentException if one or more field are <tt>null</tt>
     */
    public void setAttributes(Attribute[] fields) {

        if (fields == null) {
            throw new IllegalArgumentException("Attribute fields cannot be null");
        }

        int length = fields.length;
        HashMap backup = (HashMap) attributes.clone();

        try {

            /* Remove current attributes */
            attributes.clear();

            /* Add attributes */
            for (int i = 0; i < length; i++) {
                addAttribute(fields[i]);
            }
        }
        catch (IllegalArgumentException exception) {

            /* An error is occured, so we "rollback" */
            attributes = backup;

            /* Rethrow the exception */
            throw exception;
        }
    }

    /**
     * Set bandwith fields. Please note that previous bandwiths will be
     * replaced
     * 
     * @param fields the fields to set
     * 
     * @throws IllegalArgumentException if one or more field are <tt>null</tt>
     */
    public void setBandwiths(Bandwith[] fields) {

        if (fields == null) {
            throw new IllegalArgumentException("Bandwith fields cannot be null");
        }

        int length = fields.length;
        HashMap backup = (HashMap) bandwiths.clone();

        try {

            /* Remove current bandwiths */
            bandwiths.clear();

            /* Add bandwiths */
            for (int i = 0; i < length; i++) {
                addBandwith(fields[i]);
            }
        }
        catch (IllegalArgumentException exception) {

            /* An error is occured, so we "rollback" */
            bandwiths = backup;

            /* Rethrow the exception */
            throw exception;
        }
    }

    /**
     * Sets the connection field.
     * 
     * @param c the connection field to set
     */
    public void setConnection(final Connection c) {

        //FIXME Prevent the field removal while it isn't specified in the media descriptions
        this.c = c;
    }

    /**
     * Sets email fields.
     * 
     * @param fields the email fields to set
     * 
     * @throws IllegalArgumentException if one or more field are <tt>null</tt>
     */
    public void setEmails(final Email[] fields) throws IllegalArgumentException {

        if (fields == null) {
            throw new IllegalArgumentException("Email fields cannot be null");
        }

        int length = fields.length;
        ArrayList backup = (ArrayList) emails.clone();

        try {

            /* Clear current email fields */
            emails.clear();
        
            /* Add email fields */
            for (int i = 0; i < length; i++) {

                addEmail(fields[i]);
            }
        }
        catch (IllegalArgumentException exception) {

            /* Rollback */
            emails = backup;

            /* Rethrow the exception */
            throw exception;
        }
    }

    /**
     * Sets the information field.
     * 
     * @param i the information field to set
     */
    public void setInformation(final Information i) {

        this.i = i;
    }

    /**
     * Sets the encryption key field.
     * 
     * @param k the encryption key field to set
     */
    public void setKey(final Key k) {

        this.k = k;
    }

    /**
     * Sets media descriptions.
     * 
     * @param descriptions the media descriptions to set
     * 
     * @throws IllegalArgumentException if one or more descriptions are
     *         <tt>null</tt>
     */
    public void setMediaDescriptions(final MediaDescription[] descriptions) throws IllegalArgumentException, SDPException {

        if (descriptions == null) {
            throw new IllegalArgumentException("Media descriptions cannot be null");
        }

        int length = descriptions.length;
        ArrayList backup = (ArrayList) mediaDescriptions.clone();

        try {

            /* Clear current media descriptions */
            mediaDescriptions.clear();
        
            /* Add media descriptions */
            for (int i = 0; i < length; i++) {

                addMediaDescription(descriptions[i]);
            }
        }
        catch (IllegalArgumentException exception) {

            /* Rollback */
            mediaDescriptions = backup;

            /* Rethrow the exception */
            throw exception;
        }
        catch (SDPException sdpException) {

            /* Rollback */
            mediaDescriptions = backup;

            /* Rethrow the exception */
            throw sdpException;
        }
    }

    /**
     * Sets the origin field.
     * 
     * @param o the origin field to set
     * 
     * @throws IllegalArgumentException if the field is <tt>null</tt>
     */
    public void setOrigin(final Origin o) throws IllegalArgumentException {

        if (o == null) {
            throw new IllegalArgumentException("Origin field cannot be null");
        }

        this.o = o;
    }

    /**
     * Sets phone fields.
     * 
     * @param fields the phone fields to set
     * 
     * @throws IllegalArgumentException if one or more phone fields are
     *         <tt>null</tt>
     */
    public void setPhones(final Phone[] fields) throws IllegalArgumentException {

        if (fields == null) {
            throw new IllegalArgumentException("Phone fields cannot be null");
        }

        int length = fields.length;
        ArrayList backup = (ArrayList) phones.clone();

        try {

            /* Clear current phone fields */
            phones.clear();
        
            /* Add phone fields */
            for (int i = 0; i < length; i++) {

                addPhone(fields[i]);
            }
        }
        catch (IllegalArgumentException exception) {

            /* Rollback */
            phones = backup;

            /* Rethrow the exception */
            throw exception;
        }
    }

    /**
     * Sets session name field.
     * 
     * @param s the session name field to set
     * 
     * @throws IllegalArgumentException if the field is <tt>null</tt>
     */
    public void setSessionName(final SessionName s) throws IllegalArgumentException {

        if (s == null) {
            throw new IllegalArgumentException("Session name field cannot be null");
        }

        this.s = s;
    }

    /**
     * Sets time descriptions.
     * 
     * @param descriptions the time descriptions to set
     * 
     * @throws IllegalArgumentException if one or more description are
     *         <tt>null</tt>
     */
    public void setTimeDescriptions(final TimeDescription[] descriptions) throws IllegalArgumentException {

        if (descriptions == null) {
            throw new IllegalArgumentException("Time descriptions cannot be null");
        }

        int length = descriptions.length;
        ArrayList backup = (ArrayList) timeDescriptions.clone();

        try {

            /* Clear current time descriptions */
            timeDescriptions.clear();
        
            /* Add time descriptions */
            for (int i = 0; i < length; i++) {

                addTimeDescription(descriptions[i]);
            }
        }
        catch (IllegalArgumentException exception) {

            /* Rollback */
            timeDescriptions = backup;

            /* Rethrow the exception */
            throw exception;
        }
    }

    /**
     * Sets the timezone field.
     * 
     * @param z the timezone field to set
     */
    public void setTimeZone(final TimeZone z) {

        this.z = z;
    }

    /**
     * Sets the uri field.
     * 
     * @param u the uri field to set
     */
    public void setUri(final Uri u) {

        this.u = u;
    }

    /**
     * Sets the version field.
     * 
     * @param v the version field to set
     * 
     * @throws IllegalArgumentException if the field is <tt>null</tt>
     */
    public void setVersion(final Version v) throws IllegalArgumentException {

        if (v == null) {
            throw new IllegalArgumentException("Version field cannot be null");
        }

        this.v = v;
    }

    /**
     * Returns a string representation of this description.
     * 
     * @return A string representation of the description
     */
    public String toString() {

        /* Add the version field */
        StringBuffer result = new StringBuffer(v.toString());
        result.append(Field.END_OF_FIELD);

        /* Add the origin field */
        result.append(o.toString());
        result.append(Field.END_OF_FIELD);

        /* Add the session name field */
        result.append(s.toString());
        result.append(Field.END_OF_FIELD);

        /* Add the information field, if present */
        if (i != null) {
            result.append(i.toString());
            result.append(Field.END_OF_FIELD);
        }

        /* Add the uri field, if present */
        if (u != null) {
            result.append(u.toString());
            result.append(Field.END_OF_FIELD);
        }

        /* Add the email fields, if present */
        for (Iterator iterator = emails.iterator(); iterator.hasNext();) {
            result.append(iterator.next());
            result.append(Field.END_OF_FIELD);
        }

        /* Add the phone fields, if present */
        for (Iterator iterator = phones.iterator(); iterator.hasNext();) {
            result.append(iterator.next());
            result.append(Field.END_OF_FIELD);
        }

        /* Add the connection field, if present */
        if (c != null) {
            result.append(c.toString());
            result.append(Field.END_OF_FIELD);
        }

        int size = 0;
        
        Bandwith[] myBandwiths = getBandwiths();
        size = myBandwiths.length;

        /* Add the bandwith fields */
        for (int i = 0; i < size; i++) {
            result.append(myBandwiths[i]);
            result.append(Field.END_OF_FIELD);
        }

        /* Add the time descriptions */
        for (Iterator i = timeDescriptions.iterator(); i.hasNext();) {
            result.append(i.next());
            /*
             * Don't append END_OF_FIELD because this element is a time
             * description
             */
        }

        /* Add the timezone field, if present */
        if (z != null) {
            result.append(z.toString());
            result.append(Field.END_OF_FIELD);
        }

        /* Add the key field, if present */
        if (k != null) {
            result.append(k.toString());
            result.append(Field.END_OF_FIELD);
        }

        Attribute[] myAttributes = getAttributes();
        size = myAttributes.length;

        /* Add the attribute fields */
        for (int i = 0; i < size; i++) {
            result.append(myAttributes[i]);
            result.append(Field.END_OF_FIELD);
        }

        /* Add the media descriptions */
        for (Iterator i = mediaDescriptions.iterator(); i.hasNext();) {
            result.append(i.next());
            /*
             * Don't append END_OF_FIELD because this element is a media
             * description
             */
        }

        return result.toString();
    }
}
