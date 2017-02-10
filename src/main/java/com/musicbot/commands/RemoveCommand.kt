package com.musicbot.commands

import com.musicbot.ServerPlayer
import com.musicbot.users.Roles
import net.dv8tion.jda.core.events.message.MessageReceivedEvent

/**
 * Created by constantin on 31.01.2017.
 */
class RemoveCommand : AbstractCommand()
{
    override fun handle(args: List<String>, event: MessageReceivedEvent, serverData: ServerPlayer) {
        if (args.size == 1) serverData.trackManager.removeElement(args[0].toInt() - 1)
    }

    override fun allowRole(): Roles = Roles.MUSIC_MASTER
}