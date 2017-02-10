package com.musicbot.commands

import com.musicbot.ServerPlayer
import com.musicbot.audio.RequestSong
import net.dv8tion.jda.core.events.message.MessageReceivedEvent

/**
 * Created by constantin on 31.01.2017.
 */
class PlayCommand : AbstractCommand() {
    override fun handle(args: List<String>, event: MessageReceivedEvent, serverData: ServerPlayer) {
        if (args.isEmpty()) {
            return
        }
        val requestedSong = args.joinToString(" ")

        RequestSong.request(requestedSong, event.member, serverData)
    }
}