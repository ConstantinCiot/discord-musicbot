package com.musicbot.audio

import com.musicbot.ServerPlayer
import com.musicbot.logWarn
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import net.dv8tion.jda.core.entities.Member
import utils.MessageUtil

/**
 * Created by constantin on 01.02.2017.
 */
object RequestSong {
    fun request(song: String, member: Member, serverData: ServerPlayer) {

        val searchTag: String
        if (song.startsWith("http")) searchTag = song else searchTag = "ytsearch: " + song

        serverData.audioManager.loadItemOrdered(serverData.server, searchTag, object : AudioLoadResultHandler {
            override fun trackLoaded(track: AudioTrack) = serverData.trackManager.queue(track, member, serverData)
            override fun noMatches() = MessageUtil.sendMessage(":no_entry_sign: No playable tracks were found.", serverData.server)
            override fun loadFailed(exception: FriendlyException?) = logWarn("Song loading failed")

            override fun playlistLoaded(playlist: AudioPlaylist) {
                if (playlist.selectedTrack != null) {
                    trackLoaded(playlist.selectedTrack)
                } else if (playlist.isSearchResult) {
                    trackLoaded(playlist.tracks[0])
                } else {
                    playlist.tracks.forEach {
                        serverData.trackManager.queue(it, member, serverData)
                    }
                }
            }

        })

    }

}