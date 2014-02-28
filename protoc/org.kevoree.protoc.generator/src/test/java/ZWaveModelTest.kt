package org.kevoree.protoc.zwave.test;

import org.kevoree.protoc.impl.DefaultProtocFactory
import org.kevoree.protoc.serializer.JSONModelSerializer
import java.io.FileOutputStream
import org.kevoree.protoc.loader.JSONModelLoader
import java.io.File
import org.kevoree.log.Log
import java.io.FileInputStream
import org.kevoree.protoc.ProtocolModel
import org.kevoree.protoc.generator.ProtocolGenerator

fun main(args : Array<String>) {

    Log.TRACE()

    val factory = DefaultProtocFactory()

    var zWaveModel = factory.createProtocolModel()
    zWaveModel.name = "ZWave"
    zWaveModel.version = "1.0"

    var rawByte0 = factory.createByteValuedChunk()
    rawByte0.name = "ZWaveFrameType"
    zWaveModel.firstChunk = rawByte0

    val nackAlternative = factory.createByteValuedItem()
    nackAlternative.name = "NAK"
    nackAlternative.value = 0x15
    val nackFrame = factory.createFrameType()
    nackFrame.name = "NAK"
    nackAlternative.frameType = nackFrame
    rawByte0.addAlternatives(nackAlternative)

    val ackAlternative = factory.createByteValuedItem()
    ackAlternative.name = "ACK"
    ackAlternative.value = 0x06
    val ackFrame = factory.createFrameType()
    ackFrame.name = "ACK"
    ackAlternative.frameType = ackFrame
    rawByte0.addAlternatives(ackAlternative)

    val canAlternative = factory.createByteValuedItem()
    canAlternative.name = "CAN"
    canAlternative.value = 0x18
    val canFrame = factory.createFrameType()
    canFrame.name = "CAN"
    canAlternative.frameType = canFrame
    rawByte0.addAlternatives(canAlternative)

    val sofAlternative = factory.createByteValuedItem()
    sofAlternative.name = "SOF"
    sofAlternative.value = 0x01
    val sofFrame = factory.createFrameType()
    sofFrame.name = "SOF"
    sofAlternative.frameType = sofFrame
    rawByte0.addAlternatives(sofAlternative)

    /*
        val model = File("zwaveModel.json")
        val serializer = JSONModelSerializer()
        serializer.serializeToStream(zWaveModel, FileOutputStream(model))
        Log.debug("File path:" + model.getAbsolutePath())
    val protocolModelLoader = JSONModelLoader()
    val zWaveMode = protocolModelLoader.loadModelFromStream(FileInputStream(model))?.first as ProtocolModel
    */


    val src = File(".."+File.separator + "org.kevoree.protoc.zwave"+File.separator + "src"+File.separator+"main"+File.separator+"generated")
    if(src.exists()) {
        src.delete()
    }

    val genertor = ProtocolGenerator(src.getAbsolutePath());
    genertor.generateParser(zWaveModel)


}