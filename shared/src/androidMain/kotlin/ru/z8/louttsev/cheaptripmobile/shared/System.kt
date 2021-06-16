/**
 * CheapTrip Mobile
 * This is mobile client for LowCostsTrip server.
 */
package ru.z8.louttsev.cheaptripmobile.shared

import ru.z8.louttsev.cheaptripmobile.shared.model.data.Locale

/**
 * A platform-specific property implementation for default language.
 */
actual val currentLocale: Locale
    get() = Locale.fromLanguageCode(java.util.Locale.getDefault().language)