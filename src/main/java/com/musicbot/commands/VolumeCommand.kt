package com.musicbot.commands

import com.musicbot.ServerPlayer
import net.dv8tion.jda.core.events.message.MessageReceivedEvent
import utils.MessageUtil

/**
 * Created by constantin on 31.01.2017.
 */
class VolumeCommand : AbstractCommand() {
    override fun handle(args: List<String>, event: MessageReceivedEvent, serverData: ServerPlayer) {
        val author = event.author
        if (args.isNotEmpty()) {
            var newVolume = args[0].toInt()
            val oldVolume = serverData.audioPlayer.volume

            if (newVolume > 100) newVolume = 100
            if (newVolume < 0) newVolume = 0

            if (newVolume > oldVolume) {
                MessageUtil.sendMessage(author.name + " turned the volume up from " + oldVolume + " to " + newVolume, serverData.server)
            } else {
                MessageUtil.sendMessage(author.name + " turned the volume down from " + oldVolume + " to " + newVolume, serverData.server)
            }

            serverData.audioPlayer.volume = newVolume
        }
    }
}