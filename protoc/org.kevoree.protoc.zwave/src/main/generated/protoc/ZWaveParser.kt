package protoc

import protoc.alt.*
import protoc.msg.*
public class ZWaveParser {

    fun parse(rawFrame : Array<Byte>) : ZWaveMessage? {
        return parseZWaveFrameType(rawFrame)
    }

    fun parseZWaveFrameType(rawFrame : Array<Byte>) : ZWaveMessage? {
        when(ZWaveFrameType.valueOf(rawFrame[0])) {
            ZWaveFrameType.ACK ->{
                return ACKMessage.fromBytes(rawFrame)
            }
            ZWaveFrameType.SOF ->{
                return SOFMessage.fromBytes(rawFrame)
            }
            ZWaveFrameType.NAK ->{
                return NAKMessage.fromBytes(rawFrame)
            }
            ZWaveFrameType.CAN ->{
                return CANMessage.fromBytes(rawFrame)
            }
            else -> {
                println("[ERROR] no chunk alternative found for value:" + rawFrame[0])
                return null
            }
        }
    }
}
