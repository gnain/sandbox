package protoc.msg

import protoc.alt.*
import protoc.msg.*
public class SOFMessage : ZWaveMessage(){
override var zwaveframetype : ZWaveFrameType = ZWaveFrameType.SOF
class object {
public fun fromBytes(rawFrame : Array<Byte>) : SOFMessage {
val msg = SOFMessage()
return msg
}
}
}
