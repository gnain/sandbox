package protoc.msg

import protoc.alt.*
import protoc.msg.*
public class NAKMessage : ZWaveMessage(){
override var zwaveframetype : ZWaveFrameType = ZWaveFrameType.NAK
class object {
public fun fromBytes(rawFrame : Array<Byte>) : NAKMessage {
val msg = NAKMessage()
return msg
}
}
}
