package protoc

import protoc.alt.*
public class ZWaveParser {
fun parse(rawFrame : Array<Byte>) {
when(ZWaveFrameType.valueOf(rawFrame[0])) {
is NAK ->{}
is SOF ->{}
is CAN ->{}
is ACK ->{}
else -> {println("[ERROR] no chunk alternative found for value:" + rawFrame[0])}
}
}
}
