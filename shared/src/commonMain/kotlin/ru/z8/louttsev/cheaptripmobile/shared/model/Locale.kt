/**
 * CheapTrip Mobile
 * This is mobile client for LowCostsTrip server.
 */
package ru.z8.louttsev.cheaptripmobile.shared.model

/**
 * Declares supported language.
 */
enum class Locale(val languageCode: String) {
    RU("ru"), EN("en");

    override fun toString(): String {
        return languageCode
    }

    companion object {
        /**
         * Gives the locale by its string representation.
         *
         * @param languageCode two-letter code accordingly ISO 639-1
         * @return supported locale or EN as default
         */
        fun fromLanguageCode(languageCode: String): Locale {
            return Locale.values().find { it.languageCode == languageCode } ?: EN
        }
    }
}
