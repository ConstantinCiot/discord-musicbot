package com.musicbot.commands

import com.musicbot.ServerPlayer
import com.musicbot.users.Roles
import net.dv8tion.jda.core.events.message.MessageReceivedEvent

/**
 * Created by constantin on 3.02.2017.
 */
class ShuffleCommand: AbstractCommand() {
    override fun handle(args: List<String>, event: MessageReceivedEvent, serverData: ServerPlayer) {
        serverData.trackManager.shuffleList()
    }

    override fun allowRole(): Roles = Roles.MUSIC_MASTER
}