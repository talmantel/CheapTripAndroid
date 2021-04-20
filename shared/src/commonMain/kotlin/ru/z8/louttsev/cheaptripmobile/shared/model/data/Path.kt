/**
 * CheapTrip Mobile
 * This is mobile client for LowCostsTrip server.
 */
package ru.z8.louttsev.cheaptripmobile.shared.model.data

/**
 * Declares particular section (path) within aggregate route.
 *
 * @param transportationType Path type in relation to transport.
 * @param euroPrice Path cost in EUR currency.
 * @param durationMinutes Path duration in minutes.
 * @param from Origin name.
 * @param to Destination name.
 */
data class Path(
    val transportationType: TransportationType,
    val euroPrice: Float,
    val durationMinutes: Int,
    val from: String,
    val to: String
)
