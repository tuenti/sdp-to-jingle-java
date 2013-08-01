package com.tuenti.protocol.sdp;

import net.java.sip.communicator.impl.protocol.jabber.extensions.jingle.*;
import net.sourceforge.jsdp.*;
import org.jivesoftware.smack.packet.IQ;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Converts a PeerConnection SDP Message to Jingle and vice-versa.
 *
 * Copyright (c) Tuenti Technologies. All rights reserved.
 *
 * @author Wijnand Warren <wwarren@tuenti.com>
 * @author Manuel Peinado Gallego <mpeinado@tuenti.com>
 */
public class SdpToJingle {

	/**
	 * Creates a brand new {@link SessionDescription} object.
	 *
	 * @param sid The session ID to use.
	 * @return A new {@link SessionDescription} object.
	 *
	 * @throws SDPException When a new {@link Origin} can't be created.
	 * @throws UnknownHostException When local host can't be resolved to an address.
	 */
	private static SessionDescription getNewSessionDescription(final String sid) throws SDPException, UnknownHostException {
		Version version = Version.parse("v=0");
		long ntpTime = Time.getNTP(new Date());
		/**
		 * Passing in a fake name, all other params are the same as the {@link Origin#Origin}. Name is not that
		 * important as it doesn't exist in the Jingle IQ.
		 */
		Origin origin = new Origin("ProfessorFarnsworth", ntpTime, ntpTime, InetAddress.getLocalHost().getHostName());

		origin.setSessionID(Long.parseLong(sid));
		SessionName sessionName = new SessionName();
		TimeDescription timeDescription = new TimeDescription();

		return new SessionDescription(version, origin, sessionName, timeDescription);
	}

	/**
	 * Creates a new {@link IceUdpTransportPacketExtension} based on the passed in {@link Attribute}[].
	 *
	 * @param candidateAttrs {@link Attribute}[] - List of candidates.
	 * @return A new {@link IceUdpTransportPacketExtension}.
	 */
	private static IceUdpTransportPacketExtension getIceUdpTransportPacketExtension(final Attribute[] candidateAttrs) {
		CandidatePacketExtension candidateExtension = null;
		IceUdpTransportPacketExtension iceUdpExtension = new IceUdpTransportPacketExtension();

		for (Attribute attr : candidateAttrs) {
			String[] params = attr.getValue().split("[ ]");
			candidateExtension = new CandidatePacketExtension();
			candidateExtension.setFoundation(params[0]);
			candidateExtension.setComponent(Integer.parseInt(params[1]));
			candidateExtension.setProtocol(params[2]);
			candidateExtension.setPriority(Long.parseLong(params[3]));
			candidateExtension.setIP(params[4]);
			candidateExtension.setPort(Integer.parseInt(params[5]));

			CandidateType type = CandidateType.valueOf(params[7]);
			candidateExtension.setType(type);
			if (type == CandidateType.host) {
				candidateExtension.setGeneration(Integer.parseInt(params[9]));
			} else {
				candidateExtension.setRelAddr(params[9]);
				candidateExtension.setRelPort(Integer.parseInt(params[11]));
				candidateExtension.setGeneration(Integer.parseInt(params[13]));
			}

			iceUdpExtension.addCandidate(candidateExtension);
		}

		return iceUdpExtension;
	}

	/**
	 * Creates a Jingle "transport-info" IQ based on the passed in {@link SessionDescription}.
	 *
	 * @param sessionDescription {@link SessionDescription} - The {@link SessionDescription} to convert.
	 * @param mediaName String - The "name" to use in the "content" tag.
	 * @return {@link JingleIQ} - A new {@link JingleIQ} containing all the transport candidates.
	 */
	private static JingleIQ createJingleTransportInfo(final SessionDescription sessionDescription,
			final String mediaName) {

		JingleIQ result = new JingleIQ();
		result.setType(IQ.Type.SET);
		Origin origin = sessionDescription.getOrigin();
		result.setSID(Long.toString(origin.getSessionID()));

		ContentPacketExtension content = new ContentPacketExtension();
		content.setName(mediaName);

		try {
			result.setAction(JingleAction.parseString(JingleAction.TRANSPORT_INFO.toString()));
			Attribute[] candidateAttributes = sessionDescription.getAttributes("candidate");
			// TODO: What about TCP?
			IceUdpTransportPacketExtension iceUdpExtension = getIceUdpTransportPacketExtension(candidateAttributes);

			content.addChildExtension(iceUdpExtension);
			result.addContent(content);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			result = null;
		}

		return result;
	}

