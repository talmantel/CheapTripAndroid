/**
 * CheapTrip Mobile
 * This is mobile client for LowCostsTrip server.
 */
package ru.z8.louttsev.cheaptripmobile.shared.data

/**
 * Declares route type in relation to ways of moving.
 */
enum class RouteType(val value: String) {
    GROUND_ROUTES("ground_routes"),
    MIXED_ROUTES("mixed_routes"),
    FLYING_ROUTES("flying_routes"),
    FIXED_ROUTES_WITHOUT_RIDE_SHARE("fixed_routes_without_ride_share"),
    ROUTES_WITHOUT_RIDE_SHARE("routes_without_ride_share"),
    DIRECT_ROUTES("direct_routes");
}
