package ru.z8.louttsev.cheaptripmobile.shared

import ru.z8.louttsev.cheaptripmobile.shared.model.Locale

actual val currentLocale: Locale
    get() = Locale.fromLanguageCode(java.util.Locale.getDefault().language)