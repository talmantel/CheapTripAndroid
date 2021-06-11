/**
 * CheapTrip Mobile
 * This is mobile client for LowCostsTrip server.
 */
package ru.z8.louttsev.cheaptripmobile.shared.model.data

import dev.icerock.moko.resources.StringResource
import dev.icerock.moko.resources.format
import ru.z8.louttsev.cheaptripmobile.MR
import ru.z8.louttsev.cheaptripmobile.shared.convertToString

/**
 * Provides a duration conversion to string representations.
 */
class DurationConverter {
    companion object {
        fun minutesToTimeComponents(value: Int): String {
            fun divmod(dividend: Int, divider: Int): Pair<Int, Int> =
                dividend / divider to dividend % divider

            val (days, residue) = divmod(value, 1440)
            val (hours, minutes) = divmod(residue, 60)

            return listOf(
                days.format(MR.strings.formatted_days_time_component),
                hours.format(MR.strings.formatted_hours_time_component),
                minutes.format(MR.strings.formatted_minutes_time_component)
            ).filter { it.isNotEmpty() }.joinToString(" ")
        }

        private fun Int.format(resource: StringResource): String =
            if (this != 0) resource.format(this).convertToString() else ""
    }
}