package com.musicbot.commands

import com.musicbot.ServerPlayer
import com.musicbot.audio.RequestSong
import com.musicbot.configs
import com.musicbot.getSong
import net.dv8tion.jda.core.entities.VoiceChannel
import net.dv8tion.jda.core.events.message.MessageReceivedEvent

/**
 * Created by constantin on 31.01.2017.
 */
class SummonCommand : AbstractCommand()
{
    override fun handle(args: List<String>, event: MessageReceivedEvent, serverData: ServerPlayer) {
        val voiceChannel: VoiceChannel? = serverData.server.getVoiceChannelsByName(configs["bot.voicechannel"], true)?.first()
        serverData.server.audioManager.openAudioConnection(voiceChannel)
        RequestSong.request(getSong(), serverData.server.selfMember!!, serverData)
    }
}