	/**
	 * Creates an SDP object from a Jingle Stanza.
	 *
	 * @param jingle JingleIQ - The Jingle stanza to convert.
	 * @return SessionDescription - Converted SDP object.
	 */
	// Things that remain to be done:
	//  * Generate the "crypto" line from the <encription><crypto> element.
	//  * Generate the "ssrc" lines from the <streams> element.
	public static SessionDescription sdpFromJingle(JingleIQ jingle) {
		try {
			SessionDescription result = getNewSessionDescription(jingle.getSID());
			List<ContentPacketExtension> contents = jingle.getContentList();

			// "a=group:BUNDLE audio video"
			StringBuilder valueBuilder = new StringBuilder("BUNDLE");
			for (ContentPacketExtension content : contents) {
				valueBuilder.append(" ").append(content.getName());
			}
			Attribute attr = new Attribute("group", valueBuilder.toString());
			result.addAttribute(attr);

			for (ContentPacketExtension content : contents) {

				// "m=audio 36798 RTP/AVPF 103 104 110 107 9 102 108 0 8 106 105 13 127 126\r\n"
				String contentType = content.getName();

				Media media;
				List<RtpDescriptionPacketExtension> descriptionExts = content.getChildExtensionsOfType(RtpDescriptionPacketExtension.class);
				RtpDescriptionPacketExtension descriptionExt = descriptionExts.get(0);
				List<PayloadTypePacketExtension> payloadExts = descriptionExt.getChildExtensionsOfType(PayloadTypePacketExtension.class);
				if (payloadExts.size() > 0) {
					media = new Media(contentType, 123456789, descriptionExt.getProfile(),
									  Integer.toString(payloadExts.get(0).getID()));
					for (int i = 1, n = payloadExts.size(); i < n; ++i) {
						media.addMediaFormat(Integer.toString(payloadExts.get(i).getID()));
					}
				} else {
					throw new RuntimeException("No media format");
				}
				MediaDescription mediaDescription = new MediaDescription(media);

				List<RawUdpTransportPacketExtension> rawUdpExts = content.getChildExtensionsOfType(RawUdpTransportPacketExtension.class);
				RawUdpTransportPacketExtension firstRawUdpExt = rawUdpExts.get(0);
				CandidatePacketExtension candidateExt = firstRawUdpExt.getChildExtensionsOfType(CandidatePacketExtension.class).get(0);

				// "c=IN IP4 172.22.76.221"
				Connection connection = new Connection(candidateExt.getIP());
				mediaDescription.setConnection(connection);

				// "a=rtcp:36798 IN IP4 172.22.76.221\n"
				Attribute rawUdpAttr = new Attribute("rtcp", candidateExt.getPort() + " IN IP4 " + candidateExt.getIP());
				mediaDescription.addAttribute(rawUdpAttr);

				List<IceUdpTransportPacketExtension> iceUdpExts = content.getChildExtensionsOfType(IceUdpTransportPacketExtension.class);
				iceUdpExts = Utils.filterByClass(iceUdpExts, IceUdpTransportPacketExtension.class);
				for (IceUdpTransportPacketExtension iceUdpExt : iceUdpExts) {
					// "a=candidate:1 2 udp 1 172.22.76.221 47216 typ host generation 0"
					// There can (or better should) be multiple candidate tags inside one transport tag.
					for (CandidatePacketExtension candidateExtension : iceUdpExt.getChildExtensionsOfType(CandidatePacketExtension.class)) {
						String value = candidateExtension.getFoundation()
								+ " " + candidateExtension.getComponent()
								+ " " + candidateExtension.getProtocol()
								+ " " + candidateExtension.getPriority()
								+ " " + candidateExtension.getIP()
								+ " " + candidateExtension.getPort()
								+ " typ " + candidateExtension.getType();

						if (candidateExtension.getType() == CandidateType.host) {
							value += " generation " + candidateExtension.getGeneration();
						} else {
							value += " raddr " + candidateExtension.getRelAddr()
									+ " rport " + candidateExtension.getRelPort()
									+ " generation " + candidateExtension.getGeneration();
						}

						Attribute iceUdpAttr = new Attribute("candidate", value);
						mediaDescription.addAttribute(iceUdpAttr);
					}
				}

				// "a=sendrecv"
				Attribute sendrecvAttr = new Attribute("sendrecv");
				mediaDescription.addAttribute(sendrecvAttr);

				// "a=mid:audio"
				Attribute midAttr = new Attribute("mid", contentType);
				mediaDescription.addAttribute(midAttr);

				// "a=rtpmap:106 CN/32000"
				payloadExts = descriptionExt.getChildExtensionsOfType(PayloadTypePacketExtension.class);
				for (PayloadTypePacketExtension payloadExt : payloadExts) {
					String value = payloadExt.getID()
							+ " " + payloadExt.getName()
							+ "/" + payloadExt.getClockrate();
					Attribute rtpmapAttr = new Attribute("rtpmap", value);
					mediaDescription.addAttribute(rtpmapAttr);
				}

				// "a=crypto:0 AES_CM_128_HMAC_SHA1_32 inline:keNcG3HezSNID7LmfDa9J4lfdUL8W1F7TNJKcbuy"
				List<EncryptionPacketExtension> encryptionExts = descriptionExt.getChildExtensionsOfType(EncryptionPacketExtension.class);
				if (encryptionExts.size() > 0) {
					EncryptionPacketExtension encryptionExt = encryptionExts.get(0);
					List<CryptoPacketExtension> cryptoExts = encryptionExt.getCryptoList();
					for (CryptoPacketExtension cryptoExt : cryptoExts) {
						String value = cryptoExt.getTag()
								+ " " + cryptoExt.getCryptoSuite()
								+ " " + cryptoExt.getKeyParams();
						Attribute cryptoAttr = new Attribute("crypto", value);
						mediaDescription.addAttribute(cryptoAttr);
					}
				}

				// "a=rtcp-mux"
				List<RtcpMuxExtension> rtcpMuxExts = descriptionExt.getChildExtensionsOfType(RtcpMuxExtension.class);
				if (!rtcpMuxExts.isEmpty()) {
					Attribute muxAttr = new Attribute("rtcp-mux");
					mediaDescription.addAttribute(muxAttr);
				}

				// "a=ssrc:2570980487 cname:hsWuSQJxx7przmb8"
				// "a=ssrc:2570980487 mslabel:stream_label"
				// "a=ssrc:2570980487 label:audio_label"
				List<StreamsPacketExtension> streamsExts = descriptionExt.getChildExtensionsOfType(StreamsPacketExtension.class);
				if (!streamsExts.isEmpty()) {
					List<StreamPacketExtension> streamExts = streamsExts.get(0).getStreamList();
					StreamPacketExtension streamExt = streamExts.get(0);
					List<String> attrNames = streamExt.getAttributeNames();
					SsrcPacketExtension ssrcExt = streamExt.getSsrc();
					for (String attrName : attrNames) {
						String value = ssrcExt.getText() + " " + attrName + ":" + streamExt.getAttributeAsString(attrName);
						attr = new Attribute("ssrc", value);
						mediaDescription.addAttribute(attr);
					}
				}

				result.addMediaDescription(mediaDescription);
			}
			return result;
		} catch (SDPException e) {
			e.printStackTrace();
			return null;
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Creates a Jingle stanza from SDP.
	 *
	 * @param sdp SessionDescription - The SDP object to convert to Jingle.
	 * @return JingleIQ - Converted Jingle stanza.
	 */
	// Things that remain to be done:
	//  * Generate the <encription><crypto> element from the "crypto" line of SDP.
	//  * Generate the <streams><stream> elements from the "ssrc" lines of SDP.
	public static JingleIQ jingleFromSdp(SessionDescription sdp) {
		JingleIQ result = new JingleIQ();
		result.setType(IQ.Type.SET);
		
		Origin origin = sdp.getOrigin();
		result.setSID(Long.toString(origin.getSessionID()));

		MediaDescription[] mediaDescriptions = sdp.getMediaDescriptions();
		for (MediaDescription mediaDescription : mediaDescriptions) {
			ContentPacketExtension content = new ContentPacketExtension();

			Connection connection = mediaDescription.getConnection();
			String netType = connection.getNetType();
			if (netType.equals("IN")) {
				content.setCreator(ContentPacketExtension.CreatorEnum.initiator);
			} else {
				throw new RuntimeException("Unsupported mediaDescription connection type '" + netType + "'");
			}

			Media media = mediaDescription.getMedia();
			content.setName(media.getMediaType());

			RtpDescriptionPacketExtension rtpExt = new RtpDescriptionPacketExtension();
			rtpExt.setMedia(media.getMediaType());
			rtpExt.setProfile(media.getProtocol());

			content.addChildExtension(rtpExt);

			Attribute[] rtpmapAttrs = mediaDescription.getAttributes("rtpmap");
			for (Attribute attr : rtpmapAttrs) {
				PayloadTypePacketExtension payloadExt = new PayloadTypePacketExtension();
				String[] params = attr.getValue().split("[ /]");
				payloadExt.setId(Integer.parseInt(params[0]));
				payloadExt.setName(params[1]);
				StringBuilder clockRate = new StringBuilder(params[2]);
				if (params.length > 3) {
					clockRate.append("/").append(params[3]);
				}
				payloadExt.setClockrate(clockRate.toString());
				rtpExt.addChildExtension(payloadExt);
			}

			// <encryption><crypto /><crypto /></encription>
			Attribute[] cryptoAttrs = mediaDescription.getAttributes("crypto");
			EncryptionPacketExtension encryptionExt = null;
			if (cryptoAttrs != null && cryptoAttrs.length > 0) {
				encryptionExt = new EncryptionPacketExtension();
				encryptionExt.setRequired(true);
				rtpExt.addChildExtension(encryptionExt);
			}
			for (Attribute attr : cryptoAttrs) {
				String[] params = attr.getValue().split(" ");
				CryptoPacketExtension cryptoExt = new CryptoPacketExtension();
				cryptoExt.setTag(params[0]);
				cryptoExt.setCryptoSuite(params[1]);
				cryptoExt.setKeyParams(params[2]);
				encryptionExt.addCrypto(cryptoExt);
			}

			// <rtcp-mux />
			Attribute[] rtcpMuxAttrs = mediaDescription.getAttributes("rtcp-mux");
			if (rtcpMuxAttrs.length > 0) {
				RtcpMuxExtension rtpcMuxExt = new RtcpMuxExtension();
				rtpExt.addChildExtension(rtpcMuxExt);
			}

			// <streams><stream><ssrc>
			Attribute[] ssrcAttrs = mediaDescription.getAttributes("ssrc");
			StreamsPacketExtension streamsExt = null;
			if (cryptoAttrs != null && cryptoAttrs.length > 0) {
				streamsExt = new StreamsPacketExtension();
				rtpExt.addChildExtension(streamsExt);
			}
			Map<String, Map<String, String>> streams = new HashMap<String, Map<String, String>>();
			for (Attribute attr : ssrcAttrs) {
				String[] params = attr.getValue().split(" ");
				Map<String, String> map = streams.get(params[0]);
				if (map == null) {
					map = new HashMap<String, String>();
					streams.put(params[0], map);
				}
				String[] kv = params[1].split(":");
				map.put(kv[0], kv[1]);
			}
			for (Map.Entry<String, Map<String, String>> stream : streams.entrySet()) {
				StreamPacketExtension streamExt = new StreamPacketExtension();
				Map<String, String> streamAttrs = stream.getValue();
				for (Map.Entry<String, String> streamAttr : streamAttrs.entrySet()) {
					streamExt.setAttribute(streamAttr.getKey(), streamAttr.getValue());
				}
				SsrcPacketExtension ssrcExt = new SsrcPacketExtension();
				ssrcExt.setText(stream.getKey());
				streamExt.setSsrc(ssrcExt);
				streamsExt.addStream(streamExt);
			}

			RawUdpTransportPacketExtension rawUdpExt = new RawUdpTransportPacketExtension();
			CandidatePacketExtension candidateExt = new CandidatePacketExtension();
			candidateExt.setIP(connection.getAddress());
			candidateExt.setPort(media.getPort());
			candidateExt.setGeneration(0);
			rawUdpExt.addCandidate(candidateExt);
			content.addChildExtension(rawUdpExt);

			// TODO: What about TCP?
			Attribute[] candidateAttributes = mediaDescription.getAttributes("candidate");
			IceUdpTransportPacketExtension iceUdpExt = getIceUdpTransportPacketExtension(candidateAttributes);
			content.addChildExtension(iceUdpExt);

			result.addContent(content);
		}
		return result;
	}

	/**
	 * Creates a Jingle "transport-info" IQ based on a passed in SDP stub of ICE candidates.
	 * @see SdpToJingleTest#testJingeIceCandidatesFromSdpStub() for details.
	 *
	 * @param candidateList List<String> - List of SDP ICE candidates.
	 * @param sid String - The Jingle session ID.
	 * @param mediaName String - The "name" to use in the "content" tag.
	 * @return {@link JingleIQ} - A new {@link JingleIQ} containing all the transport candidates.
	 */
	public static JingleIQ transportInfoFromSdpStub(final List<String> candidateList, final String sid,
			final String mediaName) {

		SessionDescription sessionDescription = null;
		JingleIQ result = null;
		String candidatePrefix = "candidate:";

		// First, create a session description object.
		try {
			sessionDescription = getNewSessionDescription(sid);

			// Add candidates.
			for (String candidate : candidateList) {
				String[] keyValue = candidate.split(candidatePrefix);
				Attribute field = new Attribute("candidate", keyValue[1].trim());
				sessionDescription.addAttribute(field);
			}
		} catch (SDPException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

		// Convert SDP to Jingle.
		if (sessionDescription != null) {
			result = createJingleTransportInfo(sessionDescription, mediaName);
		}

		return result;
	}
}
