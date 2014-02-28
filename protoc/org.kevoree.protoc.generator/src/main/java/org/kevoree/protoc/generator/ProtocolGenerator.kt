package org.kevoree.protoc.generator

import org.kevoree.protoc.ProtocolModel
import org.kevoree.protoc.LengthValue
import org.kevoree.log.Log
import java.io.File
import java.io.PrintWriter
import org.apache.velocity.VelocityContext
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader
import org.apache.velocity.app.VelocityEngine
import org.kevoree.protoc.ByteValuedChunk
import org.kevoree.protoc.DefaultChunk
import org.kevoree.protoc.Chunk
import org.kevoree.protoc.FrameType
import org.kevoree.protoc.ByteValuedItem

/**
 * Created with IntelliJ IDEA.
 * User: gregory.nain
 * Date: 25/10/2013
 * Time: 11:15
 * To change this template use File | Settings | File Templates.
 */



public class ProtocolGenerator(val generationFolder : String) {




    fun generateParser(protocolModel : ProtocolModel) {

        resetGenerationFolderIfExists()

        createParserFile(protocolModel)

        Log.trace("Protocol {} model {}", protocolModel.name, protocolModel.version)

    }

    private fun resetGenerationFolderIfExists() {
        val file = File(generationFolder)
        if(file.exists()) {
            file.delete()
        }
        file.mkdirs()
    }


    private fun generateBaseMessageFile(protocolModel : ProtocolModel, genDir : File) {

        Helper.checkOrCreateFolder(genDir.getAbsolutePath() + File.separator + "msg");
        val protocolName = Helper.toCamelCase(protocolModel.name!!);

        val file = File(genDir.getAbsolutePath() + File.separator + "msg" + File.separator + protocolName + "Message.kt");
        val pr = PrintWriter(file, "utf-8");

        pr.println("package protoc.msg")
        pr.println("")
        pr.println("import protoc.alt.*")

        pr.println("public abstract class " + protocolName + "Message {")

        when(protocolModel.firstChunk) {
            is ByteValuedChunk -> {
                pr.println("abstract var " + protocolModel.firstChunk!!.name!!.toLowerCase() + " : "+Helper.toCamelCase(protocolModel.firstChunk!!.name!!))
            }
            else -> {}
        }

        pr.println("}")
        pr.flush()
        pr.close()

    }


    private fun generateMessageFile(protocolModel : ProtocolModel, frameType : FrameType, chunk : Chunk, genDir : String) {
        Helper.checkOrCreateFolder(genDir + File.separator + "msg");

        val file = File(genDir + File.separator + "msg" + File.separator + Helper.toCamelCase(frameType.name!!) + "Message.kt");
        val pr = PrintWriter(file, "utf-8");

        pr.println("package protoc.msg")
        pr.println("")
        pr.println("import protoc.alt.*")
        pr.println("import protoc.msg.*")

        pr.println("public class " + Helper.toCamelCase(frameType.name!!) + "Message : "+Helper.toCamelCase(protocolModel.name!!)+"Message(){")

        var currentChunk : Chunk? = chunk;
        while(currentChunk != null) {
            when(currentChunk) {
                is ByteValuedItem -> {
                    val tmpChk = currentChunk!! as ByteValuedItem
                    pr.println("override var " + tmpChk.byteValuedChunk!!.name!!.toLowerCase() + " : "+Helper.toCamelCase(tmpChk.byteValuedChunk!!.name!!) + " = " + Helper.toCamelCase(tmpChk.byteValuedChunk!!.name!!) + "." + tmpChk.name!!.toUpperCase() )
                }
                else -> {
                    Log.error("Chunk type unknown!")
                }
            }
            currentChunk = currentChunk!!.previousChunk;
        }

        pr.println("class object {")

        pr.println("public fun fromBytes(rawFrame : Array<Byte>) : " + Helper.toCamelCase(frameType.name!!) + "Message {")
        pr.println("val msg = " + Helper.toCamelCase(frameType.name!!) + "Message()")
        pr.println("return msg")
        pr.println("}")

        pr.println("}")
        pr.println("}")
        pr.flush()
        pr.close()
    }


