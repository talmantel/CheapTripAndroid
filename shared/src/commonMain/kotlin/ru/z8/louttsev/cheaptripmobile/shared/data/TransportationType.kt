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
 * Declares path type in relation to transport.
 */
enum class TransportationType(val value: String, private val resourceId: StringResource) {
    BUS("Bus", MR.strings.transportation_type_bus),
    TRAIN("Train", MR.strings.transportation_type_train),
    FLIGHT("Flight", MR.strings.transportation_type_flight),
    RIDE_SHARE("Ride Share", MR.strings.transportation_type_ride_share);

    override fun toString(): String {
        return StringDesc.Resource(resourceId).convertToString()
    }
}
