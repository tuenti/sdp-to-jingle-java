package com.tuenti.protocol.sdp;

import net.java.sip.communicator.impl.protocol.jabber.extensions.jingle.*;
import net.sourceforge.jsdp.*;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.List;

/**
 * Unit tests for the SdpToJingle class.
 *
 * Copyright (c) Tuenti Technologies. All rights reserved.
 *
 * @author Wijnand Warren <wwarren@tuenti.com>
 * @author Manuel Peinado Gallego <mpeinado@tuenti.com>
 */
@RunWith(JUnit4.class)
public class SdpToJingleTest {
	private static final String SAMPLE_SDP_MESSAGE = "v=0\r\n"
			+ "o=- 123 1 IN IP4 127.0.0.1\r\n"
			+ "s=session\r\n"
			+ "t=0 0\r\n"
			+ "a=group:BUNDLE audio video\r\n"
			+ "m=audio 36798 RTP/AVPF 103 104 110 107 9 102 108 0 8 106 105 13 127 126\r\n"
			+ "c=IN IP4 172.22.76.221\r\n"
			+ "a=rtcp:36798 IN IP4 172.22.76.221\r\n"
			+ "a=candidate:1 2 udp 1 172.22.76.221 47216 typ host generation 0\r\n"
			+ "a=candidate:1 1 udp 1 172.22.76.221 48235 typ host generation 0\r\n"
			+ "a=candidate:1 2 udp 2 172.22.76.221 36798 typ srflx generation 0\r\n"
			+ "a=candidate:1 1 udp 2 172.22.76.221 50102 typ srflx generation 0\r\n"
			+ "a=sendrecv\r\n"
			+ "a=mid:audio\r\n"
			+ "{{RTCP-MUX}}" // placeholder for a=rtcp-mux\r\n
			+ "a=crypto:0 AES_CM_128_HMAC_SHA1_32 inline:keNcG3HezSNID7LmfDa9J4lfdUL8W1F7TNJKcbuy \r\n"
			+ "a=rtpmap:111 opus/48000/2\r\n"
			+ "a=rtpmap:103 ISAC/16000\r\n"
			+ "a=rtpmap:104 ISAC/32000\r\n"
			+ "a=rtpmap:110 CELT/32000\r\n"
			+ "a=rtpmap:107 speex/16000\r\n"
			+ "a=rtpmap:9 G722/16000\r\n"
			+ "a=rtpmap:102 ILBC/8000\r\n"
			+ "a=rtpmap:108 speex/8000\r\n"
			+ "a=rtpmap:0 PCMU/8000\r\n"
			+ "a=rtpmap:8 PCMA/8000\r\n"
			+ "a=rtpmap:106 CN/32000\r\n"
			+ "a=rtpmap:105 CN/16000\r\n"
			+ "a=rtpmap:13 CN/8000\r\n"
			+ "a=rtpmap:127 red/8000\r\n"
			+ "a=rtpmap:126 telephone-event/8000\r\n"
			+ "a=ssrc:2570980487 cname:hsWuSQJxx7przmb8\r\n"
			+ "a=ssrc:2570980487 mslabel:stream_label\r\n"
			+ "a=ssrc:2570980487 label:audio_label\r\n"
			+ "m=video 39456 RTP/AVPF 100 101 102\r\n"
			+ "c=IN IP4 172.22.76.221\r\n"
			+ "a=rtcp:39456 IN IP4 172.22.76.221\r\n"
			+ "a=candidate:1 2 udp 1 172.22.76.221 40550 typ host generation 0\r\n"
			+ "a=candidate:1 1 udp 1 172.22.76.221 53441 typ host generation 0\r\n"
			+ "a=candidate:1 2 udp 2 172.22.76.221 46128 typ srflx generation 0\r\n"
			+ "a=candidate:1 1 udp 2 172.22.76.221 39456 typ srflx generation 0\r\n"
			+ "a=sendrecv\r\n"
			+ "a=mid:video\r\n"
			+ "{{RTCP-MUX}}" // placeholder for a=rtcp-mux\r\n
			+ "a=crypto:0 AES_CM_128_HMAC_SHA1_80 inline:5ydJsA+FZVpAyqJMT/nW/UW+tcOmDvXJh/pPhNRe \r\n"
			+ "a=rtpmap:100 VP8/90000\r\n"
			+ "a=rtpmap:101 red/90000\r\n"
			+ "a=rtpmap:102 ulpfec/90000\r\n"
			+ "a=ssrc:43633328 cname:hsWuSQJxx7przmb8\r\n"
			+ "a=ssrc:43633328 mslabel:stream_label\r\n"
			+ "a=ssrc:43633328 label:video_label\r\n";

