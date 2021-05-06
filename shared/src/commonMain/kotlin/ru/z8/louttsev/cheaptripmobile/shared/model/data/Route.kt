/**
 * CheapTrip Mobile
 * This is mobile client for LowCostsTrip server.
 */
package ru.z8.louttsev.cheaptripmobile.shared.model.data

import dev.icerock.moko.resources.StringResource
import dev.icerock.moko.resources.desc.Resource
import dev.icerock.moko.resources.desc.StringDesc
import ru.z8.louttsev.cheaptripmobile.MR
import ru.z8.louttsev.cheaptripmobile.shared.convertToString

/**
 * Declares aggregate route between selected locations.
 *
 * @param routeType Route type in relation to ways of moving.
 * @param euroPrice Total route cost in EUR currency.
 * @param durationMinutes Total route duration in minutes.
 * @param directPaths Particular sections (paths) within aggregate route.
 */
data class Route(
    val routeType: Type,
    val euroPrice: Float,
    val durationMinutes: Int,
    val directPaths: List<Path>
) {
    /**
     * Declares route type in relation to ways of moving.
     */
    enum class Type(val value: String, private val stringResourceId: StringResource) {
        GROUND("ground_routes", MR.strings.route_type_ground),
        MIXED("mixed_routes", MR.strings.route_type_ground),
        FLYING("flying_routes", MR.strings.route_type_ground),
        FIXED_WITHOUT_RIDE_SHARE(
            "fixed_routes_without_ride_share",
            MR.strings.route_type_ground
        ),
        WITHOUT_RIDE_SHARE("routes_without_ride_share", MR.strings.route_type_ground),
        DIRECT("direct_routes", MR.strings.route_type_ground);

        override fun toString(): String {
            return StringDesc.Resource(stringResourceId).convertToString()
        }
    }
}
