package ru.avem.posvanna.utils

import ru.avem.posvanna.utils.Log.Companion.d

class Logger(private val TAG: String?) {
    fun <T> log(message: T): Logger {
        d(TAG, message.toString() + "")
        return this
    }

    companion object {
        fun withTag(tag: String?): Logger {
            return Logger(tag)
        }
    }
}