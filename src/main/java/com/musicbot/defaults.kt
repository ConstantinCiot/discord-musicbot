package com.musicbot

import com.musicbot.users.Roles
import org.apache.logging.log4j.LogManager
import java.io.File
import java.io.FileInputStream
import java.util.*

/**
 *
 */
val configs by lazy {
    val configDir = File("./config")
    val listFiles = configDir.listFiles()

    val data: MutableMap<String, String> = mutableMapOf()
    listFiles?.forEach {
        val inputStream = FileInputStream(it)
        val props = Properties()
        props.load(inputStream)
        inputStream.close()

        data.putAll(props.map { Pair(it.key.toString(), it.value.toString()) })
    }

    data
}

val users by lazy {
    val data: MutableMap<String, Roles> = mutableMapOf()
    configs["bot.admins"]?.split(",")!!.forEach {
        data.put(it, Roles.ADMIN)
    }
    configs["bot.masters"]?.split(",")!!.forEach {
        data.put(it, Roles.MUSIC_MASTER)
    }

    data
}

val defaultPlaylist by lazy {
    val file = File("./config/playlist.txt")
    val data: List<String> = file.readLines()
    data
}

fun getSong(): String {
    val shuffled = shuffle(defaultPlaylist as MutableList<String>)
    return shuffled.first()
}


fun <T : Comparable<T>> shuffle(items: MutableList<T>): List<T> {
    val rg: Random = Random()
    for (i in 0..items.size - 1) {
        val randomPosition = rg.nextInt(items.size)
        val tmp: T = items[i]
        items[i] = items[randomPosition]
        items[randomPosition] = tmp
    }
    return items
}

fun Any.logInfo(message: String, opt: Any? = null) = LogManager.getLogger(javaClass).info(message, opt)
fun Any.logWarn(message: String, opt: Any? = null) = LogManager.getLogger(javaClass).warn(message, opt)
fun Any.logFatal(message: String, opt: Any? = null) = LogManager.getLogger(javaClass).fatal(message, opt)