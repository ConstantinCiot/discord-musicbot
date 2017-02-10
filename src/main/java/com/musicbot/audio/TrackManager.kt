package com.musicbot.audio

import com.musicbot.*
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason
import net.dv8tion.jda.core.entities.Game
import net.dv8tion.jda.core.entities.Member
import utils.MessageUtil
import utils.TimeUtil
import java.util.*
import java.util.concurrent.LinkedBlockingQueue


/**
 * Created by constantin on 31.01.2017.
 */

class TrackManager(var player: AudioPlayer) : AudioEventAdapter() {

    private var queue: LinkedBlockingQueue<AudioInfo>
    private var serverData: ServerPlayer? = null

    init {
        this.queue = LinkedBlockingQueue<AudioInfo>()
    }

    fun queue(track: AudioTrack, author: Member, serverData: ServerPlayer) {
        val info = AudioInfo(track, author, serverData)
        val isEmpty = queue.isEmpty()
        queue.offer(info)

        if (!isEmpty || player.playingTrack != null) {
            logInfo("Adding to Queue: " + track.info.title)

            var tillPlaying: Long = player.playingTrack.duration - player.playingTrack.position

            val copy = LinkedBlockingQueue(queue)
            copy.poll()

            copy.forEach {
                tillPlaying += it.track.duration
            }

            copy.clear()
            MessageUtil.sendMessage(author.user.asMention + " was added to queue (" + track.info.title + " " + TimeUtil.toDuration(track.duration) + "), position **" + queue.size + "** [" + TimeUtil.toDuration(tillPlaying) + " till playing]", serverData.server)
        } else {
            val songData = queue.poll()
            this.serverData = songData.serverData
            player.startTrack(songData.track, false)
            serverData.server.jda.presence.game = Game.of(track.info.title)
            if (author.user != serverData.server.selfMember.user) {
                MessageUtil.sendMessage(author.user.asMention + " your song is now playing (" + track.info.title + " " + TimeUtil.toDuration(track.duration) + ")", serverData.server)
            }
            logInfo("Nothing in queue, now playing: " + track.info.title)
        }
    }

    fun nextTrack(serverData: ServerPlayer) {
        if (queue.isEmpty()) {
            player.stopTrack()
            RequestSong.request(getSong(), serverData.server.selfMember, serverData)
            return
        }
        val (track, author, sData) = queue.poll()
        this.serverData = sData
        logInfo("Skipping current song, next song is: " + track.info.title)
        MessageUtil.sendMessage(author.user.asMention + " your song is now playing (" + track.info.title + " " + TimeUtil.toDuration(track.duration) + ")", serverData.server)
        player.startTrack(track, false)
        serverData.server.jda.presence.game = Game.of(track.info.title)
    }

    override fun onTrackStart(player: AudioPlayer, track: AudioTrack) {
        val info: AudioInfo = queue.element()
        info.author.guild.audioManager.openAudioConnection(info.author.voiceState.channel)
    }

    override fun onTrackEnd(player: AudioPlayer, track: AudioTrack, endReason: AudioTrackEndReason) {
        if (endReason.mayStartNext) {
            nextTrack(serverData!!)
        }
    }

    fun nowPlaying(author: Member) {
        val track = player.playingTrack
        MessageUtil.sendMessage(author.user.asMention + " current song playing is **" + track.info.title + "** [" + TimeUtil.toDuration(track.position) + "/" + TimeUtil.toDuration(track.duration) + "]", serverData!!.server)
    }

    fun purgeQueue() = queue.clear()

    fun printQueue(author: Member) {

        if (queue.size < 1) {
            nowPlaying(author)
            return
        }

        var tillPlaying: Long = player.playingTrack.duration - player.playingTrack.position
        val copy = LinkedBlockingQueue(queue)

        var message: String = ""
        var index: Int = 1
        copy.forEach {
            message += "`` $index. ${it.track.info.title} (${TimeUtil.toDuration(it.track.duration)}), added by `` **``${it.author.effectiveName}``** **``[${TimeUtil.toDuration(tillPlaying)} till playing]``**" + '\n'
            tillPlaying += it.track.duration
            index++
        }

        copy.clear()
        MessageUtil.sendMessage(message, serverData!!.server)
    }

    fun removeElement(index: Int) {
        val track: AudioInfo? = queue.elementAtOrNull(index)
        if (track != null) {
            MessageUtil.sendMessage(":thumbsup:", track.serverData.server)
            queue.remove(track)
        } else {
            MessageUtil.sendMessage(":thumbsdown:", queue.element().serverData.server)
        }
    }

    fun shuffleList() {
        val tempQueue = ArrayList(queue)
        Collections.shuffle(tempQueue)
        purgeQueue()
        queue.addAll(tempQueue)
    }


    fun shouldPlay(): Boolean {
        val serverData = queue.element().serverData
        return serverData.server.getVoiceChannelsByName(configs["bot.voicechannel"], true).first().members.size > 1
    }
}