    private fun createParserFile(protocolModel : ProtocolModel) {
        val directory = File(generationFolder + File.separator + "protoc")
        if(!directory.exists()) {
            directory.mkdirs()
        }

        generateBaseMessageFile(protocolModel, directory);

        val file = File(directory.getAbsolutePath() + File.separator + protocolModel.name + "Parser.kt")
        val pr = PrintWriter(file)
        pr.println("package protoc")
        pr.println("")
        pr.println("import protoc.alt.*")
        pr.println("import protoc.msg.*")

        pr.println("public class " + protocolModel.name + "Parser {")

        generateParserMethod(pr, directory.getAbsolutePath(), protocolModel)

        pr.println("}")
        pr.flush()
        pr.close()

    }

    private fun generateParserMethod(pr:PrintWriter, genDir : String, protocolModel : ProtocolModel) {
        pr.println("")
        pr.println("fun parse(rawFrame : Array<Byte>) : "+Helper.toCamelCase(protocolModel.name!!)+"Message? {")
        pr.println("return parse"+Helper.toCamelCase(protocolModel.firstChunk!!.name!!) + "(rawFrame)")
        pr.println("}")

        generateChunkParser(pr, genDir, protocolModel, protocolModel.firstChunk!!)

    }

    private fun generateChunkParser(pr : PrintWriter, genDir : String, protocolModel : ProtocolModel, chunk : Chunk) {
        pr.println("")
        pr.println("fun parse"+Helper.toCamelCase(chunk.name!!)+"(rawFrame : Array<Byte>) : "+Helper.toCamelCase(protocolModel.name!!)+"Message? {")

        when(chunk) {
            is DefaultChunk -> {
                Log.trace("Default Chunk value:{}, prevChunk:{}, nextChunk:{}", chunk.value, chunk.previousChunk, chunk.nextChunk)
            }
            is ByteValuedChunk -> {
                generateByteValuedChunkAlternatives(chunk)
                pr.println("when("+Helper.toCamelCase(chunk.name!!)+".valueOf(rawFrame[0])) {")
                for(alternative in chunk.alternatives) {
                    pr.println(Helper.toCamelCase(chunk.name!!) + "." + alternative.name!!.toUpperCase() + " ->{")
                    if(alternative.frameType != null) {
                        pr.println("return " + Helper.toCamelCase(alternative.frameType!!.name!!)+"Message.fromBytes(rawFrame)")
                        generateMessageFile(protocolModel, alternative.frameType!!, alternative, genDir)
                    } else {
                        pr.println("return parse" + Helper.toCamelCase(alternative.nextChunk!!.name!!) + "(rawFrame)")
                    }
                    pr.println("}")
                }
                pr.println("else -> {")
                pr.println("println(\"[ERROR] no chunk alternative found for value:\" + rawFrame[0])")
                pr.println("return null")
                pr.println("}")
                pr.println("}")
                Log.trace("ByteValuedChunck alternatives:{}, prevChunk:{}, nextChunk:{}", chunk.alternatives, chunk.previousChunk, chunk.nextChunk)
            }
            is LengthValue -> {
                Log.trace("LengthValue value:{}, prevChunk:{}, nextChunk:{}", chunk.length, chunk.previousChunk, chunk.nextChunk)
            }
            else -> {
                Log.error("Chunk type unknown!")
            }
        }
        pr.println("}")

    }


    private fun generateByteValuedChunkAlternatives(chunk : ByteValuedChunk) {

        Helper.checkOrCreateFolder(generationFolder + File.separator + "protoc"+File.separator + "alt");
        val chunkName = Helper.toCamelCase(chunk.name!!);

        val file = File(generationFolder + File.separator + "protoc"+File.separator + "alt" + File.separator + chunkName + ".kt");
        val pr = PrintWriter(file, "utf-8");

        val ve = VelocityEngine();
        ve.setProperty("file.resource.loader.class", javaClass<ClasspathResourceLoader>().getName());
        ve.init();
        val template = ve.getTemplate("templates/ByteValuedChunckEnum.vm");
        val ctxV = VelocityContext();
        ctxV.put("packageName", "protoc.alt");
        ctxV.put("className", chunkName);
        ctxV.put("alternatives", chunk.alternatives);
        template!!.merge(ctxV, pr);

        pr.flush();
        pr.close();

    }

}
