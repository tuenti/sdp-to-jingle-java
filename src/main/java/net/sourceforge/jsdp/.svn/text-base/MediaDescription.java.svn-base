/*
 * jSDP: A Java implementation of SDP protocol Copyright (C) 2007 Claudio Di
 * Vita
 */

package net.sourceforge.jsdp;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * A <tt>MediaDescription</tt> identifies the set of medias that may be
 * received. It includes:
 * <ul>
 * <li>A media type (e.g., audio, video, etc.)</li>
 * <li>A port number (or set of ports)</li>
 * <li>A protocol to be used (e.g., RTP/AVP)</li>
 * <li>Set of media formats which correspond to attributes</li>
 * </ul>
 * 
 * @see Attribute
 * 
 * @since 0.1.0
 * 
 * @version 1.1
 * 
 * @author <a href="mailto:cdivita@users.sourceforge.net">Claudio Di Vita</a>
 */
public class MediaDescription implements Description {

    /** The class Stream Unique Identifier, SUID */
    private static final long serialVersionUID = 2260842521161330113L;

    /** The media field */
    protected Media m;

    /** The information field */
    protected Information i;

    /** The connection field */
    protected Connection c;

    /** The bandwith fields */
    protected HashMap bandwiths;

    /** The key field */
    protected Key k;

    /** The attribute fields */
    protected HashMap attributes;

    /**
     * 
     */
    private MediaDescription() {

        super();
    }

    /**
     * Creates a new <tt>MediaDescription</tt>.
     * 
     * @param media the media field
     * 
     * @throws IllegalArgumentException if the media field is <tt>null</tt>
     */
    public MediaDescription(final Media media) throws IllegalArgumentException {

        super();

        setMedia(media);

        bandwiths = new HashMap();
        attributes = new HashMap();
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
     * @param field the bandwith field to set
     * 
     * @throws IllegalArgumentException if the bandwith field is <tt>null</tt>
     */
    public void addBandwith(final Bandwith field) throws IllegalArgumentException {

        if (field == null) {
            throw new IllegalArgumentException("A bandwith field cannot be null");
        }

        bandwiths.put(field.getModifier(), field);
    }

    /**
     * Remove all attribute fields contained in the description.
     */
    public void clearAttributes() {

        attributes.clear();
    }

    /**
     * Remove all bandwith fields contained in the description.
     */
    public void clearBandwiths() {

        bandwiths.clear();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#clone()
     */
    public Object clone() {

        MediaDescription clone = new MediaDescription();

        clone.m = (Media) this.m.clone();
        clone.i = (Information) this.i.clone();
        clone.c = (Connection) this.c.clone();
        clone.bandwiths = (HashMap) this.bandwiths.clone();
        clone.k = (Key) this.k.clone();
        clone.attributes = (HashMap) this.attributes.clone();

        return clone;
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
     * Returns the attribute fields with the specified name.
     * 
     * <p>
     * If there is only an occurence of that attribute the length of returned
     * array is <tt>1</tt>, instead if there aren't attributes with the given
     * name the length of returned array is <tt>0</tt>
     * </p>
     * 
     * @param name the name of the attribute
     * 
     * @return an array of attribute fields
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
     * Returns the number of attribute fields with the specified name.
     
     * @param name the name of the attribute
     
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

        return (Bandwith) this.bandwiths.get(modifier);
    }

    /**
     * Returns the bandwith fields.
     * 
     * @return an array that contains the bandwith fields contained in the
     *         description
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
     * @return the connection field is present, <tt>null</tt> otherwise
     */
    public Connection getConnection() {

        return c;
    }

    /**
     * Returns the information field.
     * 
     * @return the information field if is present, <tt>null</tt> otherwise
     */
    public Information getInformation() {

        return i;
    }

    /**
     * Returns the encryption key field.
     * 
     * @return the encryption key field if present, <tt>null</tt> otherwise
     */
    public Key getKey() {

        return k;
    }

    /**
     * Returns the media field.
     * 
     * @return the media field
     */
    public Media getMedia() {

        return m;
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
     * Removes the attribute field with an associated name. If there are multiple
     * occurencies of attributes with that name, the first one is removed
     * 
     * @param name the name of the attribute field to remove
     * 
     * @return the removed <tt>Attribute</tt> if present, <tt>null</tt>
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
     * Removes a specified occurence of an attribute field with the specified
     * name.
     * 
     * @param name the name of the attribute
     * @param index the occurence index
     * 
     * @return the removed field
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
     * @return the removed <tt>Bandwith</tt> if present, <tt>null</tt>
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
     * @param c the field to set. It can be <tt>null</tt>
     */
    public void setConnection(final Connection c) {

        this.c = c;
    }

    /**
     * Sets the information field.
     * 
     * @param i the field to set. It can be <tt>null</tt>
     */
    public void setInformation(final Information i) {

        this.i = i;
    }

    /**
     * Sets the encryption key field.
     * 
     * @param k the field to set. It can be <tt>null</tt>
     */
    public void setKey(final Key k) {

        this.k = k;
    }

    /**
     * Sets the media field.
     * 
     * @param m the field to set
     * 
     * @throws IllegalArgumentException if the field is <tt>null</tt>
     */
    public void setMedia(final Media m) throws IllegalArgumentException {

        if (m == null) {
            throw new IllegalArgumentException("The media field cannot be null");
        }

        this.m = m;
    }

    /**
     * Returns a string representation of the description.
     * 
     * @return a string representation of the description
     */
    public String toString() {

        /* Add the media field */
        StringBuffer result = new StringBuffer(m.toString());
        result.append(Field.END_OF_FIELD);

        /* Add the information field, if present */
        if (i != null) {
            result.append(i.toString());
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

        return result.toString();
    }
}
