package com.tuenti.protocol.sdp;

/**
 * Converts a PeerConnection SDP Message to Jingle and vice-versa.
 *
 * @author Wijnand Warren <wwarren@tuenti.com>
 */
public class SdpToJingle {

	/**
	 * Creates a Jingle stanza from SDP.
	 *
	 * @param sdp String - The SDP String to convert.
	 * @return String - Jingle stanza.
	 */
	public String createJingleStanza(final String sdp) {
		return "";
	}

	/**
	 * Creates an SDP string from a Jingle Stanza.
	 *
	 * @param jingleStanza String - The Jingle stanza to convert.
	 * @return String - SDP string.
	 */
	public String parseJingleStanza(final String jingleStanza) {
		return "";
	}

}
