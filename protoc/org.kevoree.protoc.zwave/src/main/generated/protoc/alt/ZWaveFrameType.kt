



package protoc.alt

import java.util.ArrayList
import java.util.HashMap

public class ZWaveFrameType(val byteValue : Byte, val typeName : String) {
{
ZWaveFrameType.valuesByByte.put(this.byteValue, this);
ZWaveFrameType.valuesByName.put(this.typeName.toUpperCase(), this);
}


class object {
private val valuesByByte = HashMap<Byte, ZWaveFrameType>();
private val valuesByName = HashMap<String, ZWaveFrameType>();
public fun valueOf(byteValue : Byte) : ZWaveFrameType? {
return valuesByByte.get(byteValue);
}

public fun valueOf(s : String)  : ZWaveFrameType? {
return valuesByName.get(s.toUpperCase());
}

public fun values() : Collection<ZWaveFrameType>{
    return valuesByName.values();
}

public val ACK : ZWaveFrameType = ZWaveFrameType(0x06, "ACK");
public val SOF : ZWaveFrameType = ZWaveFrameType(0x01, "SOF");
public val CAN : ZWaveFrameType = ZWaveFrameType(0x18, "CAN");
public val NAK : ZWaveFrameType = ZWaveFrameType(0x15, "NAK");

}
}