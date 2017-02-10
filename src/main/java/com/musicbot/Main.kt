package com.musicbot

import com.musicbot.commands.CommandHandler
import net.dv8tion.jda.core.AccountType
import net.dv8tion.jda.core.JDABuilder
import net.dv8tion.jda.core.events.Event
import net.dv8tion.jda.core.hooks.InterfacedEventManager
import java.util.concurrent.Executors


/**
 * Created by constantin on 31.01.2017.
 */

class Main {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            JDABuilder(AccountType.BOT)
                    .setToken(configs["bot.token"])
                    .addListener(CommandHandler)
                    .setBulkDeleteSplittingEnabled(false)
                    .setEventManager(ThreadedEventManager())
                    .buildAsync()
        }
    }

    private class ThreadedEventManager : InterfacedEventManager() {
        private val threadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1)

        override fun handle(e: Event) {
            threadPool.submit { super.handle(e) }
        }
    }
}