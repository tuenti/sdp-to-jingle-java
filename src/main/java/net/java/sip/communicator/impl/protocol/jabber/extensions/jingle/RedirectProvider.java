/*
 * Jitsi, the OpenSource Java VoIP and Instant Messaging client.
 *
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package net.java.sip.communicator.impl.protocol.jabber.extensions.jingle;

import org.jivesoftware.smack.provider.*;
import org.xmlpull.v1.*;

/**
 * The <tt>RedirectProvider</tt> parses "redirect" elements into {@link
 * RedirectPacketExtension} instances.
 *
 * @author Sebastien Vincent
 */
public class RedirectProvider
    implements PacketExtensionProvider
{
    /**
     * Parses a reason extension sub-packet and creates a {@link
     * RedirectPacketExtension} instance. At the beginning of the method call,
     * the xml parser will be positioned on the opening element of the packet
     * extension. As required by the smack API, at the end of the method call,
     * the parser will be positioned on the closing element of the packet
     * extension.
     *
     * @param parser an XML parser positioned at the opening <tt>redirect</tt>
     * element.
     *
     * @return a new {@link RedirectPacketExtension} instance.
     * @throws java.lang.Exception if an error occurs parsing the XML.
     */
    public RedirectPacketExtension parseExtension(XmlPullParser parser)
        throws Exception
    {
        String text = null;
        boolean done = false;
        int eventType;

        text = parseText(parser);

        while (!done)
        {
            eventType = parser.next();

            if (eventType == XmlPullParser.START_TAG)
            {
            }
            else if (eventType == XmlPullParser.END_TAG)
            {
                if (parser.getName().equals(
                    RedirectPacketExtension.ELEMENT_NAME))
                {
                    done = true;
                }
            }
        }

        RedirectPacketExtension redirectExt
            = new RedirectPacketExtension();
        redirectExt.setText(text);
        redirectExt.setRedir(text);
        return redirectExt;
    }

    /**
     * Returns the content of the next {@link XmlPullParser#TEXT} element that
     * we encounter in <tt>parser</tt>.
     *
     * @param parser the parse that we'll be probing for text.
     *
     * @return the content of the next {@link XmlPullParser#TEXT} element we
     * come across or <tt>null</tt> if we encounter a closing tag first.
     *
     * @throws java.lang.Exception if an error occurs parsing the XML.
     */
    public String parseText(XmlPullParser parser)
        throws Exception
    {
        boolean done = false;

        int eventType;
        String text = null;

        while (!done)
        {
            eventType = parser.next();

            if (eventType == XmlPullParser.TEXT)
            {
                text = parser.getText();
                done = true;
            }
            else if (eventType == XmlPullParser.END_TAG)
            {
                done = true;
            }
        }

        return text;
    }
}
