package protoc.msg

import protoc.alt.*
import protoc.msg.*
public class CANMessage : ZWaveMessage(){
override var zwaveframetype : ZWaveFrameType = ZWaveFrameType.CAN
class object {
public fun fromBytes(rawFrame : Array<Byte>) : CANMessage {
val msg = CANMessage()
return msg
}
}
}
