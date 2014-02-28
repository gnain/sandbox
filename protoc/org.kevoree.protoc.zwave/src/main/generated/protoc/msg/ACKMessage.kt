package protoc.msg

import protoc.alt.*
import protoc.msg.*
public class ACKMessage : ZWaveMessage(){
override var zwaveframetype : ZWaveFrameType = ZWaveFrameType.ACK
class object {
public fun fromBytes(rawFrame : Array<Byte>) : ACKMessage {
val msg = ACKMessage()
return msg
}
}
}
