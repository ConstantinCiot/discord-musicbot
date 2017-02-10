package com.musicbot.commands

import com.musicbot.ServerPlayer
import com.musicbot.users.Roles
import net.dv8tion.jda.core.events.message.MessageReceivedEvent

/**
 * Created by constantin on 2.02.2017.
 */
class ResetCommand : AbstractCommand() {
    override fun handle(args: List<String>, event: MessageReceivedEvent, serverData: ServerPlayer) {
        serverData.audioPlayer.destroy()
        serverData.trackManager.purgeQueue()
        serverData.server.audioManager.closeAudioConnection()

        SummonCommand().handle(args, event, serverData)
    }

    override fun allowRole(): Roles = Roles.ADMIN
}