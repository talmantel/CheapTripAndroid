/**
 * CheapTrip Mobile
 * This is mobile client for LowCostsTrip server.
 */
package ru.z8.louttsev.cheaptripmobile.shared.data

/**
 * Declares path type in relation to transport.
 */
enum class TransportationType(val value: String) {
    BUS("Bus"),
    TRAIN("Train"),
    FLIGHT("Flight"),
    RIDE_SHARE("Ride Share");
}
