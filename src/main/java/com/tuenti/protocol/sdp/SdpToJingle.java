package com.tuenti.protocol.sdp;

import net.java.sip.communicator.impl.protocol.jabber.extensions.jingle.*;
import net.sourceforge.jsdp.*;

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
			Version version = Version.parse("v=0");
			long ntpTime = Time.getNTP(new Date());
			/**
			 * Passing in a fake name, all other params are the same as the {@link Origin#Origin}. Name is not that
			 * important as it doesn't exist in the Jingle IQ.
			 */
			Origin origin = new Origin("ProfessorFarnsworth", ntpTime, ntpTime, InetAddress.getLocalHost().getHostName());

			origin.setSessionID(Long.parseLong(jingle.getSID()));
			SessionName sessionName = new SessionName();
			TimeDescription timeDescription = new TimeDescription();

			SessionDescription result = new SessionDescription(version, origin, sessionName, timeDescription);

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
					candidateExt = iceUdpExt.getChildExtensionsOfType(CandidatePacketExtension.class).get(0);
					String value = candidateExt.getFoundation()
							+ " " + candidateExt.getComponent()
							+ " " + candidateExt.getProtocol()
							+ " " + candidateExt.getPriority()
							+ " " + candidateExt.getIP()
							+ " " + candidateExt.getPort()
							+ " typ " + candidateExt.getType()
							+ " generation " + candidateExt.getGeneration();
					Attribute iceUdpAttr = new Attribute("candidate", value);
					mediaDescription.addAttribute(iceUdpAttr);
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

			Attribute[] candidateAttrs = mediaDescription.getAttributes("candidate");
			for (Attribute attr : candidateAttrs) {
				IceUdpTransportPacketExtension iceUdpExt = new IceUdpTransportPacketExtension();
				String[] params = attr.getValue().split("[ ]");
				candidateExt = new CandidatePacketExtension();
				candidateExt.setFoundation(Integer.parseInt(params[0]));
				candidateExt.setComponent(Integer.parseInt(params[1]));
				candidateExt.setProtocol(params[2]);
				candidateExt.setPriority(Long.parseLong(params[3]));
				candidateExt.setIP(params[4]);
				candidateExt.setPort(Integer.parseInt(params[5]));
				candidateExt.setType(CandidateType.valueOf(params[7]));
				candidateExt.setGeneration(Integer.parseInt(params[9]));
				iceUdpExt.addCandidate(candidateExt);
				content.addChildExtension(iceUdpExt);
			}
			result.addContent(content);
		}
		return result;
	}
}
