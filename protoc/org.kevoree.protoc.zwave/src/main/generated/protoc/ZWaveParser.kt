package protoc

import protoc.alt.*
public class ZWaveParser {
fun parse(rawFrame : Array<Byte>) {
when(ZWaveFrameType.valueOf(rawFrame[0])) {
ZWaveFrameType.ACK ->{}
ZWaveFrameType.SOF ->{}
ZWaveFrameType.CAN ->{}
ZWaveFrameType.NAK ->{}
else -> {println("[ERROR] no chunk alternative found for value:" + rawFrame[0])}
}
}
}
