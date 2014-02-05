package org.kevoree.protoc.generator

import org.kevoree.protoc.ProtocolModel
import org.kevoree.protoc.DefaultChunck
import org.kevoree.protoc.ByteValuedChunck
import org.kevoree.protoc.LengthValue
import org.kevoree.log.Log
import java.io.File
import java.io.PrintWriter

/**
 * Created with IntelliJ IDEA.
 * User: gregory.nain
 * Date: 25/10/2013
 * Time: 11:15
 * To change this template use File | Settings | File Templates.
 */



public class ProtocolGenerator {


    private val generationFolder = "src"+File.separator+"main"+File.separator+"generated"

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


    private fun createParserFile(protocolModel : ProtocolModel) {
        val directory = File(generationFolder + File.separator + "protoc")
        if(!directory.exists()) {
            directory.mkdirs()
        }

        val file = File(directory.getAbsolutePath() + File.separator + protocolModel.name + "Parser.kt")
        val pr = PrintWriter(file)
        pr.println("package protoc")
        pr.println("")
        pr.println("import protoc.alt.*")

        pr.println("public class " + protocolModel.name + "Parser {")

        generateParserMethod(pr, protocolModel)

        pr.println("}")
        pr.flush()
        pr.close()

    }

    private fun generateParserMethod(pr:PrintWriter, protocolModel : ProtocolModel) {
        pr.println("fun parse(rawFrame : Array<Byte>) {")

        val chunk = protocolModel.firstByte
        when(chunk) {
            is DefaultChunck -> {
                Log.trace("Default Chunk value:{}, prevChunk:{}, nextChunk:{}", chunk.value, chunk.previousChunk, chunk.nextChunk)
            }
            is ByteValuedChunck -> {
                generateByteValuedChunkAlternatives(chunk)
                pr.println("when("+toCamelCase(chunk.name!!)+".valueOf(rawFrame[0])) {")
                for(alternative in chunk.alternatives) {
                    pr.println("is " + alternative.litteral!!.toUpperCase() + " ->{}")
                }
                pr.println("else -> {println(\"[ERROR] no chunk alternative found for value:\" + rawFrame[0])}")
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

    private fun toCamelCase(str:String) : String {
        return str.substring(0,1).toUpperCase() + str.substring(1)
    }

    private fun generateByteValuedChunkAlternatives(chunk : ByteValuedChunck) {

        val directory = File(generationFolder + File.separator + "protoc"+File.separator + "alt")
        if(!directory.exists()) {
            directory.mkdirs()
        }

        val chunkName = toCamelCase(chunk.name!!)

        val file = File(directory.getAbsolutePath() + File.separator + chunkName + ".kt")
        val pr = PrintWriter(file)

        pr.println("package protoc.alt")

        pr.println("public enum class " + chunkName + "(val byteValue : Byte) {")

        for(alternative in chunk.alternatives) {
            pr.println(alternative.litteral!!.toUpperCase() + " : " + chunkName + "("+alternative.value+")")
        }

        pr.println("fun valueOf(b:Byte) : "+chunkName+"? {")
        pr.println("for(v in values()) {")
        pr.println("if(v.byteValue == b) {return v}")
        pr.println("}")
        pr.println("return null")
        pr.println("}")
        pr.println("}")
        pr.flush()
        pr.close()



    }

}
