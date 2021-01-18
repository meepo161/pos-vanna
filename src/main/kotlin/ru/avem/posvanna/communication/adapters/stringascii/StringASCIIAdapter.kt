package ru.avem.posvanna.communication.adapters.stringascii

import ru.avem.posvanna.communication.Connection
import ru.avem.posvanna.communication.adapters.Adapter

class StringASCIIAdapter(override val connection: Connection) : Adapter{
    fun read(
        deviceId: Byte,
        request: String
    ) : Int {
        val requestString = StringBuilder()
        requestString.append("A00").append(deviceId).append(" ").append(request).append("\n")
        return connection.read(requestString.toString())
    }

    fun write(
        deviceId: Byte,
        request: String
    ) {
        val requestString = StringBuilder()
        requestString.append("A00").append(deviceId).append(" ").append(request).append("\n")
        connection.write(requestString.toString())
    }
}
