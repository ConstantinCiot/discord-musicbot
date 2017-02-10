package com.musicbot.commands

import com.musicbot.ServerPlayer
import net.dv8tion.jda.core.events.message.MessageReceivedEvent

/**
 * Created by constantin on 31.01.2017.
 */
class NowPlayingCommand : AbstractCommand() {
    override fun handle(args: List<String>, event: MessageReceivedEvent, serverData: ServerPlayer) = serverData.trackManager.nowPlaying(event.member)
}