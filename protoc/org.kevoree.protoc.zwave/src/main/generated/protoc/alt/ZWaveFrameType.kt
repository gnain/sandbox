package protoc.alt



public class ZWaveFrameType(val byteValue : Byte, val typeName : String) {

values.add(this);


object values = ArrayList<>();

public static AbstractZwaveEnum valueOf(String s) {
for(AbstractZwaveEnum e : values) {
if(e.getName().equalsIgnoreCase(s)) {
return e;
}
}return null;
}

public static AbstractZwaveEnum valueOf(byte b) {
for(AbstractZwaveEnum e : values) {
if(e.getByteValue() == b) {
return e;
}
}return null;
}

public static List<AbstractZwaveEnum> getValues() {
return values;
}

public val SWITCH_BINARY_Functions SET = new SWITCH_BINARY_Functions("SET",(byte)0x01);
public val SWITCH_BINARY_Functions GET = new SWITCH_BINARY_Functions("GET",(byte)0x02);
public val SWITCH_BINARY_Functions REPORT = new SWITCH_BINARY_Functions("REPORT",(byte)0x03);

}


