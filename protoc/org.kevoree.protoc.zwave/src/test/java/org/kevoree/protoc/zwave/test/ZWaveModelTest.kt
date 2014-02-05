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


    var rawByte0 = factory.createByteValuedChunck()
    rawByte0.name = "ZWaveFrameType"
    zWaveModel.firstByte = rawByte0

    val nackAlternative = factory.createByteValuedItem()
    nackAlternative.litteral = "NAK"
    nackAlternative.value = 0x15
    val nackFrame = factory.createFrameType()
    nackFrame.isFinal = true
    nackFrame.name = "NAK"
    nackAlternative.frameType = nackFrame
    rawByte0.addAlternatives(nackAlternative)

    val ackAlternative = factory.createByteValuedItem()
    ackAlternative.litteral = "ACK"
    ackAlternative.value = 0x06
    val ackFrame = factory.createFrameType()
    ackFrame.isFinal = true
    ackFrame.name = "ACK"
    ackAlternative.frameType = ackFrame
    rawByte0.addAlternatives(ackAlternative)

    val canAlternative = factory.createByteValuedItem()
    canAlternative.litteral = "CAN"
    canAlternative.value = 0x18
    val canFrame = factory.createFrameType()
    canFrame.isFinal = true
    canFrame.name = "CAN"
    canAlternative.frameType = canFrame
    rawByte0.addAlternatives(canAlternative)

    val sofAlternative = factory.createByteValuedItem()
    sofAlternative.litteral = "SOF"
    sofAlternative.value = 0x01
    rawByte0.addAlternatives(sofAlternative)

    val sofFrame = factory.createFrameType()
    sofFrame.isFinal = false
    sofFrame.name = "SOF"
    sofAlternative.frameType = sofFrame
    sofFrame.

    /*
        val model = File("zwaveModel.json")
        val serializer = JSONModelSerializer()
        serializer.serializeToStream(zWaveModel, FileOutputStream(model))
        Log.debug("File path:" + model.getAbsolutePath())
    val protocolModelLoader = JSONModelLoader()
    val zWaveMode = protocolModelLoader.loadModelFromStream(FileInputStream(model))?.first as ProtocolModel
    */


    val genertor = ProtocolGenerator()
    genertor.generateParser(zWaveModel)


}