package utils

import com.musicbot.configs
import net.dv8tion.jda.core.entities.Guild
import net.dv8tion.jda.core.entities.Message
import java.util.concurrent.Executors

/**
 * Created by constantin on 31.01.2017.
 */
object MessageUtil {
    private val threadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1)

    fun sendMessage(msg: String, server: Guild) {
        val channel = server.getTextChannelsByName(configs["bot.channel"], true)?.first()
        val message: Message? = channel!!.sendMessage(msg).complete()
        threadPool.submit {
            Thread.sleep(10000)
            message!!.deleteMessage().complete()
        }
    }
}