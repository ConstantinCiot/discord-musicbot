package com.musicbot.commands

import com.musicbot.ServerPlayer
import com.musicbot.audio.AudioPlayerSendHandler
import com.musicbot.audio.TrackManager
import com.musicbot.configs
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers
import net.dv8tion.jda.core.entities.ChannelType
import net.dv8tion.jda.core.entities.Guild
import net.dv8tion.jda.core.entities.Message
import net.dv8tion.jda.core.events.message.MessageReceivedEvent
import net.dv8tion.jda.core.hooks.ListenerAdapter


/**
 * Created by constantin on 31.01.2017.
 */
object CommandHandler : ListenerAdapter() {

    private val commands = HashMap<String, AbstractCommand>()
    private val servers: HashMap<String, ServerPlayer> = HashMap()

    var server: Guild? = null
    val myManager: AudioPlayerManager = DefaultAudioPlayerManager()

    init {
        AudioSourceManagers.registerRemoteSources(myManager)

        commands.put(Commands.SUMMON, SummonCommand())
        commands.put(Commands.PLAY, PlayCommand())
        commands.put(Commands.SKIP, SkipCommand())
        commands.put(Commands.NOW_PLAYING, NowPlayingCommand())
        commands.put(Commands.VOLUME, VolumeCommand())
        commands.put(Commands.RESET, ResetCommand())
        commands.put(Commands.QUEUE, QueueCommand())
        commands.put(Commands.REMOVE, RemoveCommand())
        commands.put(Commands.SHUFFLE, ShuffleCommand())
    }

    override fun onMessageReceived(e: MessageReceivedEvent) {

        val serverData = getServer(e.guild)

        val prefix = e.message.content.substring(0, 1)

        if (prefix == configs["bot.prefix"] && (e.channel.name == configs["bot.channel"] || e.isFromType(ChannelType.PRIVATE))) {
            val commandArgs = commandArgs(e.message).asList()
            val commandName = commandArgs.first()

            val command = commands[commandName]
            command?.recvEvent(e, commandArgs.subList(1, commandArgs.size), serverData)
        }
    }

    fun getServer(server: Guild): ServerPlayer {
        if(servers[server.id] == null) {
            val audioPlayer = myManager.createPlayer()
            val trackManager = TrackManager(audioPlayer)

            audioPlayer.addListener(trackManager)
            val handler = AudioPlayerSendHandler(audioPlayer)
            server.audioManager.sendingHandler = handler

            servers.put(server.id, ServerPlayer(audioPlayer, trackManager, server, myManager))
        }

        return servers[server.id]!!
    }

    private fun commandArgs(msg: Message): Array<String> {
        val noPrefix = msg.rawContent.substring(configs["bot.prefix"]!!.length)
        return noPrefix.split("\\s+".toRegex()).dropLastWhile(String::isEmpty).toTypedArray()
    }

}