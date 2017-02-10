package utils

import java.util.concurrent.TimeUnit

/**
 * Created by constantin on 31.01.2017.
 */
object TimeUtil {
    fun toDuration(duration: Long): String {
        return String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(duration),
                TimeUnit.MILLISECONDS.toMinutes(duration),
                TimeUnit.MILLISECONDS.toSeconds(duration) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration))
        );
    }
}