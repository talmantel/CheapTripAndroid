/**
 * CheapTrip Mobile
 * This is mobile client for LowCostsTrip server.
 */
package ru.z8.louttsev.cheaptripmobile.shared.model

import ru.z8.louttsev.cheaptripmobile.shared.model.data.Location
import ru.z8.louttsev.cheaptripmobile.shared.model.data.Route

/**
 * Determines read-only data access logic.
 */
interface DataSource {
    /**
     * Finds all locations matching parameters.
     *
     * @param parameters request specification
     * @return list of matching results (m.b. empty) or null if an error occurred
     */
    fun getLocations(parameters: ParamsBundle): List<Location>?

    /**
     * Finds all routes matching parameters.
     *
     * @param parameters request specification
     * @return list of matching results (m.b. empty) or null if an error occurred
     */
    fun getRoutes(parameters: ParamsBundle): List<Route>?

    /**
     * Declares a merger of isolated parameters.
     */
    class ParamsBundle {
        private val params = emptyMap<String, Any>().toMutableMap()

        /**
         * Adds parameter into bundle.
         *
         * @param key allowable parameter's key
         * @param value added parameter
         */
        fun put(key: Key, value: Any) {
            params[key.value] = value
        }

        /**
         * Extract parameter from bundle.
         *
         * @param key allowable parameter's key
         * @return extracted parameter or null if it wasn't added early
         */
        fun get(key: Key): Any? {
            return params[key.value]
        }

        /**
         * Declares list of valid parameter keys.
         */
        enum class Key(internal val value: String) {
            NEEDLE("needle"),
            TYPE("type"),
            LIMIT("limit"),
            LOCALE("locale");
        }
    }
}
