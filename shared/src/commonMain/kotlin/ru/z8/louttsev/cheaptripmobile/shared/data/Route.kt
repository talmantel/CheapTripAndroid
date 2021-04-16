/**
 * CheapTrip Mobile
 * This is mobile client for LowCostsTrip server.
 */
package ru.z8.louttsev.cheaptripmobile.shared.data

/**
 * Declares aggregate route between selected locations.
 *
 * @param routeType Route type in relation to ways of moving.
 * @param euroPrice Total route cost in EUR currency.
 * @param durationMinutes Total route duration in minutes.
 * @param directPaths Particular sections (paths) within aggregate route.
 */
data class Route(
    val routeType: RouteType,
    val euroPrice: Float,
    val durationMinutes: Int,
    val directPaths: List<Path>
)
