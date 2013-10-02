gradle clean build
mvn install:install-file -Dfile=build/libs/sdp-to-jingle-java.jar  -DgroupId=com.tuenti.protocol.sdp -DartifactId=sdp-to-jingle-java -Dpackaging=jar -Dversion=0.1-SNAPSHOT
