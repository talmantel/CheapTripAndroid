/**
 * CheapTrip Mobile
 * This is mobile client for LowCostsTrip server.
 */
package ru.z8.louttsev.cheaptripmobile.shared.model.data


import dev.icerock.moko.resources.ImageResource
import dev.icerock.moko.resources.StringResource
import dev.icerock.moko.resources.desc.Resource
import dev.icerock.moko.resources.desc.StringDesc
import ru.z8.louttsev.cheaptripmobile.MR
import ru.z8.louttsev.cheaptripmobile.shared.convertToString

/**
 * Declares path type in relation to transport.
 *
 * @property value String representation for JSON conversion
 * @property imageResource Resource ID for image representation into UI
 */
enum class TransportationType(
    val value: String,
    private val stringResourceId: StringResource,
    val imageResource: ImageResource
) {
    BUS("Bus", MR.strings.transportation_type_bus, MR.images.ic_bus),
    TRAIN("Train", MR.strings.transportation_type_train, MR.images.ic_train),
    FLIGHT("Flight", MR.strings.transportation_type_flight, MR.images.ic_plane),
    RIDE_SHARE("Ride Share", MR.strings.transportation_type_ride_share, MR.images.ic_ride_share);

    override fun toString(): String {
        return StringDesc.Resource(stringResourceId).convertToString()
    }
}
