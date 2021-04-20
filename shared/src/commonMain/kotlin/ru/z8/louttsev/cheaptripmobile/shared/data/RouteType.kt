/**
 * CheapTrip Mobile
 * This is mobile client for LowCostsTrip server.
 */
package ru.z8.louttsev.cheaptripmobile.shared.data

import dev.icerock.moko.resources.StringResource
import dev.icerock.moko.resources.desc.Resource
import dev.icerock.moko.resources.desc.StringDesc
import ru.z8.louttsev.cheaptripmobile.MR
import ru.z8.louttsev.cheaptripmobile.shared.convertToString

/**
 * Declares route type in relation to ways of moving.
 */
enum class RouteType(val value: String, private val stringResourceId: StringResource) {
    GROUND_ROUTES("ground_routes", MR.strings.route_type_ground),
    MIXED_ROUTES("mixed_routes", MR.strings.route_type_ground),
    FLYING_ROUTES("flying_routes", MR.strings.route_type_ground),
    FIXED_ROUTES_WITHOUT_RIDE_SHARE(
        "fixed_routes_without_ride_share",
        MR.strings.route_type_ground
    ),
    ROUTES_WITHOUT_RIDE_SHARE("routes_without_ride_share", MR.strings.route_type_ground),
    DIRECT_ROUTES("direct_routes", MR.strings.route_type_ground);

    override fun toString(): String {
        return StringDesc.Resource(stringResourceId).convertToString()
    }
}
