package ru.z8.louttsev.cheaptripmobile.shared.payload

import ru.z8.louttsev.cheaptripmobile.shared.model.data.Location
import ru.z8.louttsev.cheaptripmobile.shared.model.data.Path
import ru.z8.louttsev.cheaptripmobile.shared.model.data.TransportationType.*

/**
 * Provides available affiliate programs payload.
 */
class PayloadActionHandler {
    companion object {
        /**
         * Chooses URL of a suitable in relation to the specified path affiliate program.
         *
         * @param path Specified section of the route
         * @return Suitable payload URL or empty
         */
        fun getAffiliateUrl(path: Path): String =
            when (path.transportationType) {
                FLIGHT -> "https://skyscanner.com"
                BUS -> {
                    if (path.relate(Location(545, "Донецк"), Location(545, "Donetsk"))) {
                        "http://bustravel.dn.ua"
                    } else {
                        "https://bus.tutu.ru"
                    }
                    // TODO add current country specific, issue #3
                }
                TRAIN -> {
                    "https://www.tutu.ru/poezda"
                    // TODO add current country specific, issue #3
                }
                CAR_DRIVE -> "" // add RentalCars?
                TAXI -> "" // add Uber?
                WALK -> "" // no affiliate program
                TOWN_CAR -> "" // don’t know a suitable service
                RIDE_SHARE -> "https://www.blablacar.com"
                SHUTTLE -> "" // don’t know a suitable service
                FERRY -> "https://www.aferry.com"
                SUBWAY -> "" // no affiliate program
                UNDEFINED -> "" // no affiliate program
            }

        private fun Path.relate(vararg locations: Location): Boolean {
            locations.forEach {
                if (this.from == it.name || this.to == it.name) return true
            }
            return false
        }

    }
}