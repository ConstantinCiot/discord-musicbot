package com.musicbot.audio

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import com.sedmelluq.discord.lavaplayer.track.playback.AudioFrame
import net.dv8tion.jda.core.audio.AudioSendHandler



/**
 * Created by constantin on 31.01.2017.
 */
class AudioPlayerSendHandler(var audioPlayer: AudioPlayer) : AudioSendHandler {

    private var lastFrame: AudioFrame? = null
    override fun provide20MsAudio(): ByteArray? = lastFrame?.data
    override fun isOpus(): Boolean = true

    override fun canProvide(): Boolean {
        lastFrame = audioPlayer.provide()
        return lastFrame != null
    }
}