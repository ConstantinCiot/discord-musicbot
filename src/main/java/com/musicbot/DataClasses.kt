package com.musicbot

import com.musicbot.audio.TrackManager
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import net.dv8tion.jda.core.entities.Guild
import net.dv8tion.jda.core.entities.Member

/**
 * Created by constantin on 31.01.2017.
 */
data class AudioInfo(var track: AudioTrack, var author: Member, var serverData: ServerPlayer)
data class ServerPlayer(var audioPlayer: AudioPlayer, var trackManager: TrackManager, var server: Guild, var audioManager: AudioPlayerManager)