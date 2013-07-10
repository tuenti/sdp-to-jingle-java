package com.tuenti.protocol.sdp;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Tests for {@link SdpToJingle}.
 *
 * @author Wijnand Warren <wwarren@tuenti.com>
 */
@RunWith(JUnit4.class)
public class SdpToJingleTest {

	private static final String SDP_TEST_STRING = "v=0\r\n"
			+ "o=- 123 1 IN IP4 127.0.0.1\r\n"
			+ "s=\r\n"
			+ "t=0 0\r\n"
			+ "a=group:BUNDLE audio video\r\n"
			+ "m=audio 36798 RTP/AVPF 103 104 110 107 9 102 108 0 8 106 105 13 127 126\r\n"
			+ "c=IN IP4 172.22.76.221\r\n"
			+ "a=rtcp:36798 IN IP4 172.22.76.221\r\n"
			+ "a=candidate:1 2 udp 1 172.22.76.221 47216 typ host generation 0\r\n"
			+ "a=candidate:1 1 udp 1 172.22.76.221 48235 typ host generation 0\r\n"
			+ "a=candidate:1 2 udp 0.9 172.22.76.221 36798 typ srflx generation 0\r\n"
			+ "a=candidate:1 1 udp 0.9 172.22.76.221 50102 typ srflx generation 0\r\n"
			+ "a=mid:audio\r\n"
			+ "a=rtcp-mux\r\n"
			+ "a=crypto:0 AES_CM_128_HMAC_SHA1_32 inline:keNcG3HezSNID7LmfDa9J4lfdUL8W1F7TNJKcbuy \r\n"
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
			+ "a=candidate:1 2 udp 0.9 172.22.76.221 46128 typ srflx generation 0\r\n"
			+ "a=candidate:1 1 udp 0.9 172.22.76.221 39456 typ srflx generation 0\r\n"
			+ "a=mid:video\r\n"
			+ "a=rtcp-mux\r\n"
			+ "a=crypto:0 AES_CM_128_HMAC_SHA1_80 inline:5ydJsA+FZVpAyqJMT/nW/UW+tcOmDvXJh/pPhNRe \r\n"
			+ "a=rtpmap:100 VP8/90000\r\n"
			+ "a=rtpmap:101 red/90000\r\n"
			+ "a=rtpmap:102 ulpfec/90000\r\n"
			+ "a=ssrc:43633328 cname:hsWuSQJxx7przmb8\r\n"
			+ "a=ssrc:43633328 mslabel:stream_label\r\n"
			+ "a=ssrc:43633328 label:video_label\r\n";

