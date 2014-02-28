package org.kevoree.protoc.zwave.test;

import org.junit.Test;
import protoc.ZWaveParser;
import protoc.msg.ACKMessage;
import protoc.msg.CANMessage;
import protoc.msg.NAKMessage;

import java.util.ArrayList;

import static org.junit.Assert.assertTrue;

/**
 * Created by gregory.nain on 28/02/2014.
 */
public class ZWaveParseTest {


    @Test
    public void ackFrameTest() {
        Byte[] raw = new Byte[]{0x06};
        ZWaveParser parser = new ZWaveParser();
        assertTrue("Wrong parsing of ACK", parser.parse(raw) instanceof ACKMessage);
    }

    @Test
    public void nakFrameTest() {
        Byte[] raw = new Byte[]{0x15};
        ZWaveParser parser = new ZWaveParser();
        assertTrue("Wrong parsing of NAK", parser.parse(raw) instanceof NAKMessage);
    }

    @Test
    public void canFrameTest() {
        Byte[] raw = new Byte[]{0x18};
        ZWaveParser parser = new ZWaveParser();
        assertTrue("Wrong parsing of CAN", parser.parse(raw) instanceof CANMessage);
    }



}
