sdp-to-jingle-java
==================
The project started as a java port of the [sdpToJingle](https://github.com/mweibel/sdpToJingle) project. At the moment it is a lot more RFC compliant than its JS counterpart.
The intention is to create a library that is able to convert [SDP](http://en.wikipedia.org/wiki/Session_Description_Protocol) into [Jingle](http://xmpp.org/extensions/xep-0166.html) messages.

This will come in handy if you want to use Jingle as a signaling layer when using [WebRTC](http://www.webrtc.org/) with [PeerConnection](https://code.google.com/p/libjingle/source/browse/trunk/talk/app/webrtc/java/src/org/webrtc/PeerConnection.java)

#Usage
Using the library is pretty easy, just have a look at the [tests](https://github.com/wjwarren/sdp-to-jingle-java/blob/master/src/test/java/com/tuenti/protocol/sdp/SdpToJingleTest.java).

## WebRTC example
**Sending** the **SDP offer** created by the PeerConnection class.
```java
// The SDP offer as created by the PeerConnection class.
SessionDescription.description sdp;

// Generate the Jingle IQ based on the offer.
JingleIQ jingleIq = SdpToJingle.jingleFromSdp(sdp);
// Update to, from and action fields.
jingleIq.setTo(to);
jingleIq.setFrom(from);
jingleIq.setAction(action);

// Send the IQ.
```

**Sending** the **ICE candidates** created by the PeerConnection class.
```java
List<String> iceCandidateList;
String sid = "[YourJingleSessionId]";
String mediaName = "[NameToUseInContentTag]";

JingleIQ jingleIq = SdpToJingle.transportInfoFromSdpStub(iceCandidateList, sid, mediaName);
// Update to and from.
jingleIq.setTo(to);
jingleIq.setFrom(from);

// Send the IQ.
```

Parsing an **SDP offer received** from Jingle.
*(This is assuming you've already created your PeerConnection instance that is ready to be used as well as a ready to use answer SdpObserver object.)*
```java
// Convert Jingle to SDP.
String sdpString = SdpToJingle.sdpFromJingle(jingleIq).toString();

// Wrap in a SessionDescription object.
SessionDescription.Type sdpType = SessionDescription.Type.OFFER;
SessionDescription sdp = new SessionDescription(sdpType, sdpString);

// Set as remote description.
peerConnection.setRemoteDescription(answerSdpObserver, sdp);
```

Parsing an **ICE candidate received** from Jingle.
*(This is assuming you've already created your PeerConnection instance and it is ready to be used.)*
```java
// Convert Jingle to SDP.
CandidatePacketExtension iceCandidate;
String sdpLine = SdpToJingle.iceCandidateLineFromJingle(iceCandidate);

// Wrap in an IceCandidate object.
IceCandidate webrtcIceCandidate = new IceCandidate("audio", 0, sdpLine);

// Add to PeerConnection.
peerConnection.addIceCandidate(webrtcIceCandidate);
```

Credits & Contact

sdp-to-jingle-java was created by Tuenti Technologies S.L.. You can follow Tuenti engineering team on Twitter @tuentieng.

License

sdp-to-jingle-java is available under the Apache License, Version 2.0. See LICENSE file for more info.
