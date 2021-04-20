package ru.z8.louttsev.cheaptripmobile.shared

import ru.z8.louttsev.cheaptripmobile.shared.model.Locale

/**
 * A platform-specific property implementation for default language.
 */
actual val currentLocale: Locale
    get() = Locale.fromLanguageCode(java.util.Locale.getDefault().language)