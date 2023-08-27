package linkpool.common.log

import org.slf4j.Logger
import org.slf4j.LoggerFactory

inline fun <reified T> logger(): Logger {
    return LoggerFactory.getLogger(T::class.java)
}

fun kLogger(): Logger {
    return logger<GlobalLogger>()
}

object GlobalLogger
