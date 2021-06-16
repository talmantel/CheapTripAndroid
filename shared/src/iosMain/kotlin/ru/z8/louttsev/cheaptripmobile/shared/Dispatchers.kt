/**
 * CheapTrip Mobile
 * This is mobile client for LowCostsTrip server.
 */
package ru.z8.louttsev.cheaptripmobile.shared

import kotlin.coroutines.CoroutineContext

/**
 * A platform-specific property implementation for I/O dispatcher.
 */
actual val ioDispatcher: CoroutineContext
    get() = TODO("Not yet implemented")

/**
 * A platform-specific property implementation for UI dispatcher.
 */
actual val uiDispatcher: CoroutineContext
    get() = TODO("Not yet implemented")