	private static final String JINGLE_TEST_STRING = "<jingle sid='123'><content creator='initiator' name='video'>"
			+ "<description xmlns='urn:xmpp:jingle:apps:video-rtp' profile='RTP/AVPF' media='video'><payload-type "
			+ "id='100' name='VP8' clockrate='90000'/><payload-type id='101' name='red' clockrate='90000'/><payload-type"
			+ " id='102' name='ulpfec' clockrate='90000'/><encryption required='1'><crypto "
			+ "crypto-suite='AES_CM_128_HMAC_SHA1_80' key-params='inline:5ydJsA+FZVpAyqJMT/nW/UW+tcOmDvXJh/pPhNRe' "
			+ "session-params='' tag='0'/></encryption><streams><stream cname='hsWuSQJxx7przmb8' mslabel='stream_label' "
			+ "label='video_label'><ssrc>43633328</ssrc></stream></streams></description><transport "
			+ "xmlns='urn:xmpp:jingle:transports:raw-udp:1'><candidate ip='172.22.76.221' port='39456' "
			+ "generation='0'/></transport><transport xmlns='urn:xmpp:jingle:transports:ice-udp:1'><candidate "
			+ "component='2' foundation='1' protocol='udp' priority='1' ip='172.22.76.221' port='40550' type='host' "
			+ "generation='0'/><candidate component='1' foundation='1' protocol='udp' priority='1' ip='172.22.76.221' "
			+ "port='53441' type='host' generation='0'/><candidate component='2' foundation='1' protocol='udp' "
			+ "priority='0.9' ip='172.22.76.221' port='46128' type='srflx' generation='0'/><candidate component='1' "
			+ "foundation='1' protocol='udp' priority='0.9' ip='172.22.76.221' port='39456' type='srflx' "
			+ "generation='0'/></transport></content><content creator='initiator' name='audio'><description "
			+ "xmlns='urn:xmpp:jingle:apps:rtp:1' profile='RTP/AVPF' media='audio'><payload-type id='103' name='ISAC' "
			+ "clockrate='16000'/><payload-type id='104' name='ISAC' clockrate='32000'/><payload-type id='110' "
			+ "name='CELT' clockrate='32000'/><payload-type id='107' name='speex' clockrate='16000'/><payload-type "
			+ "id='9' name='G722' clockrate='16000'/><payload-type id='102' name='ILBC' clockrate='8000'/><payload-type "
			+ "id='108' name='speex' clockrate='8000'/><payload-type id='0' name='PCMU' clockrate='8000'/><payload-type "
			+ "id='8' name='PCMA' clockrate='8000'/><payload-type id='106' name='CN' clockrate='32000'/><payload-type "
			+ "id='105' name='CN' clockrate='16000'/><payload-type id='13' name='CN' clockrate='8000'/><payload-type "
			+ "id='127' name='red' clockrate='8000'/><payload-type id='126' name='telephone-event' "
			+ "clockrate='8000'/><encryption required='1'><crypto crypto-suite='AES_CM_128_HMAC_SHA1_32' "
			+ "key-params='inline:keNcG3HezSNID7LmfDa9J4lfdUL8W1F7TNJKcbuy' session-params='' "
			+ "tag='0'/></encryption><streams><stream cname='hsWuSQJxx7przmb8' mslabel='stream_label' "
			+ "label='audio_label'><ssrc>2570980487</ssrc></stream></streams></description><transport "
			+ "xmlns='urn:xmpp:jingle:transports:raw-udp:1'><candidate ip='172.22.76.221' port='36798' "
			+ "generation='0'/></transport><transport xmlns='urn:xmpp:jingle:transports:ice-udp:1'><candidate "
			+ "component='2' foundation='1' protocol='udp' priority='1' ip='172.22.76.221' port='47216' type='host' "
			+ "generation='0'/><candidate component='1' foundation='1' protocol='udp' priority='1' ip='172.22.76.221' "
			+ "port='48235' type='host' generation='0'/><candidate component='2' foundation='1' protocol='udp' "
			+ "priority='0.9' ip='172.22.76.221' port='36798' type='srflx' generation='0'/><candidate component='1' "
			+ "foundation='1' protocol='udp' priority='0.9' ip='172.22.76.221' port='50102' type='srflx' "
			+ "generation='0'/></transport></content></jingle>";

	private SdpToJingle sdpToJingle;

	/**
	 * Sets up this test.
	 */
	@Before
	public void setUp() {
		sdpToJingle = new SdpToJingle();
	}

	/**
	 * Tears down this test.
	 */
	@After
	public void tearDown() {
		sdpToJingle = null;
	}

	/**
	 * Tests if converting an SDP string results in the correct Jingle stanza string.
	 */
	@Test
	public void testCreateJingleStanza() {
		String message = "The converted SDP string is not the same as the expected Jingle stanza!";
		String jingleStanza = sdpToJingle.createJingleStanza(SDP_TEST_STRING);
		Assert.assertEquals(message, JINGLE_TEST_STRING, jingleStanza);
	}

	/**
	 * Tests if converting an SDP string results in the correct Jingle stanza string.
	 */
	@Test
	public void testParseJingleStanza() {
		String message = "The converted Jingle stanza is not the same as the expected SDP string!";
		String sdp = sdpToJingle.parseJingleStanza(JINGLE_TEST_STRING);
		Assert.assertEquals(message, SDP_TEST_STRING, sdp);
	}

}