	private SessionDescription sdp;

	/**
	 * Cleans up after the test has been run.
	 */
	@After
	public void tearDown() {
		sdp = null;
	}

	public void prepare() {
		prepare(true);
	}

	/**
	 * Prepares the {@link SessionDescription} object to be used in the tests.
	 *
	 * @param includeRtcpMuxAttr boolean - Whether or not to include the RTCP MUX attribute.
	 */
	public void prepare(boolean includeRtcpMuxAttr) {
		try {
			String sdpMessage = SAMPLE_SDP_MESSAGE.replace("{{RTCP-MUX}}", includeRtcpMuxAttr ? "a=rtcp-mux\r\n" : "");
			sdp = SDPFactory.parseSessionDescription(sdpMessage);
		} catch (SDPParseException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Verifies a Jingle IQ.
	 *
	 * @param jingle {@link JingleIQ} - The Jingle IQ to check.
	 */
	private void verifyJingleIq(JingleIQ jingle, boolean checkCandidatePort) {
		Assert.assertNotNull(jingle);

		Assert.assertTrue(jingle.getSID().equals("123"));
		List<ContentPacketExtension> contentList = jingle.getContentList();
		Assert.assertTrue(contentList.size() == 2);
		for (ContentPacketExtension content : contentList) {
			Assert.assertTrue(contentList.get(0).getChildExtensionsOfType(RtpDescriptionPacketExtension.class).size() == 1);
			Assert.assertTrue(content.getCreator() == ContentPacketExtension.CreatorEnum.initiator);
		}

		ContentPacketExtension audio = contentList.get(0);
		Assert.assertTrue(audio.getName().equals("audio"));
		List<RtpDescriptionPacketExtension> packetExts = audio.getChildExtensionsOfType(RtpDescriptionPacketExtension.class);
		Assert.assertTrue(packetExts.size() == 1);
		RtpDescriptionPacketExtension packetExt = packetExts.get(0);
		Assert.assertTrue(packetExt.getMedia().equals("audio"));

		List<PayloadTypePacketExtension> payloadExts = packetExt.getChildExtensionsOfType(PayloadTypePacketExtension.class);
		Assert.assertTrue(payloadExts.size() == 15);
		Assert.assertTrue(payloadExts.get(0).getID() == 111);
		Assert.assertTrue(payloadExts.get(0).getName().equals("opus"));
		Assert.assertTrue(payloadExts.get(0).getClockrate().equals("48000/2"));
		Assert.assertTrue(payloadExts.get(8).getID() == 0);
		Assert.assertTrue(payloadExts.get(8).getName().equals("PCMU"));
		Assert.assertTrue(payloadExts.get(8).getClockrate().equals("8000"));

		List<RawUdpTransportPacketExtension> rawUdpExts = audio.getChildExtensionsOfType(RawUdpTransportPacketExtension.class);
		Assert.assertTrue(rawUdpExts.size() == 1);
		Assert.assertTrue(rawUdpExts.get(0).getCandidateList().size() == 1);
		Assert.assertTrue(rawUdpExts.get(0).getCandidateList().get(0).getIP().equals("172.22.76.221"));
		if (checkCandidatePort) {
			Assert.assertTrue(rawUdpExts.get(0).getCandidateList().get(0).getPort() == 36798);
		}
		Assert.assertTrue(rawUdpExts.get(0).getCandidateList().get(0).getGeneration() == 0);

		List<IceUdpTransportPacketExtension> iceUdpExts = audio.getChildExtensionsOfType(IceUdpTransportPacketExtension.class);
		iceUdpExts = Utils.filterByClass(iceUdpExts, IceUdpTransportPacketExtension.class);
		Assert.assertTrue(iceUdpExts.size() == 4);
		for (int i = 0; i < iceUdpExts.size(); ++i) {
			Assert.assertTrue(iceUdpExts.get(i).getCandidateList().size() == 1);
		}
		verifyCandidateExtension(iceUdpExts.get(1).getCandidateList().get(0), 1, 1, "udp", 1, 0,
				CandidateType.host, "172.22.76.221", 48235);
		verifyCandidateExtension(iceUdpExts.get(2).getCandidateList().get(0), 2, 1, "udp", 2, 0,
				CandidateType.srflx, "172.22.76.221", 36798);

		Assert.assertTrue(contentList.get(1).getName().equals("video"));
		ContentPacketExtension video = contentList.get(1);
		Assert.assertTrue(video.getName().equals("video"));
		packetExts = video.getChildExtensionsOfType(RtpDescriptionPacketExtension.class);
		Assert.assertTrue(packetExts.size() == 1);
		packetExt = packetExts.get(0);
		Assert.assertTrue(packetExt.getMedia().equals("video"));

		payloadExts = packetExt.getChildExtensionsOfType(PayloadTypePacketExtension.class);
		Assert.assertTrue(payloadExts.size() == 3);
		Assert.assertTrue(payloadExts.get(2).getID() == 102);
		Assert.assertTrue(payloadExts.get(2).getName().equals("ulpfec"));
		Assert.assertTrue(payloadExts.get(2).getClockrate().equals("90000"));

		rawUdpExts = video.getChildExtensionsOfType(RawUdpTransportPacketExtension.class);
		Assert.assertTrue(rawUdpExts.size() == 1);
		Assert.assertTrue(rawUdpExts.get(0).getCandidateList().size() == 1);
		Assert.assertTrue(rawUdpExts.get(0).getCandidateList().get(0).getIP().equals("172.22.76.221"));
		if (checkCandidatePort) {
			Assert.assertTrue(rawUdpExts.get(0).getCandidateList().get(0).getPort() == 39456);
		}
		Assert.assertTrue(rawUdpExts.get(0).getCandidateList().get(0).getGeneration() == 0);

		iceUdpExts = video.getChildExtensionsOfType(IceUdpTransportPacketExtension.class);
		iceUdpExts = Utils.filterByClass(iceUdpExts, IceUdpTransportPacketExtension.class);
		Assert.assertTrue(iceUdpExts.size() == 4);
		for (int i = 0; i < iceUdpExts.size(); ++i) {
			Assert.assertTrue(iceUdpExts.get(i).getCandidateList().size() == 1);
		}
		verifyCandidateExtension(iceUdpExts.get(1).getCandidateList().get(0), 1, 1, "udp", 1, 0,
				CandidateType.host, "172.22.76.221", 53441);
		verifyCandidateExtension(iceUdpExts.get(2).getCandidateList().get(0), 2, 1, "udp", 2, 0,
				CandidateType.srflx, "172.22.76.221", 46128);
	}

	private static void verifyCandidateExtension(CandidatePacketExtension candidateExt, int component, int foundation,
												 String protocol, int priority, int generation, CandidateType type,
												 String ip, int port) {
		Assert.assertTrue(candidateExt.getComponent() == component);
		Assert.assertTrue(candidateExt.getFoundation() == foundation);
		Assert.assertTrue(candidateExt.getProtocol().equals(protocol));
		Assert.assertTrue(candidateExt.getPriority() == priority);
		Assert.assertTrue(candidateExt.getGeneration() == generation);
		Assert.assertTrue(candidateExt.getType() == type);
		Assert.assertTrue(candidateExt.getIP().equals(ip));
		Assert.assertTrue(candidateExt.getPort() == port);
	}

	@Test
	public void testSdpToJingle() {
		prepare();
		JingleIQ jingle = SdpToJingle.jingleFromSdp(sdp);
		verifyJingleIq(jingle, true);
	}

	@Test
	public void testJingleToSdp() {
		prepare();
		// SDP => Jingle
		JingleIQ jingle = SdpToJingle.jingleFromSdp(sdp);
		Assert.assertNotNull(jingle);

		// Jingle => SDP
		sdp = SdpToJingle.sdpFromJingle(jingle);
		Assert.assertNotNull(sdp);

		jingle = SdpToJingle.jingleFromSdp(sdp);
		verifyJingleIq(jingle, false);
	}

	@Test
	public void testRtcpMuxPresentInSdp() {
		prepare();
		MediaDescription audio = sdp.getMediaDescriptions()[0];
		Assert.assertTrue(audio.getAttributes("rtcp-mux").length == 1);
	}

	@Test
	public void testRtcpMuxAbsentInSdp() {
		prepare(false);
		MediaDescription audio = sdp.getMediaDescriptions()[0];
		Assert.assertTrue(audio.getAttributes("rtcp-mux").length == 0);
	}

	@Test
	public void testRtcpMuxPresentInJingle() {
		prepare();
		JingleIQ jingle = SdpToJingle.jingleFromSdp(sdp);

		List<ContentPacketExtension> contents = jingle.getContentList();
		for (ContentPacketExtension content : contents) {

			List<RtpDescriptionPacketExtension> descriptionExts = content.getChildExtensionsOfType(RtpDescriptionPacketExtension.class);
			RtpDescriptionPacketExtension descriptionExt = descriptionExts.get(0);
			List<RtcpMuxExtension> rtcpMuxExts = descriptionExt.getChildExtensionsOfType(RtcpMuxExtension.class);
			Assert.assertTrue(rtcpMuxExts.size() == 1);
		}
	}

	@Test
	public void testRtcpMuxAbsentInJingle() {
		prepare(false);
		JingleIQ jingle = SdpToJingle.jingleFromSdp(sdp);

		List<ContentPacketExtension> contents = jingle.getContentList();
		for (ContentPacketExtension content : contents) {

			List<RtpDescriptionPacketExtension> descriptionExts = content.getChildExtensionsOfType(RtpDescriptionPacketExtension.class);
			RtpDescriptionPacketExtension descriptionExt = descriptionExts.get(0);
			List<RtcpMuxExtension> rtcpMuxExts = descriptionExt.getChildExtensionsOfType(RtcpMuxExtension.class);
			Assert.assertTrue(rtcpMuxExts.size() == 0);
		}
	}

	@Test
	public void testCryptoPresentInJingle() {
		prepare();
		JingleIQ jingle = SdpToJingle.jingleFromSdp(sdp);
		List<ContentPacketExtension> contents = jingle.getContentList();
		for (ContentPacketExtension content : contents) {
			List<RtpDescriptionPacketExtension> descriptionExts = content.getChildExtensionsOfType(RtpDescriptionPacketExtension.class);
			RtpDescriptionPacketExtension descriptionExt = descriptionExts.get(0);
			List<EncryptionPacketExtension> encryptionExts = descriptionExt.getChildExtensionsOfType(EncryptionPacketExtension.class);
			Assert.assertTrue(encryptionExts.size() == 1);
			EncryptionPacketExtension encryptionExt = encryptionExts.get(0);
			Assert.assertTrue(encryptionExt.isRequired());
			List<CryptoPacketExtension> cryptoExts = encryptionExt.getCryptoList();
			Assert.assertTrue(cryptoExts.size() == 1);
			CryptoPacketExtension cryptoExt = cryptoExts.get(0);
			Assert.assertTrue(cryptoExt.getCryptoSuite().equals("AES_CM_128_HMAC_SHA1_32") || cryptoExt.getCryptoSuite().equals("AES_CM_128_HMAC_SHA1_80"));
			Assert.assertEquals(cryptoExt.getTag(), "0");
			Assert.assertTrue(cryptoExt.getKeyParams().equals("inline:keNcG3HezSNID7LmfDa9J4lfdUL8W1F7TNJKcbuy")
					|| cryptoExt.getKeyParams().equals("inline:5ydJsA+FZVpAyqJMT/nW/UW+tcOmDvXJh/pPhNRe"));
		}
	}

	@Test
	public void testCryptoPresentInSdp() {
		prepare();
		JingleIQ jingle = SdpToJingle.jingleFromSdp(sdp);
		Assert.assertNotNull(jingle);

		sdp = SdpToJingle.sdpFromJingle(jingle);
		Assert.assertNotNull(sdp);

		String text = sdp.toString();
		Assert.assertTrue(text.contains("a=crypto:0 AES_CM_128_HMAC_SHA1_32 inline:keNcG3HezSNID7LmfDa9J4lfdUL8W1F7TNJKcbuy"));
		Assert.assertTrue(text.contains("a=crypto:0 AES_CM_128_HMAC_SHA1_80 inline:5ydJsA+FZVpAyqJMT/nW/UW+tcOmDvXJh/pPhNRe"));
	}

	@Test
	public void testStreamsPresentInJingle() {
		prepare();
		JingleIQ jingle = SdpToJingle.jingleFromSdp(sdp);
		List<ContentPacketExtension> contents = jingle.getContentList();
		ContentPacketExtension audio = contents.get(0);
		List<RtpDescriptionPacketExtension> descriptionExts = audio.getChildExtensionsOfType(RtpDescriptionPacketExtension.class);
		RtpDescriptionPacketExtension descriptionExt = descriptionExts.get(0);

		List<StreamsPacketExtension> streamsExts = descriptionExt.getChildExtensionsOfType(StreamsPacketExtension.class);
		Assert.assertTrue(streamsExts.size() == 1);
		StreamsPacketExtension streamsExt = streamsExts.get(0);
		List<StreamPacketExtension> streamExts = streamsExt.getStreamList();
		Assert.assertTrue(streamExts.size() == 1);
		StreamPacketExtension streamExt = streamExts.get(0);
		Assert.assertTrue(streamExt.getAttributeNames().size() == 3);
		Assert.assertEquals(streamExt.getAttribute("cname"), "hsWuSQJxx7przmb8");
		Assert.assertEquals(streamExt.getAttribute("mslabel"), "stream_label");
		Assert.assertEquals(streamExt.getAttribute("label"), "audio_label");
		SsrcPacketExtension ssrcExt = streamExt.getSsrc();
		Assert.assertEquals(ssrcExt.getText(), "2570980487");
	}

	@Test
	public void testSsrcPresentInSdp() {
		prepare();
		JingleIQ jingle = SdpToJingle.jingleFromSdp(sdp);
		Assert.assertNotNull(jingle);

		sdp = SdpToJingle.sdpFromJingle(jingle);
		Assert.assertNotNull(sdp);

		String text = sdp.toString();
		Assert.assertTrue(text.contains("a=ssrc:2570980487 cname:hsWuSQJxx7przmb8"));
		Assert.assertTrue(text.contains("a=ssrc:2570980487 mslabel:stream_label"));
		Assert.assertTrue(text.contains("a=ssrc:2570980487 label:audio_label"));

		Assert.assertTrue(text.contains("a=ssrc:43633328 cname:hsWuSQJxx7przmb8"));
		Assert.assertTrue(text.contains("a=ssrc:43633328 mslabel:stream_label"));
		Assert.assertTrue(text.contains("a=ssrc:43633328 label:video_label"));
	}
}