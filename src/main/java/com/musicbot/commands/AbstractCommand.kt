package com.musicbot.commands

import com.musicbot.ServerPlayer
import com.musicbot.users
import com.musicbot.users.Roles
import net.dv8tion.jda.core.entities.ChannelType
import net.dv8tion.jda.core.entities.User
import net.dv8tion.jda.core.events.message.MessageReceivedEvent

/**
 * Created by constantin on 31.01.2017.
 */
abstract class AbstractCommand {

    fun recvEvent(event: MessageReceivedEvent, args: List<String>, serverData: ServerPlayer) {
        if (event.isFromType(ChannelType.PRIVATE) && !allowPrivate()) {
            event.author.privateChannel.sendMessage("This command does not allow private usage").queue()
            return
        }
        if (getUserRole(event.author) > allowRole()) {
            event.channel.sendMessage(":no_entry: **You dont have acces to this command**").queue()
            return
        }
        handle(args, event, serverData)
    }

    abstract fun handle(args: List<String>, event: MessageReceivedEvent, serverData: ServerPlayer)

    fun allowPrivate() : Boolean = false
    fun getUserRole(author: User): Roles = users[author.id] ?: Roles.PLEB
    open fun allowRole(): Roles = Roles.PLEB
}