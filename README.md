sdp-to-jingle-java
==================
The project started as a java port of the [sdpToJingle](https://github.com/mweibel/sdpToJingle) project. At the moment it is a lot more RFC compliant than its JS counterpart.
The intention is to create a library that is able to convert [SDP](http://en.wikipedia.org/wiki/Session_Description_Protocol) into [Jingle](http://xmpp.org/extensions/xep-0166.html) messages.

This will come in handy if you want to use Jingle as a signaling layer when using [WebRTC](http://www.webrtc.org/) with [PeerConnection](https://code.google.com/p/libjingle/source/browse/trunk/talk/app/webrtc/java/src/org/webrtc/PeerConnection.java)

#Usage
Using the library is pretty easy, just have a look at the [tests](https://github.com/wjwarren/sdp-to-jingle-java/blob/master/src/test/java/com/tuenti/protocol/sdp/SdpToJingleTest.java).
